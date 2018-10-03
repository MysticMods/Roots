package epicsquid.roots.proxy;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
  public void preInit(FMLPreInitializationEvent event) {
    ModItems.registerOredict();
  }

  public void init(FMLInitializationEvent event) {
    RitualRegistry.init();
  }

  public void postInit(FMLPostInitializationEvent event) {
  }
}
