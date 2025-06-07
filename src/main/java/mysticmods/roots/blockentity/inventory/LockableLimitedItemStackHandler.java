package mysticmods.roots.blockentity.inventory;

import net.minecraft.world.item.ItemStack;

import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;

public class LockableLimitedItemStackHandler extends LimitedItemStackHandler {
  private final BooleanSupplier lockSupplier;

  public LockableLimitedItemStackHandler(int size, IntSupplier maxStackLimit, BooleanSupplier lockSupplier) {
    super(size, maxStackLimit);
    this.lockSupplier = lockSupplier;
  }

  @Override
  public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
    if (lockSupplier.getAsBoolean()) {
      return stack; // If locked, do not allow insertion
    }
    return super.insertItem(slot, stack, simulate);
  }

  @Override
  public void setStackInSlot(int slot, ItemStack stack) {
    if (lockSupplier.getAsBoolean()) {
      return; // If locked, do not allow setting stack
    }
    super.setStackInSlot(slot, stack);
  }

  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    if (lockSupplier.getAsBoolean()) {
      return ItemStack.EMPTY; // If locked, do not allow extraction
    }
    return super.extractItem(slot, amount, simulate);
  }
}
