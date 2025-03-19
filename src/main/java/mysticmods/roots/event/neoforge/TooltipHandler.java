package mysticmods.roots.event.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;


@EventBusSubscriber(modid = RootsAPI.MODID)
public class TooltipHandler {
  @SubscribeEvent
  public static void onItemTooltip(ItemTooltipEvent event) {
    ItemStack stack = event.getItemStack();
    if (stack.is(RootsTags.Items.NYI)) {
      event.getToolTip().add(Component.translatable("roots.nyi"));
    }
    if (stack.is(RootsTags.Items.WIP)) {
      event.getToolTip().add(Component.translatable("roots.wip"));
    }
  }
}
