package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.Unlock;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.recipe.grove.GroveRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GroveCategory extends RootsRecipeBaseCategory<GroveRecipe> {
  public GroveCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.GROVE_RECIPE_TYPE, helper, 186, 73, RootsAPI.rl("textures/gui/jei/fey_crafting.png"), () -> new ItemStack(ModBlocks.GROVE_CRAFTER.get()), Component.translatable("roots.jei.grove_crafting"));
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, GroveRecipe recipe, IFocusGroup iFocusGroup) {
    HolderLookup.Provider provider = Minecraft.getInstance().getConnection().registryAccess();

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


    // TODO: Multi-output recipes
    List<ChanceOutput> outputs = new ArrayList<>();
    ItemStack result = recipe.getResultItem(provider);
    if (result != null && !result.isEmpty()) {
      outputs.add(new ChanceOutput(result, 1));
    }

    for (int i = 0; i < recipe.getUnlocks().size(); i++) {
      Unlock<?> unlock = recipe.getUnlocks().get(i);
      if (unlock.getIcon().isEmpty()) {
        continue;
      }
      outputs.add(new ChanceOutput(unlock.getIcon(), 1));
    }

    outputs.addAll(recipe.getChanceOutputs());

    row = 0;
    column = 0;

    for (int i = 0; i < outputs.size(); i++) {
      if (i % 4 == 0 && i != 0) {
        row++;
        column = 0;
      }
      builder.addSlot(RecipeIngredientRole.OUTPUT, 117 + column * 17, 2 + row * 17)
          .addItemStack(outputs.get(i).getOutput());
      column++;
    }
  }
}
