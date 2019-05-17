package epicsquid.roots.integration.jei.grove;

import epicsquid.roots.Roots;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

public class GroveWrapper implements IRecipeWrapper {

  public static ResourceLocation texture = new ResourceLocation(Roots.MODID, "textures/gui/grove_stone_icon.png");

  public IRecipe recipe;

  public GroveWrapper(IRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    if (recipe != null) {
      for (Ingredient ingredient : recipe.getIngredients()) {
        if (ingredient == null || ingredient == Ingredient.EMPTY) continue;
        ingredients.setInputs(VanillaTypes.ITEM, Arrays.asList(ingredient.getMatchingStacks()));
      }
      ingredients.setOutput(VanillaTypes.ITEM, this.recipe.getRecipeOutput());
    }
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    minecraft.getTextureManager().bindTexture(texture);
    int width = 12;
    int height = 22;
  }
}
