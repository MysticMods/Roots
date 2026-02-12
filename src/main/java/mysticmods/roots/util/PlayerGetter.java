package mysticmods.roots.util;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.RootsClientHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
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

  public static ContainerLevelAccess getLevelAccess (BlockPos pos) {
    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    if (server != null && server.isSameThread()) {
      RootsAPI.LOG.error("Attempted to get level access on server thread, this is not allowed!");
      return ContainerLevelAccess.NULL;
    }

    return RootsClientHooks.getLevelAccess(pos);
  }

  public static ContainerLevelAccess getLevelAccess (RegistryFriendlyByteBuf buf) {
    if (buf == null) {
      return ContainerLevelAccess.NULL;
    }

    BlockPos pos = buf.readBlockPos();
    return getLevelAccess(pos);
  }
}
