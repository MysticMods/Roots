package mysticmods.roots.client;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(value= Dist.CLIENT, modid= RootsAPI.MODID, bus = EventBusSubscriber.Bus.GAME)
public class RenderTickHandler {
  private static float clientTicks = 0;

  @SubscribeEvent
  public static void onRenderStage(RenderLevelStageEvent event) {
    if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
      clientTicks += event.getPartialTick().getGameTimeDeltaPartialTick(false);
    }
  }

  @SubscribeEvent
  public static void onClientTick (ClientTickEvent.Post post) {
    Minecraft minecraft = Minecraft.getInstance();
    Level level;
    //noinspection ConstantValue
    if (minecraft.player != null && ((level = minecraft.player.level()) != null) && minecraft.gameMode != null) {
    }
  }

  public static float getClientTicks () {
    return clientTicks;
  }
}
