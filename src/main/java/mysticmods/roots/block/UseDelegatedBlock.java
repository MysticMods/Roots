package mysticmods.roots.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class UseDelegatedBlock extends Block implements EntityBlock, IUseDelegatedBlock {
  public UseDelegatedBlock(Properties p_49795_) {
    super(p_49795_);
  }

  @Override
  protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
    ItemInteractionResult result = useItemOnDelegate(stack, state, level, pos, player, hand, hitResult);
    if (result != null) {
      return result;
    }
    return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
  }

  @Override
  public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult ray) {
    InteractionResult result = useWithoutItemOnDelegate(state, level, pos, player, ray);
    if (result != null) {
      return result;
    }

    return super.useWithoutItem(state, level, pos, player, ray);
  }
}
