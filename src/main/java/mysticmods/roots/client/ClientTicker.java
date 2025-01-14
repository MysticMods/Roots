package mysticmods.roots.client;

import mysticmods.roots.api.RootsAPI;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;


@EventBusSubscriber(value = Dist.CLIENT, modid = RootsAPI.MODID)
public class ClientTicker {
  @SubscribeEvent
  public static void onClientTick(ClientTickEvent.Post event) {
  }
}
