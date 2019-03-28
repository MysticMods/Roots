package epicsquid.roots.api;

import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.MortarRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

import java.util.ArrayList;
import java.util.List;

public class MortarRecipeProcessor implements IComponentProcessor {

  private List<Ingredient> ingredients = new ArrayList<>();
  private ItemStack output = null;

  @Override
  public void setup(IVariableProvider<String> iVariableProvider) {
    MortarRecipe mortarBase = ModRecipes.getMortarRecipe(iVariableProvider.get("mortar"));
    if (mortarBase != null) {
      ingredients = mortarBase.getIngredients();
      output = mortarBase.getResult();
    }
  }

  @Override
  public String process(String s) {
    if (s.startsWith("item")) {
      int index = Integer.parseInt(s.substring(4)) - 1;
      Ingredient ingredient = ingredients.get(index);
      if (ingredient == null || ingredient == Ingredient.EMPTY) {
        return ItemStackUtil.serializeStack(ItemStack.EMPTY);
      }

      return ItemStackUtil.serializeIngredient(ingredient);
    }
    if (s.equalsIgnoreCase("result")) {
      return ItemStackUtil.serializeStack(output);
    }
    return null;
  }

}
