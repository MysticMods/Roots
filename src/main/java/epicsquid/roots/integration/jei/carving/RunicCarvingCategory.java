package epicsquid.roots.integration.jei.carving;

import epicsquid.roots.Roots;
import epicsquid.roots.integration.jei.JEIRootsPlugin;
import epicsquid.roots.recipe.RunicCarvingRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RunicCarvingCategory implements IRecipeCategory<RunicCarvingWrapper> {

  private final IDrawable background;

  public RunicCarvingCategory(IGuiHelper helper) {
    this.background = helper.createDrawable(new ResourceLocation(Roots.MODID, "textures/gui/jei/runic_carving.png"), 0, 0, 78, 39);
  }

  @Override
  public String getUid() {
    return JEIRootsPlugin.RUNIC_CARVING;
  }

  @Override
  public String getTitle() {
    return I18n.format("container." + JEIRootsPlugin.RUNIC_CARVING + ".name");
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
  public void setRecipe(IRecipeLayout recipeLayout, RunicCarvingWrapper recipeWrapper, IIngredients ingredients) {
    IGuiItemStackGroup group = recipeLayout.getItemStacks();
    RunicCarvingRecipe recipe = recipeWrapper.recipe;
    group.init(0, true, 0, 19);
    group.set(0, new ItemStack(recipe.getCarvingBlock().getBlock()));
    group.init(1, false, 60, 19);
    group.set(1, new ItemStack(recipe.getRuneBlock().getBlock()));
    group.init(2, true, 28, 2);
    group.set(2, new ItemStack(recipe.getHerb().getItem()));
  }
}
