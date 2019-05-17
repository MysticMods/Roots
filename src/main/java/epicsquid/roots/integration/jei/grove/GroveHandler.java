package epicsquid.roots.integration.jei.grove;

import epicsquid.roots.recipe.crafting.GroveCraftingRecipe;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

public class GroveHandler implements IRecipeHandler<GroveCraftingRecipe> {
  @Override
  public Class<GroveCraftingRecipe> getRecipeClass() {
    return GroveCraftingRecipe.class;
  }

  @Override
  public String getRecipeCategoryUid(GroveCraftingRecipe recipe) {
    return VanillaRecipeCategoryUid.CRAFTING;
  }

  @Override
  public IRecipeWrapper getRecipeWrapper(GroveCraftingRecipe recipe) {
    return new GroveWrapper(recipe);
  }

  @Override
  public boolean isRecipeValid(GroveCraftingRecipe recipe) {
    return true;
  }
}
