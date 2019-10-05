package epicsquid.roots.integration.jei.shears;

import epicsquid.roots.Roots;
import epicsquid.roots.integration.jei.JEIRootsPlugin;
import epicsquid.roots.recipe.RunicShearRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class RunicShearsEntityCategory implements IRecipeCategory<RunicShearsEntityWrapper> {

  private final IDrawable background;

  public RunicShearsEntityCategory(IGuiHelper helper) {
    this.background = helper.createDrawable(new ResourceLocation(Roots.MODID, "textures/gui/jei/runic_shears_entity.png"), 0, 0, 122, 84);
  }

  @Override
  public String getUid() {
    return JEIRootsPlugin.RUNIC_SHEARS_ENTITY;
  }

  @Override
  public String getTitle() {
    return I18n.format("container." + JEIRootsPlugin.RUNIC_SHEARS_ENTITY + ".name");
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
    group.init(0, true, 104, 32);
    group.set(0, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
  }
}
