package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public abstract class MortarCategory extends RootsDefaultCategory<MortarRecipe> {
  public MortarCategory(RecipeType<MortarRecipe> type, Component title, IGuiHelper helper) {
    super(type, helper, () -> new ItemStack(ModBlocks.MORTAR.get()), title);
  }

/*  @Override
  public void createRecipeExtras(IRecipeExtrasBuilder builder, MortarRecipe recipe, IFocusGroup focuses) {
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
    for (IPlayerCondition condition : recipe.getPlayerConditionsWithoutRanks()) {
      builder.addWidget(PlayerConditionWidget.create(getWidth(), 78 + row * 18, condition));
      row++;
    }
  }*/

  @Override
  protected void addDevice(IRecipeLayoutBuilder builder) {
    builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 80, 26)
        .addIngredients(Ingredient.of(ModBlocks.MORTAR.get()));
  }

  public static class Regular extends MortarCategory {
    public Regular(IGuiHelper helper) {
      super(RootsJEIPlugin.MORTAR_RECIPE_TYPE, Component.translatable("roots.jei.mortar_crafting"), helper);
    }
  }

  public static class Spell extends MortarCategory {
    public Spell(IGuiHelper helper) {
      super(RootsJEIPlugin.MORTAR_SPELL_RECIPE_TYPE, Component.translatable("roots.jei.mortar_spell_crafting"), helper);
    }
  }
}