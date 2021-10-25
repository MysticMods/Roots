package epicsquid.roots.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

public class PlayerSyncUtil {
  public static void syncPlayer(PlayerEntity player) {
    if (player.world.isRemote) {
      return;
    }

    syncPlayer((ServerPlayerEntity) player);
  }

  public static void syncPlayer(ServerPlayerEntity player) {
    player.sendContainerToPlayer(player.inventoryContainer);
    player.sendContainerToPlayer(player.openContainer);
  }
}
