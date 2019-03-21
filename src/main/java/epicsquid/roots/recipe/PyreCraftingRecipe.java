package epicsquid.roots.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import epicsquid.mysticallib.util.ListUtil;
import epicsquid.roots.recipe.conditions.ConditionItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class PyreCraftingRecipe {
  private List<Ingredient> ingredients = new ArrayList<>();
  private ItemStack result;

  public PyreCraftingRecipe(ItemStack result) {
    this.result = result;
  }

  public PyreCraftingRecipe addIngredient(Ingredient stack) {
    this.ingredients.add(stack);
    return this;
  }

  public PyreCraftingRecipe addIngredient(ItemStack stack) {
    this.ingredients.add(Ingredient.fromStacks(stack));
    return this;
  }

  public PyreCraftingRecipe addIngredients(Object... stacks) {
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

  public ItemStack getResult() {
    return result;
  }

  public List<ItemStack> getRecipe(){
    return ingredients.stream().map(ingredient -> ingredient.getMatchingStacks()[0]).collect(Collectors.toList());
  }

  public List<Ingredient> getIngredients(){
    return ingredients;
  }
}
