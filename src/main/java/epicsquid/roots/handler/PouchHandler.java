package epicsquid.roots.handler;

import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.item.ItemPouch;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class PouchHandler implements INBTSerializable<CompoundNBT> {
  public static final int COMPONENT_POUCH_HERB_SLOTS = 6;
  public static final int COMPONENT_POUCH_INVENTORY_SLOTS = 12;
  public static final int APOTHECARY_POUCH_HERB_SLOTS = 9;
  public static final int APOTHECARY_POUCH_INVENTORY_SLOTS = 18;

  private PouchItemHandler inventorySlots;
  private PouchHerbHandler herbSlots;
  private ItemStack pouch;

  private boolean isApoth = false;

  public PouchHandler(ItemStack pouch, int inventorySlots, int herbSlots) {
    this.pouch = pouch;
    if (inventorySlots == APOTHECARY_POUCH_INVENTORY_SLOTS) {
      isApoth = true;
    }
    this.inventorySlots = new PouchItemHandler(inventorySlots);
    this.herbSlots = new PouchHerbHandler(herbSlots);
  }

  public PouchItemHandler getInventory() {
    return inventorySlots;
  }

  public PouchHerbHandler getHerbs() {
    return herbSlots;
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT tag = new CompoundNBT();
    tag.setTag("inventory_slots", inventorySlots.serializeNBT());
    tag.setTag("herb_slots", herbSlots.serializeNBT());
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    CompoundNBT inv = nbt.getCompoundTag("inventory_slots");
    CompoundNBT herb = nbt.getCompoundTag("herb_slots");
    if (isApoth) {
      if (inv.getInteger("Size") != APOTHECARY_POUCH_INVENTORY_SLOTS) {
        inv.setInteger("Size", APOTHECARY_POUCH_INVENTORY_SLOTS);
      }
      if (herb.getInteger("Size") != APOTHECARY_POUCH_HERB_SLOTS) {
        herb.setInteger("Size", APOTHECARY_POUCH_HERB_SLOTS);
      }
    }
    inventorySlots.deserializeNBT(inv);
    herbSlots.deserializeNBT(herb);
  }

  public static PouchHandler getHandler(ItemStack stack) {
    PouchHandler handler;
    boolean isApoth = ((ItemPouch) stack.getItem()).isApothecary();
    if (isApoth) {
      handler = new PouchHandler(stack, APOTHECARY_POUCH_INVENTORY_SLOTS, APOTHECARY_POUCH_HERB_SLOTS);
    } else {
      handler = new PouchHandler(stack, COMPONENT_POUCH_INVENTORY_SLOTS, COMPONENT_POUCH_HERB_SLOTS);
    }
    if (stack.hasTagCompound()) {
      CompoundNBT tag = stack.getTagCompound();
      if (tag.hasKey("handler")) {
        handler.deserializeNBT(tag.getCompoundTag("handler"));
      }
    }

    return handler;
  }

  public void saveToStack() {
    CompoundNBT tag = pouch.getTagCompound();
    if (tag == null) {
      tag = new CompoundNBT();
      pouch.setTagCompound(tag);
    }

    tag.setTag("handler", serializeNBT());
  }

  public class PouchItemHandler extends ItemStackHandler {

    public PouchItemHandler(int size) {
      super(size);
    }

    @Override
    protected void onContentsChanged(int slot) {
      super.onContentsChanged(slot);

      PouchHandler.this.saveToStack();
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
      super.setStackInSlot(slot, stack);

      PouchHandler.this.saveToStack();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
      ItemStack result = super.insertItem(slot, stack, simulate);
      PouchHandler.this.saveToStack();
      return result;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
      ItemStack result = super.extractItem(slot, amount, simulate);

      PouchHandler.this.saveToStack();
      return result;
    }
  }

  public class PouchHerbHandler extends PouchItemHandler {
    public PouchHerbHandler(int size) {
      super(size);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
      return HerbRegistry.isHerb(stack.getItem());
    }

    public int refill (ItemStack herbStack) {
      if (!containsHerb(herbStack.getItem())) {
        return herbStack.getCount();
      }

      Item herb = herbStack.getItem();
      int count = herbStack.getCount();

      for (ItemStack stack : stacks) {
        if (stack.getItem() == herb) {
          if (stack.getCount() < stack.getMaxStackSize()) {
            int consumed = Math.min(count, stack.getMaxStackSize() - stack.getCount());
            if (consumed > 0) {
              stack.grow(consumed);
              count = Math.max(0, count - consumed);
              PouchHandler.this.saveToStack();
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
      for (ItemStack stack : stacks) {
        if (stack.getItem() == item) {
          return true;
        }
      }

      return false;
    }
  }
}
