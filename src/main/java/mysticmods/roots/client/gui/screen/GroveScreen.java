package mysticmods.roots.client.gui.screen;

import mysticmods.roots.inventory.fake.GroveContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GroveScreen extends FakeScreen<GroveContainer> {
  public GroveScreen(GroveContainer menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
  }
}
