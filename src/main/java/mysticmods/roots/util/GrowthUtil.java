package mysticmods.roots.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import mysticmods.roots.api.RootsTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

public class GrowthUtil {
  private static final Object2ObjectMap<Block, CropData> CROP_AGES = new Object2ObjectLinkedOpenHashMap<>();

  public static int growthTicks(Level level, BlockPos pos, @Nullable BlockState state, @Nullable Player player) {
    if (state == null) {
      state = level.getBlockState(pos);
    }

    if (state.is(RootsTags.Blocks.GROWTH_BLACKLIST)) {
      return 0;
    }

    CropData crop = getCropData(state);
    if (!crop.isEmpty()) {
      if (!crop.isMaxAge(state)) {
        return crop.growthTicks();
      }
    } else {
      if (state.getBlock() instanceof BonemealableBlock) {
        return 2;
      }
    }

    return 0;
  }

  private static final CropData NULL_CROP = new CropData(Blocks.AIR, null, -1);

  private record CropData(Block block, IntegerProperty ageProperty, int maxAge) {
    public boolean isEmpty() {
      return this == NULL_CROP;
    }

    public boolean isMaxAge (BlockState state) {
      return state.getValue(ageProperty) == maxAge;
    }

    public int growthTicks () {
      int ticks = 2;

      // TODO: configure these somewhere
      // also maybe make them configurable per crop
      // LIKE WITH TAGS OR SOMETHIGN
      if (maxAge < 3) {
        ticks = 20;
      } else if (maxAge < 7) {
        ticks = 5;
      }

      return ticks;
    }
  }

  private static final Set<Property<?>> AGE_PROPERTIES = new HashSet<>(Arrays.asList(BlockStateProperties.AGE_7, BlockStateProperties.AGE_5, BlockStateProperties.AGE_3, BlockStateProperties.AGE_2, BlockStateProperties.AGE_1, BlockStateProperties.AGE_25, BlockStateProperties.AGE_15));

  private static CropData getCropData(BlockState state) {
    CropData result = CROP_AGES.get(state.getBlock());
    if (result != null) {
      return result;
    }

    Block block = state.getBlock();
    if (block instanceof CropBlock crop) {
      result = new CropData(block, crop.getAgeProperty(), crop.getMaxAge());
      CROP_AGES.put(block, result);
      return result;
    } else {
      for (Property<?> prop : state.getProperties()) {
        if (AGE_PROPERTIES.contains(prop)) {
          IntegerProperty ageProp = (IntegerProperty) prop;
          int max = ageProp.getAllValues().mapToInt(Property.Value::value).max().orElseThrow(NoSuchElementException::new);
          result = new CropData(block, ageProp, max);
          CROP_AGES.put(block, result);
          return result;
        }
      }
      for (Property<?> prop : state.getProperties()) {
        if (prop instanceof IntegerProperty intProp && prop.getName().equals("age")) {
          int max = intProp.getAllValues().mapToInt(Property.Value::value).max().orElseThrow(NoSuchElementException::new);
          result = new CropData(block, intProp, max);
          CROP_AGES.put(block, result);
          return result;
        }
      }
    }

    CROP_AGES.put(block, NULL_CROP);
    return NULL_CROP;
  }
}
