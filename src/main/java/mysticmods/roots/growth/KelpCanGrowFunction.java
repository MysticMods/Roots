package mysticmods.roots.growth;

import mysticmods.roots.api.growth.CanGrowFunction;
import mysticmods.roots.init.ModTests;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;

public class KelpCanGrowFunction extends CanGrowFunction {
  @Override
  public boolean test(Level level, BlockPos blockPos, BlockState blockState, @Nullable IntegerProperty ageProperty, int maxmimumAge) {
    FluidState stateAbove = level.getFluidState(blockPos.above());
    if (stateAbove.is(Tags.Fluids.WATER) && stateAbove.isSource()) {
      return ModTests.AGE_CAN_GROW.get().test(level, blockPos, blockState, ageProperty, maxmimumAge);
    }

    return false;
  }
}
