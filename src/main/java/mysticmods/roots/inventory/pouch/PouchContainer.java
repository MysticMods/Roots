package mysticmods.roots.inventory.pouch;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class PouchContainer extends AbstractContainerMenu {
  protected final Container inventory;

  protected PouchContainer(@Nullable MenuType<?> menuType, int containerId, Container inventory) {
    super(menuType, containerId);
    this.inventory = inventory;
  }

  @Override
  public ItemStack quickMoveStack(Player player, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.slots.get(index);

    if (slot != null && slot.hasItem() && slot.mayPickup(player)) {
      ItemStack itemstack1 = slot.getItem();
      itemstack = itemstack1.copy();

      if (index < this.inventory.getContainerSize()) {
        if (!this.moveItemStackTo(itemstack1, this.inventory.getContainerSize(), this.slots.size(), true)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.moveItemStackTo(itemstack1, 0, this.inventory.getContainerSize(), false)) {
        return ItemStack.EMPTY;
      }

      if (itemstack1.isEmpty()) {
        slot.set(ItemStack.EMPTY);
      } else {
        slot.setChanged();
      }
    }

    return itemstack;
  }

  @Override
  public boolean stillValid(Player player) {
    return true;
  }


  @Override
  public void removed(Player player) {
    super.removed(player);
    this.inventory.stopOpen(player);
  }
}
