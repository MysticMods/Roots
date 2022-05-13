package mysticmods.roots.api;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public interface InventoryBlockEntity {
  ItemStackHandler getInventory ();

  default NonNullList<ItemStack> getItems () {
    ItemStackHandler inventory = getInventory();
    NonNullList<ItemStack> items = NonNullList.withSize(inventory.getSlots(), ItemStack.EMPTY);
    for (int i = 0; i < inventory.getSlots(); i++) {
      items.set(i, inventory.getStackInSlot(i));
    }
    return items;
  }

  default List<ItemStack> getNonEmptyItems () {
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
}
