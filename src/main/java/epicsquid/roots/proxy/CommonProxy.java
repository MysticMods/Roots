package epicsquid.roots.proxy;

import epicsquid.roots.advancements.Advancements;
import epicsquid.roots.command.CommandRitual;
import epicsquid.roots.command.CommandRoots;
import epicsquid.roots.command.CommandStaff;
import epicsquid.roots.effect.EffectManager;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.integration.chisel.RootsChisel;
import epicsquid.roots.integration.consecration.Consecration;
import epicsquid.roots.integration.endercore.EndercoreHarvest;
import epicsquid.roots.integration.harvest.HarvestIntegration;
import epicsquid.roots.integration.jer.JERIntegration;
import epicsquid.roots.recipe.RunicCarvingRecipes;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.rune.RuneRegistry;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.spell.modules.ModuleRegistry;
import epicsquid.roots.util.OfferingUtil;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.*;

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
    if (Loader.isModLoaded("chisel")) {
      RootsChisel.init();
    }
    if (Loader.isModLoaded("endercore")) {
      EndercoreHarvest.init();
    }
    if (Loader.isModLoaded("consecration")) {
      Consecration.init();
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
    SpellRegistry.finalise();
    RitualRegistry.finalise();
    Advancements.init();
  }

  public void serverStarting (FMLServerStartingEvent event) {
    event.registerServerCommand(new CommandStaff());
    event.registerServerCommand(new CommandRoots());
    event.registerServerCommand(new CommandRitual());
  }
}
