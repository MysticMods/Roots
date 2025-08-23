package mysticmods.roots.inventory.pouch;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.inventory.ContainerRestrictedSlot;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PouchSlot extends ContainerRestrictedSlot {
  public PouchSlot(Container container, int slot, int x, int y) {
    super(container, slot, x, y);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return stack.is(RootsTags.Items.HERBS) && super.mayPlace(stack);
  }
}
