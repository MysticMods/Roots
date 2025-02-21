package mysticmods.roots.growth;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.growth.ReplantFunction;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.mixin.AccessorMixinCropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record HarvestRecord (Block cropBlock, Item seedItem, Optional<IntegerProperty> ageProperty, int maximumAge, ReplantFunction replantFunction) {
  public static final MapCodec<HarvestRecord> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
      instance.group(
              BuiltInRegistries.BLOCK.byNameCodec().fieldOf("cropBlock").forGetter(HarvestRecord::cropBlock),
              BuiltInRegistries.ITEM.byNameCodec().fieldOf("seedItem").forGetter(HarvestRecord::seedItem),
              Codec.STRING.optionalFieldOf("ageProperty").fieldOf("ageProperty")
                  .forGetter(o -> o.ageProperty().map(Property::getName)),
              Codec.INT.fieldOf("maximumAge").forGetter(HarvestRecord::maximumAge),
              RootsRegistries.REPLANT_FUNCTIONS.byNameCodec().fieldOf("replantFunction")
                  .forGetter(HarvestRecord::replantFunction)
          )
          .apply(instance, (block, item, optProp, max, replant) -> HarvestRecord.of(block, item, optProp.orElse("age"), max, replant))
      );
  public static final Codec<HarvestRecord> CODEC = MAP_CODEC.codec();

  public static HarvestRecord of (Block cropBlock, Item seedItem, String propertyName, int maximumAge, ReplantFunction replant) {
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
    return new HarvestRecord(cropBlock, seedItem, Optional.ofNullable(ageProperty), maximumAge, replant);
  }

  public static HarvestRecord of (CropBlock cropBlock, ReplantFunction replant) {
    IntegerProperty ageProperty = ((AccessorMixinCropBlock) cropBlock).callGetAgeProperty();
    Item seedItem = ((AccessorMixinCropBlock) cropBlock).callGetBaseSeedId().asItem();

    return new HarvestRecord(cropBlock, seedItem, Optional.ofNullable(ageProperty), cropBlock.getMaxAge(), replant);
  }

  @Nullable
  public BlockState replant (Level level, BlockPos pos, BlockState state) {
    return replantFunction.replant(level, pos, state, ageProperty.orElse(null), maximumAge);
  }
}
