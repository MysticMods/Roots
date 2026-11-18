package mysticmods.roots.integration.jei.categories;

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

/*  @Override
  public void createRecipeExtras(IRecipeExtrasBuilder builder, PyreRecipe recipe, IFocusGroup focuses) {
    super.createRecipeExtras(builder, recipe, focuses);

    int column = 0;
    for (ILevelCondition condition : recipe.getLevelConditions()) {
      CanonicalRepresentation rep = condition.getRepresentation();
      int count = rep.getStates().size();
      int offset = 81;
      if (count == 4) {
        offset = 86;
      }
      if (count == 3) {
        offset = 89;
      }
      builder.addWidget(new LevelConditionWidget(column * 18, offset, 18, 40, rep.getStates(), condition.getNameComponent(), condition.getDescriptionComponent()));
      column++;
    }
    int row = 0;
    for (IPlayerCondition condition : recipe.getPlayerConditions()) {
      builder.addWidget(PlayerConditionWidget.create(getWidth(), 78 + row * 18, condition));
      row++;
    }
  }*/

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