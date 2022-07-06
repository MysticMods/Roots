package mysticmods.roots.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ServerTickBlockEntity {
  void serverTick(Level pLevel, BlockPos pPos, BlockState pState);
}
