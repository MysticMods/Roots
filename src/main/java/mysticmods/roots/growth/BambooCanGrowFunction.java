package mysticmods.roots.growth;

import mysticmods.roots.api.growth.CanGrowFunction;
import mysticmods.roots.init.ModTests;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class BambooCanGrowFunction extends CanGrowFunction {
  @Override
  public boolean test(Level level, BlockPos blockPos, BlockState blockState, @Nullable IntegerProperty ageProperty, int maxmimumAge) {
    if (!level.isEmptyBlock(blockPos.above())) {
      return false;
    }
    return ModTests.AGE_CAN_GROW.get().test(level, blockPos, blockState, ageProperty, maxmimumAge);
  }
}
