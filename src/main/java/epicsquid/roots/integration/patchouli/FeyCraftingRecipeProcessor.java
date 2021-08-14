package epicsquid.roots.integration.patchouli;

import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.FeyCraftingRecipe;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

public class FeyCraftingRecipeProcessor implements IComponentProcessor {

  private FeyCraftingRecipe groveCraftingRecipe = null;
  private String title = null;

  @Override
  public void setup(IVariableProvider<String> iVariableProvider) {
    String recipeName = iVariableProvider.get("recipe");
    groveCraftingRecipe = ModRecipes.getFeyCraftingRecipe(recipeName);
    if (iVariableProvider.has("title")) {
      title = iVariableProvider.get("title");
    } else {
      title = I18n.format("roots.patchouli.fey_crafting");
    }
  }

  @Override
  public String process(String s) {

    if ((s.startsWith("item") || s.equalsIgnoreCase("result")) && groveCraftingRecipe == null) {
      return ItemStackUtil.serializeStack(ItemStack.EMPTY);
    }
    if (s.startsWith("item")) {
      int index = Integer.parseInt(s.substring(4)) - 1;

      if (index >= groveCraftingRecipe.getIngredients().size()) {
        return ItemStackUtil.serializeStack(ItemStack.EMPTY);
      }

      Ingredient ingredient = groveCraftingRecipe.getIngredients().get(index);

      return ItemStackUtil.serializeIngredient(ingredient);
    }

    if (s.equalsIgnoreCase("result")) {
      return ItemStackUtil.serializeStack(groveCraftingRecipe.getResult());
    }

    if (s.equalsIgnoreCase("title")) {
      return this.title;
    }
    return null;
  }
}
