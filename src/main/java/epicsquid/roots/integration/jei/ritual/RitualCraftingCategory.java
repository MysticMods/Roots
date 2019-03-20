package epicsquid.roots.integration.jei.ritual;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.integration.jei.JEIRootsPlugin;
import epicsquid.roots.recipe.PyreCraftingRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RitualCraftingCategory implements IRecipeCategory<RitualCraftingWrapper> {

  private final IDrawable background;

  public RitualCraftingCategory(IGuiHelper helper) {
    this.background = helper.createDrawable(new ResourceLocation(Roots.MODID, "textures/gui/jei/ritual_crafting.png"), 0, 0, 121, 76);
  }

  @Override
  public String getUid() {
    return JEIRootsPlugin.RITUAL_CRAFTING;
  }

  @Override
  public String getTitle() {
    return I18n.format("container." + JEIRootsPlugin.RITUAL_CRAFTING + ".name");
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
  public void setRecipe(IRecipeLayout recipeLayout, RitualCraftingWrapper recipeWrapper, IIngredients ingredients) {
    IGuiItemStackGroup group = recipeLayout.getItemStacks();
    PyreCraftingRecipe recipe = recipeWrapper.recipe;
    group.init(0, true, 26, 2);
    group.set(0, recipe.getRecipe().get(0));
    group.init(1, true, 0, 23);
    group.set(1, recipe.getRecipe().get(1));
    group.init(2, true, 52, 23);
    group.set(2, recipe.getRecipe().get(2));
    group.init(3, true, 7, 56);
    group.set(3, recipe.getRecipe().get(3));
    group.init(4, true, 47, 56);
    group.set(4, recipe.getRecipe().get(4));
    group.init(5, false, 99, 23);
    group.set(5, recipe.getResult());
    group.init(6, true, 26, 30);
    group.set(6, new ItemStack(ModBlocks.bonfire));
  }
}
