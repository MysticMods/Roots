package mysticmods.roots.item;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.EntityCooldowns;
import mysticmods.roots.api.recipe.ConditionResult;
import mysticmods.roots.api.recipe.UnlockResult;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.recipe.SimpleWorldCrafting;
import mysticmods.roots.recipe.runic.RunicBlockRecipe;
import mysticmods.roots.recipe.runic.RunicEntityCrafting;
import mysticmods.roots.recipe.runic.RunicEntityRecipe;
import mysticmods.roots.util.FakePlayerUtil;
import mysticmods.roots.util.ItemUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.IShearable;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RunicShearsItem extends ShearsItem {
  private static AABB aoeBoundingBox;

  public RunicShearsItem(Properties pProperties) {
    super(pProperties);
  }

  private static AABB getBoundingBox() {
    if (aoeBoundingBox == null) {
      aoeBoundingBox = new AABB(-ConfigManager.AOE_BOUNDING_BOX_X.getAsInt(), -ConfigManager.AOE_BOUNDING_BOX_Y.getAsInt(), -ConfigManager.AOE_BOUNDING_BOX_Z.getAsInt(), ConfigManager.AOE_BOUNDING_BOX_X.getAsInt(), ConfigManager.AOE_BOUNDING_BOX_Y.getAsInt(), ConfigManager.AOE_BOUNDING_BOX_Z.getAsInt());
    }
    return aoeBoundingBox;
  }

  @Override
  public InteractionResult interactLivingEntity(ItemStack heldItem, Player player, LivingEntity entity, InteractionHand hand) {
    if (!player.isShiftKeyDown()) {
      RunicEntityCrafting crafting = new RunicEntityCrafting(entity, player, player.level(), hand, heldItem);
      RecipeHolder<RunicEntityRecipe> recipe = ResolvedRecipes.RUNIC_ENTITY.findRecipe(crafting, player.level());
      if (recipe != null) {
        if (entity.level().isClientSide()) {
          return InteractionResult.CONSUME;
        }

        // TODO: Conditions are never tested
        MinecraftServer server = entity.level().getServer();
        if (server == null) {
          return InteractionResult.FAIL;
        }
        Level level = entity.level();
        if (EntityCooldowns.hasExpired(entity, ModAttachments.RUNIC_SHEARS_ENTITY_COOLDOWN)) {
          EntityCooldowns.setExpiresAt(entity, ModAttachments.RUNIC_SHEARS_ENTITY_COOLDOWN, server.getTickCount() + recipe.value()
              .getCooldown());
          level.playSound(null, player.blockPosition(), SoundEvents.AXE_STRIP, SoundSource.PLAYERS, 0.5f, level.getRandom()
              .nextFloat() * 0.25f + 0.6f);
          heldItem.hurtAndBreak(recipe.value()
              .getDurabilityCost(), player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND); // TODO: Check hand
          List<ItemStack> results = recipe.value()
              .assembleOutputs(crafting, player.getRandom(), level.registryAccess(), null);
          for (ItemStack stack : results) {
            ItemUtil.Spawn.spawnItem(level, entity.getX(), entity.getY(), entity.getZ(), true, stack, -1);
          }
        } else {
          player.displayClientMessage(Component.translatable("roots.message.runic_shears.cooldown")
              .setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.BLUE)).withBold(true)), true);
        }
        return InteractionResult.SUCCESS;
      }
    }
    // TODO: AOE shearing
    if (entity instanceof IShearable target) {
      if (entity.level().isClientSide()) {
        return InteractionResult.CONSUME;
      }
      BlockPos pos = entity.blockPosition();
      AABB aabb = getBoundingBox().move(pos);
      boolean anySheared = false;
      for (LivingEntity newTarget : entity.level().getEntitiesOfClass(LivingEntity.class, aabb)) {
        if (newTarget instanceof IShearable) {
          if (doShear(target, player, heldItem, newTarget, entity, pos, hand)) {
            anySheared = true;
          }
        }
      }
      if (anySheared) {
        heldItem.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
        return InteractionResult.SUCCESS;
      } else {
        return InteractionResult.FAIL;
      }
    }
    return InteractionResult.PASS;
  }

  protected boolean doShear(IShearable target, Player player, ItemStack heldItem, LivingEntity entity, LivingEntity original, BlockPos pos, InteractionHand hand) {
    if (target.isShearable(player, heldItem, entity.level(), pos)) {
      // EnchantmentHelper.getItemEnchantmentLevel(entity.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE), heldItem)
      List<ItemStack> drops = target.onSheared(player, heldItem, entity.level(), pos);
      Random rand = new java.util.Random();
      drops.forEach(d -> {
        ItemEntity ent = original.spawnAtLocation(d, 1.0F);
        ent.setDeltaMovement(ent.getDeltaMovement()
            .add((rand.nextFloat() - rand.nextFloat()) * 0.1F, rand.nextFloat() * 0.05F, (rand.nextFloat() - rand.nextFloat()) * 0.1F));
      });
      entity.gameEvent(GameEvent.SHEAR, player);
      return true;
    }

    return false;
  }

  @Override
  public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
    return ItemAbilities.DEFAULT_SHEARS_ACTIONS.contains(itemAbility) || RootsAPI.RUNIC_SHEARS_DEFAULTS.contains(itemAbility);
  }

  @Override
  public InteractionResult useOn(UseOnContext pContext) {
    Level level = pContext.getLevel();
    BlockPos blockpos = pContext.getClickedPos();
    Player player = pContext.getPlayer();
    BlockState blockstate = level.getBlockState(blockpos);
    SimpleWorldCrafting crafting = new SimpleWorldCrafting(player, level, blockpos, blockstate, pContext);
    RecipeHolder<RunicBlockRecipe> recipe = ResolvedRecipes.RUNIC_BLOCK.findRecipe(crafting, level);
    ItemStack itemstack = pContext.getItemInHand();
    if (recipe != null) {
      ConditionResult conditionResult = recipe.value()
          .checkConditions(level, player, PyreBlockEntity.getPyreBoundingBox(), blockpos);
      if (conditionResult.anyFailed()) {
        if (!level.isClientSide() && player != null) {
          conditionResult.report(player);
        }
        return InteractionResult.FAIL;
      }

      if (!level.isClientSide()) {
        UnlockResult failedGrants = recipe.value().checkUnlocks(level, (ServerPlayer) player);
        if (failedGrants.anyFailed() && !recipe.value().hasOutput(level.registryAccess())) {
          RootsAPI.LOG.info("Grants failed and recipe has no output");
          // TODO:
          /*        failedUnlocks.failedUnlocks().forEach(o -> RootsAPI.LOG.info("Failed grant of type " + o.type().name() + " with id " + o.id()));*/
          failedGrants.report();
          return InteractionResult.FAIL;
        }
      }


      level.playSound(player, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);

      if (player instanceof ServerPlayer) {
        CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, blockpos, itemstack);
      }

      if (player != null) {
        itemstack.hurtAndBreak(recipe.value()
            .getDurabilityCost(), player, pContext.getHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
      }

      if (!level.isClientSide()) {
        List<ItemStack> results = recipe.value()
            .assembleOutputs(crafting, level.getRandom(), level.registryAccess(), null);
        // TODO: Item could be empty with only chance outputs
        // TODO: Isn;'t there "assemble all" now?
        for (ItemStack stack : results) {
          ItemUtil.Spawn.spawnItem(level, player == null ? blockpos : player.blockPosition(), stack);
        }
      }

      return InteractionResult.sidedSuccess(level.isClientSide);
    } else {
      return InteractionResult.PASS;
    }
  }

  public static class RunicShearsDispenseBehaviour extends DefaultDispenseItemBehavior {
    @Override
    protected ItemStack execute(BlockSource blockSource, ItemStack item) {
      ServerLevel serverLevel = blockSource.level();
      BlockState state = blockSource.state();
      if (!state.hasProperty(DispenserBlock.FACING)) {
        return item;
      }

      Direction direction = state.getValue(DispenserBlock.FACING);
      BlockPos target = blockSource.pos().relative(direction);
      BlockState targetState = serverLevel.getBlockState(target);

      Player fakePlayer = FakePlayerFactory.get(serverLevel, FakePlayerUtil.ROOTS);
      fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, item);

      BlockPos above = blockSource.pos().above();
      IItemHandler cap = serverLevel.getCapability(Capabilities.ItemHandler.BLOCK, above, null);

      BlockHitResult hit = new BlockHitResult(target.getCenter(), direction, target, false);
      UseOnContext useOnContext = new UseOnContext(serverLevel, fakePlayer, InteractionHand.MAIN_HAND, item, hit);
      SimpleWorldCrafting crafting = new SimpleWorldCrafting(fakePlayer, serverLevel, target, targetState, useOnContext);
      RecipeHolder<RunicBlockRecipe> recipe = ResolvedRecipes.RUNIC_BLOCK.findRecipe(crafting, serverLevel);
      if (recipe != null) {
        ConditionResult conditionResult = recipe.value()
            .checkConditions(serverLevel, fakePlayer, PyreBlockEntity.getPyreBoundingBox(), target);
        if (conditionResult.anyFailed()) {
          return item;
        }

        if (!recipe.value().hasOutput(serverLevel.registryAccess())) {
          return item;
        }

        serverLevel.playSound(null, target, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);

        item.hurtAndBreak(recipe.value()
            .getDurabilityCost(), fakePlayer, EquipmentSlot.MAINHAND);

        List<ItemStack> results = recipe.value()
            .assembleOutputs(crafting, serverLevel.getRandom(), serverLevel.registryAccess(), null);
        if (cap != null) {
          for (ItemStack r : results) {
            ItemStack r2 = ItemHandlerHelper.insertItem(cap, r, false);
            if (!r2.isEmpty()) {
              ItemUtil.Spawn.spawnItem(serverLevel, above, r2);
            }
          }
        } else {
          for (ItemStack r : results) {
            ItemUtil.Spawn.spawnItem(serverLevel, above, r);
          }
        }
        return item;
      }

      // Now test for entities
      for (LivingEntity livingEntity : serverLevel.getEntitiesOfClass(LivingEntity.class, new AABB(target), EntitySelector
          .NO_SPECTATORS)) {
        RunicEntityCrafting entityCrafting = new RunicEntityCrafting(livingEntity, fakePlayer, serverLevel, InteractionHand.MAIN_HAND, item);
        RecipeHolder<RunicEntityRecipe> entityRecipe = ResolvedRecipes.RUNIC_ENTITY.findRecipe(entityCrafting, serverLevel);
        if (entityRecipe != null) {
          MinecraftServer server = serverLevel.getServer();
          if (server == null) {
            return item;
          }

          if (EntityCooldowns.hasExpired(livingEntity, ModAttachments.RUNIC_SHEARS_ENTITY_COOLDOWN)) {
            EntityCooldowns.setExpiresAt(livingEntity, ModAttachments.RUNIC_SHEARS_ENTITY_COOLDOWN, server.getTickCount() + entityRecipe.value()
                .getCooldown());
            serverLevel.playSound(null, target, SoundEvents.AXE_STRIP, SoundSource.PLAYERS, 0.5f, serverLevel.getRandom()
                .nextFloat() * 0.25f + 0.6f);
            item.hurtAndBreak(entityRecipe.value()
                .getDurabilityCost(), fakePlayer, EquipmentSlot.MAINHAND);
            List<ItemStack> results = entityRecipe.value()
                .assembleOutputs(entityCrafting, serverLevel.getRandom(), serverLevel.registryAccess(), null);
            // TODO: Store in chest instead
            if (cap != null) {
              for (ItemStack r : results) {
                ItemStack r2 = ItemHandlerHelper.insertItem(cap, r, false);
                if (!r2.isEmpty()) {
                  ItemUtil.Spawn.spawnItem(serverLevel, above, r2);
                }
              }
            } else {
              for (ItemStack r : results) {
                ItemUtil.Spawn.spawnItem(serverLevel, above, r);
              }
            }
          }
          return item;
        }
        if (livingEntity instanceof IShearable shearableTarget) {
          BlockPos pos = livingEntity.blockPosition();
          AABB aabb = getBoundingBox().move(pos);
          boolean anySheared = false;
          List<ItemStack> drops = new ArrayList<>();
          for (LivingEntity newTarget : serverLevel.getEntitiesOfClass(LivingEntity.class, aabb)) {
            if (newTarget instanceof IShearable newShearable && newShearable.isShearable(fakePlayer, item, serverLevel, pos)) {
              drops.addAll(newShearable.onSheared(fakePlayer, item, serverLevel, pos));
              newTarget.gameEvent(GameEvent.SHEAR, fakePlayer);
              anySheared = true;
            }
          }
          if (anySheared) {
            item.hurtAndBreak(1, fakePlayer, EquipmentSlot.MAINHAND);
          }
          if (cap != null) {
            for (ItemStack r : drops) {
              ItemStack r2 = ItemHandlerHelper.insertItem(cap, r, false);
              if (!r2.isEmpty()) {
                ItemUtil.Spawn.spawnItem(serverLevel, above, r2);
              }
            }
          } else {
            for (ItemStack r : drops) {
              ItemUtil.Spawn.spawnItem(serverLevel, above, r);
            }
          }
        }
        return item;
      }
      return item;
    }
  }
}
