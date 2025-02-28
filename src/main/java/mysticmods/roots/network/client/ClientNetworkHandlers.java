package mysticmods.roots.network.client;

import mysticmods.roots.api.attachment.GrantStorage;
import mysticmods.roots.api.attachment.HerbStorage;
import mysticmods.roots.api.attachment.ReputationStorage;
import mysticmods.roots.api.attachment.SnapshotStorage;
import mysticmods.roots.client.gui.screen.StaffScreen;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

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

  public static void openLibrary(@Nullable InteractionHand hand, int inventorySlot) {
    StaffScreen.open(hand, inventorySlot);
  }

  public static void setEntitySnapshot(int entity, SnapshotStorage storage) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft == null || minecraft.level == null) {
      return;
    }

    Entity actualEntity = minecraft.level.getEntity(entity);
    if (actualEntity != null) {
      actualEntity.setData(ModAttachments.SNAPSHOT_STORAGE, storage);
    }
  }

  public static void discardEntityAttachment(String attachmentType, int entity) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft == null || minecraft.level == null) {
      return;
    }

    Entity actualEntity = minecraft.level.getEntity(entity);
    if (actualEntity == null) {
      return;
    }
    if (attachmentType.equals(ModAttachments.SNAPSHOT_STORAGE.getKey().location().toString())) {
      actualEntity.removeData(ModAttachments.SNAPSHOT_STORAGE);
    } else if (attachmentType.equals(ModAttachments.GRANT_STORAGE.getKey().location().toString())) {
      actualEntity.removeData(ModAttachments.GRANT_STORAGE);
    } else if (attachmentType.equals(ModAttachments.HERB_STORAGE.getKey().location().toString())) {
      actualEntity.removeData(ModAttachments.HERB_STORAGE);
    } else if (attachmentType.equals(ModAttachments.REPUTATION_STORAGE.getKey().location().toString())) {
      actualEntity.removeData(ModAttachments.REPUTATION_STORAGE);
    }
  }

  public static void syncGeas(int entityId, boolean value) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft == null || minecraft.level == null) {
      return;
    }

    Entity actualEntity = minecraft.level.getEntity(entityId);
    if (actualEntity != null) {
      actualEntity.setData(ModAttachments.HAS_GEAS, value);
    }
  }
}
