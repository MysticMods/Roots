package mysticmods.roots.api.condition;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Set;

public interface IPlayerCondition extends ICondition {
  Set<BlockPos> TRUE = Set.of(BlockPos.ZERO);
  Set<BlockPos> FALSE = Set.of();

  @Override
  default Set<BlockPos> test(Level level, @Nullable Player player, @Nullable BoundingBox bounds, @Nullable BlockPos pos, @Nullable Set<BlockPos> exclusions) {
    if (test(level, player)) {
      return TRUE;
    }

    return FALSE;
  }

  boolean test(Level level, @Nonnull Player player);

}
