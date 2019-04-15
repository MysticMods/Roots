package epicsquid.roots.inventory;

import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class PouchHandler implements INBTSerializable<NBTTagCompound> {
  public static final int COMPONENT_POUCH_HERB_SLOTS = 6;
  public static final int COMPONENT_POUCH_INVENTORY_SLOTS = 12;
  public static final int APOTHECARY_POUCH_HERB_SLOTS = 9;
  public static final int APOTHECARY_POUCH_INVENTORY_SLOTS = 18;

  private PouchItemHandler inventorySlots;
  private PouchItemHandler herbSlots;
  private ItemStack pouch;

  public PouchHandler (ItemStack pouch, int inventorySlots, int herbSlots) {
    this.pouch = pouch;
    this.inventorySlots = new PouchItemHandler(inventorySlots);
    this.herbSlots = new PouchItemHandler(herbSlots) {
      @Override
      public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return HerbRegistry.containsHerbItem(stack.getItem());
      }
    };
  }

  public PouchItemHandler getInventory () {
    return inventorySlots;
  }

  public PouchItemHandler getHerbs () {
    return herbSlots;
  }

  @Override
  public NBTTagCompound serializeNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setTag("inventory_slots", inventorySlots.serializeNBT());
    tag.setTag("herb_slots", herbSlots.serializeNBT());
    return tag;
  }

  @Override
  public void deserializeNBT(NBTTagCompound nbt) {
    inventorySlots.deserializeNBT(nbt.getCompoundTag("inventory_slots"));
    herbSlots.deserializeNBT(nbt.getCompoundTag("herb_slots"));
  }

  public static PouchHandler getHandler (ItemStack stack) {
    PouchHandler handler;
    if (stack.getItem() == ModItems.component_pouch) {
      handler = new PouchHandler(stack, COMPONENT_POUCH_INVENTORY_SLOTS, COMPONENT_POUCH_HERB_SLOTS);
    } else if (stack.getItem() == ModItems.apothecary_pouch) {
      handler = new PouchHandler(stack, APOTHECARY_POUCH_INVENTORY_SLOTS, APOTHECARY_POUCH_HERB_SLOTS);
    } else {
      return null;
    }
    if (stack.hasTagCompound()) {
      NBTTagCompound tag = stack.getTagCompound();
      if (tag.hasKey("inventory")) {
        handler.deserializeNBT(tag.getCompoundTag("inventory"));
      }
    }

    return handler;
  }

  public void saveToStack () {
    NBTTagCompound tag = pouch.getTagCompound();
    if (tag == null) {
      tag = new NBTTagCompound();
      pouch.setTagCompound(tag);
    }

    tag.setTag("inventory", serializeNBT());
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
  }
}
