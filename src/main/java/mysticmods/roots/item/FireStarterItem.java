package mysticmods.roots.item;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.block.PyreBlock;
import mysticmods.roots.blockentity.PyreBlockEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbility;

public class FireStarterItem extends Item {
  public FireStarterItem(Properties props) {
    super(props);
  }

  @Override
  public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int count) {
    if (level.isClientSide() && entity instanceof Player player) {
      BlockHitResult ray = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
      if (ray.getType() == HitResult.Type.BLOCK && count % 3 == 0) {
        int runs = player.getRandom().nextInt(3) + 2;
        Vec3 hitVec = ray.getLocation();
        for (int i = 0; i < runs; i++) {
          level.addParticle(ParticleTypes.SMOKE, hitVec.x, hitVec.y, hitVec.z, 0, 0.05, 0);
        }
      }
    }
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
    if (!level.isClientSide() && entity instanceof Player player) {
      BlockHitResult ray = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
      boolean used = false;
      if (ray.getType() == HitResult.Type.BLOCK) {
        BlockPos blockpos = ray.getBlockPos();
        BlockState stateAt = level.getBlockState(blockpos);
        BlockPos relative = blockpos.relative(ray.getDirection());
        BlockState relativeState = level.getBlockState(relative);

        boolean doPyre = false;
        BlockPos pyrePos = blockpos;

        if (stateAt.is(RootsTags.Blocks.PYRES) && stateAt.hasProperty(PyreBlock.ACTIVE) && !stateAt.getValue(PyreBlock.ACTIVE)) {
          doPyre = true;
        } else if (relativeState.is(RootsTags.Blocks.PYRES) && relativeState.hasProperty(PyreBlock.ACTIVE) && !relativeState.getValue(PyreBlock.ACTIVE)) { // The block above might have been hit
          pyrePos = relative;
          doPyre = true;
        }

        if (doPyre) {
          BlockEntity be = level.getBlockEntity(pyrePos);
          if (be instanceof PyreBlockEntity pbe) {
            if (pbe.light(player) != InteractionResult.SUCCESS_NO_ITEM_USED) {
              level.playSound(player, pyrePos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom()
                  .nextFloat() * 0.4F + 0.8F);
              used = true;
            }
          }
        } else {
          UseOnContext context = new UseOnContext(player, player.getUsedItemHand(), ray);
          BlockState blockstate2 = stateAt.getToolModifiedState(context, net.neoforged.neoforge.common.ItemAbilities.FIRESTARTER_LIGHT, false);
          BlockState blockstate3 = relativeState.getToolModifiedState(context, net.neoforged.neoforge.common.ItemAbilities.FIRESTARTER_LIGHT, false);
          if (blockstate2 != null) {
            level.playSound(player, blockpos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom()
                .nextFloat() * 0.4F + 0.8F);
            level.setBlock(blockpos, blockstate2, 11);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, blockpos);
            used = true;
          } else if (blockstate3 != null) {
            level.playSound(player, relative, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom()
                .nextFloat() * 0.4F + 0.8F);
            level.setBlock(relative, blockstate3, 11);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, relative);
            used = true;
          } else {
            BlockPos blockpos1 = blockpos.relative(context.getClickedFace());
            if (BaseFireBlock.canBePlacedAt(level, blockpos1, context.getHorizontalDirection())) {
              level.playSound(player, blockpos1, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom()
                  .nextFloat() * 0.4F + 0.8F);
              level.setBlock(blockpos1, BaseFireBlock.getState(level, blockpos1), 11);
              level.gameEvent(player, GameEvent.BLOCK_PLACE, blockpos);
              if (player instanceof ServerPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockpos1, stack);
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
              }

              used = true;
            }
          }
        }

      }

      if (used && !player.isCreative()) {
        stack.shrink(1);
      }

      return stack;
    }
    return super.finishUsingItem(stack, level, entity);
  }

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.BOW;
  }

  @Override
  public int getUseDuration(ItemStack stack, LivingEntity entity) {
    return 20;
  }

  @Override
  public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
    return itemAbility == net.neoforged.neoforge.common.ItemAbilities.FIRESTARTER_LIGHT;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    player.startUsingItem(hand);
    return InteractionResultHolder.consume(stack);
  }
}
