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
    ModRecipes.addRunicCarvingRecipe(new RunicCarvingRecipe(Blocks.DIRT.getDefaultState(), ModBlocks.runic_soil_fire.getDefaultState(), HerbRegistry.getHerbByName("infernal_bulb")));
  }
}
