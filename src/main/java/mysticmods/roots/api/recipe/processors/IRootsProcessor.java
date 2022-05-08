package mysticmods.roots.api.recipe.processors;

import mysticmods.roots.api.recipe.IRootsCrafting;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface IRootsProcessor<C extends IRootsCrafting<?>> {
  default <T extends C> List<List<ItemStack>> process(ItemStack incomingResult, List<Ingredient> ingredients, List<ItemStack> usedItems, T crafter) {
    if (ingredients.size() != usedItems.size()) {
      throw new IllegalArgumentException("size of `ingredients` doesn't match `usedItems`: " + ingredients.size() + " ingredients vs " + usedItems.size() + " items");
    }

    List<List<ItemStack>> processing = new ArrayList<>();
    processing.add(Collections.singletonList(processOutput(incomingResult, ingredients, usedItems, crafter)));
    for (int i = 0; i < ingredients.size(); i++) {
      processing.add(processIngredient(incomingResult, ingredients.get(i), usedItems.get(i), crafter));
    }

    return processing;
  }

  default <T extends C> ItemStack processOutput(ItemStack result, List<Ingredient> ingredients, List<ItemStack> usedItems, T crafter) {
    return result;
  }

  default <T extends C> List<ItemStack> processIngredient(ItemStack result, Ingredient ingredient, ItemStack usedItem, T crafter) {
    return Collections.singletonList(ItemStack.EMPTY);
  }
}
