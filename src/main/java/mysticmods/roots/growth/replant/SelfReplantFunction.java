package mysticmods.roots.growth.replant;

import mysticmods.roots.api.growth.ReplantFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public record SelfReplantFunction () implements ReplantFunction {
  @Override
  public @Nullable BlockState replant(Level level, BlockPos blockPos, BlockState blockState, @Nullable IntegerProperty ageProperty, int maximumAge) {
    return blockState;
  }
}
