package mysticmods.roots.growth.harvest;

import mysticmods.roots.api.growth.CanHarvestFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public record BambooCanHarvestFunction () implements CanHarvestFunction {
  @Override
  public boolean test(Level level, BlockPos blockPos, BlockState blockState, @Nullable IntegerProperty ageProperty, int maximumAge) {
    BlockPos below = blockPos.below();
    BlockState stateBelow = level.getBlockState(below);

    if (stateBelow.is(blockState.getBlock())) {
      return false;
    }

    // You can only harvest the lowest block of bamboo
    return true;
  }
}
