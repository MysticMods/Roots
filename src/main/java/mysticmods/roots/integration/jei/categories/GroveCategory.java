package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.Unlock;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.recipe.grove.GroveRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class GroveCategory extends RootsRecipeBaseCategory<GroveRecipe> {
  public GroveCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.GROVE_RECIPE_TYPE, helper, 121, 75, RootsAPI.rl("textures/gui/jei/fey_crafting.png"), () -> new ItemStack(ModBlocks.GROVE_CRAFTER.get()), Component.translatable("roots.jei.grove_crafting"));
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, GroveRecipe recipe, IFocusGroup iFocusGroup) {
    HolderLookup.Provider provider = Minecraft.getInstance().getConnection().registryAccess();

    for (int i = 0; i < recipe.getIngredients().size(); i++) {
      builder.addSlot(RecipeIngredientRole.INPUT, i * 18, 0).addIngredients(recipe.getIngredients().get(i));
    }

    ItemStack result = recipe.getResultItem(provider);
    if (result != null && !result.isEmpty()) {
      builder.addSlot(RecipeIngredientRole.OUTPUT, 99, 23).addItemStack(recipe.getResultItem(provider));
    }

    for (int i = 0; i < recipe.getChanceOutputs().size(); i++) {
      builder.addSlot(RecipeIngredientRole.OUTPUT, i * 18, 30)
          .addItemStack(recipe.getChanceOutputs().get(i).getOutput());
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
