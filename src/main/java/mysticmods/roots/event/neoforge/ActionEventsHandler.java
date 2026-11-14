package mysticmods.roots.event.neoforge;

import mysticmods.roots.action.CraftItemAction;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.KeybindsNotified;
import mysticmods.roots.api.item.IKeybindItem;
import mysticmods.roots.init.ModActions;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.network.client.ClientboundNotifyKeybind;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = RootsAPI.MODID)
public class ActionEventsHandler {
  @SubscribeEvent
  public static void craftEvent(PlayerEvent.ItemCraftedEvent event) {
    Player player = event.getEntity();

    if (!player.level().isClientSide() && ModActions.CRAFT_ITEM.get().shouldTest()) {
      CraftItemAction.Context context = new CraftItemAction.Context((ServerLevel) event.getEntity()
          .level(), (ServerPlayer) player, event.getCrafting());
      ModActions.CRAFT_ITEM.get().accept(context);
    }

    if (!event.getEntity().level().isClientSide()) {
      Item item = event.getCrafting().getItem();
      if (item instanceof IKeybindItem) {
        KeybindsNotified keys = player.getData(ModAttachments.NOTIFIED_KEYBINDS);
        if (!keys.has(item)) {
          player.setData(ModAttachments.NOTIFIED_KEYBINDS, keys.add(item));
          PacketDistributor.sendToPlayer((ServerPlayer) player, new ClientboundNotifyKeybind(item));
        }
      }
    }
  }
}
