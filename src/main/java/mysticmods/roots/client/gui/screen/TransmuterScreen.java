package mysticmods.roots.client.gui.screen;

import mysticmods.roots.inventory.fake.TransmuterContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class TransmuterScreen extends FakeScreen<TransmuterContainer> {
  public TransmuterScreen(TransmuterContainer menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
  }
}
