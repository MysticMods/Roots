package mysticmods.roots.api.recipe;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import mysticmods.roots.api.recipe.inventory.RecipeInputWrapper;
import mysticmods.roots.api.recipe.inventory.RecipeInventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.PlayerMainInvWrapper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RecipeUtil {
  public static boolean matchesIngredients(Recipe<?> recipe, RecipeInput input) {
    return matchesIngredients(recipe, input, null);
  }

  public static boolean matchesIngredients(Recipe<?> recipe, RecipeInput input, @Nullable Level level) {
    List<ItemStack> inputs = new ArrayList<>();
    for (int i = 0; i < input.size(); i++) {
      ItemStack stack = input.getItem(i);
      if (!stack.isEmpty()) {
        inputs.add(stack);
      }
    }
    if (inputs.isEmpty() || recipe.getIngredients().isEmpty()) {
      return false;
    }
    return RecipeMatcher.findMatches(inputs, recipe.getIngredients()) != null;
  }

  @Nullable
  public static Int2IntOpenHashMap getIngredientMap(Recipe<?> recipe, RecipeInput input) {
    Int2IntOpenHashMap map = new Int2IntOpenHashMap();
    boolean foundOuter = true;
    outer:
    for (Ingredient ingredient : recipe.getIngredients()) {
      for (int i = 0; i < input.size(); i++) {
        int currentCount = map.get(i);
        int newCount = currentCount + 1;
        ItemStack stack = input.getItem(i);
        if (ingredient.test(stack)) {
          if (newCount <= stack.getCount()) {
            map.put(i, map.get(i) + 1);
            continue outer;
          }
        }
      }
      foundOuter = false;
      break;
    }
    if (!foundOuter) {
      return null;
    }
    return map;
  }

  public static boolean refillRecipeFromPlayer(ServerPlayer player, Recipe<?> recipe, RecipeInventory inventory) {
    if (player.isCreative()) {
      for (Ingredient ingredient : recipe.getIngredients()) {
        inventory.insert(ingredient.getItems()[0].copy());
      }
      return true;
    }
    PlayerMainInvWrapper wrapper = new PlayerMainInvWrapper(player.getInventory());
    return refillRecipe(wrapper, recipe, inventory);
  }

  public static boolean refillRecipe(IItemHandler inv, Recipe<?> recipe, RecipeInventory inventory) {
    Int2IntOpenHashMap counts = getIngredientMap(recipe, new RecipeInputWrapper(inv));
    if (counts == null) {
      return false;
    }
    for (Int2IntMap.Entry entry : counts.int2IntEntrySet()) {
      for (int i = 0; i < entry.getIntValue(); i++) {
        ItemStack thisStack = inv.extractItem(entry.getIntKey(), 1, false);
        if (!inventory.insert(thisStack).isEmpty()) {
          inv.insertItem(entry.getIntKey(), thisStack, false);
        }
      }
    }
    return true;
  }
}
