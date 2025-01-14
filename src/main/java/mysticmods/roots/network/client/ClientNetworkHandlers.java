package mysticmods.roots.network.client;

import mysticmods.roots.api.attachment.GrantStorage;
import mysticmods.roots.api.attachment.HerbStorage;
import mysticmods.roots.api.attachment.ReputationStorage;
import mysticmods.roots.api.attachment.SnapshotStorage;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class ClientNetworkHandlers {
  public static void setGrantStorage(GrantStorage storage) {
    if (Minecraft.getInstance() == null) {
      return;
    }
    Player player = Minecraft.getInstance().player;

    if (player == null) {
      return;
    }

    player.setData(ModAttachments.GRANT_STORAGE, storage);
  }

  public static void setHerbStorage(HerbStorage storage) {
    if (Minecraft.getInstance() == null) {
      return;
    }
    Player player = Minecraft.getInstance().player;

    if (player == null) {
      return;
    }

    player.setData(ModAttachments.HERB_STORAGE, storage);
  }

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

  public static void setReputationStorage(ReputationStorage storage) {
    if (Minecraft.getInstance() == null) {
      return;
    }
    Player player = Minecraft.getInstance().player;

    if (player == null) {
      return;
    }

    player.setData(ModAttachments.REPUTATION_STORAGE, storage);
  }
}
