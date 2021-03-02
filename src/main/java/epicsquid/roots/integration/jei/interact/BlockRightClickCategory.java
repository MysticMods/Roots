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

public class BlockRightClickCategory implements IRecipeCategory<BlockRightClickWrapper> {

  private final IDrawable background;

  public BlockRightClickCategory(IGuiHelper helper) {
    this.background = helper.createDrawable(new ResourceLocation(Roots.MODID, "textures/gui/jei/right_click_block.png"), 0, 0, 89, 29);
  }

  @Override
  public String getUid() {
    return JEIRootsPlugin.RIGHT_CLICK_BLOCK;
  }

  @Override
  public String getTitle() {
    return I18n.format("container." + JEIRootsPlugin.RIGHT_CLICK_BLOCK + ".name");
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
  public void setRecipe(IRecipeLayout recipeLayout, BlockRightClickWrapper recipeWrapper, IIngredients ingredients) {
    IGuiItemStackGroup group = recipeLayout.getItemStacks();
    group.init(0, true, 2, 3);
    group.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
    group.init(1, true, 37, 3);
    group.set(1, ingredients.getInputs(VanillaTypes.ITEM).get(1));
    group.init(2, false, 72, 3);
    group.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
  }
}
