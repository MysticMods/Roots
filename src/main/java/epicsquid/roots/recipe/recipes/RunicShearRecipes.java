package epicsquid.roots.recipe.recipes;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.RunicShearRecipe;
import net.minecraft.init.Blocks;

public class RunicShearRecipes {

  public static void initRecipes() {
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Blocks.MOSSY_COBBLESTONE, Blocks.COBBLESTONE, ModItems.terra_moss));
  }
}
