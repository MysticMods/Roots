package epicsquid.roots.recipe.recipes;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.RunicShearRecipe;
import net.minecraft.init.Blocks;

public class RunicShearRecipes {

  public static void initRecipes() {
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Blocks.MOSSY_COBBLESTONE, Blocks.COBBLESTONE, ModItems.terra_moss));
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Blocks.WHEAT, null, ModItems.wildewheet));
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Blocks.CARROTS, null, ModItems.aubergine));
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Blocks.RED_FLOWER, Blocks.AIR, ModItems.pereskia));
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Blocks.YELLOW_FLOWER, Blocks.AIR, ModItems.pereskia));
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Blocks.SAPLING, Blocks.AIR, ModItems.spirit_herb));
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Blocks.BROWN_MUSHROOM, Blocks.AIR, ModItems.fungus_cap));
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Blocks.RED_MUSHROOM, Blocks.AIR, ModItems.fungus_cap));
  }
}
