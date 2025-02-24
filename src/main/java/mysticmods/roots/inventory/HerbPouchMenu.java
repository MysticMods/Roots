package mysticmods.roots.inventory;

import mysticmods.roots.init.ModAttachments;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.jetbrains.annotations.Nullable;

public class HerbPouchMenu implements Container, MenuProvider {
  private final ItemStack inventoryItem;

  private NonNullList<ItemStack> inventory;

  public HerbPouchMenu(ItemStack inventoryItem) {
    this.inventoryItem = inventoryItem;
    this.inventory = NonNullList.withSize(9, ItemStack.EMPTY);
    this.readFromStack(this.inventoryItem);
  }


  @Override
  public int getContainerSize() {
    return 9;
  }

  @Override
  public boolean isEmpty() {
    return inventory.isEmpty() || inventory.stream().allMatch(ItemStack::isEmpty);
  }

  @Override
  public ItemStack getItem(int slot) {
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

  @Override
  public Component getDisplayName() {
    // TODO: Translation key
    return Component.translatable("roots.container.herb_pouch");
  }

  public void readFromStack(ItemStack stack) {
    inventory = NonNullList.withSize(9, ItemStack.EMPTY);
    stack.getOrDefault(ModAttachments.HERB_POUCH_CONTENTS, ItemContainerContents.EMPTY).copyInto(inventory);
  }

  public void writeToStack(ItemStack stack) {
    stack.set(ModAttachments.HERB_POUCH_CONTENTS, ItemContainerContents.fromItems(inventory));
  }

  @Override
  public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
    return new HerbPouchContainer(containerId, playerInventory, this);
  }
}

