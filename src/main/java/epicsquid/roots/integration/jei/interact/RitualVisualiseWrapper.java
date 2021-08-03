package epicsquid.roots.integration.jei.interact;

import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RitualVisualiseWrapper implements IRecipeWrapper {

  public final KnifeRecipe recipe;

  public RitualVisualiseWrapper(KnifeRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    ingredients.setInputLists(VanillaTypes.ITEM, recipe.getInput());
    ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    IRecipeWrapper.super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);
    FontRenderer fr = minecraft.fontRenderer;
    fr.drawStringWithShadow(I18n.format("jei.roots.sneak"), 0, 23, 16777215);
  }

  @Override
  public List<String> getTooltipStrings(int mouseX, int mouseY) {
    if (mouseX >= 1 && mouseX <= 54 && mouseY >= 22 && mouseY <= 35) {
      return Collections.singletonList(I18n.format("jei.roots.sneak_right_click"));
    }
    return Collections.emptyList();
  }

  public static class KnifeRecipe {
    public static ItemStack pyre = ItemStack.EMPTY;
    public static List<List<ItemStack>> knives = null;

    public KnifeRecipe() {
      if (pyre.isEmpty()) {
        pyre = new ItemStack(ModBlocks.pyre);
      }
      if (knives == null) {
        knives = Collections.singletonList(ModItems.knives.stream().map(ItemStack::new).collect(Collectors.toList()));
      }
    }

    public List<List<ItemStack>> getInput() {
      return knives;
    }

    public ItemStack getOutput() {
      return pyre;
    }
  }
}
