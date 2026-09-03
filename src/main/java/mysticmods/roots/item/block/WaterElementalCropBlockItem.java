package mysticmods.roots.item.block;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.mixin.accessor.AccessorMixinCropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class WaterElementalCropBlockItem extends ItemNameBlockItem {
  public WaterElementalCropBlockItem(Block block, Properties properties) {
    super(block, properties);
  }

  @Override
  public InteractionResult place(BlockPlaceContext context) {
    InteractionResult result = super.place(context);
    if (result == InteractionResult.FAIL) {
      if (context.getPlayer() != null && context.getItemInHand().is(RootsTags.Items.DEWGONIA_SEEDS)) {
        BlockState blockstate = this.getBlock().getStateForPlacement(context);
        if (blockstate == null) {
          Level level = context.getLevel();
          BlockPos pos = context.getClickedPos();
          if (level.getFluidState(pos).isEmpty() && ((AccessorMixinCropBlock)getBlock()).roots$CallMayPlaceOn(level.getBlockState(pos.below()), level, pos.below())) {
            context.getPlayer()
                .displayClientMessage(Component.translatable("roots.message.dewgonia_not_waterlogged"), true);
          }
        }
      }
    }

    return result;
  }
}
