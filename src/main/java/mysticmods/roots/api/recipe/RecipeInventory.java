package mysticmods.roots.api.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public class RecipeInventory extends ItemStackHandler {
  public RecipeInventory() {
  }

  public RecipeInventory(int size) {
    super(size);
  }

  public RecipeInventory(NonNullList<ItemStack> stacks) {
    super(stacks);
  }

  public boolean isEmpty() {
    for (int i = 0; i < getSlots(); i++) {
      if (!getStackInSlot(i).isEmpty()) {
        return false;
      }
    }

    return true;
  }

  public List<ItemStack> getItems() {
    List<ItemStack> result = new ArrayList<>();
    for (int i = 0; i < getSlots(); i++) {
      ItemStack inSlot = getStackInSlot(i);
      if (!inSlot.isEmpty()) {
        result.add(inSlot);
      }
    }

    return result;
  }

  public List<ItemStack> getItemsAndClear() {
    List<ItemStack> result = getItems();
    for (int i = 0; i < getSlots(); i++) {
      setStackInSlot(i, ItemStack.EMPTY);
    }
    return result;
  }

  public ItemStack pop () {
    for (int i = getSlots() - 1; i >= 0; i--) {
      ItemStack inSlot = getStackInSlot(i);
      if (!inSlot.isEmpty()) {
        setStackInSlot(i, ItemStack.EMPTY);
        onContentsChanged(i);
        return inSlot;
      }
    }

    return ItemStack.EMPTY;
  }

  public ItemStack insert (ItemStack stack) {
    ItemStack result;
    int slot = -1;
    for (int i = 0; i < getSlots(); i++) {
      ItemStack inSlot = getStackInSlot(i);
      if (inSlot.isEmpty()) {
        slot = i;
        break;
      }
    }

    if (slot == -1) {
      return stack;
    }

    ItemStack toInsert = stack.copy();
    if (stack.getCount() > 1) {
      toInsert.setCount(1);
      result = stack.copy();
      result.shrink(1);
    } else {
      result = ItemStack.EMPTY;
    }

    setStackInSlot(slot, toInsert);
    onContentsChanged(slot);
    return result;
  }
}
