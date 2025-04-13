package mysticmods.roots.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.network.server.ServerboundOpenPouchPacket;
import mysticmods.roots.network.server.ServerboundSetSpellDataPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;


@EventBusSubscriber(modid = RootsAPI.MODID, value = Dist.CLIENT)
public class KeyHandler {
  @SubscribeEvent
  public static void onClientTick(ClientTickEvent.Post event) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player == null) {
      return;
    }

    while (KeyBindings.OPEN_POUCH.consumeClick()) {
      PacketDistributor.sendToServer(ServerboundOpenPouchPacket.INSTANCE);
      return;
    }

    while (KeyBindings.OPEN_SPELL_LIBRARY.consumeClick()) {
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

    while (KeyBindings.INCREASE_SPELL.consumeClick()) {
      op = 2;
    }
    while (KeyBindings.DECREASE_SPELL.consumeClick()) {
      op = 1;
    }
    while (KeyBindings.CYCLE_SPELL.consumeClick()) {
      op = 0;
    }

    if (op == -1) {
      return;
    }

    InteractionHand hand = InteractionHand.MAIN_HAND;
    ItemStack stack = mc.player.getMainHandItem();
    if (!stack.has(ModAttachments.SPELL_STORAGE)) {
      stack = mc.player.getOffhandItem();
      hand = InteractionHand.OFF_HAND;
      if (!stack.has(ModAttachments.SPELL_STORAGE)) {
        return;
      }
    }

    // The context enforces the spell selection
    SpellStorage storage = stack.get(ModAttachments.SPELL_STORAGE);
    if (storage == null) {
      return;
    }

    ISpellInstance spell = storage.getCurrentSpell();
    if (spell == null) {
      return;
    }

    Spell source = spell.asSpell();

    int index = source.getDataSlotValue(spell);
    if (op == 1 || op == 2) {
      if (index == -1) {
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

    if (newCurrent != current) {
      PacketDistributor.sendToServer(new ServerboundSetSpellDataPacket(hand, index, newCurrent));
    }
  }
}
