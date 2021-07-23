package epicsquid.roots.integration.patchouli;

import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.MortarRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

import java.util.ArrayList;
import java.util.List;

public class MortarRecipeProcessor implements IComponentProcessor {

  private List<Ingredient> ingredients = new ArrayList<>();
  private ItemStack output = ItemStack.EMPTY;

  @Override
  public void setup(IVariableProvider<String> iVariableProvider) {
    MortarRecipe recipe = ModRecipes.getMortarRecipe(new ResourceLocation(iVariableProvider.get("recipe")));
    output = recipe.getResult();
    ingredients = recipe.getIngredients();
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
