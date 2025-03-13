package mysticmods.roots.growth;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.growth.CanHarvestFunction;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.init.ModTests;
import mysticmods.roots.mixin.AccessorMixinCropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

// Harvesting is complicated
//
// For bamboo, sugar cane and other crops that break the block above, we need a list of block positions to destroy and the order in which to destroy them.
// Simply destroying the blocks and capturing the drops at those positions is sufficient.
// However, for other blocks, we also need to subtract a seed from them.
// Torchflowers don't drop a seed, so simply breaking both blocks is sufficient. Likewise pitcher plants.
// Melons and pumpkins are also fine to just destroy.
// For actual crops, we also need a seed item to subtract from the drops.
//
// Essential information is thus:
// - The block itself
// - The seed item
// - Optionally an integer age property
// - The maximum age
// - A way to determine if the block can be harvested
// - either a function that returns a list of Pair(BlockPos, Item, int) for each position that should be destroyed and what item should be subtracted and how many of that item should be removed (this is a record) -- magnetism handler?
// - OR a function that returns a Pair(BlockState, ItemStack) for one position -- the new block state of that position plus any item that is supposed to be spawned -- what if there are multiple positions? -- magnetism handler
//
// From this information we can then construct a list of block positions to destroy along with how the drops from them should be processed. As magnetism isn't currently available, we don't need to be concerned about it in the first place.
//

public record HarvestRecord (Block cropBlock, Item seedItem, Optional<IntegerProperty> ageProperty, int maximumAge, CanHarvestFunction canHarvestFunction, HarvestFunction harvestFunction) {
  public static final MapCodec<HarvestRecord> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
      instance.group(
              BuiltInRegistries.BLOCK.byNameCodec().fieldOf("cropBlock").forGetter(HarvestRecord::cropBlock),
              BuiltInRegistries.ITEM.byNameCodec().fieldOf("seedItem").forGetter(HarvestRecord::seedItem),
              Codec.STRING.optionalFieldOf("ageProperty").fieldOf("ageProperty")
                  .forGetter(o -> o.ageProperty().map(Property::getName)),
              Codec.INT.fieldOf("maximumAge").forGetter(HarvestRecord::maximumAge),
              RootsRegistries.CAN_HARVEST_FUNCTIONS.byNameCodec().fieldOf("canHarvestFunction")
                  .forGetter(HarvestRecord::canHarvestFunction),
              RootsRegistries.HARVEST_FUNCTIONS.byNameCodec().fieldOf("harvestFunction")
                  .forGetter(HarvestRecord::harvestFunction)
          )
          .apply(instance, (block, item, optProp, max, drops, replant) -> HarvestRecord.of(block, item, optProp.orElse("age"), max, drops, replant))
      );
  public static final Codec<HarvestRecord> CODEC = MAP_CODEC.codec();

  public static HarvestRecord of (Block cropBlock, Item seedItem, String propertyName, int maximumAge, CanHarvestFunction harvest, HarvestFunction replant) {
    IntegerProperty ageProperty = null;
    if (cropBlock instanceof CropBlock crop) {
      ageProperty = ((AccessorMixinCropBlock) crop).callGetAgeProperty();
      if (seedItem == null) {
        seedItem = ((AccessorMixinCropBlock) crop).callGetBaseSeedId().asItem();
      }
    } else if (cropBlock != null) {
      BlockState blockState = cropBlock.defaultBlockState();
      for (Property<?> property : blockState.getProperties()) {
        if (property.getName().equals(propertyName) && property instanceof IntegerProperty) {
          ageProperty = (IntegerProperty) property;
          break;
        }
      }
    }
    return new HarvestRecord(cropBlock, seedItem, Optional.ofNullable(ageProperty), maximumAge, harvest, replant);
  }

  public static HarvestRecord of (CropBlock cropBlock, HarvestFunction harvestFunction) {
    IntegerProperty ageProperty = ((AccessorMixinCropBlock) cropBlock).callGetAgeProperty();
    Item seedItem = ((AccessorMixinCropBlock) cropBlock).callGetBaseSeedId().asItem();

    return new HarvestRecord(cropBlock, seedItem, Optional.ofNullable(ageProperty), cropBlock.getMaxAge(), ModTests.SINGLE_CROP_AGE.get(), harvestFunction);
  }

  public boolean canHarvest (Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity) {
    return canHarvestFunction.test(level, pos, state, ageProperty.orElse(null), maximumAge);
  }

  @Nullable
  public BlockState replant (Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity) {
    return harvestFunction.replant(level, pos, state, ageProperty.orElse(null), maximumAge, null);
  }
}
