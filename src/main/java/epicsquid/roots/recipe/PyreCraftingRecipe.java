package epicsquid.roots.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.ListUtil;
import epicsquid.roots.tileentity.TileEntityBonfire;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.items.IItemHandlerModifiable;

public class PyreCraftingRecipe {
  private List<Ingredient> ingredients = new ArrayList<>();
  private ItemStack result;
  private String name;
  private int xp;
  private int burnTime;

  public PyreCraftingRecipe(ItemStack result, int xp) {
    this.result = result;
    this.xp = xp;
    this.burnTime = 200;
  }

  public PyreCraftingRecipe(ItemStack result) {
    this(result, 0);
  }

  public PyreCraftingRecipe setName(String name) {
    this.name = name;
    return this;
  }

  public String getName() {
    return name;
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

  public int getBurnTime() {
    return burnTime;
  }

  public PyreCraftingRecipe setBurnTime(int burnTime) {
    this.burnTime = burnTime;
    return this;
  }

  public int getXP() {
    return xp;
  }

  public List<ItemStack> getRecipe() {
    return ingredients.stream().map(ingredient -> ingredient.getMatchingStacks()[0]).collect(Collectors.toList());
  }

  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  public void postCraft(ItemStack output, IItemHandlerModifiable handler, TileEntityBonfire bonfire) {
  }
}
