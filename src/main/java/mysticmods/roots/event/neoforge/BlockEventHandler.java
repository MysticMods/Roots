package mysticmods.roots.event.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid= RootsAPI.MODID, bus= EventBusSubscriber.Bus.GAME)
public class BlockEventHandler {
  @SubscribeEvent
  public static void onRightClickBlock (PlayerInteractEvent.RightClickBlock event) {
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
}
