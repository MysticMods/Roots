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
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class MortarCategory extends RootsRecipeBaseCategory<MortarRecipe> {
  public MortarCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.MORTAR_RECIPE_TYPE, helper, 185, 117, RootsAPI.rl("textures/gui/jei/mortar_and_pestle.png"), () -> new ItemStack(ModBlocks.MORTAR.get()), Component.translatable("roots.jei.mortar_crafting"));
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, MortarRecipe recipe, IFocusGroup iFocusGroup) {
    HolderLookup.Provider provider = Minecraft.getInstance().getConnection().registryAccess();
    for (int i = 0; i < recipe.getIngredients().size(); i++) {
      builder.addSlot(RecipeIngredientRole.INPUT, 1 + i * 18, 3).addIngredients(recipe.getIngredients().get(i));
    }

    boolean hasOutput = false;

    List<ChanceOutput> outputs = new ArrayList<>();

    if (recipe.getResultItem(provider) != null && !recipe.getResultItem(provider).isEmpty()) {
      hasOutput = true;
      outputs.add(new ChanceOutput(recipe.getResultItem(provider), 1));
/*      builder.addSlot(RecipeIngredientRole.OUTPUT, 73, 28).addItemStack(recipe.getResultItem(provider));*/
    } else {
      if (recipe.getUnlocks().size() == 1) {
        Unlock<?> unlock = recipe.getUnlocks().getFirst();
        outputs.add(new ChanceOutput(unlock.getIcon(), 1));
/*        builder.addSlot(RecipeIngredientRole.OUTPUT, 73, 28).addItemStack(unlock.getIcon());*/
      }
    }

    outputs.addAll(recipe.getChanceOutputs());

/*    for (int i = 0; i < recipe.getChanceOutputs().size(); i++) {
      builder.addSlot(RecipeIngredientRole.OUTPUT, i * 18, 30)
          .addItemStack(recipe.getChanceOutputs().get(i).getOutput());
    }*/

    if (hasOutput) {
      for (int i = 0; i < recipe.getUnlocks().size(); i++) {
        Unlock<?> unlock = recipe.getUnlocks().get(i);
        if (unlock.getIcon().isEmpty()) {
          continue;
        }
        outputs.add(new ChanceOutput(unlock.getIcon(), 1));
/*        builder.addSlot(RecipeIngredientRole.OUTPUT, i * 18, 50).addItemStack(unlock.getIcon());*/
      }
    }

    int row = 0;
    int column = 0;

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
  public void draw(MortarRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
    super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);

    int row;
    int column = 0;

    for (LevelCondition condition : recipe.getLevelConditions()) {
      row = 0;
      CanonicalRepresentation rep = condition.getRepresentation();
      int count = rep.getStates().size();
      for (BlockState state : rep.getStates()) {
        RenderUtil.renderBlock(guiGraphics, state, 10 + column * 18, 81 + (count - row) * 6, row * 3, 45f, 6f);
        row++;
      }
      column++;
    }
  }
}