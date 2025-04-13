package mysticmods.roots.inventory.pouch.herb;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModContainers;
import mysticmods.roots.inventory.TagRestrictedSlot;
import mysticmods.roots.inventory.pouch.PouchContainer;
import mysticmods.roots.inventory.pouch.PouchSlot;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;

public class HerbPouchContainer extends PouchContainer {
  public HerbPouchContainer(int id, Inventory inventory) {
    this(id, inventory, new SimpleContainer(9));
  }

  public HerbPouchContainer(int id, Inventory playerInventory, Container inventory) {
    super(ModContainers.HERB_POUCH.get(), id, inventory);
    for (int row = 0; row < 3; ++row) {
      for (int column = 0; column < 3; ++column) {
        this.addSlot(new PouchSlot(inventory, column + row * 3, 58 + column * 22, 19 + row * 24));
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
