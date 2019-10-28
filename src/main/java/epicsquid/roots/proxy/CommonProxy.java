package epicsquid.roots.proxy;

import crafttweaker.mc1120.commands.CTChatCommand;
import epicsquid.roots.advancements.Advancements;
import epicsquid.roots.command.CommandRitual;
import epicsquid.roots.command.CommandRoots;
import epicsquid.roots.command.CommandStaff;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModEntities;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.integration.chisel.RootsChisel;
import epicsquid.roots.integration.consecration.Consecration;
import epicsquid.roots.integration.crafttweaker.commands.CommandProperties;
import epicsquid.roots.integration.crafttweaker.commands.CommandRecipes;
import epicsquid.roots.integration.endercore.EndercoreHarvest;
import epicsquid.roots.integration.harvest.HarvestIntegration;
import epicsquid.roots.integration.jer.JERIntegration;
import epicsquid.roots.integration.patchouli.ConfigKeys;
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
    ModuleRegistry.init();
    SpellRegistry.preInit();
    HerbRegistry.init();
    RitualRegistry.preInit();
    OfferingUtil.init();
    RuneRegistry.init();
    ModEntities.registerLootTables();
  }

  public void init(FMLInitializationEvent event) {
    SpellRegistry.init();
    RitualRegistry.init();
    ConfigKeys.init();
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
    if (Loader.isModLoaded("crafttweaker")) {
      CTChatCommand.registerCommand(new CommandProperties());
      CTChatCommand.registerCommand(new CommandRecipes());
    }
  }

  public void postInit(FMLPostInitializationEvent event) {
    SpellRegistry.finalise();
    RitualRegistry.finalise();
  }

  public void loadComplete(FMLLoadCompleteEvent event) {
    if (Loader.isModLoaded("harvest")) {
      HarvestIntegration.init();
    }
    Advancements.init();
    ModRecipes.clearGeneratedEntityRecipes();
  }

  public void serverStarting(FMLServerStartingEvent event) {
    event.registerServerCommand(new CommandStaff());
    event.registerServerCommand(new CommandRoots());
    event.registerServerCommand(new CommandRitual());
  }
}
