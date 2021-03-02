package epicsquid.roots.integration.jei.interact;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;

import java.util.Arrays;
import java.util.Collections;

public class BlockRightClickWrapper implements IRecipeWrapper {

  public final BlockRightClickRecipe recipe;

  public BlockRightClickWrapper(BlockRightClickRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    ingredients.setInputLists(VanillaTypes.ITEM, Arrays.asList(Collections.singletonList(recipe.getInput()), recipe.getBlocks()));
    ingredients.setOutputLists(VanillaTypes.ITEM, Collections.singletonList(recipe.getOutputs()));
  }
}
