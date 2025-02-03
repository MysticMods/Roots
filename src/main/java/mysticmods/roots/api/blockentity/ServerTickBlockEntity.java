package mysticmods.roots.api.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ServerTickBlockEntity {
  void serverTick(ServerLevel pLevel, BlockPos pPos, BlockState pState);
}
