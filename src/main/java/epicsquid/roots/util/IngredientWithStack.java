package epicsquid.roots.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class IngredientWithStack {
  public static IngredientWithStack EMPTY = new IngredientWithStack(Ingredient.EMPTY, 0);

  private final Ingredient ingredient;
  private int count;

  public IngredientWithStack(Ingredient ingredient, int count) {
    this.ingredient = ingredient;
    this.count = count;
  }

  public IngredientWithStack(ItemStack stack) {
    this.ingredient = Ingredient.fromStacks(stack);
    this.count = stack.getCount();
  }

  public Ingredient getIngredient() {
    return ingredient;
  }

  public int getCount() {
    return count;
  }

  public void increment() {
    count++;
  }

  public IngredientWithStack copy() {
    return new IngredientWithStack(this.ingredient, this.count);
  }
}
