package mysticmods.roots.growth;

import mysticmods.roots.api.growth.CanGrowFunction;
import mysticmods.roots.init.ModTests;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class CactusCanGrowFunction extends CanGrowFunction {
  @Override
  public boolean test(Level level, BlockPos pos, BlockState blockState, @Nullable IntegerProperty ageProperty, int maxmimumAge) {
    // Hard-coded as 3 currently
    int i = 1;

    while (level.getBlockState(pos.below(i)).is(blockState.getBlock())) {
      i++;
    }

    if (i < 3) {
      if (level.isEmptyBlock(pos.above())) {
        return true;
      }
      //return ModTests.AGE_CAN_GROW.get().test(level, pos, blockState, ageProperty, maxmimumAge);
    }

    return false;
  }
}
