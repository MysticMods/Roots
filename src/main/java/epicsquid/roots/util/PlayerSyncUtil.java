package epicsquid.roots.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PlayerSyncUtil {
  public static void syncPlayer(EntityPlayer player) {
    if (player.world.isRemote) {
      return;
    }

    syncPlayer((EntityPlayerMP) player);
  }

  public static void syncPlayer(EntityPlayerMP player) {
    player.sendContainerToPlayer(player.inventoryContainer);
    player.sendContainerToPlayer(player.openContainer);
  }
}
