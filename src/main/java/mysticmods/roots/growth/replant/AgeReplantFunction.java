package mysticmods.roots.growth.replant;

import mysticmods.roots.api.growth.ReplantFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public record AgeReplantFunction () implements ReplantFunction {
  @Override
  @Nullable
  public BlockState replant(Level level, BlockPos blockPos, BlockState blockState, @Nullable IntegerProperty ageProperty, int maximumAge) {
    if (ageProperty == null) {
      return blockState;
    }

    if (!blockState.hasProperty(ageProperty)) {
      return null;
    }

    return blockState.setValue(ageProperty, 0);
  }
}
