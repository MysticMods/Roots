package epicsquid.roots.handler;

import epicsquid.mysticallib.util.ItemUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

@Deprecated
public class NBTStackHandler implements IItemHandler, IItemHandlerModifiable {
  private ItemStack stack;
  private int slots;
  private String identifier;

  public NBTStackHandler(ItemStack stack, String identifier, int slots) {
    this.stack = stack;
    this.identifier = identifier;
    this.slots = slots;
    ensureSlots(slots);
  }

  private void ensureSlots(int slots) {
    NBTTagList tag = getTag();
    if (tag.tagCount() < slots) {
      for (int i = tag.tagCount(); i < slots; i++) {
        tag.appendTag(ItemStack.EMPTY.serializeNBT());
      }
    }
  }

  public NBTTagList getTag() {
    NBTTagCompound tag = stack.getTagCompound();
    if (tag == null) {
      tag = new NBTTagCompound();
      stack.setTagCompound(tag);
    }
    if (tag.hasKey(identifier, Constants.NBT.TAG_LIST)) {
      return tag.getTagList(identifier, Constants.NBT.TAG_COMPOUND);
    } else {
      NBTTagList list = new NBTTagList();
      tag.setTag(identifier, list);
      return list;
    }
  }

  @Override
  public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
    if (slot >= slots) {
      throw new IllegalStateException("Invalid slot " + slot + ", maximum number of slots is " + slots);
    }
    NBTTagList tag = getTag();
    tag.set(slot, stack.serializeNBT());
  }

  @Override
  public int getSlots() {
    return slots;
  }

  @Nonnull
  @Override
  public ItemStack getStackInSlot(int slot) {
    if (slot >= slots) {
      throw new IllegalStateException("Invalid slot " + slot + ", maximum number of slots is " + slots);
    }
    NBTTagList tag = getTag();
    return new ItemStack(tag.getCompoundTagAt(slot));
  }

  @Nonnull
  @Override
  public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
    if (slot >= slots) {
      throw new IllegalStateException("Invalid slot " + slot + ", maximum number of slots is " + slots);
    }
    NBTTagList tag = getTag();
    ItemStack inSlot = new ItemStack(tag.getCompoundTagAt(slot));
    if (!inSlot.isEmpty() && !ItemUtil.equalWithoutSize(inSlot, stack)) {
      return stack;
    }

    if (inSlot.getCount() + stack.getCount() < inSlot.getMaxStackSize()) {
      if (!simulate) {
        inSlot.grow(stack.getCount());
        tag.set(slot, inSlot.serializeNBT());
      }
      return ItemStack.EMPTY;
    } else {
      int remainder = stack.getCount() - (inSlot.getMaxStackSize() - inSlot.getCount());
      if (simulate) {
        inSlot.setCount(remainder);
        return inSlot;
      } else {
        ItemStack result = inSlot.copy();
        result.setCount(remainder);
        inSlot.setCount(inSlot.getMaxStackSize());
        tag.set(slot, inSlot.serializeNBT());
        return result;
      }
    }
  }

  @Nonnull
  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    if (slot >= slots) {
      throw new IllegalStateException("Invalid slot " + slot + ", maximum number of slots is " + slots);
    }
    NBTTagList tag = getTag();
    ItemStack inSlot = new ItemStack(tag.getCompoundTagAt(slot));
    if (inSlot.isEmpty()) {
      return ItemStack.EMPTY;
    }

    if (inSlot.getCount() <= amount) {
      if (!simulate) {
        tag.set(slot, ItemStack.EMPTY.serializeNBT());
      }
      return inSlot;
    } else {
      ItemStack result = inSlot.copy();
      result.setCount(amount);
      if (simulate) {
        return result;
      }

      inSlot.shrink(amount);
      tag.set(slot, inSlot.serializeNBT());
      return result;
    }
  }

  @Override
  public int getSlotLimit(int slot) {
    return 64;
  }
}
