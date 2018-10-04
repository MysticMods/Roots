package epicsquid.roots.proxy;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.spell.SpellRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
  public void preInit(FMLPreInitializationEvent event) {
    ModItems.registerOredict();
  }

  public void init(FMLInitializationEvent event) {
    RitualRegistry.init();
    SpellRegistry.init();
  }

  public void postInit(FMLPostInitializationEvent event) {
  }
}
