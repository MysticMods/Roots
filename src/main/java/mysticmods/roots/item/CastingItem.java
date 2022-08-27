package mysticmods.roots.item;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.item.ICastingItem;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellInstance;
import mysticmods.roots.api.spell.SpellStorage;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

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

    SpellInstance spell = storage.get();
    if (spell == null) {
      pPlayer.stopUsingItem();
      return;
    }
    int ticks = pStack.getUseDuration() - pRemainingUseDuration;

    Costing costs = new Costing(spell);

    // Cost is triggered every second
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
    SpellStorage storage = SpellStorage.fromItem(stack);
    if (storage == null) {
      return InteractionResultHolder.fail(stack);
    }

    if (pPlayer.isCrouching()) {
      storage.next();
    } else {
      SpellInstance spell = storage.get();
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
  public int getSlots() {
    return 5;
  }
}
