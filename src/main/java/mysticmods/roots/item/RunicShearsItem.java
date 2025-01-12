package mysticmods.roots.item;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.capability.Capabilities;
import mysticmods.roots.api.capability.EntityCooldowns;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModSounds;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.recipe.SimpleWorldCrafting;
import mysticmods.roots.recipe.runic.RunicBlockRecipe;
import mysticmods.roots.recipe.runic.RunicEntityCrafting;
import mysticmods.roots.recipe.runic.RunicEntityRecipe;
import mysticmods.roots.util.ItemUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.IShearable;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RunicShearsItem extends ShearsItem {
  public RunicShearsItem(Properties pProperties) {
    super(pProperties);
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

        MinecraftServer server = entity.level().getServer();
        if (server == null) {
          return InteractionResult.FAIL;
        }
        Level level = entity.level();
        if (EntityCooldowns.hasExpired(entity, ModAttachments.RUNIC_SHEARS_ENTITY_COOLDOWN)) {
          EntityCooldowns.setExpiresAt(entity, ModAttachments.RUNIC_SHEARS_ENTITY_COOLDOWN, server.getTickCount() + recipe.value().getCooldown());
          level.playSound(null, player.blockPosition(), ModSounds.SQUID_MILK.get(), SoundSource.PLAYERS, 0.5f, level.getRandom().nextFloat() * 0.25f + 0.6f);
          heldItem.hurtAndBreak(recipe.value().getDurabilityCost(), player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND); // TODO: Check hand
          List<ItemStack> results = recipe.value().assembleOutputs(crafting, player.getRandom(), level.registryAccess(), null);
          for (ItemStack stack : results) {
            ItemUtil.Spawn.spawnItem(level, player.blockPosition(), stack);
          }
        } else {
          player.displayClientMessage(Component.translatable("roots.message.runic_shears.cooldown").setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.BLUE)).withBold(true)), true);
        }
        return InteractionResult.SUCCESS;
      }
    }
    // TODO: AOE shearing
    if (entity instanceof IShearable target) {
      if (entity.level().isClientSide) return InteractionResult.CONSUME;
      BlockPos pos = entity.blockPosition();
      if (target.isShearable(player, heldItem, entity.level(), pos)) {
        // EnchantmentHelper.getItemEnchantmentLevel(entity.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE), heldItem)
        List<ItemStack> drops = target.onSheared(player, heldItem, entity.level(), pos);
        Random rand = new java.util.Random();
        drops.forEach(d -> {
          ItemEntity ent = entity.spawnAtLocation(d, 1.0F);
          ent.setDeltaMovement(ent.getDeltaMovement().add((double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double) (rand.nextFloat() * 0.05F), (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
        });
        heldItem.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
      }
      return InteractionResult.SUCCESS;
    }
    return InteractionResult.PASS;
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
      level.playSound(player, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);

      if (player instanceof ServerPlayer) {
        CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, blockpos, itemstack);
      }

      if (player != null) {
        itemstack.hurtAndBreak(recipe.value().getDurabilityCost(), player, pContext.getHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
      }

      if (!level.isClientSide()) {
        List<ItemStack> results = recipe.value().assembleOutputs(crafting, level.getRandom(), level.registryAccess(), null);
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
}
