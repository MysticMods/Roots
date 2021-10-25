package epicsquid.mysticallib.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public class ConfigUtil {
  @Nullable
  public static ItemStack parseItemStack (String itemStack) {
    String[] split = itemStack.split(":");
    if (split.length < 2) {
      return null;
    }

    ResourceLocation rl = new ResourceLocation(split[0], split[1]);
    int meta = 0;
    int count = 1;

    if (split.length == 3) {
      meta = Integer.parseInt(split[2]);
    }

    if (split.length == 4) {
      count = Integer.parseInt(split[3]);
    }

    Item item = ForgeRegistries.ITEMS.getValue(rl);
    if (item == null) {
      return null;
    }

    return new ItemStack(item, count, meta);
  }

  @Nullable
  public static Block parseBlock (String blockRL) {
    String[] split = blockRL.split(":");

    if (split.length != 2) {
      return null;
    }

    ResourceLocation rl = new ResourceLocation(split[0], split[1]);

    return ForgeRegistries.BLOCKS.getValue(rl);
  }

  public static boolean parseBoolean (String bool) {
    return bool.trim().equalsIgnoreCase("true");
  }

  @Nullable
  public static ResourceLocation parseResourceLocation (String rl) {
    return new ResourceLocation(rl);
  }

  public static <V, T extends Collection<V>> T parseLines (T result, Function<String, V> converter, String[] input) {
    result.clear();
    for (String line : input) {
      V potential = converter.apply(line);
      if (potential != null) {
        result.add(potential);
      }
    }
    return result;
  }

  public static <T, V> Map<T, V> parseMap (Map<T, V> result, Function<String, T> converter1, Function<String, V> converter2, String separator, String[] input) {
    result.clear();
    for (String line : input) {
      String[] split = line.split(separator);
      T key = converter1.apply(split[0]);
      V value = converter2.apply(split[1]);
      if (key == null || value == null) {
        continue;
      }
      result.put(key, value);
    }
    return result;
  }

  public static Set<ItemStack> parseItemStacksSet (String[] input) {
    return parseLines(new HashSet<>(), ConfigUtil::parseItemStack, input);
  }

  public static List<ItemStack> parseItemStacks (String[] input) {
    return parseLines(new ArrayList<>(), ConfigUtil::parseItemStack, input);
  }

  public static Set<Block> parseBlocksSet (String[] input) {
    return parseLines(new HashSet<>(), ConfigUtil::parseBlock, input);
  }

  public static List<Block> parseBlocks (String[] input) {
    return parseLines(new ArrayList<>(), ConfigUtil::parseBlock, input);
  }

  public static List<ResourceLocation> parseLocations (String[] input) {
    return parseLines(new ArrayList<>(), ConfigUtil::parseResourceLocation, input);
  }

  public static Set<ResourceLocation> parseLocationsSet (String[] input) {
    return parseLines(new HashSet<>(), ConfigUtil::parseResourceLocation, input);
  }

  public static boolean setContainsItemStack (Set<ItemStack> set, ItemStack checkFor) {
    for (ItemStack checking : set) {
      if (ItemUtil.equalWithoutSize(checking, checkFor)) {
        return true;
      }
    }

    return false;
  }
}
