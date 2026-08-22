package mysticmods.roots.event.neoforge;

import mysticmods.roots.action.CraftItemAction;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModActions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = RootsAPI.MODID)
public class ActionEventsHandler {
  @SubscribeEvent
  public static void craftEvent(PlayerEvent.ItemCraftedEvent event) {
    if (!event.getEntity().level().isClientSide() && ModActions.CRAFT_ITEM.get().shouldTest()) {
      CraftItemAction.Context context = new CraftItemAction.Context((ServerLevel) event.getEntity()
          .level(), (ServerPlayer) event.getEntity(), event.getCrafting());
      ModActions.CRAFT_ITEM.get().accept(context);
    }
  }
}
