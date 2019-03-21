package epicsquid.roots.recipe;

import epicsquid.mysticallib.util.ListUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class SpellRecipe {
  private List<Ingredient> ingredients = new ArrayList<>();
  private String result;

  public SpellRecipe(String result) {
    this.result = result;
  }

  public SpellRecipe addIngredient(ItemStack stack) {
    this.ingredients.add(Ingredient.fromStacks(stack));
    return this;
  }

  public SpellRecipe addIngredients(Object... stacks) {
    for (Object stack : stacks) {
      if (stack instanceof Ingredient) {
        ingredients.add((Ingredient) stack);
      } else if (stack instanceof ItemStack) {
        ingredients.add(Ingredient.fromStacks((ItemStack) stack));
      }
    }
    return this;
  }

  public boolean matches(List<ItemStack> ingredients) {
    return ListUtil.matchesIngredients(ingredients, this.ingredients);
  }

  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  public String getResult() {
    return result;
  }
}