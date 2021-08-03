package epicsquid.roots.integration.jei.interact;

import epicsquid.roots.Roots;
import epicsquid.roots.integration.jei.JEIRootsPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class RitualVisualiseCategory implements IRecipeCategory<RitualVisualiseWrapper> {

  private final IDrawable background;

  public RitualVisualiseCategory(IGuiHelper helper) {
    this.background = helper.createDrawable(new ResourceLocation(Roots.MODID, "textures/gui/jei/ritual_visualise.png"), 0, 0, 54, 35);
  }

  @Override
  public String getUid() {
    return JEIRootsPlugin.RITUAL_VISUALISE;
  }

  @Override
  public String getTitle() {
    return I18n.format("container." + JEIRootsPlugin.RITUAL_VISUALISE+ ".name");
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
  public void setRecipe(IRecipeLayout recipeLayout, RitualVisualiseWrapper recipeWrapper, IIngredients ingredients) {
    IGuiItemStackGroup group = recipeLayout.getItemStacks();
    group.init(0, true, 1, 2);
    group.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
    group.init(1, false, 36, 2);
    group.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
  }
}
