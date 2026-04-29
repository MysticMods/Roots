package mysticmods.roots.inventory;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.SlotItemHandler;

public class SlotSingleItem extends SlotItemHandler {
  private final int limit;

  public SlotSingleItem(IItemHandlerModifiable handler, int limit, int slot, int x, int y) {
    super(handler, slot, x, y);
    this.limit = limit;
  }

  @Override
  public int getMaxStackSize() {
    return limit;
  }

  @Override
  public int getMaxStackSize(ItemStack stack) {
    return limit;
  }
}
