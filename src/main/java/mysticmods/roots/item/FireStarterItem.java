package mysticmods.roots.item;

import mysticmods.roots.RootsTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class FireStarterItem extends Item {
  public FireStarterItem(Properties props) {
    super(props);
  }

  @Override
  public void onUseTick(World level, LivingEntity entity, ItemStack stack, int count) {
    if (level.isClientSide() && entity instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) entity;
      BlockRayTraceResult ray = getPlayerPOVHitResult(level, player, RayTraceContext.FluidMode.NONE);
      if (ray.getType() == RayTraceResult.Type.BLOCK && count % 3 == 0) {
        int runs = player.getRandom().nextInt(3) + 2;
        Vector3d hitVec = ray.getLocation();
        for (int i = 0; i < runs; i++) {
          level.addParticle(ParticleTypes.SMOKE, hitVec.x, hitVec.y, hitVec.z, 0, 0.05, 0);
        }
      }
    }
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, World level, LivingEntity entity) {
    if (!level.isClientSide() && entity instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) entity;
      BlockRayTraceResult ray = getPlayerPOVHitResult(level, player, RayTraceContext.FluidMode.NONE);
      boolean used = false;
      if (ray.getType() == RayTraceResult.Type.BLOCK) {
        BlockPos blockpos = ray.getBlockPos();
        if (level.mayInteract(player, blockpos) && player.mayUseItemAt(blockpos, ray.getDirection(), stack)) {
          BlockState stateAt = level.getBlockState(blockpos);
          if (stateAt.isAir()) {
            level.playSound(player, blockpos, SoundEvents.FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            level.setBlock(blockpos, Blocks.FIRE.defaultBlockState(), 11);
            used = true;
          } else if (stateAt.is(RootsTags.Blocks.PYRE)) {
            // TODO: Light the Pyre
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
  public UseAction getUseAnimation(ItemStack p_77661_1_) {
    return UseAction.BOW;
  }

  @Override
  public int getUseDuration(ItemStack p_77626_1_) {
    return 60;
  }

  @Override
  public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
    ItemStack stack = player.getItemInHand(hand);
    player.startUsingItem(hand);
    return ActionResult.consume(stack);
  }
}
