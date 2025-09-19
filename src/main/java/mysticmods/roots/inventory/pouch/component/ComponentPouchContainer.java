package mysticmods.roots.inventory.pouch.component;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModContainers;
import mysticmods.roots.inventory.ContainerRestrictedSlot;
import mysticmods.roots.inventory.TagRestrictedSlot;
import mysticmods.roots.inventory.pouch.PouchContainer;
import mysticmods.roots.inventory.pouch.PouchSlot;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;

public class ComponentPouchContainer extends PouchContainer {
  public ComponentPouchContainer(int containerId, Inventory inventory) {
    this(containerId, inventory, new SimpleContainer(18));
  }

  public ComponentPouchContainer(int containerId, Inventory playerInventory, Container inventory) {
    super(ModContainers.COMPONENT_POUCH.get(), containerId, inventory);
    // Item slots
    for (int row = 0; row < 3; ++row) {
      for (int column = 0; column < 4; ++column) {
        this.addSlot(new ContainerRestrictedSlot(inventory, column + row * 4, 19 + column * 21, 19 + row * 24));
      }
    }

    // Herb slots
    for (int row = 0; row < 3; ++row) {
      for (int column = 0; column < 2; ++column) {
        this.addSlot(new PouchSlot(inventory, column + row * 2 + 12, 121 + column * 21, 19 + row * 24));
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
}
