package mysticmods.roots.event.forge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;




@EventBusSubscriber(modid= RootsAPI.MODID)
public class TooltipHandler {
  @SubscribeEvent
  public static void onItemTooltip (ItemTooltipEvent event) {
    ItemStack stack = event.getItemStack();
    if (stack.is(RootsTags.Items.NYI)) {
      event.getToolTip().add(Component.literal("Not Yet Implemented"));
    }
  }
}
