package epicsquid.roots.event;

import epicsquid.roots.Roots;
import epicsquid.roots.client.PatchouliHack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid= Roots.MODID, value= Dist.CLIENT)
public class ClientTicker {
  public static void tick (TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      PatchouliHack.timer.onDraw();
    }
  }
}
