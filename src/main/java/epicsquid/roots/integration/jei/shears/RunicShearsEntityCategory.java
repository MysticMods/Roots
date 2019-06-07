package epicsquid.roots.integration.jei.shears;

import epicsquid.roots.Roots;
import epicsquid.roots.integration.jei.JEIRootsPlugin;
import epicsquid.roots.recipe.RunicShearRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class RunicShearsEntityCategory implements IRecipeCategory<RunicShearsEntityWrapper> {

  private final IDrawable background;

  public RunicShearsEntityCategory(IGuiHelper helper) {
    this.background = helper.createDrawable(new ResourceLocation(Roots.MODID, "textures/gui/jei/runic_shears.png"), 0, 0, 78, 22);
  }

  @Override
  public String getUid() {
    return JEIRootsPlugin.RUNIC_SHEARS;
  }

  @Override
  public String getTitle() {
    return I18n.format("container." + JEIRootsPlugin.RUNIC_SHEARS + ".name");
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
  public void setRecipe(IRecipeLayout recipeLayout, RunicShearsEntityWrapper recipeWrapper, IIngredients ingredients) {
    IGuiItemStackGroup group = recipeLayout.getItemStacks();
    RunicShearRecipe recipe = recipeWrapper.recipe;
    group.init(0, true, 0, 2);
    group.set(0, recipe.getOptionalDisplayItem());
    group.init(1, false, 60, 2);
    group.set(1, recipe.getDrop());
  }
}
