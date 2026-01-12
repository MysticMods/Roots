package mysticmods.roots.network.server;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.attachment.GrantStorage;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.item.GramaryItem;
import mysticmods.roots.item.PouchItem;
import mysticmods.roots.network.client.ClientboundChangeTomeMode;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.List;

public class ServerNetworkHooks {
  public static void openPouch(Player player) {
    List<ItemStack> pouches = RootsAPI.getInstance().getPouches(player);
    if (pouches.isEmpty()) {
      return;
    }

    ItemStack stack = pouches.getFirst();

    if (!(stack.getItem() instanceof PouchItem pouchItem)) {
      return;
    }

    player.openMenu(pouchItem.getMenuProvider().createMenu(stack));
  }

  public static void setSpellData(Player player, InteractionHand hand, int index, int value) {
    ItemStack stack = player.getItemInHand(hand);
    if (!stack.is(RootsTags.Items.CASTING_TOOLS) || !stack.has(ModAttachments.SPELL_STORAGE)) {
      if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
        RootsAPI.LOG.error("No spell storage found in item stack");
      }
      return;
    }
    SpellStorage existing = stack.get(ModAttachments.SPELL_STORAGE);
    if (existing == null) {
      if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
        RootsAPI.LOG.error("No spell storage found in item stack");
      }
      return;
    }
    ISpellInstance instance = existing.getCurrentSpell();
    if (instance == null) {
      if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
        RootsAPI.LOG.error("No current spell found in spell storage");
      }
      return;
    }

    int spellSlot = existing.currentSlot();

    Spell spell = instance.asSpell();
    if (index != 0 && value > spell.getDataMaximumValue(index)) {
      if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
        RootsAPI.LOG.error("Value {} exceeds maximum for index {} in spell {}", value, index, spell.getDescriptionId());
      }
      return;
    }

    SpellStorage newStorage = existing.setData(spellSlot, index, value);
    if (!newStorage.equals(existing)) {
      stack.set(ModAttachments.SPELL_STORAGE, newStorage);
      if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
        RootsAPI.LOG.error("Setting spell data for spell {} at index {} to value {}", spell.getDescriptionId(), index, value);
      }
      player.displayClientMessage(spell.describeData(index, value), true);
    }
  }

  public static void setSpellSlot(Player player, @Nullable InteractionHand hand, int inventorySlot, int staffSlot, Spell spell) {
    ItemStack stack;
    if (hand != null) {
      stack = player.getItemInHand(hand);
    } else {
      stack = player.getInventory().getItem(inventorySlot);
    }
    if (!stack.is(RootsTags.Items.CASTING_TOOLS) || !stack.has(ModAttachments.SPELL_STORAGE)) {
      if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
        RootsAPI.LOG.error("No spell storage found in item stack");
      }
      return;
    }
    SpellStorage existing = stack.get(ModAttachments.SPELL_STORAGE);
    if (existing == null) {
      if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
        RootsAPI.LOG.error("No spell storage found in item stack");
      }
      return;
    }

    GrantStorage grants = player.getData(ModAttachments.GRANT_STORAGE);
    if (grants == null || !grants.hasSpell(spell)) {
      player.displayClientMessage(Component.translatable("roots.message.spell.not_granted", spell.getDescriptionId()), true);
      return;
    }
    // TODO: Validate that the player has the spell
    stack.set(ModAttachments.SPELL_STORAGE, existing.setSpell(staffSlot, spell));
    if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
      RootsAPI.LOG.error("Setting spell slot {} to {}", staffSlot, spell.getDescriptionId());
    }
  }

  public static void clearSpellSlot(Player player, @Nullable InteractionHand hand, int inventorySlot, int staffSlot) {
    ItemStack stack;
    if (hand != null) {
      stack = player.getItemInHand(hand);
    } else {
      stack = player.getInventory().getItem(inventorySlot);
    }
    if (!stack.is(RootsTags.Items.CASTING_TOOLS) || !stack.has(ModAttachments.SPELL_STORAGE)) {
      if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
        RootsAPI.LOG.error("No spell storage found in item stack");
      }
      return;
    }
    SpellStorage existing = stack.get(ModAttachments.SPELL_STORAGE);
    if (existing == null) {
      if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
        RootsAPI.LOG.error("No spell storage found in item stack");
      }
      return;
    }

    // TODO: Validate that the player has the spell
    stack.set(ModAttachments.SPELL_STORAGE, existing.clearSpell(staffSlot));
    if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
      RootsAPI.LOG.error("Setting spell slot {} to null", staffSlot);
    }
  }

  public static void swapSpellSlots(Player player, @Nullable InteractionHand hand, int inventorySlot, int slot1, int slot2) {
    ItemStack stack;
    if (hand != null) {
      stack = player.getItemInHand(hand);
    } else {
      stack = player.getInventory().getItem(inventorySlot);
    }
    if (!stack.is(RootsTags.Items.CASTING_TOOLS) || !stack.has(ModAttachments.SPELL_STORAGE)) {
      if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
        RootsAPI.LOG.error("No spell storage found in item stack");
      }
      return;
    }
    SpellStorage existing = stack.get(ModAttachments.SPELL_STORAGE);
    if (existing == null) {
      if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
        RootsAPI.LOG.error("No spell storage found in item stack");
      }
      return;
    }
    SpellStorage newStorage = existing.swapSlots(slot1, slot2);
    stack.set(ModAttachments.SPELL_STORAGE, newStorage);
    if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
      RootsAPI.LOG.error("Swapping spell slots {} and {} in spell storage", slot1, slot2);
    }
  }

  public static void cycleTome(Player player) {
    ItemStack tome = RootsAPI.getInstance().getTome(player);
    if (tome.isEmpty()) {
      if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
        RootsAPI.LOG.error("Attempted to cycle tome, but no tome was found in the player's inventory.");
      }
      return;
    }

    GramaryItem.GramaryMode current = GramaryItem.getMode(tome);
    GramaryItem.GramaryMode newMode = current.cycle();
    tome.set(ModAttachments.GRAMARY_MODE, newMode);
    if (ConfigManager.DEBUG_KEYBINDS.getAsBoolean()) {
      RootsAPI.LOG.error("Cycling tome to from mode {} to mode {}", current, newMode);
    }

    if (tome != player.getMainHandItem() && tome != player.getOffhandItem()) {
      //PacketDistributor.sendToPlayer((ServerPlayer) player, ClientboundChangeTomeMode.INSTANCE);
      // This is handled directly in the Curios integration in CuriosEventHandler.
      // As otherwise the itemstack sync happens later than this packet.
    } else if (current != newMode){
      player.getServer().tell(new TickTask(2, () -> {
        PacketDistributor.sendToPlayer((ServerPlayer) player, new ClientboundChangeTomeMode(true));
      }));
    }
  }

  public static void cancelLightDrifter(Player player) {
    player.removeEffect(ModEffects.LIGHT_DRIFTER);
  }

  public static void cancelEffect(Player player, Holder<MobEffect> effect) {
    if (!effect.is(RootsTags.MobEffects.CANCELLABLE_EFFECTS)) {
      return;
    }

    player.removeEffect(effect);
  }
}
