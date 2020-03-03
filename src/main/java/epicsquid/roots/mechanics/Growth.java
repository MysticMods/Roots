package epicsquid.roots.mechanics;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.config.CropConfig;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.*;

public class Growth {
  private static Set<Block> BLACKLIST = new HashSet();
  private static Set<IProperty<?>> AGE_PROPERTIES = new HashSet<>();
  private static Object2IntOpenHashMap<PropertyInteger> AGE_MAP = new Object2IntOpenHashMap();

  static {
    AGE_MAP.defaultReturnValue(-1);

    // Initialise some vanilla values
    AGE_PROPERTIES.add(BlockReed.AGE);
    AGE_PROPERTIES.add(BlockChorusFlower.AGE);
    AGE_PROPERTIES.add(BlockCactus.AGE);
    AGE_PROPERTIES.add(BlockBeetroot.BEETROOT_AGE);
    AGE_PROPERTIES.add(BlockStem.AGE);
    AGE_PROPERTIES.add(BlockNetherWart.AGE);
    AGE_PROPERTIES.add(BlockCrops.AGE);
    AGE_PROPERTIES.add(BlockCocoa.AGE);

    AGE_MAP.put(BlockReed.AGE, 15);
    AGE_MAP.put(BlockChorusFlower.AGE, 5);
    AGE_MAP.put(BlockCactus.AGE, 15);
    AGE_MAP.put(BlockBeetroot.BEETROOT_AGE, 3);
    AGE_MAP.put(BlockStem.AGE, 7);
    AGE_MAP.put(BlockNetherWart.AGE, 3);
    AGE_MAP.put(BlockCrops.AGE, 7);
    AGE_MAP.put(BlockCocoa.AGE, 2);

    addBlacklist(Blocks.TALLGRASS, Blocks.DOUBLE_PLANT, Blocks.GRASS, Blocks.DOUBLE_PLANT, Blocks.RED_FLOWER, Blocks.YELLOW_FLOWER);
  }

  public static void addBlacklist(Block... blocks) {
    BLACKLIST.addAll(Arrays.asList(blocks));
  }

  public static List<BlockPos> collect(World world, BlockPos startPosition, int radiusX, int radiusY, int radiusZ) {
    return Util.getBlocksWithinRadius(world, startPosition, radiusX, radiusY, radiusZ, (pos) -> canGrow(world, pos, world.getBlockState(pos)));
  }

  public static boolean canGrow(World world, BlockPos pos, IBlockState state) {
    if (BLACKLIST.contains(state.getBlock())) return false;

    if (CropConfig.getGrowthBlacklist().contains(state.getBlock())) return false;

    if (CropConfig.getGrowthModBlacklist().contains(Objects.requireNonNull(state.getBlock().getRegistryName()).getNamespace())) return false;

    // Hard-code this for some dumb reason as max age doesn't mean grown
    if (state.getBlock() == Blocks.REEDS || state.getBlock() == Blocks.CACTUS) {
      return world.isAirBlock(pos.up());
    }

    if (state.getBlock() instanceof BlockStem) {
      return true;
    }

    if (state.getBlock() instanceof BlockMushroom) {
      return true;
    }

    if (state.getBlock() instanceof IGrowable) {
      return ((IGrowable) state.getBlock()).canGrow(world, pos, state, true);
    }

    if (state.getBlock() instanceof IPlantable) {
      Collection<IProperty<?>> keys = state.getPropertyKeys();
      IProperty<?> toCheck = null;
      for (IProperty<?> prop : AGE_PROPERTIES) {
        if (keys.contains(prop)) {
          toCheck = prop;
          break;
        }
      }
      if (toCheck == null) {
        for (IProperty<?> prop : keys) {
          if (prop.getName().contains("age")) {
            AGE_PROPERTIES.add(prop);
            toCheck = prop;
            break;
          }
        }
      }
      if (toCheck == null) {
        return false;
      }

      if (!toCheck.getValueClass().equals(Integer.class)) {
        return false;
      }

      PropertyInteger prop = (PropertyInteger) toCheck;
      int max = AGE_MAP.getInt(prop);
      if (max == -1) {
        max = Collections.max(prop.getAllowedValues());
        AGE_MAP.put(prop, max);
      }

      return state.getValue(prop) < max;
    }

    return false;
  }

}
