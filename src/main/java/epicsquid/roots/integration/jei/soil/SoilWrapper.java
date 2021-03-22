package epicsquid.roots.integration.jei.soil;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.List;

public class SoilWrapper implements IRecipeWrapper {

  public SoilRecipe recipe;

  public SoilWrapper(SoilRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    switch (recipe.getType()) {
      case WATER:
      case FIRE:
        SoilCategory.liquid.draw(minecraft, 15, 23);
        break;
      case AIR:
        SoilCategory.air.draw(minecraft, 15, 23);
        break;
      case EARTH:
        SoilCategory.earth.draw(minecraft, 15, 23);
        break;
    }
  }

  public static List<FluidStack> FLUIDS = Arrays.asList(new FluidStack(FluidRegistry.LAVA, 1000), new FluidStack(FluidRegistry.WATER, 1000));

  @Override
  public void getIngredients(IIngredients ingredients) {
    if (recipe != null) {
      ingredients.setInput(VanillaTypes.ITEM, this.recipe.getSoil());
      ingredients.setOutput(VanillaTypes.ITEM, this.recipe.getOutput());
      ingredients.setInputs(VanillaTypes.FLUID, FLUIDS);
    }
  }
}
