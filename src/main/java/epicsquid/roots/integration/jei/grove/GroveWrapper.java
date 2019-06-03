package epicsquid.roots.integration.jei.grove;

import epicsquid.roots.recipe.GroveCraftingRecipe;
import epicsquid.roots.recipe.MortarRecipe;
import epicsquid.roots.spell.SpellBase;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.crafting.Ingredient;

import java.util.Arrays;

public class GroveWrapper implements IRecipeWrapper {

  public GroveCraftingRecipe recipe;

  public GroveWrapper(GroveCraftingRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    if (recipe != null) {
      for (Ingredient ingredient : recipe.getIngredients()) {
        if (ingredient == null || ingredient == Ingredient.EMPTY) continue;
        ingredients.setInputs(VanillaTypes.ITEM, Arrays.asList(ingredient.getMatchingStacks()));
      }
      ingredients.setOutput(VanillaTypes.ITEM, this.recipe.getResult());
    }
  }
}
