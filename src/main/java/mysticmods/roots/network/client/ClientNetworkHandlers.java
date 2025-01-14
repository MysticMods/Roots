package mysticmods.roots.network.client;

import mysticmods.roots.api.attachment.SnapshotStorage;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class ClientNetworkHandlers {
  public static void setSnapshotStorage(SnapshotStorage storage) {
    if (Minecraft.getInstance() == null) {
      return;
    }
    Player player = Minecraft.getInstance().player;

    if (player == null) {
      return;
    }

    player.setData(ModAttachments.SNAPSHOT_STORAGE, storage);
  }
}
