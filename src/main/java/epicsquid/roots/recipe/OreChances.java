package epicsquid.roots.recipe;

import epicsquid.mysticallib.types.OneTimeSupplier;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class OreChances {
  private static final List<OreItem> items = new ArrayList<>();

  public static void clear () {
    items.clear();
  }

  public static void addOreChance (OreItem item) {
    items.add(item);
  }

  public static IBlockState getRandomState () {
    OreItem item = WeightedRandom.getRandomItem(Util.rand, items);
    IBlockState state = item.getState();
    if (state == null) {
      int tries = 20;
      while (tries > 0 && state == null) {
        tries--;
        state = WeightedRandom.getRandomItem(Util.rand, items).getState();
      }
      if (state == null) {
        return Blocks.AIR.getDefaultState();
      } else {
        return state;
      }
    } else {
      return state;
    }
  }

  public static class StateItem extends OreItem {
    private final IBlockState state;

    public StateItem(IBlockState state, int itemWeightIn) {
      super(itemWeightIn);
      this.state = state;
    }

    @Nullable
    @Override
    public IBlockState getState() {
      return state;
    }
  }

  public static class OreDictItem extends OreItem {
    private final Supplier<Ingredient> ore;
    private final String name;

    public OreDictItem(String ore, int itemWeightIn) {
      super(itemWeightIn);
      this.ore = new OneTimeSupplier<>(() -> new OreIngredient(ore));
      this.name = ore;
    }


    @SuppressWarnings("deprecation")
    @Override
    @Nullable
    public IBlockState getState() {
      ItemStack stack = ore.get().getMatchingStacks()[0];
      if (stack.getItem() instanceof ItemBlock) {
        Block block = ((ItemBlock) stack.getItem()).getBlock();
        return block.getStateFromMeta(stack.getMetadata());
      } else {
        Roots.logger.error("OreDictItem for " + name + " does not contain a usable itemblock.");
      }
      return null;
    }
  }

  public static abstract class OreItem extends WeightedRandom.Item {
    protected OreItem(int itemWeightIn) {
      super(itemWeightIn);
    }

    @Nullable
    public abstract IBlockState getState ();
  }

  static {
    addOreChance(new OreDictItem("oreGold", 10));
    addOreChance(new OreDictItem("oreIron", 40));
    addOreChance(new OreDictItem("oreLapis", 12));
    addOreChance(new OreDictItem("oreDiamond", 5));
    addOreChance(new OreDictItem("oreEmerald", 4));
    addOreChance(new OreDictItem("oreRedstone", 22));
    addOreChance(new OreDictItem("oreQuartz", 18));
    addOreChance(new OreDictItem("oreCoal", 75));
  }
}
