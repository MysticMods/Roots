package epicsquid.roots.container.slots;

import epicsquid.roots.init.ModItems;
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
  private IInventory libraryInventory;
  private final SpellLibraryData data;
  private int slot;

  public static SlotLibraryInfo create (SpellLibraryData data, int slot, int x, int y) {
    IInventory inventory = new InventoryBasic("[Slot: " + slot + "]", true, 1);
    inventory.setInventorySlotContents(0, ItemStack.EMPTY);
    return new SlotLibraryInfo(data, inventory, slot, x, y);
  }

  private SlotLibraryInfo(SpellLibraryData data, IInventory libraryInventory, int slot, int xPosition, int yPosition) {
    super(libraryInventory, 0, xPosition, yPosition);
    this.libraryInventory = libraryInventory;
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
    // TODO

    if (data == null) {
      return null;
    }

    return data.get(slot);
  }

  // TODO
  @Override
  public ItemStack getStack() {
    if (data == null) {
      return libraryInventory.getStackInSlot(0);
    }

    LibrarySpellInfo info = data.get(slot);
    if (info == null) {
      return ItemStack.EMPTY;
    }

    return info.asStack();
  }

  @Override
  public boolean getHasStack() {
    if (data == null) {
      return !libraryInventory.getStackInSlot(0).isEmpty();
    }

    LibrarySpellInfo info = data.get(slot);
    if (info == null) {
      return false;
    }

    return info.isObtained();
  }

  @Override
  public void putStack(ItemStack stack) {
    if (data == null && (stack.getItem() == ModItems.spell_dust || stack.isEmpty())) {
      libraryInventory.setInventorySlotContents(0, stack);
    }
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
      if (((SlotLibraryInfo)other).data == null) {
        return ItemStack.areItemStacksEqual(((SlotLibraryInfo) other).getStack(), getStack());
      }

      return ((SlotLibraryInfo) other).data.equals(data);
    }
    return false;
  }
}
