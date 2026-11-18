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

    int row = 0;
    int column = 0;

    for (int i = 0; i < recipe.getIngredients().size(); i++) {
      if (i % 4 == 0 && i != 0) {
        row++;
        column = 0;
      }
      builder.addSlot(RecipeIngredientRole.INPUT, 2 + column * 16, 2 + row * 16)
          .addIngredients(recipe.getIngredients().get(i));
      column++;
    }

    int count = recipe.getIngredients().size();

    // TODO: Tweak this for the rest of the requirements
    for (int j = 0; j < recipe.getPowerRequirements().size(); j++) {
      int i = count + j;
      if (i % 4 == 0 && i != 0) {
        row++;
        column = 0;
      }
      builder.addSlot(RecipeIngredientRole.INPUT, 2 + column * 16, 2 + row * 16)
          .addIngredients(RootsJEIPlugin.GROVE_POWER_TYPE, GroveGeneratedPower.fromRequest(recipe.getPowerRequirements()
              .get(j)))
          .setCustomRenderer(RootsJEIPlugin.GROVE_POWER_TYPE, RootsJEIPlugin.GROVE_POWER_RENDERER);
      column++;
    }

    List<ChanceOutput> outputs = recipe.getCachedOutputs();

    row = 0;
    column = 0;

    for (int i = 0; i < outputs.size(); i++) {
      if (i % 4 == 0 && i != 0) {
        row++;
        column = 0;
      }
      builder.addSlot(RecipeIngredientRole.OUTPUT, 112 + column * 16, 2 + row * 16)
          .addItemStack(outputs.get(i).output()).setSlotName(String.valueOf(i))
          .addRichTooltipCallback(this.richestTooltip(recipe));
      column++;
    }

    addDevice(builder);
  }

  protected abstract void addDevice(IRecipeLayoutBuilder builder);
}
