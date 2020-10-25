package epicsquid.roots.util;

import epicsquid.mysticallib.types.OneTimeSupplier;
import epicsquid.mysticallib.util.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class OreDictCache {
  private static Map<String, Supplier<Ingredient>> entries = new HashMap<>();
  private static Map<String, Set<Supplier<Ingredient>>> prefixMatches = new HashMap<>();
  private static Set<String> noIngredients = new HashSet<>();

  public static boolean matches(String oreName, IBlockState state) {
    return matches(oreName, ItemUtil.stackFromState(state));
  }

  public static boolean matches(String oreName, Block block) {
    return matches(oreName, new ItemStack(block));
  }

  public static boolean matches(String oreName, ItemStack stack) {
    if (stack.isEmpty() || oreName == null || oreName.isEmpty()) {
      return false;
    }
    return entries.computeIfAbsent(oreName, (name) -> new OneTimeSupplier<>(() -> new OreIngredient(name))).get().apply(stack);
  }

  public static boolean matchesPrefix(String prefix, ItemStack stack) {
    if (stack.isEmpty()) {
      return false;
    }
    if (noIngredients.contains(prefix)) {
      return false;
    }
    Set<Supplier<Ingredient>> matches = prefixMatches.computeIfAbsent(prefix, (p) -> {
      Set<Supplier<Ingredient>> ingredientSet = new HashSet<>();
      for (String oreName : OreDictionary.getOreNames()) {
        if (oreName.startsWith(p)) {
          ingredientSet.add(entries.computeIfAbsent(oreName, (name) -> new OneTimeSupplier<>(() -> new OreIngredient(name))));
        }
      }
      return ingredientSet;
    });
    if (matches.isEmpty()) {
      noIngredients.add(prefix);
      return false;
    }
    for (Supplier<Ingredient> match : matches) {
      if (match.get().apply(stack)) {
        return true;
      }
    }
    return false;
  }
}
