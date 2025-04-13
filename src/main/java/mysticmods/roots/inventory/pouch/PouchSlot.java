package mysticmods.roots.inventory.pouch;

import mysticmods.roots.api.RootsTags;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PouchSlot extends Slot {
  public PouchSlot(Container container, int slot, int x, int y) {
    super(container, slot, x, y);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return stack.is(RootsTags.Items.HERBS);
  }
}
