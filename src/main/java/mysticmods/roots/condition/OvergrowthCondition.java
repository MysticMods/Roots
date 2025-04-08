package mysticmods.roots.condition;

import mysticmods.roots.api.condition.CanonicalRepresentation;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.block.CreepingGroveMossBlock;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.item.GroveSporesItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

public class OvergrowthCondition extends LevelCondition {
  @Override
  protected CanonicalRepresentation getDefaultRepresentation() {
    return new CanonicalRepresentation(ModBlocks.CREEPING_GROVE_MOSS.get());
  }

  @Override
  public Set<BlockPos> test(BlockPos pos, Level level, @Nullable Player player) {
    BlockState state = level.getBlockState(pos);
    boolean creeping = state.is(ModBlocks.CREEPING_GROVE_MOSS.get());
    boolean water = state.getFluidState().isSource() && state.getFluidState().is(FluidTags.WATER);
    if (!water && !creeping) {
      return Collections.emptySet();
    }
    for (Direction dir : Direction.values()) {
      BlockPos offset = pos.above().relative(dir);
      if (water) {
        if (level.getFluidState(offset).isEmpty() && GroveSporesItem.canPlace(level, offset, Direction.UP)) {
          return Set.of(pos);
        }
      }
      offset = pos.relative(dir);
      if (creeping) {
        if (level.getFluidState(offset).isEmpty() && GroveSporesItem.canPlace(level, offset, Direction.UP)) {
          return Set.of(pos);
        }
      }
    }

    return Collections.emptySet();
  }
}
