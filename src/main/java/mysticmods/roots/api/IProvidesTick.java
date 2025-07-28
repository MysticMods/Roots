package mysticmods.roots.api;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IProvidesTick {
  default long getTickCount() {
    if (this instanceof BlockEntity blockEntity && blockEntity.getLevel() instanceof ServerLevel serverLevel) {
      return serverLevel.getGameTime();
    } else if (this instanceof Entity entity && entity.level() instanceof ServerLevel serverLevel) {
      return serverLevel.getGameTime();
    } else {
      return -1L;
    }
  }
}
