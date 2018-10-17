package epicsquid.roots;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.roots.capability.PlayerGroveCapabilityProvider;
import epicsquid.roots.network.MessagePlayerGroveUpdate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class EventManager {

  public static long ticks = 0;

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onTick(TickEvent.ClientTickEvent event) {
    if (event.side == Side.CLIENT) {
      ClientProxy.particleRenderer.updateParticles();
      ticks++;
    }
  }

  @SubscribeEvent
  public void copyCapabilities(PlayerEvent.Clone event) {
    if (event.isWasDeath()) {
      if (event.getOriginal().hasCapability(PlayerGroveCapabilityProvider.PLAYER_GROVE_CAPABILITY, null)) {
        event.getEntityPlayer().getCapability(PlayerGroveCapabilityProvider.PLAYER_GROVE_CAPABILITY, null)
            .setData(event.getOriginal().getCapability(PlayerGroveCapabilityProvider.PLAYER_GROVE_CAPABILITY, null).getData());
      }
    }
  }

  @SubscribeEvent
  public void addCapabilities(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof EntityPlayer) {
      event.addCapability(new ResourceLocation(Roots.MODID, "player_grove_capability"), new PlayerGroveCapabilityProvider());
    }
  }

  @SubscribeEvent
  public void livingUpdate(LivingUpdateEvent event) {
    if (event.getEntity() instanceof EntityPlayer) {
      if (event.getEntity().hasCapability(PlayerGroveCapabilityProvider.PLAYER_GROVE_CAPABILITY, null)) {
        if (!event.getEntity().world.isRemote && event.getEntity().getCapability(PlayerGroveCapabilityProvider.PLAYER_GROVE_CAPABILITY, null).isDirty()) {
          PacketHandler.INSTANCE.sendToAll(new MessagePlayerGroveUpdate(event.getEntity().getUniqueID(),
              event.getEntity().getCapability(PlayerGroveCapabilityProvider.PLAYER_GROVE_CAPABILITY, null).getData()));
          event.getEntity().getCapability(PlayerGroveCapabilityProvider.PLAYER_GROVE_CAPABILITY, null).clean();
        }
      }
    }
  }

}
