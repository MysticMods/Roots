package epicsquid.roots.integration.jei.grove;

import epicsquid.roots.recipe.GroveCraftingRecipe;
import epicsquid.roots.recipe.MortarRecipe;
import epicsquid.roots.spell.SpellBase;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroveWrapper implements IRecipeWrapper {

  public GroveCraftingRecipe recipe;

  public GroveWrapper(GroveCraftingRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    if (recipe != null) {
      List<List<ItemStack>> inputs = new ArrayList<>();
      for (Ingredient ingredient : recipe.getIngredients()) {
        if (ingredient == null || ingredient == Ingredient.EMPTY) {
          inputs.add(new ArrayList<>());
        } else {
          inputs.add(Arrays.asList(ingredient.getMatchingStacks()));
        }
      }
      ingredients.setInputLists(VanillaTypes.ITEM, inputs);
      ingredients.setOutput(VanillaTypes.ITEM, this.recipe.getResult());
    }
  }
}
