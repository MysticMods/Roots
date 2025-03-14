package mysticmods.roots.growth;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.growth.CanHarvestFunction;
import mysticmods.roots.api.growth.HarvestFunction;
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
// - A function that actually harvests the block (presumably triggering BlockDropsEvent)
//   The function needs to (not worry about starting or ending captures):
//    - Inform HarvestUtil::adjustOrCapture that it's performing an operation on a blockpos in a dimension
//    - Break the block and trigger BlockDropsEvent
//    - Otherwise call HarvestUtil.capture on any item entity created
//    - If manually creating drops, it should ignore the seed item
//    - Always a non-null user
//    - For manually creating drops, it needs to do so via loot table for fortune, silk touch, etc
//

public record HarvestRecord (Block cropBlock, Optional<Item> seedItem, Optional<IntegerProperty> ageProperty, int maximumAge, CanHarvestFunction canHarvestFunction, HarvestFunction harvestFunction) {
  public static final MapCodec<HarvestRecord> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
      instance.group(
              BuiltInRegistries.BLOCK.byNameCodec().fieldOf("cropBlock").forGetter(HarvestRecord::cropBlock),
              BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("seedItem").forGetter(HarvestRecord::seedItem),
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

  public static HarvestRecord of (Block cropBlock, Optional<Item> seedItem, String propertyName, int maximumAge, CanHarvestFunction harvest, HarvestFunction replant) {
    IntegerProperty ageProperty = null;
    if (cropBlock instanceof CropBlock crop) {
      ageProperty = ((AccessorMixinCropBlock) crop).callGetAgeProperty();
      if (seedItem.isEmpty()) {
        seedItem = Optional.of(((AccessorMixinCropBlock) crop).callGetBaseSeedId().asItem());
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

    return new HarvestRecord(cropBlock, Optional.of(seedItem), Optional.ofNullable(ageProperty), cropBlock.getMaxAge(), ModTests.SINGLE_CROP_AGE.get(), harvestFunction);
  }

  public boolean canHarvest (Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity) {
    return canHarvestFunction.test(level, pos, state, ageProperty.orElse(null), maximumAge);
  }

  public void harvest (Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity) {
    harvestFunction.harvest(level, pos, state, entity, ageProperty.orElse(null), maximumAge, seedItem.orElse(null));
  }
}
