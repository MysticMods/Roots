package epicsquid.roots.proxy;

import epicsquid.roots.advancements.Advancements;
import epicsquid.roots.command.CommandRitual;
import epicsquid.roots.command.CommandRoots;
import epicsquid.roots.command.CommandStaff;
import epicsquid.roots.init.ModEntities;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.integration.cfb.RootsCFB;
import epicsquid.roots.integration.chisel.RootsChisel;
import epicsquid.roots.integration.consecration.Consecration;
import epicsquid.roots.integration.crafttweaker.commands.Inject;
import epicsquid.roots.integration.endercore.EndercoreHarvest;
import epicsquid.roots.integration.harvest.HarvestIntegration;
import epicsquid.roots.integration.jer.JERIntegration;
import epicsquid.roots.integration.patchouli.ConfigKeys;
import epicsquid.roots.integration.thaumcraft.ThaumcraftInit;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.spell.SpellRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;

public class CommonProxy {
  public void preInit(FMLPreInitializationEvent event) {
    SpellRegistry.preInit();
    RitualRegistry.preInit();
    ModEntities.registerLootTables();
    if (Loader.isModLoaded("thaumcraft")) {
      ThaumcraftInit.init();
    }
  }

  public void init(FMLInitializationEvent event) {
    SpellRegistry.init();
    RitualRegistry.init();
    ConfigKeys.init();
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
    if (Loader.isModLoaded("crafttweaker")) {
      Inject.inject();
    }
    if (Loader.isModLoaded("cookingforblockheads")) {
      RootsCFB.init();
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
    ModRecipes.generateLifeEssence();

    try {
      Files.write(Paths.get("roots.log"), Collections.singletonList(""), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException ignored) {
    }
  }

  public void serverStarting(FMLServerStartingEvent event) {
    event.registerServerCommand(new CommandStaff());
    event.registerServerCommand(new CommandRoots());
    event.registerServerCommand(new CommandRitual());
  }
}
