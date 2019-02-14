package epicsquid.roots.recipe.recipes;

import epicsquid.mysticalworld.init.ModBlocks;
import epicsquid.roots.Roots;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.RunicCarvingRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

public class RunicCarvingRecipes {

  public static void initRecipes() {
    ModRecipes.addRunicCarvingRecipe(new RunicCarvingRecipe(ModBlocks.runic_soil.getDefaultState(), ModBlocks.runic_soil_fire.getDefaultState(), HerbRegistry.getHerbByName("infernal_bulb")));
    ModRecipes.addRunicCarvingRecipe(new RunicCarvingRecipe(ModBlocks.runic_soil.getDefaultState(), ModBlocks.runic_soil_water.getDefaultState(), HerbRegistry.getHerbByName("dewgonia")));
    ModRecipes.addRunicCarvingRecipe(new RunicCarvingRecipe(ModBlocks.runic_soil.getDefaultState(), ModBlocks.runic_soil_air.getDefaultState(), HerbRegistry.getHerbByName("cloud_berry")));
    ModRecipes.addRunicCarvingRecipe(new RunicCarvingRecipe(ModBlocks.runic_soil.getDefaultState(), ModBlocks.runic_soil_earth.getDefaultState(), HerbRegistry.getHerbByName("stalicripe")));
  }
}
