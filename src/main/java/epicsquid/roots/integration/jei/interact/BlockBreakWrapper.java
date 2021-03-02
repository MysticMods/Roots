package epicsquid.roots.integration.jei.interact;

import epicsquid.roots.recipe.BarkRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;

import java.util.Arrays;
import java.util.Collections;

public class BlockBreakWrapper implements IRecipeWrapper {

  public final BlockBreakRecipe recipe;

  public BlockBreakWrapper(BlockBreakRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    ingredients.setInputLists(VanillaTypes.ITEM, Collections.singletonList(Arrays.asList(recipe.getBreaks().getMatchingStacks())));
    ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
  }
}
