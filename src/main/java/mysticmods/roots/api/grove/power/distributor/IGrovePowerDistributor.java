package mysticmods.roots.api.grove.power.distributor;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public interface IGrovePowerDistributor {
  int getMaxPower();

  int getUsedPower();

  void generateTick(ServerLevel level, BlockPos pos, BlockState state);

  void consumeTick(ServerLevel level, BlockPos pos, BlockState state);
}
