package epicsquid.roots.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import epicsquid.mysticallib.util.ListUtil;
import net.minecraft.item.ItemStack;

public class PyreCraftingRecipe {
  private List<ItemStack> ingredients = new ArrayList<>();
  private ItemStack result;

  public PyreCraftingRecipe(ItemStack result) {
    this.result = result;
  }

  public PyreCraftingRecipe addIngredient(ItemStack stack) {
    this.ingredients.add(stack);
    return this;
  }

  public PyreCraftingRecipe addIngredients(ItemStack... stack) {
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
