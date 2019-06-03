package epicsquid.roots.event.handlers;

import com.google.common.collect.Sets;
import epicsquid.mysticalworld.entity.EntityDeer;
import epicsquid.roots.Roots;
import epicsquid.roots.capability.runic_shears.RunicShearsCapabilityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;

@Mod.EventBusSubscriber(modid = Roots.MODID)
public class CapabilityHandler {
  public static Set<Class<?>> entityClasses = Sets.newHashSet(EntityCow.class, EntityLlama.class, EntitySquid.class, EntityDeer.class);

  @SubscribeEvent
  public static void onAttachCapabilityEntity(AttachCapabilitiesEvent<Entity> event) {
    if (entityClasses.contains(event.getObject().getClass())) {
      event.addCapability(RunicShearsCapabilityProvider.IDENTIFIER, new RunicShearsCapabilityProvider());
    }
  }
}
