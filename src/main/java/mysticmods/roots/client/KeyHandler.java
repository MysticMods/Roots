package mysticmods.roots.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.client.gui.layer.HudOverlay;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.network.server.*;
import mysticmods.roots.network.server.staff.ServerboundCycleSpellModePacket;
import mysticmods.roots.network.server.staff.ServerboundCycleStaffSpellPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;


@EventBusSubscriber(modid = RootsAPI.MODID, value = Dist.CLIENT)
public class KeyHandler {
  private static int cancelEffect = -1;
  private static boolean handledKeyThisTick = false;

  public static boolean isCancelingEffect() {
    return cancelEffect > 0;
  }

/*  @SubscribeEvent
  public static void onScrenKeyPressed (ScreenEvent.KeyReleased.Post event) {
    if (!event.isCanceled()) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.player.hasEffect(ModEffects.LIGHT_DRIFTER)) {
        return;
      }

      if (KeyBindings.OPEN_POUCH.matches(event.getKeyCode(), event.getScanCode()) && KeyBindings.OPEN_POUCH.isConflictContextAndModifierActive()) {
        tryOpenPouch(mc);
        event.setCanceled(true);
        return;
      }

      if (KeyBindings.OPEN_SPELL_LIBRARY.matches(event.getKeyCode(), event.getScanCode()) && KeyBindings.OPEN_SPELL_LIBRARY.isConflictContextAndModifierActive()) {
        tryOpenLibrary(mc);
        event.setCanceled(true);
        return;
      }
    }
  }*/

