package epicsquid.roots.integration.jei.interact;

import epicsquid.roots.init.ModBlocks;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PyreLightWrapper implements IRecipeWrapper {

  public final PyreLightRecipe recipe;

  public PyreLightWrapper(PyreLightRecipe recipe) {
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
    if (recipe.getUseTime() != 0) {
      FontRenderer fr = minecraft.fontRenderer;
      fr.drawStringWithShadow(I18n.format("jei.roots.pyre_light", recipe.getUseTime()), 10, 20, 16777215);
    }
  }

  public static class PyreLightRecipe {
    public static ItemStack pyre = ItemStack.EMPTY;
    public List<List<ItemStack>> lighter = null;
    public final int use_time;

    public PyreLightRecipe (ItemStack lighter) {
      this.lighter = Collections.singletonList(Collections.singletonList(lighter));
      if (pyre.isEmpty()) {
        pyre = new ItemStack(ModBlocks.pyre);
      }
      use_time = lighter.getMaxItemUseDuration() == 0 ? 0 : lighter.getMaxItemUseDuration() / 20;
    }

    public List<List<ItemStack>> getInput () {
      return lighter;
    }

    public ItemStack getOutput () {
      return pyre;
    }

    public int getUseTime () {
      return use_time;
    }
  }
}
