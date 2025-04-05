package mysticmods.roots.growth.harvestable;

import mysticmods.roots.api.growth.CanHarvestFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public record CanHarvestTwoBlockPlantAge() implements CanHarvestFunction {
  @Override
  public boolean test(Level level, BlockPos blockPos, BlockState blockState, @Nullable IntegerProperty ageProperty, int maximumAge) {
    if (ageProperty == null) {
      return false;
    }

    BlockPos below = blockPos.below();
    BlockState belowState = level.getBlockState(below);
    if (belowState.is(blockState.getBlock())) {
      return false;
    }

    BlockPos above = blockPos.above();
    BlockState aboveState = level.getBlockState(above);
    if (!aboveState.is(blockState.getBlock())) {
      return false;
    }

    if (!blockState.hasProperty(ageProperty) || !aboveState.hasProperty(ageProperty)) {
      return false;
    }

    return blockState.getValue(ageProperty) == maximumAge && aboveState.getValue(ageProperty) == maximumAge;
  }
}
