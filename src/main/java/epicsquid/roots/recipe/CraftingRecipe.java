package epicsquid.roots.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import epicsquid.mysticallib.util.ListUtil;
import net.minecraft.item.ItemStack;

public class CraftingRecipe {
  private List<ItemStack> ingredients = new ArrayList<>();
  private ItemStack result;

  public CraftingRecipe(ItemStack result) {
    this.result = result;
  }

  public CraftingRecipe addIngredient(ItemStack stack) {
    this.ingredients.add(stack);
    return this;
  }

  public CraftingRecipe addIngredients(ItemStack... stack) {
    this.ingredients.addAll(Arrays.asList(stack));
    return this;
  }

  public boolean matches(List<ItemStack> ingredients) {
    return ListUtil.stackListsMatch(ingredients, this.ingredients);
  }

  public ItemStack getResult() {
    return result;
  }

  public List<ItemStack> getIngredients() {
    return ingredients;
  }
}
