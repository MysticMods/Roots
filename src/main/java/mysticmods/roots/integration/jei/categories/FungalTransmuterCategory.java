package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.condition.CanonicalRepresentation;
import mysticmods.roots.api.condition.ILevelCondition;
import mysticmods.roots.api.condition.IPlayerCondition;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.integration.jei.categories.widget.GrovePowerWidget;
import mysticmods.roots.integration.jei.categories.widget.LevelConditionWidget;
import mysticmods.roots.integration.jei.categories.widget.PlayerConditionWidget;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.transmutation.TransmutationRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class FungalTransmuterCategory extends RootsRecipeBaseCategory<TransmutationRecipe> {
  public FungalTransmuterCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.TRANSMUTATION_RECIPE_TYPE, helper, 166, 128, RootsAPI.rl("textures/gui/jei/fungal_transmuter.png"), () -> new ItemStack(ModBlocks.FUNGAL_TRANSMUTER.get()), Component.translatable("roots.jei.fungal_transmuter"));
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, TransmutationRecipe recipe, IFocusGroup iFocusGroup) {
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

    builder.addSlot(RecipeIngredientRole.INPUT, 74, 34)
        .addIngredients(Ingredient.of(ModItems.FUNGAL_TRANSMUTER.get()));

    List<ChanceOutput> outputs = recipe.getCachedOutputs();

    row = 0;
    column = 0;

    for (int i = 0; i < outputs.size(); i++) {
      if (i % 4 == 0 && i != 0) {
        row++;
        column = 0;
      }
      builder.addSlot(RecipeIngredientRole.OUTPUT, 105 + column * 17, 2 + row * 17)
          .addItemStack(outputs.get(i).output()).setSlotName(String.valueOf(i))
          .addRichTooltipCallback(this.richestTooltip(recipe));
      column++;
    }
  }

  @Override
  public void createRecipeExtras(IRecipeExtrasBuilder builder, TransmutationRecipe recipe, IFocusGroup focuses) {
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
    builder.addWidget(new GrovePowerWidget(recipe.getPower(), 66, 50, Component.translatable("roots.jei.text.grove_power", recipe.getPower())));
  }
}
