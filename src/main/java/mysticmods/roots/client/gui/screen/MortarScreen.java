package mysticmods.roots.client.gui.screen;

import mysticmods.roots.inventory.fake.MortarContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class MortarScreen extends FakeScreen<MortarContainer> {
  public MortarScreen(MortarContainer menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
  }
}
