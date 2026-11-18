package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.recipe.grove.GroveRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class GroveCategory extends RootsDefaultCategory<GroveRecipe> {
  public GroveCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.GROVE_RECIPE_TYPE, helper, () -> new ItemStack(ModBlocks.GROVE_CRAFTER.get()), Component.translatable("roots.jei.grove_crafting"));
  }
/*  @Override
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

    int count = recipe.getIngredients().size();
    int count2 = recipe.getPlayerRankConditions().size();

    for (int j = 0; j < count2; j++) {
      int i = j + count;
      if (i % 4 == 0 && i != 0) {
        row++;
        column = 0;
      }
      builder.addSlot(RecipeIngredientRole.INPUT, 2 + column * 17, 2 + row * 17)
          .addIngredient(RootsJEIPlugin.GROVE_RANK_TYPE, (GroveRankReputation) recipe.getPlayerRankConditions().get(j))
          .setCustomRenderer(RootsJEIPlugin.GROVE_RANK_TYPE, RootsJEIPlugin.GROVE_RANK_RENDERER);
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
          .addItemStack(outputs.get(i).output()).setSlotName(String.valueOf(i))
          .addRichTooltipCallback(this.richestTooltip(recipe));
      column++;
    }
  }*/

  @Override
  protected void addDevice(IRecipeLayoutBuilder builder) {
    builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 80, 26)
        .addIngredients(Ingredient.of(ModBlocks.GROVE_CRAFTER.get()));
  }
}
