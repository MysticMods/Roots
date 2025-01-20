package mysticmods.roots.block;

import mysticmods.roots.blockentity.template.UseDelegatedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class UseDelegatedBlock extends Block implements EntityBlock {
  public UseDelegatedBlock(Properties p_49795_) {
    super(p_49795_);
  }

  // TODO: ???

  @Override
  protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
    BlockEntity be = level.getBlockEntity(pos);
    if (be instanceof UseDelegatedBlockEntity ube) {
      return fromResult(ube.use(state, level, pos, player, hitResult, hand, stack));
    }

    return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
  }

  @Override
  public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult ray) {
    BlockEntity be = level.getBlockEntity(pos);
    if (be instanceof UseDelegatedBlockEntity ube) {
      return ube.use(state, level, pos, player, ray, InteractionHand.MAIN_HAND, player.getMainHandItem());
    }

    return super.useWithoutItem(state, level, pos, player, ray);
  }

  public static ItemInteractionResult fromResult (InteractionResult result) {
    return switch (result) {
      case CONSUME -> ItemInteractionResult.CONSUME;
      case SUCCESS -> ItemInteractionResult.SUCCESS;
      case CONSUME_PARTIAL -> ItemInteractionResult.CONSUME_PARTIAL;
      case FAIL -> ItemInteractionResult.FAIL;
      default -> ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    };
  }
}
