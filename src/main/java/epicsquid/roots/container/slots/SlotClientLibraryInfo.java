package epicsquid.roots.container.slots;

import epicsquid.roots.spell.info.LibrarySpellInfo;
import epicsquid.roots.world.data.SpellLibraryData;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class SlotClientLibraryInfo extends Slot implements ILibrarySlot {
  private final IInventory inventory;

  public SlotClientLibraryInfo(SpellLibraryData data, int index, int xPosition, int yPosition) {
    this(new Inventory("[Slot]", true, 1), index, xPosition, yPosition);
  }

  public SlotClientLibraryInfo(IInventory inventoryIn, int index, int xPosition, int yPosition) {
    super(inventoryIn, index, xPosition, yPosition);
    this.inventory = inventoryIn;
  }


  @Nullable
  @Override
  public LibrarySpellInfo getInfo() {
    ItemStack stack = getStack();
    if (stack.isEmpty()) {
      return null;
    }

    return null;
  }
}
