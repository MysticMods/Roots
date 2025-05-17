package mysticmods.roots.client;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import mysticmods.roots.api.attachment.GrantStorage;
import mysticmods.roots.api.attachment.HerbStorage;
import mysticmods.roots.api.attachment.ReputationStorage;
import mysticmods.roots.api.attachment.SnapshotStorage;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.gui.layer.HerbLayer;
import mysticmods.roots.client.gui.screen.ReputationScreen;
import mysticmods.roots.client.gui.screen.StaffScreen;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.item.TokenItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class RootsClientHooks {
  public static void setGrantStorage(GrantStorage storage) {
    if (Minecraft.getInstance() == null) {
      return;
    }
    Player player = Minecraft.getInstance().player;

    if (player == null) {
      return;
    }

    GrantStorage oldStorage = player.getData(ModAttachments.GRANT_STORAGE);
    oldStorage.difference(storage);

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

  public static void openReputation() {
    ReputationScreen.open();
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

  public static void discardBlockEntityAttachment(String attachmentType, long entity) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft == null || minecraft.level == null) {
      return;
    }

    BlockEntity actualEntity = minecraft.level.getBlockEntity(BlockPos.of(entity));
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

  public static void setHerbCount(Object2DoubleMap<Herb> map) {
    for (Object2DoubleMap.Entry<Herb> entry : map.object2DoubleEntrySet()) {
      HerbLayer.updateHerb(entry.getKey(), entry.getDoubleValue());
    }
  }

  public static Component getStaffKeyBind() {
    return KeyBindings.OPEN_SPELL_LIBRARY.getKey().getDisplayName();
  }

  public static Component getPouchKeyBind () {
    return KeyBindings.OPEN_POUCH.getKey().getDisplayName();
  }

  public static void appendTokenHoverText(TokenItem item, ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
    Minecraft minecraft = Minecraft.getInstance();

    if (minecraft.screen instanceof StaffScreen || minecraft.player == null) {
      return;
    }


    GrantStorage grants = minecraft.player.getData(ModAttachments.GRANT_STORAGE);
    if (item instanceof TokenItem.SpellTokenItem spellTokenItem) {
      tooltipComponents.add(Component.empty());
      if (grants.hasSpell(spellTokenItem.getSpell())) {
        tooltipComponents.add(Component.translatable("roots.tooltip.token.unlocked"));
      } else {
        tooltipComponents.add(Component.translatable("roots.tooltip.token.unlock"));
      }
    }
  }
}
