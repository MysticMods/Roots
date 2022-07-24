package mysticmods.roots.api.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface DualTickBlockEntity extends ClientTickBlockEntity, ServerTickBlockEntity {
  @Override
  default void clientTick(Level pLevel, BlockPos pPos, BlockState pState) {
    dualTick(pLevel, pPos, pState, true);
  }

  @Override
  default void serverTick(Level pLevel, BlockPos pPos, BlockState pState) {
    dualTick(pLevel, pPos, pState, false);
  }

  void dualTick(Level pLevel, BlockPos pPos, BlockState pState, boolean isClient);
}
