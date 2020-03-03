package epicsquid.roots.recipe;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.ListUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;

import java.util.List;
import java.util.stream.Collectors;

public interface IRootsRecipe<T extends TileEntity> {
  default boolean matches(List<ItemStack> ingredients) {
    return ListUtil.matchesIngredients(ingredients, getIngredients());
  }

  default List<ItemStack> getRecipe() {
    return getIngredients().stream().map(ingredient -> ingredient.getMatchingStacks()[0]).collect(Collectors.toList());
  }

  List<Ingredient> getIngredients();

  default List<ItemStack> transformIngredients(List<ItemStack> items, T tile) {
    return ItemUtil.transformContainers(items);
  }
}
