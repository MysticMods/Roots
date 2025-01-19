package mysticmods.roots.client;

import com.mojang.brigadier.context.ContextChain;
import mysticmods.roots.api.RootsAPI;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(value= Dist.CLIENT, modid= RootsAPI.MODID, bus = EventBusSubscriber.Bus.GAME)
public class RenderTickHandler {
  private static float clientTicks = 0;

  @SubscribeEvent
  public static void onClientTick (RenderLevelStageEvent event) {
    if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
      clientTicks += event.getPartialTick().getGameTimeDeltaPartialTick(true);
    }
  }

  public static float getClientTicks () {
    return clientTicks;
  }
}
