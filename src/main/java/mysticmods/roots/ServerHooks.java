package mysticmods.roots;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellInstance;
import mysticmods.roots.api.spell.SpellStorage;
import mysticmods.roots.network.Networking;
import mysticmods.roots.network.client.ClientBoundUpdateStaffStackPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class ServerHooks {
  private static SpellStorage getSpellStorage (ItemStack stack) {
    if (!stack.is(RootsTags.Items.CASTING_TOOLS)) {
      throw new IllegalStateException("not a casting tool " + stack);
    }
    return SpellStorage.fromItem(stack);
  }

  private static void validateSlot (SpellStorage storage, int slot) {
    if (slot < 0 || slot >= storage.size()) {
      throw new IllegalStateException("out of range: " + slot + " for size " + storage.size());
    }
  }

  private static void validateSlot (SpellStorage storage, int slot1, int slot2) {
    validateSlot(storage, slot1);
    validateSlot(storage, slot2);
  }

  public static void swapSlots(ServerPlayer player, InteractionHand hand, int slot1, int slot2) {
    ItemStack stack = player.getItemInHand(hand);
    SpellStorage storage = getSpellStorage(stack);
    if (storage == null) {
      return;
    }
    validateSlot(storage, slot1, slot2);

    SpellInstance first = storage.getSpell(slot1);
    SpellInstance second = storage.getSpell(slot2);
    if (first == null && second == null) {
      return;
    }

    if (first == null) {
      storage.setSpell(slot1, second);
      storage.clearSpell(slot2);
    } else if (second == null) {
      storage.clearSpell(slot1);
      storage.setSpell(slot2, first);
    } else {
      storage.setSpell(slot1, second);
      storage.setSpell(slot2, first);
    }

    updateStack(player, stack, hand, storage);
  }

  public static void insertSpell(ServerPlayer player, InteractionHand hand, int slot, Spell spell) {
    ItemStack stack = player.getItemInHand(hand);
    SpellStorage storage = getSpellStorage(stack);
    if (storage == null) {
      return;
    }
    validateSlot(storage, slot);

    storage.setSpell(slot, spell);

    updateStack(player, stack, hand, storage);
  }

  private static void updateStack (ServerPlayer player, ItemStack stack, InteractionHand hand, SpellStorage storage) {
    storage.save(stack);
    player.setItemInHand(hand, stack);
    ClientBoundUpdateStaffStackPacket packet = new ClientBoundUpdateStaffStackPacket(stack);
    Networking.sendTo(packet, player);
  }
}
