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

  @Override
  public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult ray) {
    BlockEntity be = level.getBlockEntity(pos);
    if (be instanceof UseDelegatedBlockEntity ube) {
      return ube.useWithoutItem(state, level, pos, player, ray);
    }

    return super.useWithoutItem(state, level, pos, player, ray);
  }
}
