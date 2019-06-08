package epicsquid.roots.integration.jei.carving;

import epicsquid.roots.recipe.BarkRecipe;
import epicsquid.roots.recipe.RunicCarvingRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BarkRecipeWrapper implements IRecipeWrapper {

  public final BarkRecipe recipe;

  public BarkRecipeWrapper(BarkRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    ingredients.setInput(VanillaTypes.ITEM, recipe.getBlockStack());
    ingredients.setOutput(VanillaTypes.ITEM, recipe.getBarkStack(1));
  }
}
