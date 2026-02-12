package mysticmods.roots.client.gui.screen;

import mysticmods.roots.inventory.fake.PyreContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class PyreScreen extends FakeScreen<PyreContainer> {
  public PyreScreen(PyreContainer menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
  }
}
