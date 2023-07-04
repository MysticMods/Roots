package mysticmods.roots.item;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.blockentity.PyreBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

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
        BlockPos blockpos = ray.getBlockPos().relative(ray.getDirection());
        if (level.mayInteract(player, blockpos) && player.mayUseItemAt(blockpos, ray.getDirection(), stack)) {
          BlockState stateAt = level.getBlockState(blockpos);
          BlockPos below = blockpos.below();
          BlockState stateBelow = level.getBlockState(below);
          if (stateBelow.is(RootsTags.Blocks.PYRES)) {
            if (level.getBlockEntity(below) instanceof PyreBlockEntity pyreBlockEntity) {
              level.playSound(player, below, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
              pyreBlockEntity.light(player, below);
              used = true;
            }
          } else if (stateAt.isAir()) {
            level.playSound(player, blockpos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.setBlock(blockpos, Blocks.FIRE.defaultBlockState(), 11);
            used = true;
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
  public UseAnim getUseAnimation(ItemStack p_77661_1_) {
    return UseAnim.BOW;
  }

  @Override
  public int getUseDuration(ItemStack p_77626_1_) {
    return 60;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    player.startUsingItem(hand);
    return InteractionResultHolder.consume(stack);
  }
}
