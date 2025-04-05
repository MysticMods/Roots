package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public abstract class RootsRecipeBaseCategory<T extends RootsRecipe<?, ?>> implements IRecipeCategory<T> {
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
  public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
    recipe.buildCachedOutputs(Minecraft.getInstance().level.registryAccess());
  }

  @Override
  public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
    IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
  }

  @Override
  public RecipeType<T> getRecipeType() {
    return recipeType;
  }

  @Override
  public Component getTitle() {
    return title;
  }

  @SuppressWarnings("removal")
  @Override
  public IDrawable getBackground() {
    return background;
  }

  @Override
  public IDrawable getIcon() {
    return icon;
  }

  protected IRecipeSlotRichTooltipCallback richestTooltip(T recipe) {
    return (IRecipeSlotView view, ITooltipBuilder builder) -> {
      view.getSlotName().ifPresent(slot -> {
        try {
          int num = Integer.parseInt(slot);
          if (num >= 0 && num < recipe.getCachedOutputs().size()) {
            ChanceOutput output = recipe.getCachedOutputs().get(num);
            builder.add(Component.translatable("roots.tooltip.chance", String.format("%.2f", output.getChance() * 100)));
          }
        } catch (NumberFormatException ignored) {

        }
      });
    };
  }
}
