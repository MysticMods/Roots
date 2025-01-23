package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.Unlock;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class PyreCategory extends RootsRecipeBaseCategory<PyreRecipe> {
  public PyreCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.PYRE_RECIPE_TYPE, helper, 121, 76, RootsAPI.rl("textures/gui/jei/ritual_crafting.png"), () -> new ItemStack(ModBlocks.PYRE.value()), Component.translatable("roots.jei.pyre"));
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, PyreRecipe recipe, IFocusGroup iFocusGroup) {
    builder.addSlot(RecipeIngredientRole.INPUT, 27, 3).addIngredients(recipe.getIngredients().get(0));
    builder.addSlot(RecipeIngredientRole.INPUT, 1, 24).addIngredients(recipe.getIngredients().get(1));
    builder.addSlot(RecipeIngredientRole.INPUT, 53, 24).addIngredients(recipe.getIngredients().get(2));
    builder.addSlot(RecipeIngredientRole.INPUT, 8, 57).addIngredients(recipe.getIngredients().get(3));
    builder.addSlot(RecipeIngredientRole.INPUT, 48, 57).addIngredients(recipe.getIngredients().get(4));

    if (recipe.getRitual() != null) {
      builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 24).addItemStack(recipe.getRitual().getIcon());
    } else {
      builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 24).addItemStack(recipe.getResultItem(Minecraft.getInstance().getConnection().registryAccess()));
    }

    for (int i = 0; i < recipe.getChanceOutputs().size(); i++) {
      builder.addSlot(RecipeIngredientRole.OUTPUT, i * 18, 30).addItemStack(recipe.getChanceOutputs().get(i).getOutput());
    }

    for (int i = 0; i < recipe.getUnlocks().size(); i++) {
      Unlock<?> unlock = recipe.getUnlocks().get(i);
      if (unlock.getIcon().isEmpty()) {
        continue;
      }
      builder.addSlot(RecipeIngredientRole.OUTPUT, i * 18, 50).addItemStack(unlock.getIcon());
    }
  }
}