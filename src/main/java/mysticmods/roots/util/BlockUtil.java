package mysticmods.roots.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public class BlockUtil {
  public static List<BlockPos> getBlocksWithinRadius(Level level, BlockPos center, float radiusXZ, float radiusY, BiPredicate<Level, BlockPos> predicate) {
    return getBlocksWithinRadius(level, center, radiusXZ, radiusY, radiusXZ, predicate);
  }

  public static List<BlockPos> getBlocksWithinRadius(Level level, BlockPos center, float radiusX, float radiusY, float radiusZ, BiPredicate<Level, BlockPos> predicate) {
    List<BlockPos> result = new ArrayList<>();
    BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
    for (int x = (int) -radiusX; x < radiusX; x++) {
      mutable.setX(center.getX() + x);
      for (int y = (int) -radiusY; y < radiusY; y++) {
        mutable.setY(center.getY() + y);
        for (int z = (int) -radiusZ; z < radiusZ; z++) {
          mutable.setZ(center.getZ() + z);
          if (predicate.test(level, mutable)) {
            // Add to list
            result.add(mutable.immutable());
          }
        }
      }
    }
    return result;
  }
}
