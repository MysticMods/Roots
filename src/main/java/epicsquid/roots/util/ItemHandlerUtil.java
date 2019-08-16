package epicsquid.roots.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

// TODO: Move to lib
public class ItemHandlerUtil {
  public static class Iterable implements java.lang.Iterable<ItemStack> {
    private IItemHandler inventory;
    private SlotIterator iter;

    public Iterable(IItemHandler inventory) {
      this.inventory = inventory;
    }

    @Override
    public Iterator<ItemStack> iterator() {
      this.iter = new SlotIterator();
      return iter;
    }

    public int getSlot() {
      return this.iter.cursor;
    }

    public class SlotIterator implements Iterator<ItemStack> {
      int cursor;
      int lastRet = -1;
      int size = inventory.getSlots();

      SlotIterator() {
      }

      @Override
      public boolean hasNext() {
        return cursor != size;
      }

      @Override
      public ItemStack next() {
        int i = cursor;
        if (i >= inventory.getSlots()) {
          throw new NoSuchElementException();
        }
        cursor = i + 1;
        return inventory.getStackInSlot(lastRet = i);
      }
    }
  }

  public static boolean isEmpty (IItemHandler handler) {
    for (ItemStack stack : new Iterable(handler)) {
      if (!stack.isEmpty()) return false;
    }

    return true;
  }

  public static List<ItemStack> getItemsInSlots (IItemHandler handler, List<Integer> slotList, boolean simulate) {
    List<ItemStack> result = new ArrayList<>();
    for (int slot : slotList) {
      if (slot < handler.getSlots()) {
        result.add(handler.extractItem(slot, 1, simulate));
      }
    }
    result.removeIf(ItemStack::isEmpty);
    return result;
  }
}
