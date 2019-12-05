package epicsquid.roots.mechanics;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.config.CropConfig;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.*;
import net.minecraft.state.IProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.*;

public class Growth {
  private static Set<Block> BLACKLIST = new HashSet();
  private static Set<IProperty<?>> AGE_PROPERTIES = new HashSet<>();
  private static Object2IntOpenHashMap<IntegerProperty> AGE_MAP = new Object2IntOpenHashMap();

  static {
    AGE_MAP.defaultReturnValue(-1);

    // Initialise some vanilla values
    AGE_PROPERTIES.add(SugarCaneBlock.AGE);
    AGE_PROPERTIES.add(ChorusFlowerBlock.AGE);
    AGE_PROPERTIES.add(CactusBlock.AGE);
    AGE_PROPERTIES.add(BeetrootBlock.BEETROOT_AGE);
    AGE_PROPERTIES.add(StemBlock.AGE);
    AGE_PROPERTIES.add(NetherWartBlock.AGE);
    AGE_PROPERTIES.add(CropsBlock.AGE);
    AGE_PROPERTIES.add(CocoaBlock.AGE);

    AGE_MAP.put(SugarCaneBlock.AGE, 15);
    AGE_MAP.put(ChorusFlowerBlock.AGE, 5);
    AGE_MAP.put(CactusBlock.AGE, 15);
    AGE_MAP.put(BeetrootBlock.BEETROOT_AGE, 3);
    AGE_MAP.put(StemBlock.AGE, 7);
    AGE_MAP.put(NetherWartBlock.AGE, 3);
    AGE_MAP.put(CropsBlock.AGE, 7);
    AGE_MAP.put(CocoaBlock.AGE, 2);

    addBlacklist(Blocks.GRASS, Blocks.GRASS); // Still need flowers, blocks
  }

  public static void addBlacklist(Block... blocks) {
    BLACKLIST.addAll(Arrays.asList(blocks));
  }

  public static List<BlockPos> collect(World world, BlockPos startPosition, int radiusX, int radiusY, int radiusZ) {
    return Util.getBlocksWithinRadius(world, startPosition, radiusX, radiusY, radiusZ, (pos) -> canGrow(world, pos, world.getBlockState(pos)));
  }

  public static boolean canGrow(World world, BlockPos pos, BlockState state) {
    if (BLACKLIST.contains(state.getBlock())) return false;

    if (CropConfig.getGrowthBlacklist().contains(state.getBlock())) return false;

    if (CropConfig.getGrowthModBlacklist().contains(Objects.requireNonNull(state.getBlock().getRegistryName()).getNamespace()))
      return false;

    if (state.getBlock() instanceof IGrowable) {
      return ((IGrowable) state.getBlock()).canGrow(world, pos, state, true);
    }

    // Hard-code this for some dumb reason as max age doesn't mean grown
    if (state.getBlock() == Blocks.SUGAR_CANE || state.getBlock() == Blocks.CACTUS) {
      return world.isAirBlock(pos.up());
    }

    if (state.getBlock() instanceof StemBlock) {
      return true;
    }

    if (state.getBlock() instanceof MushroomBlock) {
      return true;
    }

    if (state.getBlock() instanceof IPlantable) {
      Collection<IProperty<?>> keys = state.getProperties();
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

      IntegerProperty prop = (IntegerProperty) toCheck;
      int max = AGE_MAP.getInt(prop);
      if (max == -1) {
        max = Collections.max(prop.getAllowedValues());
        AGE_MAP.put(prop, max);
      }

      return state.get(prop) < max;
    }

    return false;
  }

}
