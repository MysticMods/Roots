package mysticmods.roots.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.ArrayList;
import java.util.List;

public interface IVariableHandler extends IItemHandlerModifiable {
  NonNullList<ItemStack> getItemStacks();

  default List<ItemStack> getContainedItems() {
    List<ItemStack> result = new ArrayList<>();
    for (ItemStack stack : getItemStacks()) {
      if (!stack.isEmpty()) {
        result.add(stack);
      }
    }

    return result;
  }

  default List<ItemStack> getAndRemoveItemSet() {
    List<ItemStack> result = new ArrayList<>();
    for (int i = 0; i < getItemStacks().size(); i++) {
      ItemStack stack = getItemStacks().get(i);
      if (!stack.isEmpty()) {
        if (stack.getCount() > 1) {
          result.add(stack.split(1));
        } else {
          result.add(stack);
          getItemStacks().set(i, ItemStack.EMPTY);
        }
      }
    }
    return result;
  }

  default int size() {
    int size = 0;
    for (ItemStack stack : getItemStacks()) {
      if (!stack.isEmpty()) {
        size++;
      }
    }
    return size;
  }
}
