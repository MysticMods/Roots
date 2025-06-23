package mysticmods.roots.inventory.quiver;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModContainers;
import mysticmods.roots.inventory.TagRestrictedSlot;
import mysticmods.roots.inventory.pouch.PouchContainer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;

public class QuiverContainer extends PouchContainer {
  public QuiverContainer(int containerId, Inventory playerInventory) {
    this(containerId, playerInventory, new SimpleContainer(6));
  }

  public QuiverContainer(int containerId, Inventory playerInventory, Container container) {
    super(ModContainers.QUIVER.get(), containerId, container);
    // Item slots
    for (int row = 0; row < 2; ++row) {
      for (int column = 0; column < 3; ++column) {
        this.addSlot(new QuiverSlot(inventory, column + row * 3, 60 + column * 20, 21 + row * 20));
      }
    }

    for (int i1 = 0; i1 < 3; ++i1) {
      for (int k1 = 0; k1 < 9; ++k1) {
        this.addSlot(new TagRestrictedSlot(RootsTags.Items.QUIVERS, playerInventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 96 + i1 * 18));
      }
    }

    for (int j1 = 0; j1 < 9; ++j1) {
      this.addSlot(new TagRestrictedSlot(RootsTags.Items.QUIVERS, playerInventory, j1, 8 + j1 * 18, 154));
    }
  }
}
