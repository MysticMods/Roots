package mysticmods.roots.api.recipe.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public class RecipeInventory extends ItemStackHandler {
  private boolean empty = false;
  private boolean emptyChecked = false;
  private boolean full = false;
  private boolean fullChecked = false;

  public RecipeInventory() {
  }

  public RecipeInventory(int size) {
    super(size);
  }

  public RecipeInventory(NonNullList<ItemStack> stacks) {
    super(stacks);
  }

  protected void invalidateCache() {
    emptyChecked = false;
    fullChecked = false;
  }

  public boolean isEmpty() {
    if (emptyChecked) {
      return empty;
    }

    empty = true;
    for (int i = 0; i < getSlots(); i++) {
      if (!getStackInSlot(i).isEmpty()) {
        empty = false;
        break;
      }
    }
    emptyChecked = true;
    return empty;
  }

  public boolean isFull() {
    if (fullChecked) {
      return full;
    }

    full = true;
    for (int i = 0; i < getSlots(); i++) {
      if (getStackInSlot(i).isEmpty()) {
        full = false;
        break;
      }
    }
    fullChecked = true;
    return full;
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
    return result;
  }

  public ItemStack pop() {
    for (int i = getSlots() - 1; i >= 0; i--) {
      ItemStack inSlot = getStackInSlot(i);
      if (!inSlot.isEmpty()) {
        // setStackInSlot invalidates the cache and fires onContentsChanged
        setStackInSlot(i, ItemStack.EMPTY);
        return inSlot;
      }
    }

    return ItemStack.EMPTY;
  }

  public ItemStack insert(ItemStack stack) {
    if (stack.isEmpty()) {
      return stack;
    }

    int slot = -1;
    for (int i = 0; i < getSlots(); i++) {
      if (getStackInSlot(i).isEmpty()) {
        slot = i;
        break;
      }
    }

    if (slot == -1) {
      return stack;
    }

    ItemStack toInsert = stack.copyWithCount(1);
    ItemStack result = stack.getCount() > 1 ? stack.copyWithCount(stack.getCount() - 1) : ItemStack.EMPTY;

    setStackInSlot(slot, toInsert);
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
    return result;
  }

  @Override
  public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
    if (!simulate) {
      invalidateCache();
    }
    return super.insertItem(slot, stack, simulate);
  }

  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    if (!simulate) {
      invalidateCache();
    }
    return super.extractItem(slot, amount, simulate);
  }

  @Override
  public void setStackInSlot(int slot, ItemStack stack) {
    invalidateCache();
    super.setStackInSlot(slot, stack);
  }

  @Override
  public void setSize(int size) {
    invalidateCache();
    super.setSize(size);
  }

  @Override
  protected void onLoad() {
    invalidateCache();
    super.onLoad();
  }

  @Override
  protected void onContentsChanged(int slot) {
    invalidateCache();
  }
}
