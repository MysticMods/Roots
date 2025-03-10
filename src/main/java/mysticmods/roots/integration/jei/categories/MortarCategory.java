package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.Unlock;
import mysticmods.roots.api.condition.CanonicalRepresentation;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.integration.jei.categories.widget.ConditionWidget;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

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
          .addIngredient(RootsJEIPlugin.CHANCE_OUTPUT, outputs.get(i));
      column++;
    }
  }

  @Override
  public void createRecipeExtras(IRecipeExtrasBuilder builder, MortarRecipe recipe, IFocusGroup focuses) {
    super.createRecipeExtras(builder, recipe, focuses);

    int column = 0;
    for (LevelCondition condition : recipe.getLevelConditions()) {
      CanonicalRepresentation rep = condition.getRepresentation();
      int count = rep.getStates().size();
      int offset = 81;
      if (count == 4) {
        offset = 86;
      }
      if (count == 3) {
        offset = 89;
      }
      builder.addWidget(new ConditionWidget(column * 18, offset, 18, 40, rep.getStates(), condition.getName()));
      column++;
    }
  }
}