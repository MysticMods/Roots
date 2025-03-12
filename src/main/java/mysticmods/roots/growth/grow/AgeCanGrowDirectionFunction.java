package mysticmods.roots.growth.grow;

import mysticmods.roots.api.growth.CanGrowFunction;
import mysticmods.roots.init.ModTests;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public record AgeCanGrowDirectionFunction(Direction dir) implements CanGrowFunction {
  @Override
  public boolean test(Level level, BlockPos blockPos, BlockState blockState, @Nullable IntegerProperty ageProperty, int maximumAge) {
    if (!level.isEmptyBlock(blockPos.relative(dir))) {
      return false;
    }

    return ModTests.AGE_CAN_GROW.get().test(level, blockPos, blockState, ageProperty, maximumAge);
  }
}
