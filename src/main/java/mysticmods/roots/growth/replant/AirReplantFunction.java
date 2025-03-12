package mysticmods.roots.growth.replant;

import mysticmods.roots.api.growth.ReplantFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public record AirReplantFunction () implements ReplantFunction {
  private static final BlockState AIR_STATE = Blocks.AIR.defaultBlockState();

  @Override
  public @Nullable BlockState replant(Level level, BlockPos blockPos, BlockState blockState, @Nullable IntegerProperty ageProperty, int maximumAge) {
    return AIR_STATE;
  }
}
