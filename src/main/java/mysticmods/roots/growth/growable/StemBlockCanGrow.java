package mysticmods.roots.growth.growable;

import mysticmods.roots.api.growth.CanGrowFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public record StemBlockCanGrow () implements CanGrowFunction {
  @Override
  public boolean test(Level level, BlockPos blockPos, BlockState blockState, @Nullable IntegerProperty ageProperty, int maximumAge) {
    for (Direction dir : Direction.values()) {
      if (dir.getAxis().isVertical()) {
        continue;
      }

      BlockPos offset = blockPos.relative(dir);
      BlockState offsetState = level.getBlockState(offset);
      if (offsetState.canBeReplaced() || offsetState.isAir()) {
        return true;
      }
    }

    return false;
  }
}
