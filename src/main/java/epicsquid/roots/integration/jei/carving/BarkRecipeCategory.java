package epicsquid.roots.integration.jei.carving;

import epicsquid.roots.Roots;
import epicsquid.roots.integration.jei.JEIRootsPlugin;
import epicsquid.roots.recipe.RunicCarvingRecipe;
import epicsquid.roots.recipe.RunicShearRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class BarkRecipeCategory implements IRecipeCategory<BarkRecipeWrapper> {

  private final IDrawable background;

  public BarkRecipeCategory(IGuiHelper helper) {
    this.background = helper.createDrawable(new ResourceLocation(Roots.MODID, "textures/gui/jei/bark_carving.png"), 0, 0, 78, 22);
  }

  @Override
  public String getUid() {
    return JEIRootsPlugin.BARK_CARVING;
  }

  @Override
  public String getTitle() {
    return I18n.format("container." + JEIRootsPlugin.BARK_CARVING + ".name");
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
  public void setRecipe(IRecipeLayout recipeLayout, BarkRecipeWrapper recipeWrapper, IIngredients ingredients) {
    IGuiItemStackGroup group = recipeLayout.getItemStacks();
    group.init(0, true, 0, 2);
    group.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
    group.init(1, false, 60, 2);
    group.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
  }
}
