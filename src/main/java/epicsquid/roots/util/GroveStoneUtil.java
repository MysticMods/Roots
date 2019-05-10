package epicsquid.roots.util;

import com.sun.istack.internal.Nullable;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class GroveStoneUtil {
  public static int GROVE_STONE_RADIUS = 5;

  @Nullable
  public static BlockPos findGroveStone(World world, BlockPos near) {
    List<BlockPos> positions = Util.getBlocksWithinRadius(world, near, GROVE_STONE_RADIUS, GROVE_STONE_RADIUS, GROVE_STONE_RADIUS, ModBlocks.grove_stone);
    if (positions.isEmpty()) return null;
    return positions.get(0);
  }
}
