package mysticmods.roots.inventory.pouch.apothecary;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModContainers;
import mysticmods.roots.inventory.TagRestrictedSlot;
import mysticmods.roots.inventory.pouch.PouchContainer;
import mysticmods.roots.inventory.pouch.PouchSlot;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class ApothecaryPouchContainer extends PouchContainer {
  public ApothecaryPouchContainer(int containerId, Inventory inventory) {
    this(containerId, inventory, new SimpleContainer(27));
  }

  public ApothecaryPouchContainer(int containerId, Inventory playerInventory, Container inventory) {
    super(ModContainers.APOTHECARY_POUCH.get(), containerId, inventory);
    // Item slots
    for (int row = 0; row < 3; ++row) {
      for (int column = 0; column < 6; ++column) {
        this.addSlot(new Slot(inventory, column + row * 6, 18 + column * 18, 19 + row * 24));
      }
    }

    // Herb slots
    for (int row = 0; row < 3; ++row) {
      for (int column = 0; column < 3; ++column) {
        this.addSlot(new PouchSlot(inventory, column + row * 3 + 18, 134 + column * 18, 19 + row * 24));
      }
    }

    for (int i1 = 0; i1 < 3; ++i1) {
      for (int k1 = 0; k1 < 9; ++k1) {
        this.addSlot(new TagRestrictedSlot(RootsTags.Items.ALL_POUCHES, playerInventory, k1 + i1 * 9 + 9, 22 + k1 * 18, 133 + i1 * 18));
      }
    }

    for (int j1 = 0; j1 < 9; ++j1) {
      this.addSlot(new TagRestrictedSlot(RootsTags.Items.ALL_POUCHES, playerInventory, j1, 22 + j1 * 18, 191));
    }
  }
}
