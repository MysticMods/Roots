package epicsquid.roots.container.slots;

import epicsquid.roots.modifiers.instance.ModifierInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class SlotModifierInfo extends Slot {
  private static IInventory emptyInventory = new InventoryBasic("[Null]", true, 0);
  private Supplier<ModifierInstance> info;

  public SlotModifierInfo(Supplier<ModifierInstance> info, int xPosition, int yPosition) {
    super(emptyInventory, 0, xPosition, yPosition);
    this.info = info;
  }

  @Override
  public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean isItemValid(ItemStack stack) {
    return false;
  }

  // TODO
  @Override
  public ItemStack getStack() {
    ModifierInstance info = this.info.get();
    if (info == null) {
      return ItemStack.EMPTY;
    }
    return info.getActualItem();
  }

  @Override
  public boolean getHasStack() {
    return info.get() != null;
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

  // TODO?
  @Nullable
  @Override
  public String getSlotTexture() {
    return super.getSlotTexture();
  }

  @Override
  public ItemStack decrStackSize(int amount) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canTakeStack(EntityPlayer playerIn) {
    return false;
  }

  private boolean enabled = true;

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public boolean isSameInventory(Slot other) {
    if (other instanceof SlotModifierInfo) {
      return ((SlotModifierInfo) other).info.get().equals(info.get());
    }
    return false;
  }
}
