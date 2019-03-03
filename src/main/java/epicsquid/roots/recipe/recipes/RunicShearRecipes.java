package epicsquid.roots.recipe.recipes;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.RunicShearRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RunicShearRecipes {

  public static void initRecipes() {
    ModRecipes
        .addRunicShearRecipe(new RunicShearRecipe(Blocks.MOSSY_COBBLESTONE, Blocks.COBBLESTONE, ModItems.terra_moss, "terra_moss", new ItemStack(Blocks.MOSSY_COBBLESTONE)));
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Blocks.WHEAT, null, ModItems.wildewheet, "wildewheet", new ItemStack(Items.WHEAT)));
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Blocks.CARROTS, null, ModItems.aubergine, "aubergine", new ItemStack(Items.CARROT)));
    ModRecipes.addRunicShearRecipe(new RunicShearRecipe(Blocks.BEETROOTS, null, ModItems.spirit_herb, "spirit_herb", new ItemStack(Items.BEETROOT)));
  }
}
