package epicsquid.roots;

import java.util.List;

import epicsquid.mysticallib.fx.EffectManager;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.roots.capability.PlayerGroveCapabilityProvider;
import epicsquid.roots.entity.spell.EntityPetalShell;
import epicsquid.roots.network.MessagePlayerGroveUpdate;
import epicsquid.roots.network.fx.MessageMindWardRingFX;
import epicsquid.roots.network.fx.MessagePetalShellBurstFX;
import epicsquid.roots.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onDamage(LivingHurtEvent event){
    if (event.getEntityLiving() instanceof EntityPlayer && !event.getEntityLiving().getEntityWorld().isRemote){
      EntityPlayer p = ((EntityPlayer)event.getEntityLiving());
      List<EntityPetalShell> shells = p.getEntityWorld().getEntitiesWithinAABB(EntityPetalShell.class, new AxisAlignedBB(p.posX-0.5,p.posY,p.posZ-0.5,p.posX+0.5,p.posY+2.0,p.posZ+0.5));
      if (shells.size() > 0){
        for (EntityPetalShell s : shells){
          if (s.playerId.compareTo(p.getUniqueID()) == 0){
            if (s.getDataManager().get(s.getCharge()) > 0){
              event.setAmount(0);
              event.setCanceled(true);
              s.getDataManager().set(s.getCharge(), s.getDataManager().get(s.getCharge())-1);
              s.getDataManager().setDirty(s.getCharge());
              PacketHandler.INSTANCE.sendToAll(new MessagePetalShellBurstFX(p.posX,p.posY+1.0f,p.posZ));
              if (s.getDataManager().get(s.getCharge()) <= 0){
                p.world.removeEntity(s);
              }
            }
          }
        }
      }
    }
    if (event.getEntity().getEntityData().hasKey(Constants.MIND_WARD_TAG)){
      if (event.getSource().getTrueSource() instanceof EntityLivingBase){
        if (event.getSource().getTrueSource().getUniqueID().compareTo(event.getEntity().getUniqueID()) != 0){
          event.getEntity().getEntityData().removeTag(Constants.MIND_WARD_TAG);
        }
      }
    }
    if (event.getSource().getTrueSource() instanceof EntityLivingBase){
      if (event.getSource().getTrueSource().getEntityData().hasKey(Constants.MIND_WARD_TAG)){
        EntityLivingBase e = (EntityLivingBase)event.getSource().getTrueSource();
        e.attackEntityFrom(DamageSource.WITHER, event.getAmount()*2.0f);
        event.setAmount(0);
        PacketHandler.INSTANCE.sendToAll(new MessageMindWardRingFX(e.posX,e.posY+1.0,e.posZ));
      }
    }
  }

}
