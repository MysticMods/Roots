package epicsquid.mysticallib.util;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class ListUtil {
  public static class StateComparator implements Comparator<IBlockState> {
    @Override
    public int compare(IBlockState arg0, IBlockState arg1) {
      return (arg0.getBlock().getRegistryName().toString() + "_" + arg0.getBlock().getMetaFromState(arg0))
          .compareTo(arg1.getBlock().getRegistryName().toString() + "_" + arg1.getBlock().getMetaFromState(arg1));
    }
  }

  public static class StackComparator implements Comparator<ItemStack> {
    @Override
    public int compare(ItemStack arg0, ItemStack arg1) {
      return (arg0.getItem().getRegistryName().toString() + "_" + arg0.getItemDamage())
          .compareTo(arg1.getItem().getRegistryName().toString() + "_" + arg1.getItemDamage());
    }
  }

  public static StateComparator stateComparator = new StateComparator();

  public static boolean stateListsMatch(List<IBlockState> list1, List<IBlockState> list2) {
    list1.sort(stateComparator);
    list2.sort(stateComparator);
    boolean doMatch = list1.size() == list2.size();
    if (doMatch) {
      for (int i = 0; i < list1.size(); i++) {
        if (list1.get(i).getBlock() != list2.get(i).getBlock() || list1.get(i).getBlock().getMetaFromState(list1.get(i)) != list2.get(i).getBlock()
            .getMetaFromState(list2.get(i))) {
          doMatch = false;
        }
      }
    }
    return doMatch;
  }

  public static StackComparator stackComparator = new StackComparator();

  public static boolean stackListsMatch(List<ItemStack> list1, List<ItemStack> list2) {
    list1.sort(stackComparator);
    list2.sort(stackComparator);
    boolean doMatch = list1.size() == list2.size();
    if (doMatch) {
      for (int i = 0; i < list1.size(); i++) {
        if (list1.get(i).getItem() != list2.get(i).getItem() || list1.get(i).getItemDamage() != list2.get(i).getItemDamage()) {
          doMatch = false;
        }
      }
    }
    return doMatch;
  }

  public static boolean matchesIngredients(List<ItemStack> ingredients, List<Ingredient> selfIngredients) {
    List<ItemStack> actualInput = ingredients.stream().filter((i) -> i != null && !i.isEmpty()).sorted(stackComparator).collect(Collectors.toList());
    List<Ingredient> actualIngredients = selfIngredients.stream().filter(Objects::nonNull).collect(Collectors.toList());

    if (actualInput.size() != actualIngredients.size())
      return false;

    IntOpenHashSet foundIngredients = new IntOpenHashSet();
    IntOpenHashSet usedItemStacks = new IntOpenHashSet();

    Ingredient ingredient;

    for (int i = 0; i < actualIngredients.size(); i++) {
      ingredient = actualIngredients.get(i);
      for (int j = 0; j < actualInput.size(); j++) {
        if (usedItemStacks.contains(j))
          continue;
        if (ingredient.apply(actualInput.get(j))) {
          usedItemStacks.add(j);
          foundIngredients.add(i);
          break;
        }
      }
    }

    return foundIngredients.size() == actualIngredients.size();
  }
}