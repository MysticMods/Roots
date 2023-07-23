package mysticmods.roots.item;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.item.ICastingItem;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellInstance;
import mysticmods.roots.api.spell.SpellStorage;
import mysticmods.roots.client.ClientHooks;
import mysticmods.roots.init.ModLang;
import mysticmods.roots.network.Networking;
import mysticmods.roots.network.client.ClientBoundOpenLibraryPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CastingItem extends Item implements ICastingItem {
  public CastingItem(Properties pProperties) {
    super(pProperties);
  }

  @Override
  public UseAnim getUseAnimation(ItemStack pStack) {
    // TODO: Bow?
    return UseAnim.BOW;
  }

  @Override
  public int getUseDuration(ItemStack pStack) {
    return 72000;
  }

  @Override
  public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
    if (!(pLivingEntity instanceof Player pPlayer) || pLevel.isClientSide()) {
      return;
    }

    SpellStorage storage = SpellStorage.fromItem(pStack);
    if (storage == null) {
      pPlayer.stopUsingItem();
      return;
    }

    SpellInstance spell = storage.getSpell();
    if (spell == null) {
      pPlayer.stopUsingItem();
      return;
    }

    int ticks = pStack.getUseDuration() - pRemainingUseDuration;

    Costing costs = new Costing(spell);

    // TODO: Charge every tick instead of assuming 20 ticks will elapse properly
    if (ticks % 20 == 0) {
      if (!costs.canAfford(pPlayer, true)) {
        RootsAPI.LOG.info("Not enough herbs to continue casting: " + spell.getSpell().getName());
        pPlayer.stopUsingItem();
        return;
      }
    }

    spell.cast(pPlayer, pStack, pPlayer.getUsedItemHand(), costs, ticks);

    if (ticks % 20 == 0) {
      costs.charge(pPlayer);
    }
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
    ItemStack stack = pPlayer.getItemInHand(pUsedHand);

    if (pLevel.isClientSide()) {
      return InteractionResultHolder.consume(stack);
    }
    SpellStorage storage = SpellStorage.getOrCreate(stack);
    if (storage == null) {
      return InteractionResultHolder.fail(stack);
    }

    if (pPlayer.isShiftKeyDown()) {
      // TODO: Show spell library
      ClientBoundOpenLibraryPacket packet = new ClientBoundOpenLibraryPacket(pUsedHand);
      Networking.sendTo(packet, (ServerPlayer) pPlayer);
      return InteractionResultHolder.success(stack);
/*      storage.nextSpell();*/
    } else {
      SpellInstance spell = storage.getSpell();
      if (spell == null || !spell.canCast(pPlayer)) {
        return InteractionResultHolder.fail(stack);
      }

      // TODO: check costs
      Costing costing = new Costing(spell);
      if (!costing.canAfford(pPlayer, true)) {
        // TODO: display a warning
        RootsAPI.LOG.info("Not enough herbs to cast: " + spell.getSpell().getName());
        return InteractionResultHolder.fail(stack);
      }

      spell.setCooldown(pPlayer);

      if (spell.getType() == Spell.Type.INSTANT) {
        spell.cast(pPlayer, stack, pUsedHand, costing, -1);
        costing.charge(pPlayer);
      } else {
        pPlayer.startUsingItem(pUsedHand);
      }
    }

    if (storage.isDirty()) {
      storage.save(stack);
      pPlayer.setItemInHand(pUsedHand, stack);
    }

    return InteractionResultHolder.success(stack);
  }

  @Override
  public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
    super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    if (!pLevel.isClientSide()) {
      int dur = getUseDuration(pStack) - pTimeCharged;
      RootsAPI.LOG.info("Finished using after {} ticks {} seconds", dur, dur / 20);
    }
  }

  @Override
  // TODO: Replace with tag???
  public int getSlots() {
    return 5;
  }

  @Override
  public boolean isBarVisible(ItemStack pStack) {
    SpellStorage storage = SpellStorage.fromItem(pStack);
    if (storage == null) {
      return false;
    }

    return storage.getCooldown() > 0;
  }

  @Override
  public int getBarWidth(ItemStack pStack) {
    SpellStorage storage = SpellStorage.fromItem(pStack);
    if (storage == null) {
      return 0;
    }

    return Math.round(13.0F - (float) storage.getCooldown() * 13.0F / (float) storage.getMaxCooldown());
  }

  // TODO: This means spell cooldowns won't tick outside inventories
  @Override
  public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
    super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);

    SpellStorage storage = SpellStorage.fromItem(pStack);
    if (storage != null && storage.tick()) {
      storage.save(pStack);
    }
  }

  // TODO: This is probably over-simplified
  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
    return slotChanged || oldStack.getItem() != newStack.getItem();
  }

  @Override
  public Component getName(ItemStack pStack) {
    SpellStorage storage = SpellStorage.fromItem(pStack);
    if (storage != null) {
      SpellInstance spell = storage.getSpell();
      if (spell != null) {
        return Component.translatable("roots.item.staff.with_spell", spell.getSpell().getStyledName());
      }
    }

    return super.getName(pStack);
  }

  @Override
  public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
    super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

    SpellStorage storage = SpellStorage.fromItem(pStack);
    if (storage != null) {
      pTooltipComponents.add(Component.translatable("roots.tooltip.staff.selected", storage.getSlot() + 1));
      SpellInstance spell = storage.getSpell();
      pTooltipComponents.add(Component.literal(""));
      if (spell != null) {
        Costing cost = new Costing(spell);
        pTooltipComponents.add(spell.getSpell().getStyledName());
        // TODO: Put this in a better place
        for (Object2DoubleMap.Entry<Herb> entry : cost.getMinimumCost().object2DoubleEntrySet()) {
          Herb herb = entry.getKey();
          String herbCost = String.format("%.4f", entry.getDoubleValue());
          pTooltipComponents.add(Component.translatable("roots.tooltip.cost.herb_cost", herb.getStyledName(), Component.translatable("roots.tooltip.cost.cost_amount", herbCost)));
        }
      } else {
        pTooltipComponents.add(Component.translatable("roots.tooltip.staff.no_spell"));
      }
      pTooltipComponents.add(Component.literal(""));
      if (RootsAPI.getInstance().isShiftKeyDown()) {
        for (SpellStorage.Entry entry : storage.entryList()) {
          pTooltipComponents.add(Component.translatable("roots.tooltip.staff.spell_in_slot", entry.getSlot() + 1, entry.getSpell() == null ? Component.translatable("roots.tooltip.staff.no_spell") : entry.getSpell().getStyledName(), entry.getSlot() == storage.getSlot() ? Component.translatable("roots.tooltip.staff.is_selected") : Component.literal("")));
        }
        // TODO: list the other spells in the staff
      } else {
        pTooltipComponents.add(ModLang.holdShift());
      }
    }
  }
}
