package mysticmods.roots.impl;

import mysticmods.roots.client.impl.ClientAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

public class Accessor {
  public static @Nullable RecipeManager getManager() {
    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    if (server == null || !server.isDedicatedServer()) {
      return ClientAccessor.getManager();
    }

    return ServerAccessor.getManager();
  }

  public static @Nullable Player getPlayer() {
    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    if (server == null || !server.isDedicatedServer()) {
      return ClientAccessor.getPlayer();
    }

    return null;
  }

  public static boolean isShiftKeyDown() {
    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    if (server == null || !server.isDedicatedServer()) {
      return ClientAccessor.isShiftKeyDown();
    }

    return false;
  }
}
