package epicsquid.roots.recipe;

import epicsquid.mysticallib.types.OneTimeSupplier;
import epicsquid.mysticallib.util.Util;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.oredict.OreIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SpiritDrops {
  private static final List<SpiritItem> reliquary = new ArrayList<>();
  private static final List<SpiritItem> pouch = new ArrayList<>();

  public static void clearReliquary () {
    reliquary.clear();
  }

  public static void clearPouch () {
    pouch.clear();
  }

  public static void clear () {
    clearReliquary();
    clearPouch();
  }

  public static void addReliquary (SpiritItem item) {
    reliquary.add(item);
  }

  public static void addPouch (SpiritItem item) {
    reliquary.add(item);
  }

  public static ItemStack getRandomReliquary () {
    if (reliquary.isEmpty()) {
      return ItemStack.EMPTY;
    }
    SpiritItem item = WeightedRandom.getRandomItem(Util.rand, reliquary);
    return item.getItem();
  }

  public static ItemStack getRandomPouch () {
    if (pouch.isEmpty()) {
      return ItemStack.EMPTY;
    }
    SpiritItem item = WeightedRandom.getRandomItem(Util.rand, pouch);
    return item.getItem();
  }

  public static class StackItem extends SpiritItem {
    private final ItemStack stack;

    public StackItem(ItemStack stack, int itemWeightIn) {
      super(itemWeightIn);
      this.stack = stack;
    }


    @Override
    public ItemStack getItem() {
      return stack.copy();
    }
  }

  public static class OreSpiritItem extends SpiritItem {
    private final Supplier<Ingredient> item;
    private final String name;

    public OreSpiritItem(String ore, int itemWeightIn) {
      super(itemWeightIn);
      this.item = new OneTimeSupplier<>(() -> new OreIngredient(ore));
      this.name = ore;
    }

    @Override
    public ItemStack getItem() {
      return item.get().getMatchingStacks()[0].copy();
    }
  }

  public static abstract class SpiritItem extends WeightedRandom.Item {
    protected SpiritItem(int itemWeightIn) {
      super(itemWeightIn);
    }

    public abstract ItemStack getItem ();
  }
}
