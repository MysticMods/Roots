package epicsquid.roots.integration.jei.mortar;

import java.util.Arrays;

import epicsquid.roots.recipe.MortarRecipe;
import epicsquid.roots.recipe.RunicShearRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.crafting.Ingredient;

public class MortarWrapper implements IRecipeWrapper {

  public final MortarRecipe recipe;

  public MortarWrapper(MortarRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    for (Ingredient ingredient: recipe.getIngredients()) {
      ingredients.setInputs(VanillaTypes.ITEM, Arrays.asList(ingredient.getMatchingStacks()));
    }
    ingredients.setOutput(VanillaTypes.ITEM, this.recipe.getResult());
  }
}