  @SubscribeEvent
  public static void onClientTick(ClientTickEvent.Post event) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player == null) {
      return;
    }

    boolean foundEffect = false;

    // Should happen at all times, probably check KeyInput events etc
    for (MobEffectInstance instance : mc.player.getActiveEffects()) {
      Holder<MobEffect> effect = instance.getEffect();
      if (effect.is(RootsTags.MobEffects.CANCELLABLE_EFFECTS)) {
        foundEffect = true;
        if (KeyBindings.CANCEL_EFFECT.isDown()) {
          if (effect.is(RootsTags.MobEffects.INSTANT_CANCEL_EFFECT)) {
            PacketDistributor.sendToServer(new ServerboundCancelEffectPacket(effect));
            cancelEffect = -1;
          } else {
            if (cancelEffect == -1) {
              cancelEffect = 1;
            } else {
              cancelEffect++;
            }
          }
        } else {
          cancelEffect = -1;
        }

        if (cancelEffect > 40) {
          PacketDistributor.sendToServer(new ServerboundCancelEffectPacket(effect));
          cancelEffect = -1;
        }
      }
    }

    if (!foundEffect) {
      cancelEffect = -1;
    }

    // Only in world
    if (KeyBindings.OPEN_FAKE_MENU.consumeClick() && HudOverlay.getStoredBlockPos() != null) {
      PacketDistributor.sendToServer(new ServerboundFakeMenuPacket(HudOverlay.getStoredBlockPos()));
      return;
    }

    // Only in world
    if (KeyBindings.CLEAR_CONTAINER.consumeClick() && HudOverlay.getStoredBlockPos() != null) {
      PacketDistributor.sendToServer(new ServerboundClearContainerPacket(HudOverlay.getStoredBlockPos()));
      return;
    }

    if (KeyBindings.OPEN_REPUTATION.consumeClick()) {
      if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
        RootsAPI.LOG.error("Opening reputation screen via keybind");
      }
      RootsClientHooks.openReputation();
      return;
    }

    if (mc.player.hasEffect(ModEffects.LIGHT_DRIFTER)) {
      return;
    }

    if (KeyBindings.OPEN_POUCH.consumeClick()) {
      tryOpenPouch(mc);
      return;
    }

    if (KeyBindings.OPEN_SPELL_LIBRARY.consumeClick()) {
      tryOpenLibrary(mc);
      return;
    }

    if (KeyBindings.CYCLE_STAFF_SPELL.consumeClick()) {
      tryCycleStaff(mc);
      return;
    }

    if (KeyBindings.CYCLE_SPELL_MODE.consumeClick()) {
      if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
        RootsAPI.LOG.error("Cycling adjustable spell data via keybind");
      }
    } else {
      return;
    }

    InteractionHand hand = InteractionHand.MAIN_HAND;
    ItemStack stack = mc.player.getMainHandItem();

    if (!stack.has(ModAttachments.SPELL_STORAGE)) {
      ItemStack tome = RootsAPI.getInstance().getTome(mc.player);
      if (!tome.isEmpty()) {
        if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
          RootsAPI.LOG.error("Cycling tome via keybind");
        }
        PacketDistributor.sendToServer(ServerboundCycleTomePacket.INSTANCE);
        return;
      }
    }

    if (!stack.has(ModAttachments.SPELL_STORAGE)) {
      stack = mc.player.getOffhandItem();
      hand = InteractionHand.OFF_HAND;
      if (!stack.has(ModAttachments.SPELL_STORAGE)) {
        if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
          RootsAPI.LOG.error("No spell storage found in main or off hand");
        }
        return;
      }
    }

    // The context enforces the spell selection
    SpellStorage storage = stack.get(ModAttachments.SPELL_STORAGE);
    if (storage == null) {
      if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
        RootsAPI.LOG.error("No spell storage found in item stack");
      }
      return;
    }

    ISpellInstance spell = storage.getCurrentSpell();
    if (spell == null) {
      if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
        RootsAPI.LOG.error("No spell selected in spell storage");
      }
      return;
    }

    if (spell.getCycleComponent() != null) {
      if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
        RootsAPI.LOG.error("Sending spell data change to server");
      }
      // TODO: This seems potentially dangerous, should just cycle the item not send this
      PacketDistributor.sendToServer(new ServerboundCycleSpellModePacket(hand, spell.getCycleComponent()));
    }
  }

  private static void tryOpenLibrary(Minecraft mc) {
    if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
      RootsAPI.LOG.error("Opening spell library via keybind");
    }
    int inventorySlot = -1;
    InteractionHand hand = InteractionHand.MAIN_HAND;
    ItemStack stack = mc.player.getItemInHand(hand);
    if (!stack.is(RootsTags.Items.CASTING_TOOLS) || !stack.has(ModAttachments.SPELL_STORAGE)) {
      hand = InteractionHand.OFF_HAND;
      stack = mc.player.getItemInHand(hand);
    }
    if (!stack.is(RootsTags.Items.CASTING_TOOLS) || !stack.has(ModAttachments.SPELL_STORAGE)) {
      Inventory inv = mc.player.getInventory();
      for (int i = 0; i < inv.getContainerSize(); i++) {
        stack = inv.getItem(i);
        if (stack.is(RootsTags.Items.CASTING_TOOLS) && stack.has(ModAttachments.SPELL_STORAGE)) {
          hand = null;
          inventorySlot = i;
          break;
        }
      }
    }
    if (!stack.is(RootsTags.Items.CASTING_TOOLS) || !stack.has(ModAttachments.SPELL_STORAGE)) {
      return;
    }

    RootsClientHooks.openLibrary(hand, inventorySlot);
  }

  private static void tryOpenPouch(Minecraft mc) {
    if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
      RootsAPI.LOG.error("Opening pouch via keybind");
    }
    PacketDistributor.sendToServer(ServerboundOpenPouchPacket.INSTANCE);
  }

  private static void tryCycleStaff (Minecraft mc) {
    if (mc.player == null) {
      return;
    }
    InteractionHand hand = mc.player.getMainHandItem().is(RootsTags.Items.CASTING_TOOLS) ? InteractionHand.MAIN_HAND : mc.player.getOffhandItem().is(RootsTags.Items.CASTING_TOOLS) ? InteractionHand.OFF_HAND : null;

    if (hand == null) {
      RootsAPI.LOG.error("Somehow managed to trigger the 'cycle staff' keybinding, but neither the held item nor the off-hand held item are tagged properly.");
    }

    PacketDistributor.sendToServer(new ServerboundCycleStaffSpellPacket(hand));
  }
}
