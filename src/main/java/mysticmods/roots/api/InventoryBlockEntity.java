package mysticmods.roots.api;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public interface InventoryBlockEntity {
  ItemStackHandler getInventory();

  default NonNullList<ItemStack> getItems() {
    ItemStackHandler inventory = getInventory();
    NonNullList<ItemStack> items = NonNullList.withSize(inventory.getSlots(), ItemStack.EMPTY);
    for (int i = 0; i < inventory.getSlots(); i++) {
      items.set(i, inventory.getStackInSlot(i));
    }
    return items;
  }

  default List<ItemStack> getNonEmptyItems() {
    ItemStackHandler inventory = getInventory();
    List<ItemStack> items = new ArrayList<>();
    for (int i = 0; i < inventory.getSlots(); i++) {
      ItemStack inSlot = inventory.getStackInSlot(i);
      if (!inSlot.isEmpty()) {
        items.add(inSlot);
      }
    }
    return items;
  }

  default ItemStack popOne() {
    ItemStackHandler inventory = getInventory();
    ItemStack inSlot = inventory.getStackInSlot(0);
    if (inSlot.isEmpty()) {
      return ItemStack.EMPTY;
    }

    if (inSlot.getCount() > 1) {
      ItemStack result = inSlot.copy();
      result.setCount(1);
      inSlot.shrink(1);
      inventory.setStackInSlot(0, inSlot);
      return result;
    } else {
      inventory.setStackInSlot(0, ItemStack.EMPTY);
      return inSlot;
    }
  }
}
