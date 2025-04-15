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
import mysticmods.roots.recipe.grove.GroveRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class GroveCategory extends RootsRecipeBaseCategory<GroveRecipe> {
  public GroveCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.GROVE_RECIPE_TYPE, helper, 186, 118, RootsAPI.rl("textures/gui/jei/sylvan_crafting.png"), () -> new ItemStack(ModBlocks.GROVE_CRAFTER.get()), Component.translatable("roots.jei.grove_crafting"));
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, GroveRecipe recipe, IFocusGroup iFocusGroup) {
    super.setRecipe(builder, recipe, iFocusGroup);

    int row = 0;
    int column = 0;

    for (int i = 0; i < recipe.getIngredients().size(); i++) {
      if (i % 4 == 0 && i != 0) {
        row++;
        column = 0;
      }
      builder.addSlot(RecipeIngredientRole.INPUT, 2 + column * 17, 2 + row * 17)
          .addIngredients(recipe.getIngredients().get(i));
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
      builder.addSlot(RecipeIngredientRole.OUTPUT, 117 + column * 17, 2 + row * 17)
          .addItemStack(outputs.get(i).getOutput()).setSlotName(String.valueOf(i))
          .addRichTooltipCallback(this.richestTooltip(recipe));
      column++;
    }
  }

  @Override
  public void createRecipeExtras(IRecipeExtrasBuilder builder, GroveRecipe recipe, IFocusGroup focuses) {
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
