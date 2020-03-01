package epicsquid.roots.integration.jei.chrysopoeia;

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

public class ChrysopoeiaCategory implements IRecipeCategory<ChrysopoeiaWrapper> {

  private final IDrawable background;

  public ChrysopoeiaCategory(IGuiHelper helper) {
    this.background = helper.createDrawable(new ResourceLocation(Roots.MODID, "textures/gui/jei/chrysopoeia.png"), 0, 0, 86, 22);
  }

  @Override
  public String getUid() {
    return JEIRootsPlugin.CHRYSOPOEIA;
  }

  @Override
  public String getTitle() {
    return I18n.format("container." + JEIRootsPlugin.CHRYSOPOEIA + ".name");
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
  public void setRecipe(IRecipeLayout recipeLayout, ChrysopoeiaWrapper recipeWrapper, IIngredients ingredients) {
    IGuiItemStackGroup group = recipeLayout.getItemStacks();
    group.init(0, true, 0, 2);
    group.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
    group.init(1, false, 68, 2);
    group.set(1, recipeWrapper.recipe.getOutput());
  }
}
