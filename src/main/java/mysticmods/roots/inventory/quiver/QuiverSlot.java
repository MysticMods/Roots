package mysticmods.roots.inventory.quiver;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class QuiverSlot extends Slot {
  public QuiverSlot(Container container, int slot, int x, int y) {
    super(container, slot, x, y);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return stack.is(ItemTags.ARROWS);
  }
}
