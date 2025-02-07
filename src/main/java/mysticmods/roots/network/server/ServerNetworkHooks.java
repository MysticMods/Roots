package mysticmods.roots.network.server;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class ServerNetworkHooks {

  public static void setSpellSlot(Player player, @Nullable InteractionHand hand, int inventorySlot, int staffSlot, Spell spell) {
    ItemStack stack;
    if (hand != null) {
      stack = player.getItemInHand(hand);
    } else {
      stack = player.getInventory().getItem(inventorySlot);
    }
    if (!stack.is(RootsTags.Items.CASTING_TOOLS) || !stack.has(ModAttachments.SPELL_STORAGE)) {
      return;
    }
    SpellStorage existing = stack.get(ModAttachments.SPELL_STORAGE);
    if (existing == null) {
      return;
    }
    // TODO: Validate that the player has the spell
    stack.set(ModAttachments.SPELL_STORAGE, existing.setSpell(staffSlot, spell));
  }

  public static void swapSpellSlots(Player player, @Nullable InteractionHand hand, int inventorySlot, int slot1, int slot2) {
    ItemStack stack;
    if (hand != null) {
      stack = player.getItemInHand(hand);
    } else {
      stack = player.getInventory().getItem(inventorySlot);
    }
    if (!stack.is(RootsTags.Items.CASTING_TOOLS) || !stack.has(ModAttachments.SPELL_STORAGE)) {
      return;
    }
    SpellStorage existing = stack.get(ModAttachments.SPELL_STORAGE);
    if (existing == null) {
      return;
    }
    SpellStorage newStorage = existing.swapSlots(slot1, slot2);
    stack.set(ModAttachments.SPELL_STORAGE, newStorage);
  }
}
