package mysticmods.roots.growth.growable;

import mysticmods.roots.api.growth.CanGrowFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

// AI disclaimer:
// - ChatGPT was used to simplify this code
// I still feel like this logic is just too much to trigger so often
public record VinesCanSpreadFunction() implements CanGrowFunction {
  private static BooleanProperty getPropertyForFace(Direction face) {
    return VineBlock.PROPERTY_BY_DIRECTION.get(face);
  }

  private static boolean isAcceptableNeighbour(BlockGetter level, BlockPos pos, Direction face) {
    return MultifaceBlock.canAttachTo(level, face, pos, level.getBlockState(pos));
  }

  private static boolean hasHorizontalConnection(BlockState state) {
    return state.getValue(VineBlock.NORTH) || state.getValue(VineBlock.EAST) ||
        state.getValue(VineBlock.SOUTH) || state.getValue(VineBlock.WEST);
  }

  private static BlockState fillFaces(BlockState source, BlockState target) {
    for (Direction direction : Direction.Plane.HORIZONTAL) {
      BooleanProperty property = getPropertyForFace(direction);
      if (source.getValue(property)) {
        target = target.setValue(property, true);
      }
    }
    return target;
  }

  @Override
  public boolean test(Level level, BlockPos pos, BlockState state, @Nullable IntegerProperty ageProperty, int maximumAge) {
    if (!level.getGameRules().getBoolean(GameRules.RULE_DO_VINES_SPREAD)) {
      return false;
    }

    // Prevent vines from consuming too many ticks
    if (level.getRandom().nextBoolean()) {
      return false;
    }

    for (Direction direction : Direction.values()) {
      if (canSpread(level, pos, state, direction)) {
        return true;
      }
    }
    return false;
  }

  private boolean canSpread(Level level, BlockPos pos, BlockState state, Direction direction) {
    if (direction == Direction.UP && canGrowUp(level, pos, state)) {
      return true;
    }
    if (direction == Direction.DOWN && canGrowDown(level, pos, state)) {
      return true;
    }
    return canGrowHorizontally(level, pos, state, direction);
  }

  private boolean canGrowUp(Level level, BlockPos pos, BlockState state) {
    BlockPos above = pos.above();
    if (pos.getY() >= level.getMaxBuildHeight() - 1 || !level.isEmptyBlock(above)) {
      return false;
    }

    for (Direction dir : Direction.Plane.HORIZONTAL) {
      if (isAcceptableNeighbour(level, above.relative(dir), dir)) {
        return true;
      }
    }
    return hasHorizontalConnection(state);
  }

  private boolean canGrowDown(Level level, BlockPos pos, BlockState state) {
    BlockPos below = pos.below();
    BlockState belowState = level.getBlockState(below);
    if (!belowState.isAir() && !belowState.is(Blocks.VINE)) {
      return false;
    }
    return hasHorizontalConnection(fillFaces(state, belowState.isAir() ? Blocks.VINE.defaultBlockState() : belowState));
  }

  private boolean canGrowHorizontally(Level level, BlockPos pos, BlockState state, Direction direction) {
    if (!direction.getAxis().isHorizontal() || state.getValue(getPropertyForFace(direction))) {
      return false;
    }

    BlockPos targetPos = pos.relative(direction);
    BlockState targetState = level.getBlockState(targetPos);
    if (targetState.isAir() && checkSideGrowth(level, state, direction, targetPos)) {
      return true;
    }
    return isAcceptableNeighbour(level, targetPos, direction);
  }

  private boolean checkSideGrowth(Level level, BlockState state, Direction direction, BlockPos targetPos) {
    Direction left = direction.getClockWise();
    Direction right = direction.getCounterClockWise();
    boolean canSpreadLeft = state.getValue(getPropertyForFace(left)) && isAcceptableNeighbour(level, targetPos.relative(left), left);
    boolean canSpreadRight = state.getValue(getPropertyForFace(right)) && isAcceptableNeighbour(level, targetPos.relative(right), right);
    return canSpreadLeft || canSpreadRight;
  }
}

