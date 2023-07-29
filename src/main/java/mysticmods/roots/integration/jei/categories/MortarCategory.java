package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.client.ItemCache;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import net.minecraft.network.chat.Component;

public class MortarCategory extends RootsRecipeBaseCategory<MortarRecipe> {
  public MortarCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.MORTAR_RECIPE_TYPE, helper, 90, 53, RootsAPI.rl("textures/gui/jei/mortar_and_pestle.png"), ModBlocks.MORTAR::asStack, Component.translatable("roots.jei.mortar_crafting"));
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, MortarRecipe recipe, IFocusGroup iFocusGroup) {
    for (int i = 0; i < recipe.getIngredients().size(); i++) {
      builder.addSlot(RecipeIngredientRole.INPUT, 1 + i * 18, 3).addIngredients(recipe.getIngredients().get(i));
    }

    boolean hasOutput = false;

    // TODO: If it's empty there's a grant
    if (recipe.getResultItem() != null && !recipe.getResultItem().isEmpty()) {
      hasOutput = true;
      builder.addSlot(RecipeIngredientRole.OUTPUT, 73, 28).addItemStack(recipe.getResultItem());
    } else {
      if (recipe.getGrants().size() == 1) {
        Grant grant = recipe.getGrants().get(0);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 73, 28).addItemStack(ItemCache.getGrantStack(grant));
      }
    }

    for (int i = 0; i < recipe.getChanceOutputs().size(); i++) {
      builder.addSlot(RecipeIngredientRole.OUTPUT, i * 18, 30).addItemStack(recipe.getChanceOutputs().get(i).getOutput());
    }

    if (hasOutput) {
      for (int i = 0; i < recipe.getGrants().size(); i++) {
        Grant grant = recipe.getGrants().get(i);
        builder.addSlot(RecipeIngredientRole.OUTPUT, i * 18, 50).addItemStack(ItemCache.getGrantStack(grant));
      }
    }
  }
}
