package epicsquid.roots.recipe;

import epicsquid.mysticallib.types.OneTimeSupplier;
import epicsquid.mysticallib.util.Util;
import epicsquid.mysticalworld.init.ModItems;
import epicsquid.mysticalworld.materials.Materials;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.oredict.OreIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class SpiritDrops {
  private static final List<SpiritItem> reliquary = new ArrayList<>();
  private static final List<SpiritItem> pouch = new ArrayList<>();

  public static void clearReliquary() {
    reliquary.clear();
  }

  public static void clearPouch() {
    pouch.clear();
  }

  public static void clear() {
    clearReliquary();
    clearPouch();
  }

  public static void addReliquary(SpiritItem item) {
    reliquary.add(item);
  }

  public static void addPouch(SpiritItem item) {
    pouch.add(item);
  }

  public static List<SpiritItem> getReliquary () {
    return reliquary;
  }

  public static List<SpiritItem> getPouch () {
    return pouch;
  }

  public static ItemStack getRandomReliquary() {
    if (reliquary.isEmpty()) {
      return ItemStack.EMPTY;
    }
    SpiritItem item = WeightedRandom.getRandomItem(Util.rand, reliquary);
    return item.getItem();
  }

  public static ItemStack getRandomPouch() {
    if (pouch.isEmpty()) {
      return ItemStack.EMPTY;
    }
    SpiritItem item = WeightedRandom.getRandomItem(Util.rand, pouch);
    return item.getItem();
  }

  public static class StackItem extends SpiritItem {
    private final Supplier<ItemStack> stack;

    public StackItem(Supplier<ItemStack> stack, int itemWeightIn) {
      super(itemWeightIn);
      this.stack = stack;
    }


    @Override
    public ItemStack getItem() {
      return stack.get().copy();
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

    public abstract ItemStack getItem();
    
    
    @Override
    public int hashCode() {
      return getItem().hashCode();
    }
  
    @Override
    public boolean equals(Object o) {
      return o instanceof SpiritItem && ((SpiritItem) o).getItem().equals(this.getItem());
    }
  }

  static {
    addPouch(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.APPLE, 3)), 31));
    addPouch(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Blocks.WEB, 2)), 22));
    addPouch(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.FEATHER, 6)), 29));
    addPouch(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.COAL, 7, 1)), 45));
    addPouch(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.BOOK)), 26));
    addPouch(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.WRITABLE_BOOK)), 23));
    addPouch(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.GOLD_NUGGET, 4)), 17));
    addPouch(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.IRON_NUGGET, 4)), 21));
    addPouch(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.FISH, 4, ItemFishFood.FishType.PUFFERFISH.getMetadata())), 14));
    addPouch(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.FISH, 4, ItemFishFood.FishType.CLOWNFISH.getMetadata())), 13));
    addPouch(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.GUNPOWDER, 3)), 7));
    addPouch(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.SLIME_BALL, 2)), 5));
    addPouch(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.DYE, 3, EnumDyeColor.BLUE.getMetadata())), 9));
    addPouch(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.GOLD_INGOT)), 11));
    addPouch(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.IRON_INGOT)), 13));
    addPouch(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.REDSTONE, 6)), 10));
    addPouch(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.NAME_TAG, 2)), 8));
    addPouch(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.COMPASS)), 6));
    addPouch(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.CLOCK)), 5));
    addPouch(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(ModItems.unripe_pearl, 4)), 4));
    addPouch(new OreSpiritItem("nugget" + Materials.copper.getOredictNameSuffix(), 21));
    addPouch(new OreSpiritItem("nugget" + Materials.silver.getOredictNameSuffix(), 16));
    addPouch(new OreSpiritItem("ingot" + Materials.copper.getOredictNameSuffix(), 11));
    addPouch(new OreSpiritItem("ingot" + Materials.silver.getOredictNameSuffix(), 13));
  
  
    addReliquary(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.EXPERIENCE_BOTTLE)), 35));
    addReliquary(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.NETHER_WART, 3)), 29));
    addReliquary(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.SPIDER_EYE, 2)), 32));
    addReliquary(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.GLOWSTONE_DUST, 5)), 14));
    addReliquary(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.DIAMOND)), 12));
    addReliquary(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.ENDER_PEARL, 2)), 14));
    addReliquary(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.BLAZE_ROD, 2)), 12));
    addReliquary(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.EMERALD, 2)), 13));
    addReliquary(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Items.GHAST_TEAR, 3)), 13));
    addReliquary(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(Objects.requireNonNull(Materials.amethyst.getItem()))), 15));
    addReliquary(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(ModItems.pearl, 6)), 15));
    addReliquary(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(ModItems.beetle_mask)), 5));
    addReliquary(new StackItem(new OneTimeSupplier<>(() -> new ItemStack(ModItems.antler_hat)), 6));
  
    
  }
}
