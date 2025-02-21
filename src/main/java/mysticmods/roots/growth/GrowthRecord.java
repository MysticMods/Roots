package mysticmods.roots.growth;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.growth.CanGrowFunction;
import mysticmods.roots.api.growth.LightFunction;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.init.ModTests;
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

import java.util.Collections;
import java.util.Optional;

public record GrowthRecord(Block cropBlock, Optional<IntegerProperty> ageProperty,
                           int maximumAge, int ticks,
                           CanGrowFunction canGrowFunction,
                           LightFunction lightFunction) {
  public static final MapCodec<GrowthRecord> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
      instance.group(
              BuiltInRegistries.BLOCK.byNameCodec().optionalFieldOf("cropBlock")
                  .forGetter(o -> Optional.ofNullable(o.cropBlock)),
              Codec.STRING.optionalFieldOf("ageProperty").fieldOf("ageProperty")
                  .forGetter(o -> o.ageProperty().map(Property::getName)),
              Codec.INT.fieldOf("maximumAge").forGetter(GrowthRecord::maximumAge),
              Codec.INT.fieldOf("ticks").forGetter(GrowthRecord::ticks),
              RootsRegistries.CAN_GROW_FUNCTIONS.byNameCodec().fieldOf("canGrowFunction")
                  .forGetter(GrowthRecord::canGrowFunction),
              RootsRegistries.LIGHT_FUNCTIONS.byNameCodec().fieldOf("lightFunction").forGetter(GrowthRecord::lightFunction)
          )
          .apply(instance, (block, optPropName, maxAge, ticks, canGrow, light) -> GrowthRecord.of(block.orElse(null), optPropName.orElse("age"), maxAge, ticks, canGrow, light))
  );
  public static final Codec<GrowthRecord> CODEC = MAP_CODEC.codec();

  public static GrowthRecord of(Block cropBlock, String agePropertyName, int maximumAge, int ticks, CanGrowFunction canGrowFunction, LightFunction lightFunction) {
    IntegerProperty ageProperty = null;
    if (cropBlock instanceof CropBlock crop) {
      ageProperty = ((AccessorMixinCropBlock) crop).callGetAgeProperty();
    } else if (cropBlock != null) {
      BlockState blockState = cropBlock.defaultBlockState();
      for (Property<?> property : blockState.getProperties()) {
        if (property.getName().equals(agePropertyName) && property instanceof IntegerProperty) {
          ageProperty = (IntegerProperty) property;
          break;
        }
      }
    }
    return new GrowthRecord(cropBlock, Optional.ofNullable(ageProperty), maximumAge, ticks, canGrowFunction, lightFunction);
  }


  public static GrowthRecord of(Block cropBlock, String agePropertyName, CanGrowFunction canGrowFunction, LightFunction lightFunction) {
    IntegerProperty ageProperty = null;
    int maxValue = -1;
    if (cropBlock instanceof CropBlock crop) {
      ageProperty = ((AccessorMixinCropBlock) crop).callGetAgeProperty();
      maxValue = crop.getMaxAge();
    } else {
      BlockState blockState = cropBlock.defaultBlockState();
      for (Property<?> property : blockState.getProperties()) {
        if (property.getName().equals(agePropertyName) && property instanceof IntegerProperty) {
          ageProperty = (IntegerProperty) property;
          break;
        }
      }

      if (ageProperty != null) {
        maxValue = Collections.max(ageProperty.getPossibleValues());
      }
    }
    return new GrowthRecord(cropBlock, Optional.ofNullable(ageProperty), maxValue, 1, canGrowFunction, lightFunction);
  }

  public static GrowthRecord ofCrop(CropBlock cropBlock, String agePropertyName) {
    return of(cropBlock, agePropertyName, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get());
  }

  public static GrowthRecord ofCrop(CropBlock cropBlock) {
    return ofCrop(cropBlock, "age");
  }

  public boolean canGrow(Level level, BlockPos pos, BlockState state) {
    if (!lightFunction.test(level, pos, state)) {
      return false;
    }
    return canGrowFunction.test(level, pos, state, ageProperty.orElse(null), maximumAge);
  }

  // Block

  // Age property (if any)

  // Maximum age property value (-1 if no age property)

  /*
    Potatoes: nothing special -> replace with age 0 -> self seed                                9
    Carrots: nope -> replace with age 0 -> self seed                                            9
    Nether wart: how does this grow? Nothing special -> replace with age 0 -> self seed         0
    Berry bush: nope -> replace with age 0 -> self seed but doesn't consume seed to be grown    9
    Pitcher crop:                                                                               0
      Crop becomes new block
      Grows in multipart
      No seed
      Replace with air
    Torchflower:                                                                                9
      Crop becomes new block
      No seed
      Replace with air
    Beetroot: nothing special -> replace with age 0 -> has own seed item                        9
    Melon:                                                                                      9
      Crop becomes new block (attached to facing)
      Spawns nearby block (melon)
      Doesn't actually produce a crop
      When crop is broken reverts to original block
    Pumpkin:                                                                                    9
      Same as melon
    Glow lichen:                                                                                0
      Only bone meal
      Doesn't tick
    Growing Plant Head Block:
      Max age 25
    Cave Vines Plant Block:                                                                     0

    Vines:                                                                                      0
      Ticks and spreads randomly
    Weeping/twisting vines:
      Can be sheared to stop growing:
      age becomes 25, stops growing
      Once grown becomes weeping/twisting vines plant
      Only grow the non-plant block
    Sugar cane/cactus:                                                                          0
      Has a specific height limit (determine limit)
      Extra ticks will cause the top block to grow
      Non-top blocks shouldn't accept ticks
    Bamboo:                                                                                     9
      Bamboo sapling
      Becomes bamboo
      Has an age property
      Only the top-most block should grow
      Has a specific height limit
      Non-top blocks shouldn't accept ticks
    Mushrooms:                                                                                  0-12
      Red/brown:
        Can spread
        Can be grown into a big mushroom
        Has light limits
      Warped/crimson:
        Cannot spread
    Kelp:                                                                                       0
      Must be waterlogged
      Age not 25
    Cocoa:                                                                                      0
      Replant set age to 0 and persist facing
    Saplings:                                                                                   9

    Bushes, grasses:
      No growth ticks




   */
}
