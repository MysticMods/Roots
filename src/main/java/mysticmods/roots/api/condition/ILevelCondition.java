package mysticmods.roots.api.condition;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

public interface ILevelCondition extends ICondition {
  boolean place (Level level, BlockPos position);

  Set<BlockPos> test(BlockPos pos, Level level, @javax.annotation.Nullable Player player);

  default Set<BlockPos> test(Level level, @Nullable Player player, BoundingBox bounds, BlockPos pos, Set<BlockPos> exclusions) {
    BoundingBox newBounds = bounds.moved(pos.getX(), pos.getY(), pos.getZ());
    for (int x = newBounds.minX(); x < newBounds.maxX(); x++) {
      for (int y = newBounds.minY(); y < newBounds.maxY(); y++) {
        for (int z = newBounds.minZ(); z < newBounds.maxZ(); z++) {
          pos = new BlockPos(x, y, z);
          if (exclusions.contains(pos)) {
            continue;
          }
          Set<BlockPos> result = test(pos, level, player);
          if (!result.isEmpty()) {
            return result;
          }
        }
      }
    }

    return Collections.emptySet();
  }

}
