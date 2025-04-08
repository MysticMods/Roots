package mysticmods.roots.growth.growable;

import mysticmods.roots.api.growth.CanGrowFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public record AlwaysCanGrowUp() implements CanGrowFunction {
  @Override
  public boolean test(Level level, BlockPos blockPos, BlockState blockState, @Nullable IntegerProperty ageProperty, int maximumAge) {
    return level.isEmptyBlock(blockPos.above());
  }
}
