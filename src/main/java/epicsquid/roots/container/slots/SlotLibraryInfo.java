package epicsquid.roots.container.slots;

import epicsquid.roots.spell.info.LibrarySpellInfo;
import epicsquid.roots.world.data.SpellLibraryData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class SlotLibraryInfo extends Slot implements ILibrarySlot {
  private static IInventory emptyInventory = new InventoryBasic("[Null]", true, 0);
  private final SpellLibraryData data;
  private int slot;

  public SlotLibraryInfo(SpellLibraryData data, int slot, int xPosition, int yPosition) {
    super(emptyInventory, 0, xPosition, yPosition);
    this.data = data;
    this.slot = slot;
  }

  @Override
  public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
    return ItemStack.EMPTY;
  }

  public int getSlot() {
    return slot;
  }

  @Override
  public boolean isItemValid(ItemStack stack) {
    return false;
  }

  @Override
  @Nullable
  public LibrarySpellInfo getInfo() {
    if (data == null) {
      return null;
    }

    return data.get(slot);
  }

  // TODO
  @Override
  public ItemStack getStack() {
    if (data == null) {
      return ItemStack.EMPTY;
    }

    LibrarySpellInfo info = data.get(slot);
    if (info == null) {
      return ItemStack.EMPTY;
    }

    return info.toStaff().asStack();
  }

  @Override
  public boolean getHasStack() {
    if (data == null) {
      return false;
    }

    LibrarySpellInfo info = data.get(slot);
    if (info == null) {
      return false;
    }

    return info.isObtained();
  }

  @Override
  public void putStack(ItemStack stack) {
  }

  @Override
  public int getSlotStackLimit() {
    return 1;
  }

  @Override
  public int getItemStackLimit(ItemStack stack) {
    return 1;
  }

  @Override
  public ItemStack decrStackSize(int amount) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canTakeStack(EntityPlayer playerIn) {
    return false;
  }

  @Override
  public boolean isSameInventory(Slot other) {
    if (other instanceof SlotLibraryInfo) {
      return ((SlotLibraryInfo) other).data.equals(data);
    }
    return false;
  }
}
