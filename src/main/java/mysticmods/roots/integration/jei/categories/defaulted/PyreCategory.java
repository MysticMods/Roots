package mysticmods.roots.integration.jei.categories.defaulted;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public abstract class PyreCategory extends RootsDefaultCategory<PyreRecipe> {
  public PyreCategory(RecipeType<PyreRecipe> type, Component title, IGuiHelper helper) {
    super(type, helper, () -> new ItemStack(ModBlocks.PYRE.value()), title);
  }

  @Override
  protected void addDevice(IRecipeLayoutBuilder builder) {
    builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 80, 26)
        .addIngredients(Ingredient.of(RootsTags.Items.FUNCTIONAL_PYRES));
  }

  public static class Pyre extends PyreCategory {
    public Pyre(IGuiHelper helper) {
      super(RootsJEIPlugin.PYRE_RECIPE_TYPE, Component.translatable("roots.jei.pyre"), helper);
    }
  }

  public static class Ritual extends PyreCategory {
    public Ritual(IGuiHelper helper) {
      super(RootsJEIPlugin.RITUAL_RECIPE_TYPE, Component.translatable("roots.jei.ritual"), helper);
    }
  }
}