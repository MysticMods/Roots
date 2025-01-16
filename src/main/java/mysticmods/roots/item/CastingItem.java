package mysticmods.roots.item;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.item.ICastingItem;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

// TODO: Handle item colors
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
  public int getUseDuration(ItemStack pStack, LivingEntity entity) {
    return 72000;
  }

  @Override
  public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
    if (!(pLivingEntity instanceof Player pPlayer) || pLevel.isClientSide()) {
      return;
    }

    SpellStorage storage = pStack.get(ModAttachments.SPELL_STORAGE);
    if (storage == null) {
      pPlayer.stopUsingItem();
      return;
    }

    ISpellInstance spell = storage.getSpell(pStack.get(ModAttachments.CURRENT_SLOT));
    if (spell == null) {
      pPlayer.stopUsingItem();
      return;
    }

    int ticks = pStack.getUseDuration(pLivingEntity) - pRemainingUseDuration;

    Costing costs = new Costing(spell);

    // TODO: Charge every tick instead of assuming 20 ticks will elapse properly
    if (ticks % 20 == 0) {
      if (!costs.canAfford(pPlayer, true)) {
        RootsAPI.LOG.info("Not enough herbs to continue casting: {}", spell.getSpell().getName());
        pPlayer.stopUsingItem();
        return;
      }
    }

    if (spell.cast(pLevel, pPlayer, pStack, pPlayer.getUsedItemHand(), costs, ticks) != 0) {
      RootsAPI.LOG.error("Failed casting spell returned a cooldown on a channel: {}", spell.getSpell().getName());
    }

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

    SpellStorage storage = stack.get(ModAttachments.SPELL_STORAGE);
    if (storage == null) {
      return InteractionResultHolder.fail(stack);
    }

    int current = stack.get(ModAttachments.CURRENT_SLOT);
    int max = storage.maxSlot() - 1;

    if (pPlayer.isShiftKeyDown()) {
      int newSlot = current + 1;
      if (newSlot + 1 > max) {
        newSlot = 0;
      }

      if (newSlot != current) {
        stack.set(ModAttachments.CURRENT_SLOT, newSlot);
      }

      return InteractionResultHolder.success(stack);
    }

    ISpellInstance spell = storage.getSpell(current);
    if (spell == null || !spell.canCast(pPlayer)) {
      return InteractionResultHolder.fail(stack);
    }

    // TODO: check costs
    Costing costing = new Costing(spell);
    if (!costing.canAfford(pPlayer, true)) {
      // TODO: display a warning
      pPlayer.displayClientMessage(Component.translatable("roots.message.staff.missing_herbs", spell.getStyledName()), true);
      RootsAPI.LOG.info("Not enough herbs to cast: {}", spell.getSpell().getName());
      return InteractionResultHolder.fail(stack);
    }

    if (spell.getType() == Spell.Type.INSTANT) {
      int cooldown = spell.cast(pLevel, pPlayer, stack, pUsedHand, costing, -1);
      if (costing.charge(pPlayer)) {
        stack.set(ModAttachments.SPELL_STORAGE, storage.setCooldown(current, cooldown));
      }
    } else {
      pPlayer.startUsingItem(pUsedHand);
    }

    return InteractionResultHolder.success(stack);
  }

  @Override
  public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
    super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    if (!pLevel.isClientSide()) {
      int dur = getUseDuration(pStack, pLivingEntity) - pTimeCharged;
      RootsAPI.LOG.info("Finished using after {} ticks {} seconds", dur, dur / 20);
    }
  }

  @Override
  public boolean isBarVisible(ItemStack pStack) {
    SpellStorage storage = pStack.get(ModAttachments.SPELL_STORAGE);
    int currentSlot = pStack.get(ModAttachments.CURRENT_SLOT);
    return storage != null && storage.getCooldown(currentSlot) > 0;
  }

  @Override
  public int getBarWidth(ItemStack pStack) {
    SpellStorage storage = pStack.get(ModAttachments.SPELL_STORAGE);
    if (storage == null) {
      return 0;
    }

    int currentSlot = pStack.get(ModAttachments.CURRENT_SLOT);

    return Math.round(13.0F - (float) storage.getCooldown(currentSlot) * 13.0F / (float) storage.getMaxCooldown(currentSlot));
  }

  @Override
  public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
    super.inventoryTick(stack, level, entity, slotId, isSelected);

    SpellStorage storage = stack.get(ModAttachments.SPELL_STORAGE);
    if (storage != null) {
      stack.set(ModAttachments.SPELL_STORAGE, storage.tick());
    }
  }

  // TODO: This is probably over-simplified
  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
    return slotChanged || oldStack.getItem() != newStack.getItem();
  }

  @Override
  public Component getName(ItemStack pStack) {
    SpellStorage storage = pStack.get(ModAttachments.SPELL_STORAGE);
    if (storage != null) {
      ISpellInstance spell = storage.getSpell(pStack.get(ModAttachments.CURRENT_SLOT));
      if (spell != null) {
        return Component.translatable("roots.item.staff.with_spell", spell.getSpell().getStyledName());
      }
    }

    return super.getName(pStack);
  }

/*  @Override
  public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
    super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

    TooltipUtil.spellStaffTooltip(pTooltipComponents, pStack, pIsAdvanced);
  }*/
}
