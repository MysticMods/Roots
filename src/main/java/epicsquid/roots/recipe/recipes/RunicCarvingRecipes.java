package epicsquid.roots.recipe.recipes;

import epicsquid.mysticalworld.init.ModBlocks;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.RunicCarvingRecipe;

public class RunicCarvingRecipes {

  public static void initRecipes() {
    ModRecipes.addRunicCarvingRecipe(new RunicCarvingRecipe(ModBlocks.runic_soil.getDefaultState(), ModBlocks.runic_soil_fire.getDefaultState(), HerbRegistry.getHerbByName("infernal_bulb")));
    ModRecipes.addRunicCarvingRecipe(new RunicCarvingRecipe(ModBlocks.runic_soil.getDefaultState(), ModBlocks.runic_soil_water.getDefaultState(), HerbRegistry.getHerbByName("dewgonia")));
    ModRecipes.addRunicCarvingRecipe(new RunicCarvingRecipe(ModBlocks.runic_soil.getDefaultState(), ModBlocks.runic_soil_air.getDefaultState(), HerbRegistry.getHerbByName("cloud_berry")));
    ModRecipes.addRunicCarvingRecipe(new RunicCarvingRecipe(ModBlocks.runic_soil.getDefaultState(), ModBlocks.runic_soil_earth.getDefaultState(), HerbRegistry.getHerbByName("stalicripe")));
    ModRecipes.addRunicCarvingRecipe(new RunicCarvingRecipe(ModBlocks.wildwoodLog.getDefaultState(), epicsquid.roots.init.ModBlocks.speed_rune.getDefaultState(), HerbRegistry.getHerbByName("moonglow_leaf")));
    ModRecipes.addRunicCarvingRecipe(new RunicCarvingRecipe(ModBlocks.wildwoodLog.getDefaultState(), epicsquid.roots.init.ModBlocks.overgrowth_rune.getDefaultState(), HerbRegistry.getHerbByName("terra_moss")));
  }
}
