package epicsquid.roots.proxy;

import epicsquid.roots.effect.EffectManager;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.integration.harvest.HarvestIntegration;
import epicsquid.roots.integration.jer.JERIntegration;
import epicsquid.roots.recipe.RunicCarvingRecipes;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.rune.RuneRegistry;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.spell.modules.ModuleRegistry;
import epicsquid.roots.util.OfferingUtil;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
  public void preInit(FMLPreInitializationEvent event) {
  }

  public void init(FMLInitializationEvent event) {
    HerbRegistry.init();
    RitualRegistry.init();
    ModuleRegistry.init();
    SpellRegistry.init();
    OfferingUtil.init();
    EffectManager.init();
    RuneRegistry.init();
    RunicCarvingRecipes.initRecipes();
    if (Loader.isModLoaded("jeresources")) {
      JERIntegration.init();
    }
    //MapGenStructureIO.registerStructureComponent(ComponentDruidHut.class, Roots.MODID + ":" + "druidhut");
    //VillagerRegistry.instance().registerVillageCreationHandler(new ComponentDruidHut.CreationHandler());
  }

  public void postInit(FMLPostInitializationEvent event) {
  }

  public void loadComplete(FMLLoadCompleteEvent event) {
    if (Loader.isModLoaded("harvest")) {
      HarvestIntegration.init();
    }
  }
}
