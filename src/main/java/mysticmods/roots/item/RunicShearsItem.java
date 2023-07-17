package mysticmods.roots.item;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.recipe.SimpleWorldCrafting;
import mysticmods.roots.recipe.runic.RunicBlockRecipe;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import noobanidus.libs.noobutil.util.ItemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RunicShearsItem extends ShearsItem {
  public RunicShearsItem(Properties pProperties) {
    super(pProperties);
  }

  @Override
  public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity entity, InteractionHand hand) {
    if (entity instanceof IForgeShearable target) {
      if (entity.level.isClientSide) return InteractionResult.CONSUME;
      BlockPos pos = new BlockPos(entity.getX(), entity.getY(), entity.getZ());
      if (target.isShearable(stack, entity.level, pos)) {
        List<ItemStack> drops = target.onSheared(playerIn, stack, entity.level, pos,
          EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, stack));
        Random rand = new java.util.Random();
        drops.forEach(d -> {
          ItemEntity ent = entity.spawnAtLocation(d, 1.0F);
          ent.setDeltaMovement(ent.getDeltaMovement().add((double)((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double)(rand.nextFloat() * 0.05F), (double)((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
        });
        stack.hurtAndBreak(1, playerIn, e -> e.broadcastBreakEvent(hand));
      }
      return InteractionResult.SUCCESS;
    }
    return InteractionResult.PASS;
  }

  @Override
  public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
    return ToolActions.DEFAULT_SHEARS_ACTIONS.contains(toolAction) || RootsAPI.RUNIC_SHEARS_DEFAULTS.contains(toolAction);
  }

  @Override
  public InteractionResult useOn(UseOnContext pContext) {
    Level level = pContext.getLevel();
    BlockPos blockpos = pContext.getClickedPos();
    Player player = pContext.getPlayer();
    BlockState blockstate = level.getBlockState(blockpos);
    SimpleWorldCrafting crafting = new SimpleWorldCrafting(player, level, blockpos, blockstate, pContext);
    RunicBlockRecipe recipe = ResolvedRecipes.RUNIC_BLOCK.findRecipe(crafting, level);
    ItemStack itemstack = pContext.getItemInHand();
    if (recipe != null) {
      level.playSound(player, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);

      if (player instanceof ServerPlayer) {
        CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, blockpos, itemstack);
      }

      if (player != null) {
        itemstack.hurtAndBreak(recipe.getDurabilityCost(), player, (p_150686_) -> {
          p_150686_.broadcastBreakEvent(pContext.getHand());
        });
      }

      if (!level.isClientSide()) {
        List<ItemStack> results = new ArrayList<>();
        // TODO: Item could be empty with only chance outputs
        results.add(recipe.assemble(crafting));
        results.addAll(recipe.assembleChanceOutputs(level.getRandom()));
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
