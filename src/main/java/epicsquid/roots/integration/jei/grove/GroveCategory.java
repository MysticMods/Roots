package epicsquid.roots.integration.jei.grove;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.integration.jei.JEIRootsPlugin;
import epicsquid.roots.recipe.GroveCraftingRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.List;

public class GroveCategory implements IRecipeCategory<GroveWrapper> {

  private final IDrawable background;

  public GroveCategory(IGuiHelper helper) {
    this.background = helper.createDrawable(new ResourceLocation(Roots.MODID, "textures/gui/jei/grove_crafting.png"), 0, 0, 121, 76);
  }

  @Override
  public String getUid() {
    return JEIRootsPlugin.GROVE_CRAFTING;
  }

  @Override
  public String getTitle() {
    return I18n.format("container." + JEIRootsPlugin.GROVE_CRAFTING + ".name");
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
  public void setRecipe(IRecipeLayout recipeLayout, GroveWrapper recipeWrapper, IIngredients ingredients) {
    IGuiItemStackGroup group = recipeLayout.getItemStacks();
    if (recipeWrapper.recipe != null) {
      List<List<ItemStack>> data = ingredients.getInputs(VanillaTypes.ITEM);
      group.init(0, true, 26, 2);
      group.set(0, data.get(0));
      group.init(1, true, 0, 23);
      group.set(1, data.get(1));
      group.init(2, true, 52, 23);
      group.set(2, data.get(2));
      group.init(3, true, 7, 56);
      group.set(3, data.get(3));
      group.init(4, true, 47, 56);
      group.set(4, data.get(4));
      group.init(5, false, 99, 23);
      group.set(5, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
    }
  }
}
