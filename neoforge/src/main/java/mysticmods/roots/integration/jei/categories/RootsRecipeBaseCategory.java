package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.api.recipe.RootsRecipeBase;
import mysticmods.roots.client.ItemCache;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public abstract class RootsRecipeBaseCategory<T extends RootsRecipeBase> implements IRecipeCategory<T> {
  private final int width;
  private final int height;

  private final IDrawable background;
  private final IDrawable icon;
  private final Component title;
  private final RecipeType<T> recipeType;

  public RootsRecipeBaseCategory(RecipeType<T> recipeType, IGuiHelper helper, int width, int height, ResourceLocation background, Supplier<ItemStack> icon, Component title) {
    this.recipeType = recipeType;
    this.width = width;
    this.height = height;
    this.background = helper.createDrawable(background, 0, 0, width, height);
    this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, icon.get());
    this.title = title;
  }

  @Override
  public RecipeType<T> getRecipeType() {
    return recipeType;
  }

  @Override
  public Component getTitle() {
    return title;
  }

  @Override
  public IDrawable getBackground() {
    return background;
  }

  @Override
  public IDrawable getIcon() {
    return icon;
  }
}
