package mysticmods.roots.api.recipe.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public class RecipeInventory extends ItemStackHandler {
  private boolean empty = false;
  private boolean emptyChecked = false;

  public RecipeInventory() {
  }

  public RecipeInventory(int size) {
    super(size);
  }

  public RecipeInventory(NonNullList<ItemStack> stacks) {
    super(stacks);
  }

  public boolean isEmpty() {
    if (emptyChecked) {
      return empty;
    }
    emptyChecked = true;

    for (int i = 0; i < getSlots(); i++) {
      if (!getStackInSlot(i).isEmpty()) {
        empty = false;
        return false;
      }
    }

    empty = true;
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

  public List<ItemStack> getItemsCopy() {
    List<ItemStack> result = new ArrayList<>();
    for (int i = 0; i < getSlots(); i++) {
      ItemStack inSlot = getStackInSlot(i);
      if (!inSlot.isEmpty()) {
        result.add(inSlot.copy());
      }
    }
    return result;
  }

  public List<ItemStack> getItemsAndClear() {
    List<ItemStack> result = getItems();
    for (int i = 0; i < getSlots(); i++) {
      setStackInSlot(i, ItemStack.EMPTY);
    }
    emptyChecked = true;
    empty = true;
    return result;
  }

  public ItemStack pop() {
    for (int i = getSlots() - 1; i >= 0; i--) {
      ItemStack inSlot = getStackInSlot(i);
      if (!inSlot.isEmpty()) {
        setStackInSlot(i, ItemStack.EMPTY);
        onContentsChanged(i);
        emptyChecked = false;
        return inSlot;
      }
    }

    return ItemStack.EMPTY;
  }

  public ItemStack insert(ItemStack stack) {
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

  public List<ItemStack> insertAll(List<ItemStack> stacks) {
    List<ItemStack> result = new ArrayList<>();
    for (ItemStack stack : stacks) {
      ItemStack leftover = insert(stack);
      if (!leftover.isEmpty()) {
        result.add(leftover);
      }
    }
    emptyChecked = false;
    return result;
  }

  @Override
  public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
    if (!simulate) {
      emptyChecked = false;
    }
    return super.insertItem(slot, stack, simulate);
  }

  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    if (!simulate) {
      emptyChecked = false;
    }
    return super.extractItem(slot, amount, simulate);
  }

  @Override
  public void setStackInSlot(int slot, ItemStack stack) {
    super.setStackInSlot(slot, stack);
    emptyChecked = false;
  }

  @Override
  public void setSize(int size) {
    super.setSize(size);
    emptyChecked = false;
  }

  @Override
  protected void onContentsChanged(int slot) {
    this.emptyChecked = false;
  }
}
