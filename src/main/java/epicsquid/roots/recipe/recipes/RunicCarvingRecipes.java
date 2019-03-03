package epicsquid.roots.recipe.recipes;

import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.RunicCarvingRecipe;

public class RunicCarvingRecipes {

  public static void initRecipes() {
    ModRecipes.addRunicCarvingRecipe(new RunicCarvingRecipe(ModBlocks.runic_soil.getDefaultState(), ModBlocks.runic_soil_fire.getDefaultState(), HerbRegistry.getHerbByName("infernal_bulb"), "runic_soil_fire"));
    ModRecipes.addRunicCarvingRecipe(new RunicCarvingRecipe(ModBlocks.runic_soil.getDefaultState(), ModBlocks.runic_soil_water.getDefaultState(), HerbRegistry.getHerbByName("dewgonia"), "runic_soil_water"));
    ModRecipes.addRunicCarvingRecipe(new RunicCarvingRecipe(ModBlocks.runic_soil.getDefaultState(), ModBlocks.runic_soil_air.getDefaultState(), HerbRegistry.getHerbByName("cloud_berry"), "runic_soil_air"));
    ModRecipes.addRunicCarvingRecipe(new RunicCarvingRecipe(ModBlocks.runic_soil.getDefaultState(), ModBlocks.runic_soil_earth.getDefaultState(), HerbRegistry.getHerbByName("stalicripe"), "runic_soil_earth"));
    ModRecipes.addRunicCarvingRecipe(new RunicCarvingRecipe(ModBlocks.wildwoodLog.getDefaultState(), epicsquid.roots.init.ModBlocks.speed_rune.getDefaultState(), HerbRegistry.getHerbByName("moonglow_leaf"), "speed_rune"));
  }
}
