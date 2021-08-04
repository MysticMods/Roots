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

public class ImposingCategory implements IRecipeCategory<ImposingWrapper> {

  private final IDrawable background;

  public ImposingCategory(IGuiHelper helper) {
    this.background = helper.createDrawable(new ResourceLocation(Roots.MODID, "textures/gui/jei/spell_imposing.png"), 0, 0, 90, 43);
  }

  @Override
  public String getUid() {
    return JEIRootsPlugin.SPELL_IMPOSING;
  }

  @Override
  public String getTitle() {
    return I18n.format("container." + JEIRootsPlugin.SPELL_IMPOSING+ ".name");
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
  public void setRecipe(IRecipeLayout recipeLayout, ImposingWrapper recipeWrapper, IIngredients ingredients) {
    IGuiItemStackGroup group = recipeLayout.getItemStacks();
    group.init(0, true, 1, 13);
    group.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
    group.init(1, true, 36, 2);
    group.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
    group.init(2, false, 36, 23);
    group.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(1));
    group.init(3, false, 71, 13);
    group.set(3, ingredients.getOutputs(VanillaTypes.ITEM).get(2));
  }
}
