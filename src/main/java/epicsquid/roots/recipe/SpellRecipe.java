package epicsquid.roots.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import epicsquid.mysticallib.util.ListUtil;
import net.minecraft.item.ItemStack;

public class SpellRecipe {
  public List<ItemStack> ingredients = new ArrayList<>();
  public String result;

  public SpellRecipe(String result) {
    this.result = result;
  }

  public SpellRecipe addIngredient(ItemStack stack) {
    this.ingredients.add(stack);
    return this;
  }

  public SpellRecipe addIngredients(ItemStack... stack) {
    this.ingredients.addAll(Arrays.asList(stack));
    return this;
  }

  public boolean matches(List<ItemStack> ingredients) {
    return ListUtil.stackListsMatch(ingredients, this.ingredients);
  }
}