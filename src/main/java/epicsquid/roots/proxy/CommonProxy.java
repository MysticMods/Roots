package epicsquid.roots.proxy;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.util.OfferingUtil;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
  public void preInit(FMLPreInitializationEvent event) {
    ModItems.registerOredict();
  }

  public void init(FMLInitializationEvent event) {
    HerbRegistry.init();
    RitualRegistry.init();
    SpellRegistry.init();
    OfferingUtil.init();
  }

  public void postInit(FMLPostInitializationEvent event) {
  }
}
