package epicsquid.roots.recipe;

import epicsquid.mysticallib.types.OneTimeSupplier;
import epicsquid.mysticallib.util.Util;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FoodPoisoning {
  private static List<OreToStack> items = new ArrayList<>();

  public static void addPoison(String oreName, ItemStack stack, float chance) {
    items.add(new OreToStack(oreName, stack, chance));
  }

  public static ItemStack replacement(ItemStack input) {
    for (OreToStack o : items) {
      if (o.test(input)) {
        return o.result();
      }
    }
    return input;
  }

  static {
    addPoison("cropPotato", new ItemStack(Items.POISONOUS_POTATO), 0.18f);
  }

  public static class OreToStack implements Predicate<ItemStack> {
    private final Supplier<Ingredient> input;
    private final ItemStack stack;
    private final String oreName;
    private final float chance;

    public OreToStack(String oreName, ItemStack stack, float chance) {
      this.oreName = oreName;
      this.input = new OneTimeSupplier<>(() -> new OreIngredient(oreName));
      this.stack = stack;
      this.chance = chance;
    }

    @Override
    public boolean test(ItemStack stack) {
      return input.get().apply(stack) && Util.rand.nextFloat() <= chance;
    }

    public ItemStack result() {
      return stack.copy();
    }

    public String oreName() {
      return oreName;
    }
  }
}
