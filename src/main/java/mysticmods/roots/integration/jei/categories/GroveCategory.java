package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.Unlock;
import mysticmods.roots.api.condition.CanonicalRepresentation;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.client.RenderUtil;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.recipe.grove.GroveRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class GroveCategory extends RootsRecipeBaseCategory<GroveRecipe> {
  public GroveCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.GROVE_RECIPE_TYPE, helper, 186, 118, RootsAPI.rl("textures/gui/jei/fey_crafting.png"), () -> new ItemStack(ModBlocks.GROVE_CRAFTER.get()), Component.translatable("roots.jei.grove_crafting"));
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

  @Override
  public void draw(GroveRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
    super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);

    int row;
    int column = 0;

    for (LevelCondition condition : recipe.getLevelConditions()) {
      row = 0;
      CanonicalRepresentation rep = condition.getRepresentation();
      int count = rep.getStates().size();
      int offset = 81;
      if (count == 3) {
        offset = 86;
      }
      for (BlockState state : rep.getStates()) {
        RenderUtil.renderBlock(guiGraphics, state, 10 + column * 18, offset + (count - row) * 5.2f, row * 3, 45f, 6f);
        row++;
      }
      column++;
    }
  }
}
