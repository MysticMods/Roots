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

  private ItemStackHandler inventorySlots;
  private ItemStackHandler herbSlots;

  public PouchHandler (int inventorySlots, int herbSlots) {
    this.inventorySlots = new ItemStackHandler(inventorySlots);
    this.herbSlots = new ItemStackHandler(herbSlots) {
      @Override
      public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return HerbRegistry.containsHerbItem(stack.getItem());
      }
    };
  }

  public ItemStackHandler getInventory () {
    return inventorySlots;
  }

  public ItemStackHandler getHerbs () {
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
      handler = new PouchHandler(COMPONENT_POUCH_INVENTORY_SLOTS, COMPONENT_POUCH_HERB_SLOTS);
    } else if (stack.getItem() == ModItems.apothecary_pouch) {
      handler = new PouchHandler(APOTHECARY_POUCH_INVENTORY_SLOTS, APOTHECARY_POUCH_HERB_SLOTS);
    } else {
      return null;
    }
    if (stack.hasTagCompound()) {
      handler.deserializeNBT(stack.getTagCompound());
    }

    return handler;
  }
}
