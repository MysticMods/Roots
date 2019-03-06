package epicsquid.roots.integration.jei.carving;

import java.util.ArrayList;
import java.util.List;

import epicsquid.roots.recipe.RunicCarvingRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class RunicCarvingWrapper implements IRecipeWrapper {

  public final RunicCarvingRecipe recipe;

  public RunicCarvingWrapper(RunicCarvingRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    List<ItemStack> inputs = new ArrayList<>();
    inputs.add(new ItemStack(recipe.getCarvingBlock().getBlock()));
    inputs.add(new ItemStack(recipe.getHerb().getItem()));
    ingredients.setInputs(VanillaTypes.ITEM, inputs);
    ingredients.setOutput(VanillaTypes.ITEM, new ItemStack(recipe.getRuneBlock().getBlock()));
  }
}
