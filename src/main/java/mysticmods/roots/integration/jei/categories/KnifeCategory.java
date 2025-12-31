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
import mysticmods.roots.init.ModItems;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.integration.jei.widget.DurabilityWidget;
import mysticmods.roots.integration.jei.widget.LevelConditionWidget;
import mysticmods.roots.integration.jei.widget.PlayerConditionWidget;
import mysticmods.roots.recipe.knife.KnifeOffHandRecipe;
import mysticmods.roots.recipe.knife.KnifeRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

// TODO: Durability cost
public class KnifeCategory extends RootsRecipeBaseCategory<KnifeRecipe> {
  private static ItemStack knife;

  public KnifeCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.KNIFE_RECIPE_TYPE, helper, 166, 124, RootsAPI.rl("textures/gui/jei/bark_carving.png"), () -> new ItemStack(ModItems.SILVER_KNIFE.get()), Component.translatable("roots.jei.knife_crafting"));
  }

  public static void generateDynamicIngredients() {
    if (knife == null) {
      knife = new ItemStack(ModItems.NETHERITE_KNIFE);
    }
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, KnifeRecipe recipe, IFocusGroup iFocusGroup) {
    super.setRecipe(builder, recipe, iFocusGroup);

    if (recipe instanceof KnifeOffHandRecipe offHandRecipe) {
      builder.addSlot(RecipeIngredientRole.INPUT, 39, 11)
          .addIngredients(Ingredient.of(offHandRecipe.getOffHandTag()));
    }

    WorldRecipeUtil.setWorldRecipe(builder, recipe, iFocusGroup, 7, 34, 73, 34);

    List<ChanceOutput> outputs = recipe.getCachedOutputs();

    int row = 0;
    int column = 0;

    for (int i = 0; i < outputs.size(); i++) {
      if (i % 4 == 0 && i != 0) {
        row++;
        column = 0;
      }
      builder.addSlot(RecipeIngredientRole.OUTPUT, 97 + column * 17, 2 + row * 17)
          .addItemStack(outputs.get(i).output()).setSlotName(String.valueOf(i))
          .addRichTooltipCallback(this.richestTooltip(recipe));
      column++;
    }
  }

  @Override
  public void createRecipeExtras(IRecipeExtrasBuilder builder, KnifeRecipe recipe, IFocusGroup focuses) {
    super.createRecipeExtras(builder, recipe, focuses);

    if (recipe.getDurabilityCost() != -1) {
      Component durability = Component.translatable("roots.jei.text.durability", recipe.getDurabilityCost());
      builder.addWidget(new DurabilityWidget(recipe.getDurabilityCost(), 44, 55, durability));
    }

    int column = 0;
    for (ILevelCondition condition : recipe.getLevelConditions()) {
      CanonicalRepresentation rep = condition.getRepresentation();
      int count = rep.getStates().size();
      int offset = 71;
      if (count == 4) {
        offset = 76;
      }
      if (count == 3) {
        offset = 79;
      }
      builder.addWidget(new LevelConditionWidget(column * 18, offset, 18, 40, rep.getStates(), condition.getNameComponent(), condition.getDescriptionComponent()));
      column++;
    }

    int row = 0;
    for (IPlayerCondition condition : recipe.getPlayerConditions()) {
      builder.addWidget(PlayerConditionWidget.create(getWidth(), 78 + row * 18, condition));
      row++;
    }
  }
}
