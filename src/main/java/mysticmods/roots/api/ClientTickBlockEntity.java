package mysticmods.roots.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ClientTickBlockEntity {
  void clientTick (Level pLevel, BlockPos pPos, BlockState pState);
}
