package epicsquid.roots;

import epicsquid.mysticallib.proxy.ClientProxy;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class EventManager {

  public static long ticks = 0;

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onTick(TickEvent.ClientTickEvent event){
    if (event.side == Side.CLIENT){
      ClientProxy.particleRenderer.updateParticles();
      ticks ++;
    }
  }

}
