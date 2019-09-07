package epicsquid.roots.integration.jei.carving;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.MossRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.Arrays;

public class MossRecipeWrapper implements IRecipeWrapper {

  public static ItemStack moss = ItemStack.EMPTY;
  public final MossRecipe recipe;

  public MossRecipeWrapper(MossRecipe recipe) {
    this.recipe = recipe;
    if (moss.isEmpty()) {
      moss = new ItemStack(ModItems.terra_moss);
    }
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    ingredients.setInput(VanillaTypes.ITEM, recipe.getInput());
    ingredients.setOutputs(VanillaTypes.ITEM, Arrays.asList(recipe.getOutput(), moss));
  }
}
