package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.condition.CanonicalRepresentation;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.integration.jei.categories.widget.ConditionWidget;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PyreCategory extends RootsRecipeBaseCategory<PyreRecipe> {
  public PyreCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.PYRE_RECIPE_TYPE, helper, 167, 124, RootsAPI.rl("textures/gui/jei/ritual_crafting.png"), () -> new ItemStack(ModBlocks.PYRE.value()), Component.translatable("roots.jei.pyre"));
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, PyreRecipe recipe, IFocusGroup iFocusGroup) {
    super.setRecipe(builder, recipe, iFocusGroup);

    builder.addSlot(RecipeIngredientRole.INPUT, 27, 6).addIngredients(recipe.getIngredients().get(0));
    builder.addSlot(RecipeIngredientRole.INPUT, 1, 27).addIngredients(recipe.getIngredients().get(1));
    builder.addSlot(RecipeIngredientRole.INPUT, 53, 27).addIngredients(recipe.getIngredients().get(2));
    builder.addSlot(RecipeIngredientRole.INPUT, 8, 60).addIngredients(recipe.getIngredients().get(3));
    builder.addSlot(RecipeIngredientRole.INPUT, 48, 60).addIngredients(recipe.getIngredients().get(4));

    List<ChanceOutput> outputs = recipe.getCachedOutputs();

    int row = 0;
    int column = 0;

    for (int i = 0; i < outputs.size(); i++) {
      if (i % 4 == 0 && i != 0) {
        row++;
        column = 0;
      }
      builder.addSlot(RecipeIngredientRole.OUTPUT, 97 + column * 17, 2 + row * 17)
          .addItemStack(outputs.get(i).getOutput()).setSlotName(String.valueOf(i)).addRichTooltipCallback(this.richestTooltip(recipe));
      column++;
    }
  }

  @Override
  public void createRecipeExtras(IRecipeExtrasBuilder builder, PyreRecipe recipe, IFocusGroup focuses) {
    super.createRecipeExtras(builder, recipe, focuses);

    int column = 0;
    for (LevelCondition condition : recipe.getLevelConditions()) {
      CanonicalRepresentation rep = condition.getRepresentation();
      int count = rep.getStates().size();
      int offset = 81;
      if (count == 4) {
        offset = 86;
      }
      if (count == 3) {
        offset = 89;
      }
      builder.addWidget(new ConditionWidget(column * 18, offset, 18, 40, rep.getStates(), condition.getName()));
      column++;
    }
  }
}