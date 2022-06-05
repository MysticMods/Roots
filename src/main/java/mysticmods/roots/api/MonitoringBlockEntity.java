package mysticmods.roots.api;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public interface MonitoringBlockEntity {
  void notify (ServerLevel pLevel, BlockPos pPos);
}
