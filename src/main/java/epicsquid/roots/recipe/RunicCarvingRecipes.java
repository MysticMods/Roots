package epicsquid.roots.recipe;

import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.RunicCarvingRecipe;

public class RunicCarvingRecipes {

  public static void initRecipes() {
    ModRecipes.addRunicCarvingRecipe(new RunicCarvingRecipe(ModBlocks.elemental_soil.getDefaultState(), ModBlocks.elemental_soil_fire.getDefaultState(), HerbRegistry.getHerbByName("infernal_bulb"), "elemental_soil_fire"));
    ModRecipes.addRunicCarvingRecipe(new RunicCarvingRecipe(ModBlocks.elemental_soil.getDefaultState(), ModBlocks.elemental_soil_water.getDefaultState(), HerbRegistry.getHerbByName("dewgonia"), "elemental_soil_water"));
    ModRecipes.addRunicCarvingRecipe(new RunicCarvingRecipe(ModBlocks.elemental_soil.getDefaultState(), ModBlocks.elemental_soil_air.getDefaultState(), HerbRegistry.getHerbByName("cloud_berry"), "elemental_soil_air"));
    ModRecipes.addRunicCarvingRecipe(new RunicCarvingRecipe(ModBlocks.elemental_soil.getDefaultState(), ModBlocks.elemental_soil_earth.getDefaultState(), HerbRegistry.getHerbByName("stalicripe"), "elemental_soil_earth"));
    ModRecipes.addRunicCarvingRecipe(new RunicCarvingRecipe(ModBlocks.wildwood_log.getDefaultState(), epicsquid.roots.init.ModBlocks.wildwood_rune.getDefaultState(), HerbRegistry.getHerbByName("moonglow_leaf"), "wildwood_rune"));
  }
}
