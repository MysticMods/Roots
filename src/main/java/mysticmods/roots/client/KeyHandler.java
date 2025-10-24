package mysticmods.roots.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.network.server.ServerboundCancelEffectPacket;
import mysticmods.roots.network.server.ServerboundCycleTomePacket;
import mysticmods.roots.network.server.ServerboundOpenPouchPacket;
import mysticmods.roots.network.server.ServerboundSetSpellDataPacket;
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

  public static boolean isCancelingEffect() {
    return cancelEffect > 0;
  }

  @SubscribeEvent
  public static void onClientTick(ClientTickEvent.Post event) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player == null) {
      return;
    }

    boolean foundEffect = false;

    for (MobEffectInstance instance : mc.player.getActiveEffects()) {
      Holder<MobEffect> effect = instance.getEffect();
      if (effect.is(RootsTags.MobEffects.CANCELLABLE_EFFECTS)) {
        foundEffect = true;
        // TODO: Should this be consumeClick?
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
      if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
        RootsAPI.LOG.error("Opening pouch via keybind");
      }
      PacketDistributor.sendToServer(ServerboundOpenPouchPacket.INSTANCE);
      return;
    }

    if (KeyBindings.OPEN_SPELL_LIBRARY.consumeClick()) {
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
      return;
    }

    int op = -1;

    if (KeyBindings.INCREASE_SPELL.consumeClick()) {
      if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
        RootsAPI.LOG.error("Increasing spell data via keybind");
      }
      op = 2;
    }
    if (KeyBindings.DECREASE_SPELL.consumeClick()) {
      if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
        RootsAPI.LOG.error("Decreasing spell data via keybind");
      }
      op = 1;
    }
    if (KeyBindings.CYCLE_ADJUSTABLE.consumeClick()) {
      if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
        RootsAPI.LOG.error("Cycling adjustable spell data via keybind");
      }
      op = 0;
    }

    if (op == -1) {
      return;
    }

    InteractionHand hand = InteractionHand.MAIN_HAND;
    ItemStack stack = mc.player.getMainHandItem();

    if (op == 0 && !stack.has(ModAttachments.SPELL_STORAGE)) {
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

    Spell source = spell.asSpell();

    int index = source.getDataSlotValue(spell);
    if (op == 1 || op == 2) {
      if (index == -1) {
        if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
          RootsAPI.LOG.error("Spell does not have adjustable data");
        }
        return;
      }
    } else if (op == 0) {
      index = 0;
    }

    int max = source.getDataMaximumValue(index);
    int current = source.getDataValue(spell, index);
    int newCurrent = current;

    if (op == 2 && current < max) {
      newCurrent++;
    } else if (op == 1 && newCurrent > 0) {
      newCurrent--;
    } else if (op == 0) {
      if (current == max) {
        newCurrent = 1;
      } else {
        newCurrent++;
      }
    }

    if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
      RootsAPI.LOG.error("Spell data change: hand={}, index={}, current={}, newCurrent={}", hand, index, current, newCurrent);
    }

    if (newCurrent != current) {
      if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
        RootsAPI.LOG.error("Sending spell data change to server");
      }
      PacketDistributor.sendToServer(new ServerboundSetSpellDataPacket(hand, index, newCurrent));
    }
  }
}
