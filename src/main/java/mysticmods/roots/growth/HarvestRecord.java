package mysticmods.roots.growth;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.growth.CanHarvestFunction;
import mysticmods.roots.api.growth.GetDropsFunction;
import mysticmods.roots.api.growth.ReplantFunction;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.init.ModTests;
import mysticmods.roots.mixin.AccessorMixinCropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record HarvestRecord (Block cropBlock, Item seedItem, Optional<IntegerProperty> ageProperty, int maximumAge, GetDropsFunction dropsFunction, CanHarvestFunction canHarvestFunction, ReplantFunction replantFunction) {
  public static final MapCodec<HarvestRecord> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
      instance.group(
              BuiltInRegistries.BLOCK.byNameCodec().fieldOf("cropBlock").forGetter(HarvestRecord::cropBlock),
              BuiltInRegistries.ITEM.byNameCodec().fieldOf("seedItem").forGetter(HarvestRecord::seedItem),
              Codec.STRING.optionalFieldOf("ageProperty").fieldOf("ageProperty")
                  .forGetter(o -> o.ageProperty().map(Property::getName)),
              Codec.INT.fieldOf("maximumAge").forGetter(HarvestRecord::maximumAge),
              RootsRegistries.GET_DROPS_FUNCTIONS.byNameCodec().fieldOf("getDropsFunction").forGetter(HarvestRecord::dropsFunction),
              RootsRegistries.CAN_HARVEST_FUNCTIONS.byNameCodec().fieldOf("canHarvestFunction")
                  .forGetter(HarvestRecord::canHarvestFunction),
              RootsRegistries.REPLANT_FUNCTIONS.byNameCodec().fieldOf("replantFunction")
                  .forGetter(HarvestRecord::replantFunction)
          )
          .apply(instance, (block, item, optProp, max, drops, harvest, replant) -> HarvestRecord.of(block, item, optProp.orElse("age"), max, drops, harvest, replant))
      );
  public static final Codec<HarvestRecord> CODEC = MAP_CODEC.codec();

  public static HarvestRecord of (Block cropBlock, Item seedItem, String propertyName, int maximumAge, GetDropsFunction drops, CanHarvestFunction harvest, ReplantFunction replant) {
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
    return new HarvestRecord(cropBlock, seedItem, Optional.ofNullable(ageProperty), maximumAge, drops, harvest, replant);
  }

  public static HarvestRecord of (CropBlock cropBlock, ReplantFunction replant) {
    IntegerProperty ageProperty = ((AccessorMixinCropBlock) cropBlock).callGetAgeProperty();
    Item seedItem = ((AccessorMixinCropBlock) cropBlock).callGetBaseSeedId().asItem();

    return new HarvestRecord(cropBlock, seedItem, Optional.ofNullable(ageProperty), cropBlock.getMaxAge(), ModTests.GENERATE_DROPS.get(), ModTests.SINGLE_CROP_AGE.get(), replant);
  }

  public boolean canHarvest (Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity) {
    return canHarvestFunction.test(level, pos, state, ageProperty.orElse(null), maximumAge);
  }

  @Nullable
  public BlockState replant (Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity) {
    return replantFunction.replant(level, pos, state, ageProperty.orElse(null), maximumAge);
  }

  public List<ItemStack> drops (Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity) {
    return dropsFunction.getDrops(level, pos, state, seedItem, ageProperty.orElse(null), maximumAge, entity);
  }
}
