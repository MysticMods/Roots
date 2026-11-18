package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.integration.jei.ingredient.grove.GroveGeneratedPower;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public abstract class RootsDefaultCategory<T extends RootsRecipe<?, ?>> extends RootsRecipeBaseCategory<T> {
  public RootsDefaultCategory(RecipeType<T> recipeType, IGuiHelper helper, Supplier<ItemStack> icon, Component title) {
    this(recipeType, helper, 0, 0, null, icon, title);
  }

  public RootsDefaultCategory(RecipeType<T> recipeType, IGuiHelper helper, int width, int height, @Nullable ResourceLocation background, Supplier<ItemStack> icon, Component title) {
    super(recipeType, helper, width == 0 ? 178 : width, height == 0 ? 68 : height, background == null ? RootsAPI.rl("textures/gui/jei/default.png") : background, icon, title);
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup iFocusGroup) {
    super.setRecipe(builder, recipe, iFocusGroup);

    int ingredientCount = addIngredients(builder, recipe, iFocusGroup);
    addConditions(builder, recipe, iFocusGroup, ingredientCount);
    addOutputs(builder, recipe, iFocusGroup);
    addDevice(builder);
  }

  private void addOutputs(IRecipeLayoutBuilder builder, T recipe, IFocusGroup iFocusGroup) {
    int row = 0;
    int column = 0;

    List<ChanceOutput> outputs = recipe.getCachedOutputs();

    for (int i = 0; i < outputs.size(); i++) {
      if (i % getOutputColumnCount() == 0 && i != 0) {
        row++;
        column = 0;
      }
      builder.addSlot(RecipeIngredientRole.OUTPUT, getOutputXPadding() + column * getOutputWidth(), getOutputYPadding() + row * getOutputHeight())
          .addItemStack(outputs.get(i).output()).setSlotName(String.valueOf(i))
          .addRichTooltipCallback(this.richestTooltip(recipe));
      column++;
    }
  }

  private void addConditions(IRecipeLayoutBuilder builder, T recipe, IFocusGroup iFocusGroup, int count) {
    int row = 0;
    int column = 0;

    // TODO: Tweak this for the rest of the requirements
    for (int j = 0; j < recipe.getPowerRequirements().size(); j++) {
      int i = count + j;
      if (i % getIngredientColumnCount() == 0 && i != 0) {
        row++;
        column = 0;
      }
      builder.addSlot(RecipeIngredientRole.INPUT, getIngredientXPadding() + column * getIngredientWidth(), getIngredientYPadding() + row * getIngredientHeight())
          .addIngredients(RootsJEIPlugin.GROVE_POWER_TYPE, GroveGeneratedPower.fromRequest(recipe.getPowerRequirements()
              .get(j)))
          .setCustomRenderer(RootsJEIPlugin.GROVE_POWER_TYPE, RootsJEIPlugin.GROVE_POWER_RENDERER);
      column++;
    }
  }

  protected int getIngredientWidth () {
    return 16;
  }

  protected int getIngredientXPadding () {
    return 2;
  }

  protected int getIngredientYPadding () {
    return 2;
  }

  protected int getIngredientHeight () {
    return 16;
  }

  protected int getIngredientColumnCount(){
    return 4;
  }

  protected int getOutputWidth () {
    return 16;
  }

  protected int getOutputHeight () {
    return 16;
  }

  protected int getOutputYPadding () {
    return 2;
  }

  protected int getOutputXPadding () {
    return 112;
  }

  protected int getOutputColumnCount () {
    return 4;
  }

  protected int addIngredients(IRecipeLayoutBuilder builder, T recipe, IFocusGroup iFocusGroup) {
    int row = 0;
    int column = 0;

    int ingredientCount = 0;
    for (int i = 0; i < recipe.getIngredients().size(); i++) {
      if (i % getIngredientColumnCount() == 0 && i != 0) {
        row++;
        column = 0;
      }
      builder.addSlot(RecipeIngredientRole.INPUT, getIngredientXPadding() + column * getIngredientWidth(), getIngredientYPadding() + row * getIngredientHeight())
          .addIngredients(recipe.getIngredients().get(i));
      column++;
      ingredientCount++;
    }

    return ingredientCount;
  }

  protected abstract void addDevice(IRecipeLayoutBuilder builder);
}
