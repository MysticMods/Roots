package mysticmods.roots.api.action;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public interface GroveContext {
  ServerLevel level();
  ServerPlayer player();

}
