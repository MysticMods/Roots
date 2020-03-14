package epicsquid.roots.recipe;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.ListUtil;
import epicsquid.roots.tileentity.TileEntityFeyCrafter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FeyCraftingRecipe implements IRootsRecipe<TileEntityFeyCrafter> {
  private List<Ingredient> ingredients = new ArrayList<>();
  private ItemStack result;
  private String name;
  private int xp;

  public FeyCraftingRecipe(ItemStack result, int xp) {
    this.result = result;
    this.xp = xp;
  }

  public FeyCraftingRecipe(ItemStack result) {
    this(result, 0);
  }

  public FeyCraftingRecipe setName(String name) {
    this.name = name;
    return this;
  }

  public String getName() {
    return name;
  }

  public FeyCraftingRecipe addIngredient(Ingredient stack) {
    this.ingredients.add(stack);
    return this;
  }

  public FeyCraftingRecipe addIngredient(ItemStack stack) {
    this.ingredients.add(Ingredient.fromStacks(stack));
    return this;
  }

  public FeyCraftingRecipe addIngredients(Object... stacks) {
    for (Object stack : stacks) {
      if (stack instanceof Ingredient) {
        ingredients.add((Ingredient) stack);
      } else if (stack instanceof ItemStack) {
        ingredients.add(Ingredient.fromStacks((ItemStack) stack));
      }
    }
    return this;
  }

  public ItemStack getResult() {
    return result;
  }

  public int getXP() {
    return xp;
  }

  @Override
  public List<ItemStack> getRecipe() {
    return ingredients.stream().map(ingredient -> ingredient.getMatchingStacks()[0]).collect(Collectors.toList());
  }

  @Override
  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  /**
   * This is only ever called when the output result has a maxStackSize of 1.
   *
   * @param output
   * @param inputs
   */
  public void postCraft(ItemStack output, List<ItemStack> inputs, EntityPlayer player) {
  }
}
