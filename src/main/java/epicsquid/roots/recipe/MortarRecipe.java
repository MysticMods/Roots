package epicsquid.roots.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import epicsquid.mysticallib.util.ListUtil;
import net.minecraft.item.ItemStack;

public class MortarRecipe {

  private ItemStack result;

  private List<ItemStack> ingredients = new ArrayList<>();

  private float r1, g1, b1, r2, g2, b2;

  public MortarRecipe(ItemStack result, ItemStack[] ingredients, float red1, float green1, float blue1, float red2, float green2, float blue2) {
    this.result = result;
    this.ingredients.addAll(Arrays.asList(ingredients));

    while (this.ingredients.size() < 5) {
      this.ingredients.add(ItemStack.EMPTY);
    }

    this.r1 = red1;
    this.g1 = green1;
    this.b1 = blue1;
    this.r2 = red2;
    this.g2 = green2;
    this.b2 = blue2;
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

  public float getR1() {
    return r1;
  }

  public float getG1() {
    return g1;
  }

  public float getB1() {
    return b1;
  }

  public float getR2() {
    return r2;
  }

  public float getG2() {
    return g2;
  }

  public float getB2() {
    return b2;
  }
}
