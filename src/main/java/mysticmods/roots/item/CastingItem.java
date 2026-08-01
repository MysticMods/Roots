package mysticmods.roots.item;

import mysticmods.roots.action.SpellCastAction;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.attachment.CooldownStorage;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.modifier.SpellModifierSet;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.SpellCastType;
import mysticmods.roots.client.RootsClientHooks;
import mysticmods.roots.init.ModActions;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.item.util.CastingSuccessCache;
import mysticmods.roots.network.client.ClientboundClearHighlightPacket;
import mysticmods.roots.network.client.fx.casting.CastChannelFXPacket;
import mysticmods.roots.network.client.fx.casting.CastChannelFailFXPacket;
import mysticmods.roots.network.client.fx.casting.CastChannelChargingFXPacket;
import mysticmods.roots.network.client.fx.casting.CastChannelTargetFXPacket;
import mysticmods.roots.util.PlayerGetter;
import mysticmods.roots.util.TooltipUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

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
    ISpellInstance spell = getCurrentSpell(entity.level(), entity, pStack);
    if (spell == null) {
      return 0;
    }

    return spell.getMaxUse();
  }

  @Override
  public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
    var currentSpell = getCastingSpell(null, null, stack);
    if (currentSpell != null) {
      TagKey<Block> tag = currentSpell.getData(DataMaps.CAN_BREAK_BLOCKS_TAG);
      if (tag != null) {
        return state.is(tag);
      }
    }
    return super.isCorrectToolForDrops(stack, state);
  }

  // TODO: Not sure if this is correct
  @Override
  public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
    if (itemAbility == ItemAbilities.SHEARS_DIG) {
      var currentSpell = getCastingSpell(null, null, stack);
      if (currentSpell != null && currentSpell.has(RootsTags.SpellModifiers.SHEARING)) {
        return true;
      }
    } else if (itemAbility == ItemAbilities.AXE_DIG || itemAbility == ItemAbilities.HOE_DIG || itemAbility == ItemAbilities.SHOVEL_DIG || itemAbility == ItemAbilities.SWORD_DIG || itemAbility == ItemAbilities.PICKAXE_DIG) {
      var currentSpell = getCastingSpell(null, null, stack);
      if (currentSpell != null && currentSpell.is(RootsTags.Spells.PRETEND_PICKAXE)) {
        return true;
      }
    }
    return super.canPerformAction(stack, itemAbility);
  }

  @Override
  public int getEnchantmentLevel(ItemStack stack, Holder<Enchantment> enchantment) {
    int baseValue = super.getEnchantmentLevel(stack, enchantment);

    if (enchantment.is(RootsTags.Enchantments.INCREASES_FORTUNE)) {
      baseValue += countCastingModifiers(null, null, stack, RootsTags.SpellModifiers.INCREASES_FORTUNE);
    }

    if (enchantment.is(RootsTags.Enchantments.INCREASES_LOOTING)) {
      baseValue += countCastingModifiers(null, null, stack, RootsTags.SpellModifiers.INCREASES_LOOTING);
    }

    if (enchantment.is(RootsTags.Enchantments.SILK_TOUCH)) {
      if (countCastingModifiers(null, null, stack, RootsTags.SpellModifiers.SILK_TOUCH) > 0) {
        baseValue = 1;
      }
    }

    return baseValue;
  }

  @Override
  public ItemEnchantments getAllEnchantments(ItemStack stack, HolderLookup.RegistryLookup<Enchantment> lookup) {
    ItemEnchantments baseValue = super.getAllEnchantments(stack, lookup);

    SpellModifierSet set = getCastingModifiers(null, null, stack);
    if (set.isEmpty()) {
      return baseValue;
    }

    ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(baseValue);

    var fortune = set.count(RootsTags.SpellModifiers.INCREASES_FORTUNE);
    if (fortune > 0) {
      lookup.get(Enchantments.FORTUNE).ifPresentOrElse(
          o -> mutable.upgrade(o, fortune),
          () -> RootsAPI.LOG.error("Fortune enchantment wasn't found in the registry!"));
    }

    var looting = set.count(RootsTags.SpellModifiers.INCREASES_LOOTING);
    if (looting > 0) {
      lookup.get(Enchantments.LOOTING).ifPresentOrElse(
          o -> mutable.upgrade(o, looting),
          () -> RootsAPI.LOG.error("Looting enchantment wasn't found in the registry!"));
    }

    var silk_touch = set.count(RootsTags.SpellModifiers.SILK_TOUCH);
    if (silk_touch > 0) {
      lookup.get(Enchantments.SILK_TOUCH).ifPresentOrElse(
          o -> mutable.set(o, 1),
          () -> RootsAPI.LOG.error("Silk touch enchantment wasn't found in the registry!"));
    }

    if (silk_touch > 0 || fortune > 0 || looting > 0) {
      return mutable.toImmutable();
    }

    return baseValue;
  }

  @Override
  public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
    if (!(pLivingEntity instanceof Player pPlayer) || pLevel.isClientSide()) {
      return;
    }

    InteractionHand pHand = pLivingEntity.getUsedItemHand();

    SpellStorage storage = getStorage(pLevel, pLivingEntity, pStack);
    if (storage == null) {
      CastingSuccessCache.clear(pStack);
      pStack.set(ModAttachments.CASTING_CURRENT_SPELL, false);
      pPlayer.stopUsingItem();
      return;
    }

    ISpellInstance spell = storage.getCurrentSpell();
    if (spell == null) {
      CastingSuccessCache.clear(pStack);
      pPlayer.stopUsingItem();
      pStack.set(ModAttachments.CASTING_CURRENT_SPELL, false);
      return;
    }

    int ticks = pStack.getUseDuration(pLivingEntity) - pRemainingUseDuration;
    pStack.set(ModAttachments.CASTING_CURRENT_SPELL, true);

    if (spell.getType() == SpellCastType.CONTINUOUS) {
      Costing costs = new Costing(spell);
      costs.updateHerbCache(pPlayer);

      if (ticks % 20 == 0) {
        if (!costs.canAfford(pPlayer, true)) {
          RootsAPI.LOG.info("Not enough herbs to continue casting: {}", spell.getName().getString());
          CastingSuccessCache.clear(pStack);
          pPlayer.stopUsingItem();
          return;
        }
      }

      boolean lastSuccess = spell.cast(pLevel, pPlayer, pStack, pHand, costs, ticks) >= 0;

      CastingSuccessCache.note(pStack, lastSuccess);

      if (ticks % 2 == 0) {
        Vec3 stop = spell.getBlockTarget(pPlayer);
        IRootsPacket packet = null;

        if (stop != null) {
          if (lastSuccess) {
            Vec3 lookDir = pPlayer.getViewVector(1.0f);
            Vec3 rightVec = lookDir.cross(new Vec3(0, 1, 0)).normalize();
            double sideOffset = 0.3;
            Vec3 handOffset = pHand == InteractionHand.MAIN_HAND ? rightVec.scale(sideOffset) : rightVec.scale(-sideOffset);
            Vec3 start = pPlayer.getEyePosition().add(handOffset).add(lookDir.scale(0.6));
            packet = new CastChannelTargetFXPacket(spell.asSpell(), pPlayer.getId(), start, stop, ticks);
          }
        } else {
          if (CastingSuccessCache.isASuccess(pStack)) {
            packet = new CastChannelFXPacket(spell.asSpell(), pPlayer.getId(), ticks);
          } else {
            packet = new CastChannelFailFXPacket(spell.asSpell(), pPlayer.getId(), ticks);
          }
        }

        if (packet != null) {
          PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, packet);
        }
      }

      // TODO: Properly handle operations
      SpellCastAction.Context context = new SpellCastAction.Context((ServerLevel) pLevel, (ServerPlayer) pPlayer, pHand, pStack, spell, costs);
      ModActions.SPELL_CAST.get().accept(context);
      costs.charge(pPlayer, true);
    } else if (spell.getType() == SpellCastType.CHARGED) {
      pPlayer.displayClientMessage(spell.getChargeText(ticks), true);

      if (ticks % 2 == 0) {
        // TODO: Jaunt effect should be triggered from something else?
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new CastChannelChargingFXPacket(spell.asSpell(), pPlayer.getId(), ticks));
      }
    }
  }

  // TODO: #1351 pass allows interaction with items but
  @Override
  public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
    ItemStack stack = pPlayer.getItemInHand(pUsedHand);

    if (pLevel.isClientSide()) {
      if (pUsedHand == InteractionHand.MAIN_HAND && !pPlayer.getOffhandItem().isEmpty()) {
        return InteractionResultHolder.pass(stack);
      }
      return InteractionResultHolder.consume(stack);
    }

    stack.set(ModAttachments.CASTING_CURRENT_SPELL, false);

    SpellStorage storage = getStorage(pLevel, pPlayer, stack);
    if (storage == null) {
      return InteractionResultHolder.pass(stack);
    }

    int current = storage.currentSlot();
    int max = storage.maxSlot();

    if (pPlayer.isShiftKeyDown()) {
      if (storage.isEmpty()) {
        return InteractionResultHolder.pass(stack);
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
        setStorage(pLevel, pPlayer, stack, storage, newStorage);
        CastingSuccessCache.clear(stack);
        if (pUsedHand == InteractionHand.MAIN_HAND) {
          PacketDistributor.sendToPlayer((ServerPlayer) pPlayer, ClientboundClearHighlightPacket.INSTANCE);
        }
        return InteractionResultHolder.success(stack);
      } else {
        return InteractionResultHolder.pass(stack);
      }
    }

    ISpellInstance spell = storage.getSpell(current);
    if (spell == null || !spell.offCooldown(stack, pPlayer)) {
      return InteractionResultHolder.pass(stack);
    }

    Costing costing = new Costing(spell);
    if (!costing.canAfford(pPlayer, true)) {
      // TODO: display a warning
      pPlayer.displayClientMessage(Component.translatable("roots.message.staff.missing_herbs", spell.getStyledName()), true);
      RootsAPI.LOG.info("Not enough herbs to cast: {}", spell.getName());
      return InteractionResultHolder.pass(stack);
    }

    if (spell.getType() == SpellCastType.INSTANT) {
      stack.set(ModAttachments.CASTING_CURRENT_SPELL, true);
      // TODO: (#1353) Improve spell casting results
      int cooldown = spell.cast(pLevel, pPlayer, stack, pUsedHand, costing, -1);
      if (costing.charge(pPlayer)) {
        SpellCastAction.Context context = new SpellCastAction.Context((ServerLevel) pLevel, (ServerPlayer) pPlayer, pUsedHand, stack, spell, costing);
        ModActions.SPELL_CAST.get().accept(context);
        if (!stack.is(RootsTags.Items.CREATIVE_CASTING_TOOLS)) {
          CooldownStorage cdStorage = pPlayer.getData(ModAttachments.COOLDOWN_STORAGE);
          cdStorage.setCooldown(spell.asSpell(), cooldown, cooldown);
        }
        stack.set(ModAttachments.CASTING_CURRENT_SPELL, false);
        return InteractionResultHolder.success(stack);
      }
      stack.set(ModAttachments.CASTING_CURRENT_SPELL, false);
    } else {
      CastingSuccessCache.clear(stack);
      pPlayer.startUsingItem(pUsedHand);
      stack.set(ModAttachments.CASTING_CURRENT_SPELL, true);
      return InteractionResultHolder.success(stack);
    }

    // TODO: Pass result allows for interaction with other things
    return InteractionResultHolder.pass(stack);
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
    this.releaseUsing(stack, level, livingEntity, stack.getUseDuration(livingEntity));
    if (!level.isClientSide()) {
      CastingSuccessCache.clear(stack);
      stack.set(ModAttachments.CASTING_CURRENT_SPELL, false);
    }
    return stack;
  }

  @Override
  public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
    super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    if (!pLevel.isClientSide()) {
      int dur = getUseDuration(pStack, pLivingEntity) - pTimeCharged;
      CastingSuccessCache.clear(pStack);
      //RootsAPI.LOG.info("Finished using after {} ticks {} seconds", dur, dur / 20);
    }

    if (!(pLivingEntity instanceof Player pPlayer) || pLevel.isClientSide()) {
      return;
    }

    SpellStorage storage = getStorage(pLevel, pLivingEntity, pStack);
    if (storage == null) {
      pStack.set(ModAttachments.CASTING_CURRENT_SPELL, false);
      return;
    }

    ISpellInstance spell = storage.getCurrentSpell();
    if (spell == null) {
      pStack.set(ModAttachments.CASTING_CURRENT_SPELL, false);
      return;
    }

    int ticksUsed = pStack.getUseDuration(pLivingEntity) - pTimeCharged;

    if (spell.getType() == SpellCastType.CHARGED) {
      Costing costing = new Costing(spell);

      // TODO: Charge every tick instead of assuming 20 ticks will elapse properly
      if (!costing.canAfford(pPlayer, true)) {
        RootsAPI.LOG.info("Not enough herbs to cast: {}", spell.getName().getString());
        return;
      }

      int cooldown = spell.cast(pLevel, pPlayer, pStack, pPlayer.getUsedItemHand(), costing, ticksUsed);
      if (costing.charge(pPlayer)) {
        SpellCastAction.Context context = new SpellCastAction.Context((ServerLevel) pLevel, (ServerPlayer) pPlayer, pPlayer.getUsedItemHand(), pPlayer.getItemInHand(pPlayer.getUsedItemHand()), spell, costing);
        ModActions.SPELL_CAST.get().accept(context);
        if (!pStack.is(RootsTags.Items.CREATIVE_CASTING_TOOLS)) {
          CooldownStorage cdStorage = pPlayer.getData(ModAttachments.COOLDOWN_STORAGE);
          cdStorage.setCooldown(spell.asSpell(), cooldown, cooldown);
        }
      }
      pStack.set(ModAttachments.CASTING_CURRENT_SPELL, false);
    }
  }

  @Override
  public boolean isBarVisible(ItemStack pStack) {
    if (pStack.is(RootsTags.Items.CREATIVE_CASTING_TOOLS)) {
      return false;
    }

    Player player = PlayerGetter.getPlayer();
    if (player == null) {
      return false;
    }

    if (!player.hasData(ModAttachments.COOLDOWN_STORAGE)) {
      return false;
    }

    ISpellInstance spell = getCurrentSpell(null, null, pStack);
    if (spell == null) {
      return false;
    }

    CooldownStorage cooldownStorage = player.getData(ModAttachments.COOLDOWN_STORAGE);
    return cooldownStorage.getCooldown(spell.asSpell()) > 0;
  }

  @Override
  public int getBarWidth(ItemStack pStack) {
    if (pStack.is(RootsTags.Items.CREATIVE_CASTING_TOOLS)) {
      return 0;
    }

    Player player = PlayerGetter.getPlayer();
    if (player == null) {
      return 0;
    }

    if (!player.hasData(ModAttachments.COOLDOWN_STORAGE)) {
      return 0;
    }

    ISpellInstance spell = getCurrentSpell(null, null, pStack);
    if (spell == null) {
      return 0;
    }

    CooldownStorage cooldownStorage = player.getData(ModAttachments.COOLDOWN_STORAGE);
    int cooldown = cooldownStorage.getCooldown(spell.asSpell());
    if (cooldown <= 0) {
      return 0;
    }

    int maxCooldown = cooldownStorage.getMaxCooldown(spell.asSpell());

    return Math.round((float) cooldown * 13.0F / (float) maxCooldown);
  }

  // TODO: This is probably over-simplified
  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
    return slotChanged || oldStack.getItem() != newStack.getItem();
  }

  @Override
  public Component getName(ItemStack pStack) {
    ISpellInstance spell = getCurrentSpell(null, null, pStack);
    if (spell != null) {
      var name = spell.getStyledName();
      return spell.getEnabledModifiers()
          .isEmpty() ? Component.translatable("roots.item.staff.with_spell", name) : Component.translatable("roots.item.staff.with_modified_spell", name);
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

  @Override
  public boolean isFoil(ItemStack stack) {
    return stack.is(RootsTags.Items.CREATIVE_CASTING_TOOLS) || super.isFoil(stack);
  }

  public static UUID getUUID(ItemStack stack) {
    if (!stack.is(RootsTags.Items.CASTING_TOOLS)) {
      throw new IllegalArgumentException("CastingItem.getUUID can only be called on a staff item");
    }
    UUID current = stack.get(ModAttachments.ITEM_UUID);
    if (current == null) {
      UUID newUUID = UUID.randomUUID();
      stack.set(ModAttachments.ITEM_UUID, newUUID);
      return newUUID;
    } else {
      return current;
    }
  }

  public static ISpellInstance getCastingSpell(@Nullable Level level, @Nullable LivingEntity entity, ItemStack stack) {
    if (Boolean.FALSE.equals(stack.get(ModAttachments.CASTING_CURRENT_SPELL))) {
      return null;
    }
    return getCurrentSpell(level, entity, stack);
  }

  @Nullable
  public static SpellStorage getStorage(@Nullable Level level, @Nullable LivingEntity entity, ItemStack item) {
    return item.get(ModAttachments.SPELL_STORAGE);
  }

  @Nullable
  public static ISpellInstance getCurrentSpell(@Nullable Level level, @Nullable LivingEntity entity, ItemStack itemStack) {
    SpellStorage storage = getStorage(level, entity, itemStack);
    if (storage == null) {
      return null;
    }

    return storage.getCurrentSpell();
  }

  public static SpellModifierSet getCastingModifiers(@Nullable Level level, @Nullable LivingEntity entity, ItemStack stack) {
    var spell = getCastingSpell(level, entity, stack);
    if (spell == null) {
      return SpellModifierSet.EMPTY;
    }

    return spell.getEnabledModifiers();
  }

  public static int countCastingModifiers(@Nullable Level level, @Nullable LivingEntity entity, ItemStack stack, TagKey<SpellModifier> tagType) {
    return getCastingModifiers(level, entity, stack).count(tagType);
  }

  public static void setStorage(@Nullable Level level, @Nullable LivingEntity entity, ItemStack itemStack, @Nullable SpellStorage previousStorage, SpellStorage newStorage) {
    if (newStorage != previousStorage) {
      itemStack.set(ModAttachments.SPELL_STORAGE, newStorage);
    }
  }
}
