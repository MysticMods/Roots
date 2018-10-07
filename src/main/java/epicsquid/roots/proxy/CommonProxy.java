package epicsquid.roots.proxy;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.capability.IPlayerGroveCapability;
import epicsquid.roots.capability.PlayerGroveCapability;
import epicsquid.roots.capability.PlayerGroveCapabilityStorage;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.util.OfferingUtil;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
  public void preInit(FMLPreInitializationEvent event) {
    ModItems.registerOredict();

    CapabilityManager.INSTANCE.register(IPlayerGroveCapability.class, new PlayerGroveCapabilityStorage(), PlayerGroveCapability::new);
  }

  public void init(FMLInitializationEvent event) {
    RitualRegistry.init();
    SpellRegistry.init();
    OfferingUtil.init();
  }

  public void postInit(FMLPostInitializationEvent event) {
  }
}
