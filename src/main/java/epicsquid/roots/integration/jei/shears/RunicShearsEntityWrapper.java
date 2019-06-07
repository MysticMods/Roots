package epicsquid.roots.integration.jei.shears;

import epicsquid.roots.recipe.RunicShearRecipe;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RunicShearsEntityWrapper implements IRecipeWrapper, ITooltipCallback<ItemStack> {

  public final RunicShearRecipe recipe;

  public RunicShearsEntityWrapper(RunicShearRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    ingredients.setInput(VanillaTypes.ITEM, this.recipe.getOptionalDisplayItem());
    ingredients.setOutput(VanillaTypes.ITEM, this.recipe.getDrop());
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

  }

  @Override
  public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {

  }
}
