package mysticmods.roots.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ContainerRestrictedSlot extends Slot {
  public ContainerRestrictedSlot(Container container, int slot, int x, int y) {
    super(container, slot, x, y);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return !stack.getItem().canFitInsideContainerItems();
  }
}
