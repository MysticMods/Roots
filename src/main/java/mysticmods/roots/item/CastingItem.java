package mysticmods.roots.item;

import mysticmods.roots.action.SpellCastAction;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.RootsClientHooks;
import mysticmods.roots.init.ModActions;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.network.client.fx.CastChannelFXPacket;
import mysticmods.roots.network.client.fx.CastChannelFailFXPacket;
import mysticmods.roots.network.client.fx.CastChannelTargetFXPacket;
import mysticmods.roots.util.TooltipUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class CastingItem extends Item {
  public CastingItem(Properties pProperties) {
    super(pProperties);
  }

  @Override
  public UseAnim getUseAnimation(ItemStack pStack) {
    return UseAnim.BOW;
  }

  @Override
  public int getUseDuration(ItemStack pStack, LivingEntity entity) {
    SpellStorage storage = pStack.get(ModAttachments.SPELL_STORAGE);
    if (storage == null) {
      return 0;
    }

    ISpellInstance spell = storage.getCurrentSpell();
    if (spell == null) {
      return 0;
    }

    return spell.getMaxUse();
  }


  @Override
  public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
    if (!(pLivingEntity instanceof Player pPlayer) || pLevel.isClientSide()) {
      return;
    }

    InteractionHand pHand = pLivingEntity.getUsedItemHand();

    SpellStorage storage = pStack.get(ModAttachments.SPELL_STORAGE);
    if (storage == null) {
      pPlayer.stopUsingItem();
      return;
    }

    ISpellInstance spell = storage.getCurrentSpell();
    if (spell == null) {
      pPlayer.stopUsingItem();
      return;
    }

    int ticks = pStack.getUseDuration(pLivingEntity) - pRemainingUseDuration;

    if (spell.getType() == Spell.Type.CONTINUOUS) {
      Costing costs = new Costing(spell, pPlayer);

      if (ticks % 20 == 0) {
        if (!costs.canAfford(pPlayer, true)) {
          RootsAPI.LOG.info("Not enough herbs to continue casting: {}", spell.getSpell().getName());
          pPlayer.stopUsingItem();
          return;
        }
      }

      if (spell.cast(pLevel, pPlayer, pStack, pHand, costs, ticks) < 0) {
        // This means the psell didn't cast
        // TODO: Kind of decide something about this
        /*        RootsAPI.LOG.error("Failed casting spell returned a cooldown on a channel: {}", spell.getSpell().getName());*/
        if (ticks % 2 == 0) {
          PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new CastChannelFailFXPacket(spell.getSpell(), pPlayer.getId(), ticks));
        }
      } else {
        if (ticks % 2 == 0) {
          Vec3 stop = spell.getBlockTarget(pPlayer);
          IRootsPacket packet;
          if (stop == null) {
            packet = new CastChannelFXPacket(spell.getSpell(), pPlayer.getId(), ticks);
          } else {
            // Actually transmit particles now
            Vec3 lookDir = pPlayer.getViewVector(1.0f);
            Vec3 rightVec = lookDir.cross(new Vec3(0, 1, 0)).normalize();
            double sideOffset = 0.3;
            Vec3 handOffset = pHand == InteractionHand.MAIN_HAND ? rightVec.scale(sideOffset) : rightVec.scale(-sideOffset);
            Vec3 start = pPlayer.getEyePosition().add(handOffset).add(lookDir.scale(0.6));
            packet = new CastChannelTargetFXPacket(spell.getSpell(), pPlayer.getId(), start, stop, ticks);
          }

          PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, packet);
        }
      }

      // TODO: Properly handle operations
      SpellCastAction.Context context = new SpellCastAction.Context((ServerLevel) pLevel, (ServerPlayer) pPlayer, pHand, pStack, spell, costs);
      ModActions.SPELL_CAST.get().accept(context);
      costs.charge(pPlayer, true);
    } else if (spell.getType() == Spell.Type.CHARGED) {
      pPlayer.displayClientMessage(spell.getSpell().getChargeText(ticks), true);
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

    int current = storage.currentSlot();
    int max = storage.maxSlot();

    if (pPlayer.isShiftKeyDown()) {
      if (storage.isEmpty()) {
        return InteractionResultHolder.fail(stack);
      }

      int newSlot = current + 1;
      if (newSlot >= max) {
        newSlot = 0;
      }

      while (storage.getSpell(newSlot) == null) {
        newSlot++;
        if (newSlot >= max) {
          newSlot = 0;
        }
      }

      if (newSlot != current && storage.getSpell(newSlot) != null) {
        SpellStorage newStorage = storage.setCurrentSlot(newSlot);
        if (newStorage != storage) {
          stack.set(ModAttachments.SPELL_STORAGE, newStorage);
        }
        return InteractionResultHolder.success(stack);
      } else {
        return InteractionResultHolder.fail(stack);
      }


    }

    ISpellInstance spell = storage.getSpell(current);
    if (spell == null || !spell.canCast(pPlayer)) {
      return InteractionResultHolder.fail(stack);
    }

    Costing costing = new Costing(spell, pPlayer);
    if (!costing.canAfford(pPlayer, true)) {
      // TODO: display a warning
      pPlayer.displayClientMessage(Component.translatable("roots.message.staff.missing_herbs", spell.getStyledName()), true);
      RootsAPI.LOG.info("Not enough herbs to cast: {}", spell.getSpell().getName());
      return InteractionResultHolder.fail(stack);
    }

    if (spell.getType() == Spell.Type.INSTANT) {
      int cooldown = spell.cast(pLevel, pPlayer, stack, pUsedHand, costing, -1);
      if (costing.charge(pPlayer)) {
        SpellCastAction.Context context = new SpellCastAction.Context((ServerLevel) pLevel, (ServerPlayer) pPlayer, pUsedHand, stack, spell, costing);
        ModActions.SPELL_CAST.get().accept(context);
        stack.set(ModAttachments.SPELL_STORAGE, storage.setCooldown(current, cooldown));
      }
    } else {
      pPlayer.startUsingItem(pUsedHand);
    }

    return InteractionResultHolder.success(stack);
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
    this.releaseUsing(stack, level, livingEntity, stack.getUseDuration(livingEntity));
    return stack;
  }

  @Override
  public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
    super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    if (!pLevel.isClientSide()) {
      int dur = getUseDuration(pStack, pLivingEntity) - pTimeCharged;
      //RootsAPI.LOG.info("Finished using after {} ticks {} seconds", dur, dur / 20);
    }

    if (!(pLivingEntity instanceof Player pPlayer) || pLevel.isClientSide()) {
      return;
    }

    SpellStorage storage = pStack.get(ModAttachments.SPELL_STORAGE);
    if (storage == null) {
      return;
    }

    ISpellInstance spell = storage.getCurrentSpell();
    if (spell == null) {
      return;
    }

    int ticks = pStack.getUseDuration(pLivingEntity) - pTimeCharged;
    int current = storage.currentSlot();

    if (spell.getType() == Spell.Type.CHARGED) {
      Costing costing = new Costing(spell, pPlayer);

      // TODO: Charge every tick instead of assuming 20 ticks will elapse properly
      if (!costing.canAfford(pPlayer, true)) {
        RootsAPI.LOG.info("Not enough herbs to cast: {}", spell.getSpell().getName());
        return;
      }

      int cooldown = spell.cast(pLevel, pPlayer, pStack, pPlayer.getUsedItemHand(), costing, ticks);
      if (costing.charge(pPlayer)) {
        SpellCastAction.Context context = new SpellCastAction.Context((ServerLevel) pLevel, (ServerPlayer) pPlayer, pPlayer.getUsedItemHand(), pPlayer.getItemInHand(pPlayer.getUsedItemHand()), spell, costing);
        ModActions.SPELL_CAST.get().accept(context);
        pStack.set(ModAttachments.SPELL_STORAGE, storage.setCooldown(current, cooldown));
      }
    }
  }

  @Override
  public boolean isBarVisible(ItemStack pStack) {
    SpellStorage storage = pStack.get(ModAttachments.SPELL_STORAGE);
    if (storage == null) {
      return false;
    }
    int cooldown = storage.getCurrentCooldown();
    return cooldown > 0;
  }

  @Override
  public int getBarWidth(ItemStack pStack) {
    SpellStorage storage = pStack.get(ModAttachments.SPELL_STORAGE);
    if (storage == null) {
      return 0;
    }

    return Math.round((float) storage.getCurrentCooldown() * 13.0F / (float) storage.getCurrentMaxCooldown());

  }

  @Override
  public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
    super.inventoryTick(stack, level, entity, slotId, isSelected);

    if (level.isClientSide()) {
      return;
    }

    SpellStorage storage = stack.get(ModAttachments.SPELL_STORAGE);
    if (storage != null) {
      SpellStorage newStorage = storage.tick();
      if (storage != newStorage) {
        stack.set(ModAttachments.SPELL_STORAGE, newStorage);
      }
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
      ISpellInstance spell = storage.getCurrentSpell();
      if (spell != null) {
        return Component.translatable("roots.item.staff.with_spell", spell.getSpell().getStyledName());
      }
    }

    return super.getName(pStack);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

    if (context.level() != null && context.level().isClientSide()) {
      tooltipComponents.add(Component.translatable("roots.tooltip.staff.key_binding", RootsClientHooks.getStaffKeyBind()));
    }

    TooltipUtil.spellStaffTooltip(context, tooltipComponents, stack, tooltipFlag);
  }
}
