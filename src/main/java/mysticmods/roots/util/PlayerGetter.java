package mysticmods.roots.util;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.RootsClientHooks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;

public class PlayerGetter {
  @Nullable
  public static Player getPlayer() {
    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    if (server != null && server.isSameThread()) {
      RootsAPI.LOG.error("Attempted to get player on server thread, this is not allowed!");
      return null;
    }

    return RootsClientHooks.getPlayer();
  }
}
