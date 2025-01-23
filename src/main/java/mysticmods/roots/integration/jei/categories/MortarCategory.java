package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.Unlock;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class MortarCategory extends RootsRecipeBaseCategory<MortarRecipe> {
  public MortarCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.MORTAR_RECIPE_TYPE, helper, 90, 53, RootsAPI.rl("textures/gui/jei/mortar_and_pestle.png"), () -> new ItemStack(ModBlocks.MORTAR.get()), Component.translatable("roots.jei.mortar_crafting"));
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, MortarRecipe recipe, IFocusGroup iFocusGroup) {
    HolderLookup.Provider provider = Minecraft.getInstance().getConnection().registryAccess();
    for (int i = 0; i < recipe.getIngredients().size(); i++) {
      builder.addSlot(RecipeIngredientRole.INPUT, 1 + i * 18, 3).addIngredients(recipe.getIngredients().get(i));
    }

    boolean hasOutput = false;

    // TODO: If it's empty there's a grant
    if (recipe.getResultItem(provider) != null && !recipe.getResultItem(provider).isEmpty()) {
      hasOutput = true;
      builder.addSlot(RecipeIngredientRole.OUTPUT, 73, 28).addItemStack(recipe.getResultItem(provider));
    } else {
      if (recipe.getUnlocks().size() == 1) {
        Unlock<?> unlock = recipe.getUnlocks().getFirst();
        builder.addSlot(RecipeIngredientRole.OUTPUT, 73, 28).addItemStack(unlock.getIcon());
      }
    }

    for (int i = 0; i < recipe.getChanceOutputs().size(); i++) {
      builder.addSlot(RecipeIngredientRole.OUTPUT, i * 18, 30)
          .addItemStack(recipe.getChanceOutputs().get(i).getOutput());
    }

    if (hasOutput) {
      for (int i = 0; i < recipe.getUnlocks().size(); i++) {
        Unlock<?> unlock = recipe.getUnlocks().get(i);
        if (unlock.getIcon().isEmpty()) {
          continue;
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, i * 18, 50).addItemStack(unlock.getIcon());
      }
    }
  }
}