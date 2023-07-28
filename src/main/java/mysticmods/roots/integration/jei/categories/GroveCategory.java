package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.client.ItemCache;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.recipe.grove.GroveRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class GroveCategory implements IRecipeCategory<GroveRecipe> {
  private static final int WIDTH = 121;
  private static final int HEIGHT = 75;

  private final IDrawable background;
  private final IDrawable icon;
  private final Component title;

  public GroveCategory(IGuiHelper helper) {
    this.background = helper.createDrawable(RootsAPI.rl("textures/gui/jei/fey_crafting.png"), 0, 0, WIDTH, HEIGHT);
    this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ModBlocks.GROVE_CRAFTER.get().asItem().getDefaultInstance());
    this.title = Component.translatable("gui.grove_jei");
  }

  @Override
  public RecipeType<GroveRecipe> getRecipeType() {
    return RootsJEIPlugin.GROVE_RECIPE_TYPE;
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

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, GroveRecipe groveRecipe, IFocusGroup iFocusGroup) {
    for (int i = 0; i < groveRecipe.getIngredients().size(); i++) {
      builder.addSlot(RecipeIngredientRole.INPUT, i * 18, 0).addIngredients(groveRecipe.getIngredients().get(i));
    }
    ItemStack result = groveRecipe.getResultItem();
    if (result != null && !result.isEmpty()) {
      builder.addSlot(RecipeIngredientRole.OUTPUT, 99, 23).addItemStack(groveRecipe.getResultItem());
    }

    for (int i = 0; i < groveRecipe.getChanceOutputs().size(); i++) {
      builder.addSlot(RecipeIngredientRole.OUTPUT, i * 18, 30).addItemStack(groveRecipe.getChanceOutputs().get(i).getOutput());
    }

    for (int i = 0; i < groveRecipe.getGrants().size(); i++) {
      Grant grant = groveRecipe.getGrants().get(i);
      builder.addSlot(RecipeIngredientRole.OUTPUT, i * 18, 50).addItemStack(ItemCache.getGrantStack(grant));
    }
  }
}
