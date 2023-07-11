package mysticmods.roots.item;

import mysticmods.roots.api.RootsAPI;
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
    BlockState blockstate = level.getBlockState(blockpos);
    Block block = blockstate.getBlock();
    if (block instanceof GrowingPlantHeadBlock growingplantheadblock) {
      if (!growingplantheadblock.isMaxAge(blockstate)) {
        Player player = pContext.getPlayer();
        ItemStack itemstack = pContext.getItemInHand();
        if (player instanceof ServerPlayer) {
          CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
        }

        level.playSound(player, blockpos, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.setBlockAndUpdate(blockpos, growingplantheadblock.getMaxAgeState(blockstate));
        if (player != null) {
          itemstack.hurtAndBreak(1, player, (p_186374_) -> {
            p_186374_.broadcastBreakEvent(pContext.getHand());
          });
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
      }
    }

    return super.useOn(pContext);
  }
}
