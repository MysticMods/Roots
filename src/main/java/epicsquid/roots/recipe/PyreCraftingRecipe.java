package epicsquid.roots.recipe;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.ListUtil;
import epicsquid.roots.Roots;
import epicsquid.roots.tileentity.TileEntityPyre;
import epicsquid.roots.util.types.RegistryItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PyreCraftingRecipe extends RegistryItem implements IRootsRecipe<TileEntityPyre> {
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
    setRegistryName(new ResourceLocation(Roots.MODID, name));
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

  @Override
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

  @Override
  public List<ItemStack> getRecipe() {
    return ingredients.stream().map(ingredient -> ingredient.getMatchingStacks()[0]).collect(Collectors.toList());
  }

  @Override
  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  public void postCraft(ItemStack output, IItemHandlerModifiable handler, TileEntityPyre pyre) {
  }

  @Override
  public List<ItemStack> transformIngredients(List<ItemStack> items, TileEntityPyre pyre) {
    return ItemUtil.transformContainers(items);
  }
}
