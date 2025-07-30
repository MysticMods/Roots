package mysticmods.roots.event.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = RootsAPI.MODID)
public class BlockEventHandler {
  @SubscribeEvent
  public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
    Player player = event.getEntity();
    InteractionHand hand = event.getHand();
    ItemStack heldItem = player.getItemInHand(hand);
    if (heldItem.is(RootsTags.Items.CASTING_TOOLS)) {
      BlockState block = player.level().getBlockState(event.getPos());
      if (!block.is(RootsTags.Blocks.ALLOW_CASTING_TOOL_RIGHT_CLICK)) {
        event.setUseItem(TriState.TRUE);
        event.setUseBlock(TriState.FALSE);
      }
    }
  }

  @SubscribeEvent
  public static void onHoeUse(BlockEvent.BlockToolModificationEvent event) {
    if (event.getItemAbility() == ItemAbilities.HOE_TILL) {
      LevelAccessor world = event.getLevel();
      UseOnContext pContext = event.getContext();
      BlockPos blockpos = pContext.getClickedPos();
      FluidState fluidstate = world.getFluidState(blockpos.above());
      if (pContext.getClickedFace() != Direction.DOWN && fluidstate.is(FluidTags.WATER)) {
        BlockState blockstate = world.getBlockState(blockpos);
        if (blockstate.is(RootsTags.Blocks.UNDERWATER_FARMLAND)) {
          event.setFinalState(Blocks.FARMLAND.defaultBlockState());
        }
      }
    }
  }
}
