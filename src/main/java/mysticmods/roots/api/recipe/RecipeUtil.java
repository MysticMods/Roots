package mysticmods.roots.api.recipe;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import mysticmods.roots.api.recipe.inventory.RecipeInputWrapper;
import mysticmods.roots.api.recipe.inventory.RecipeInventory;
import net.minecraft.network.chat.Component;
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
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

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

  public static boolean partiallyMatchesIngredients(Recipe<?> recipe, RecipeInput input) {
    return partiallyMatchesIngredients(recipe, input, null);
  }

  public static boolean partiallyMatchesIngredients (Recipe<?> recipe, RecipeInput input, @Nullable Level level) {
    List<ItemStack> inputs = new ArrayList<>();
    for (int i = 0; i < input.size(); i++) {
      ItemStack stack = input.getItem(i);
      if (!stack.isEmpty()) {
        inputs.add(stack);
      }
    }

    if (inputs.size() == recipe.getIngredients().size()) {
      return matchesIngredients(recipe, input, level);
    } else if (inputs.size() > recipe.getIngredients().size()) {
      return false;
    }

    BitSet matchedIngredients = new BitSet(recipe.getIngredients().size());
    for (ItemStack stack : inputs) {
      boolean found = false;
      for (int i = 0; i < recipe.getIngredients().size(); i++)
      {
        if (!matchedIngredients.get(i) && recipe.getIngredients().get(i).test(stack)) {
          matchedIngredients.set(i);
          found = true;
          break;
        }
      }
      if (!found) {
        return false;
      }
    }

    return true;
  }

  public static IngredientMatchResult getIngredientMap(Recipe<?> recipe, RecipeInput input) {
    Int2IntOpenHashMap map = new Int2IntOpenHashMap();
    List<Ingredient> missing = new ArrayList<>();
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
      missing.add(ingredient);
    }
    return new IngredientMatchResult(map, missing);
  }

  private static final class PlayerThingy implements Consumer<List<Ingredient>> {
    private final ServerPlayer player;

    public PlayerThingy(ServerPlayer player) {
      this.player = player;
    }

    @Override
    public void accept(List<Ingredient> ingredients) {
      this.player.displayClientMessage(Component.literal("Unable to refill this recipe as " + ingredients.size() + " ingredient(s) are missing."), true);
      // TODO: Find a way to convert an ingredient into a readable string
/*      for (Ingredient ingredient : ingredients) {
        this.player.displayClientMessage(Component.literal("Missing: " + ingredient.toString()), true);
      }*/
    }
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
    return refillRecipe(inv, recipe, inventory, (stack) -> {});
  }

  public static boolean refillRecipe(IItemHandler inv, Recipe<?> recipe, RecipeInventory inventory, Consumer<List<Ingredient>> missingConsumer) {
    var matchResult = getIngredientMap(recipe, new RecipeInputWrapper(inv));
    if (!matchResult.isComplete()) {
      missingConsumer.accept(matchResult.getMissing());
      return false;
    }
    Int2IntOpenHashMap counts = matchResult.getSlots();
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

  public static final class IngredientMatchResult {
    private final Int2IntOpenHashMap slotCounts;
    private final List<Ingredient> missing;

    private IngredientMatchResult(Int2IntOpenHashMap slotCounts, List<Ingredient> missing) {
      this.slotCounts = slotCounts;
      this.missing = Collections.unmodifiableList(missing);
    }

    public Int2IntOpenHashMap getSlots() {
      return slotCounts;
    }

    public List<Ingredient> getMissing() {
      return missing;
    }

    public boolean isComplete () {
      return missing.isEmpty();
    }
  }

}
