package mysticmods.roots.blockentity.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class LimitedItemStackHandler extends ItemStackHandler {
    private final int maxStackLimit;

    public LimitedItemStackHandler(int size, int maxStackLimit) {
        super(size);
        this.maxStackLimit = maxStackLimit;
    }

    @Override
    protected int getStackLimit(int slot, ItemStack stack) {
        return maxStackLimit;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (!isItemValid(slot, stack)) {
            return stack;
        }

        validateSlotIndex(slot);

        ItemStack existing = this.stacks.get(slot);

        int limit = getStackLimit(slot, stack);

        if (!existing.isEmpty()) {
            if (!ItemStack.isSameItemSameComponents(stack, existing)) {
                return stack;
            }

            limit -= existing.getCount();
        }

        if (limit <= 0) {
            return stack;
        }

        boolean reachedLimit = stack.getCount() > limit;
        int toInsert = reachedLimit ? limit : stack.getCount();

        if (!simulate) {
            if (existing.isEmpty()) {
                this.stacks.set(slot, stack.copyWithCount(toInsert));
            } else {
                existing.grow(toInsert);
            }
            onContentsChanged(slot);
        }

        return reachedLimit ? stack.copyWithCount(stack.getCount() - toInsert) : ItemStack.EMPTY;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        if (stack.getCount() > maxStackLimit) {
            stack = stack.copyWithCount(maxStackLimit);
        }
        super.setStackInSlot(slot, stack);
    }
}
