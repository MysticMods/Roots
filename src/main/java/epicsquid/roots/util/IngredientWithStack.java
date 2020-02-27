package epicsquid.roots.util;

import net.minecraft.item.crafting.Ingredient;

public class IngredientWithStack {
  private final Ingredient ingredient;
  private int count;

  public IngredientWithStack(Ingredient ingredient, int count) {
    this.ingredient = ingredient;
    this.count = count;
  }

  public Ingredient getIngredient() {
    return ingredient;
  }

  public int getCount() {
    return count;
  }

  public void increment () {
    count++;
  }
}
