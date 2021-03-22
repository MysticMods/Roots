package epicsquid.roots.integration.jei.soil;

import epicsquid.roots.Roots;
import epicsquid.roots.integration.jei.JEIRootsPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.List;

public class SoilCategory implements IRecipeCategory<SoilWrapper> {
  public static IDrawable air;
  public static IDrawable earth;
  public static IDrawable liquid;
  public static IDrawable background;

  public SoilCategory(IGuiHelper helper) {
    air = helper.createDrawable(new ResourceLocation(Roots.MODID, "textures/gui/jei/elemental_soil_air.png"), 0, 0, 24, 20);
    earth = helper.createDrawable(new ResourceLocation(Roots.MODID, "textures/gui/jei/elemental_soil_earth.png"), 0, 0, 24, 20);
    liquid = helper.createDrawable(new ResourceLocation(Roots.MODID, "textures/gui/jei/elemental_soil_liquid.png"), 0, 0, 24, 20);
    background = helper.createDrawable(new ResourceLocation(Roots.MODID, "textures/gui/jei/elemental_soil.png"), 0, 0, 75, 44);
  }

  @Override
  public String getUid() {
    return JEIRootsPlugin.SOIL;
  }

  @Override
  public String getTitle() {
    return I18n.format("container." + JEIRootsPlugin.SOIL + ".name");
  }

  @Override
  public String getModName() {
    return Roots.NAME;
  }

  @Override
  public IDrawable getBackground() {
    return background;
  }

  @Override
  public void setRecipe(IRecipeLayout recipeLayout, SoilWrapper recipeWrapper, IIngredients ingredients) {
    IGuiItemStackGroup group = recipeLayout.getItemStacks();
    IGuiFluidStackGroup group2 = recipeLayout.getFluidStacks();
    if (recipeWrapper.recipe != null) {
      group.init(0, true, 0, 2);
      group.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
      group.init(1, false, 57, 24);
      group.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));

      if (recipeWrapper.recipe.getType() == SoilRecipe.RecipeType.FIRE || recipeWrapper.recipe.getType() == SoilRecipe.RecipeType.WATER) {
        group2.init(0, true, 19, 25);
        if (recipeWrapper.recipe.getType() == SoilRecipe.RecipeType.FIRE) {
          group2.set(0, ingredients.getInputs(VanillaTypes.FLUID).get(0));
        } else {
          group2.set(0, ingredients.getInputs(VanillaTypes.FLUID).get(1));
        }
      }
    }
  }
}
