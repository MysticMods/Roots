package mysticmods.roots.inventory.pouch;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class PouchMenu implements Container, MenuProvider {
  protected final ItemStack inventoryItem;
  protected NonNullList<ItemStack> inventory;

  public PouchMenu(ItemStack inventoryItem) {
    this.inventoryItem = inventoryItem;
    this.inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
    this.readFromStack(this.inventoryItem);
  }
  @Override
  public boolean isEmpty() {
    return inventory.isEmpty() || inventory.stream().allMatch(ItemStack::isEmpty);
  }

  @Override
  public ItemStack getItem(int slot) {
    if (slot < 0 || slot >= getContainerSize()) {
      throw new IllegalStateException("Slot index out of bounds: " + slot + ", container size: " + getContainerSize());
    }
    return inventory.get(slot);
  }

  @Override
  public ItemStack removeItem(int slot, int amount) {
    ItemStack stack = getItem(slot);
    if (!stack.isEmpty()) {
      if (stack.getCount() > amount) {
        stack = stack.split(amount);
        this.setChanged();
        ;
      } else {
        this.setItem(slot, ItemStack.EMPTY);
      }
    }
    return stack;
  }

  @Override
  public ItemStack removeItemNoUpdate(int slot) {
    ItemStack stack = this.getItem(slot);
    this.setItem(slot, ItemStack.EMPTY);
    return stack;
  }

  @Override
  public void setItem(int slot, ItemStack stack) {
    inventory.set(slot, stack);
    this.setChanged();
  }

  @Override
  public void setChanged() {
    this.writeToStack(this.inventoryItem);
  }

  @Override
  public boolean stillValid(Player player) {
    return true;
  }

  @Override
  public void clearContent() {
    for (int i = 0; i < getContainerSize(); i++) {
      this.setItem(i, ItemStack.EMPTY);
    }
  }

  public abstract int getContainerSize();

  public abstract Component getDisplayName();

  public abstract void readFromStack(ItemStack stack);

  public abstract void writeToStack(ItemStack stack);

  @Override
  public abstract @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player);
}
