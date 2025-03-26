package mysticmods.roots.inventory;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModContainers;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class HerbPouchContainer extends AbstractContainerMenu {
  private final Container inventory;

  public HerbPouchContainer(int id, Inventory inventory) {
    this(id, inventory, new SimpleContainer(9));
  }

  public HerbPouchContainer(int id, Inventory playerInventory, Container inventory) {
    super(ModContainers.HERB_POUCH.get(), id);

    this.inventory = inventory;
    for (int row = 0; row < 3; ++row) {
      for (int column = 0; column < 3; ++column) {
        this.addSlot(new HerbPouchSlot(inventory, column + row * 3, 58 + column * 22, 19 + row * 24));
      }
    }

    for (int i1 = 0; i1 < 3; ++i1) {
      for (int k1 = 0; k1 < 9; ++k1) {
        this.addSlot(new TagRestrictedSlot(RootsTags.Items.ALL_POUCHES, playerInventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 125 + i1 * 18));
      }
    }

    for (int j1 = 0; j1 < 9; ++j1) {
      this.addSlot(new TagRestrictedSlot(RootsTags.Items.ALL_POUCHES, playerInventory, j1, 8 + j1 * 18, 183));
    }
  }

  // TODO: Filtering
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
