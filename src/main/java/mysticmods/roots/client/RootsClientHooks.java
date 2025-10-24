package mysticmods.roots.client;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.attachment.*;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.client.gui.layer.HerbOverlay;
import mysticmods.roots.client.gui.screen.ReputationScreen;
import mysticmods.roots.client.gui.screen.StaffScreen;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.entity.other.LightDrifterEntity;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.item.TokenItem;
import mysticmods.roots.mixin.client.accessor.AccessorMixinGui;
import mysticmods.roots.recipe.AnimalHarvestRecipe;
import mysticmods.roots.util.LightDrifterUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
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

public class RootsClientHooks {
  public static void clearTooltipItem() {
    ((AccessorMixinGui) Minecraft.getInstance().gui).rootsSetLastToolHighlight(ItemStack.EMPTY);
  }

  public static void setAnimalHarvestRecipes(List<AnimalHarvestRecipe> recipes) {
    if (ConfigManager.DEBUG_JEI.getAsBoolean()) {
      RootsAPI.LOG.error("Setting animal harvest recipes on client: {}", recipes);
    }
    ClientRecipes.ANIMAL_HARVEST_RECIPES = recipes;
  }

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

  public static void setCooldownStorage(CooldownStorage storage) {
    if (Minecraft.getInstance() == null) {
      return;
    }
    Player player = Minecraft.getInstance().player;

    if (player == null) {
      return;
    }

    player.setData(ModAttachments.COOLDOWN_STORAGE, storage);
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
      HerbOverlay.updateHerb(entry.getKey(), entry.getDoubleValue());
    }
  }

  public static Component getStaffKeyBind() {
    return KeyBindings.OPEN_SPELL_LIBRARY.getKey().getDisplayName();
  }

  public static Component getPouchKeyBind() {
    return KeyBindings.OPEN_POUCH.getKey().getDisplayName();
  }

  public static void appendTokenHoverText(TokenItem item, ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
    Minecraft minecraft = Minecraft.getInstance();

    if (minecraft.screen instanceof StaffScreen) {
      if (stack.has(ModAttachments.DELETABLE)) {
        tooltipComponents.add(Component.empty());
        tooltipComponents.add(Component.translatable("roots.tooltip.token.delete"));
      }
      return;
    } else if (minecraft.player == null) {
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

  @Nullable
  public static Player getPlayer() {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft == null) {
      return null;
    }
    return minecraft.player;
  }

  public static void stopUsingItem(Screen newScreen) {
    if (Minecraft.getInstance().gameMode != null && Minecraft.getInstance().player != null) {
      Minecraft.getInstance().gameMode.releaseUsingItem(Minecraft.getInstance().player);
      Minecraft.getInstance().setScreen(newScreen);
    }
  }

  public static void setLightDrifterSync(int entityId) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player == null) {
      return;
    }

    if (!mc.player.hasEffect(ModEffects.LIGHT_DRIFTER) || entityId == -1) {
      // Entity will be discarded by the server
      mc.player.removeData(ModAttachments.DRIFTER_CLIENT_STORAGE);
      mc.setCameraEntity(mc.player);
      return;
    }

    LightDrifterStorage storage = mc.player.getData(ModAttachments.DRIFTER_CLIENT_STORAGE);
    storage.setEntityId(entityId);
    // ??? Necessary? TODO
    mc.player.setData(ModAttachments.DRIFTER_CLIENT_STORAGE, storage);

    LightDrifterEntity drifter = LightDrifterUtil.getLightDrifterEntity(mc.player);
    if (drifter != null) {
      mc.setCameraEntity(drifter);
    }
  }

  public static void stopPlayerMovement() {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player != null) {
      mc.player.zza = 0;
      mc.player.xxa = 0;
      mc.player.setJumping(false);
    }
  }

  public static void showTomeTooltip() {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player != null) {
      if (!mc.player.getMainHandItem().is(RootsTags.Items.GRAMARIES) && !mc.player.getOffhandItem()
          .is(RootsTags.Items.GRAMARIES)) {
        ItemStack tome = RootsAPI.getInstance().getTome(mc.player);
        if (tome.isEmpty()) {
          return;
        }

        ((AccessorMixinGui) Minecraft.getInstance().gui).rootsSetLastToolHighlight(tome);
        ((AccessorMixinGui) Minecraft.getInstance().gui).rootsSetToolHighlightTimer((int) (40.0 * mc.options.notificationDisplayTime()
            .get()));
      }
    }
  }
}
