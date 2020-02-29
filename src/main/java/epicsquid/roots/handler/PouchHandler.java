package epicsquid.roots.handler;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.ItemPouch;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class PouchHandler {
  public static final int COMPONENT_POUCH_HERB_SLOTS = 6;
  public static final int COMPONENT_POUCH_INVENTORY_SLOTS = 12;
  public static final int APOTHECARY_POUCH_HERB_SLOTS = 9;
  public static final int APOTHECARY_POUCH_INVENTORY_SLOTS = 18;

  private NBTStackHandler inventorySlots;
  private HerbNBTStackHandler herbSlots;
  private ItemStack pouch;

  public PouchHandler(ItemStack pouch) {
    this.pouch = pouch;

    if (isApoth()) {
      this.inventorySlots = new NBTStackHandler(pouch, "inventory", APOTHECARY_POUCH_INVENTORY_SLOTS);
      this.herbSlots = new HerbNBTStackHandler(pouch, "herbs", APOTHECARY_POUCH_HERB_SLOTS);
    } else {
      this.inventorySlots = new NBTStackHandler(pouch, "inventory", COMPONENT_POUCH_INVENTORY_SLOTS);
      this.herbSlots = new HerbNBTStackHandler(pouch, "herbs", COMPONENT_POUCH_HERB_SLOTS);
    }
  }

  private boolean isApoth () {
    return ((ItemPouch) pouch.getItem()).isApothecary();
  }

  public NBTStackHandler getInventory() {
    return inventorySlots;
  }

  public HerbNBTStackHandler getHerbs() {
    return herbSlots;
  }

  public static PouchHandler getHandler(ItemStack stack) {
    return new PouchHandler(stack);
  }

  public static class NBTStackHandler implements IItemHandler, IItemHandlerModifiable {
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

  public static class HerbNBTStackHandler extends NBTStackHandler {
    public HerbNBTStackHandler(ItemStack stack, String identifier, int slots) {
      super(stack, identifier, slots);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
      return HerbRegistry.isHerb(stack.getItem());
    }

    public int refill(ItemStack herbStack) {
      if (!containsHerb(herbStack.getItem())) {
        return herbStack.getCount();
      }

      Item herb = herbStack.getItem();
      int count = herbStack.getCount();

      NBTTagList tag = getTag();
      for (int i = 0; i < tag.tagCount(); i++) {
        ItemStack stack = new ItemStack(tag.getCompoundTagAt(i));
        if (stack.getItem() == herb) {
          if (stack.getCount() < stack.getMaxStackSize()) {
            int consumed = Math.min(count, stack.getMaxStackSize() - stack.getCount());
            if (consumed > 0) {
              stack.grow(consumed);
              count = Math.max(0, count - consumed);
              tag.set(i, stack.serializeNBT());
            }
          }
        }
        if (count == 0) {
          return 0;
        }
      }

      return count;
    }

    public boolean containsHerb(Item item) {
      NBTTagList tag = getTag();
      for (int i = 0; i < tag.tagCount(); i++) {
        ItemStack stack = new ItemStack(tag.getCompoundTagAt(i));
        if (stack.getItem() == item) {
          return true;
        }
      }

      return false;
    }
  }
}
