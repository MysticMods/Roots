package mysticmods.roots.growth.harvest;

import mysticmods.roots.api.growth.CanHarvestFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public record SingleCropAgeCanHarvest() implements CanHarvestFunction {
  @Override
  public boolean test(Level level, BlockPos blockPos, BlockState blockState, @Nullable IntegerProperty ageProperty, int maximumAge) {
    if (ageProperty == null) {
      return false;
    }
    if (!blockState.hasProperty(ageProperty)) {
      return false;
    }
    int age = blockState.getValue(ageProperty);
    if (age < maximumAge) {
      return false;
    }

    // Single crop, so check above and below
    BlockPos below = blockPos.below();
    BlockState belowState = level.getBlockState(below);

    if (belowState.is(blockState.getBlock())) {
      // If the block below is the same type, then we need a special case for harvesting
      return false;
    }

    BlockPos above = blockPos.above();
    BlockState aboveState = level.getBlockState(above);

    if (aboveState.is(blockState.getBlock())) {
      // If the block above is the same type, then we also need a special case for harvesting
      return false;
    }

    return true;
  }
}
