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
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;
import java.util.function.Supplier;

public abstract class RootsRecipeBaseCategory<T> implements IRecipeCategory<T> {
  private final IDrawable background;
  private final IDrawable icon;
  private final Component title;
  private final RecipeType<T> recipeType;

  public RootsRecipeBaseCategory(RecipeType<T> recipeType, IGuiHelper helper, int width, int height, ResourceLocation background, Supplier<ItemStack> icon, Component title) {
    this.recipeType = recipeType;
    this.background = helper.createDrawable(background, 0, 0, width, height);
    this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, icon.get());
    this.title = title;
  }

  public List<ChanceOutput> getChanceOutputs(T recipe) {
    if (recipe instanceof RootsRecipe<?, ?> rootsRecipe) {
      return rootsRecipe.getCachedOutputs();
    }
    throw new NotImplementedException();
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
    if (recipe instanceof RootsRecipe<?, ?> rootsRecipe) {
      rootsRecipe.buildCachedOutputs(Minecraft.getInstance().level.registryAccess());
    }
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
          if (num >= 0 && num < getChanceOutputs(recipe).size()) {
            ChanceOutput output = getChanceOutputs(recipe).get(num);
            builder.add(Component.translatable("roots.tooltip.chance", String.format("%.2f", output.chance() * 100)));
          }
        } catch (NumberFormatException ignored) {

        }
      });
    };
  }
}
