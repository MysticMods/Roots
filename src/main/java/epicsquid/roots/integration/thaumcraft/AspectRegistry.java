package epicsquid.roots.integration.thaumcraft;

import epicsquid.roots.Roots;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.api.aspects.AspectRegistryEvent;

@Mod.EventBusSubscriber(modid= Roots.MODID)
public class AspectRegistry {
  @SubscribeEvent
  @Optional.Method(modid="thaumcraft")
  public static void registerAspects (AspectRegistryEvent event) {

  }
}
