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
import mysticmods.roots.recipe.grove.GroveRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class GroveCategory extends RootsRecipeBaseCategory<GroveRecipe> {
  public GroveCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.GROVE_RECIPE_TYPE, helper, 121, 75, RootsAPI.rl("textures/gui/jei/fey_crafting.png"), ModBlocks.GROVE_CRAFTER::asStack, Component.translatable("roots.jei.grove_crafting"));
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, GroveRecipe recipe, IFocusGroup iFocusGroup) {
    for (int i = 0; i < recipe.getIngredients().size(); i++) {
      builder.addSlot(RecipeIngredientRole.INPUT, i * 18, 0).addIngredients(recipe.getIngredients().get(i));
    }

    ItemStack result = recipe.getBaseResultItem();
    if (result != null && !result.isEmpty()) {
      builder.addSlot(RecipeIngredientRole.OUTPUT, 99, 23).addItemStack(recipe.getBaseResultItem());
    }

    for (int i = 0; i < recipe.getChanceOutputs().size(); i++) {
      builder.addSlot(RecipeIngredientRole.OUTPUT, i * 18, 30).addItemStack(recipe.getChanceOutputs().get(i).getOutput());
    }

    for (int i = 0; i < recipe.getGrants().size(); i++) {
      Grant grant = recipe.getGrants().get(i);
      builder.addSlot(RecipeIngredientRole.OUTPUT, i * 18, 50).addItemStack(ItemCache.getGrantStack(grant));
    }
  }
}
