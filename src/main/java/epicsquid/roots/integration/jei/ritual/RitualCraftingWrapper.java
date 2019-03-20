package epicsquid.roots.integration.jei.ritual;

import java.util.List;

import epicsquid.roots.recipe.PyreCraftingRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class RitualCraftingWrapper implements IRecipeWrapper {

  public final PyreCraftingRecipe recipe;

  public RitualCraftingWrapper(PyreCraftingRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    List<ItemStack> inputs = recipe.getRecipe();
    ingredients.setInputs(VanillaTypes.ITEM, inputs);
    ingredients.setOutput(VanillaTypes.ITEM, recipe.getResult());
  }
}
