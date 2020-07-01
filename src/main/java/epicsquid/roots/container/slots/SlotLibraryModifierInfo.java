package epicsquid.roots.container.slots;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstance;
import epicsquid.roots.modifiers.IModifierCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class SlotLibraryModifierInfo extends Slot {
  private static IInventory emptyInventory = new InventoryBasic("[Null]", true, 0);
  private final IModifierProvider info;
  private final IModifierCore core;
  private final IBooleanProvider isHidden;

  public SlotLibraryModifierInfo(IBooleanProvider isHidden, IModifierProvider info, IModifierCore core, int xPosition, int yPosition) {
    super(emptyInventory, 0, xPosition, yPosition);
    this.info = info;
    this.isHidden = isHidden;
    this.core = core;
  }

  @Nullable
  public StaffModifierInstance get () {
    return info.get(core);
  }

  public IModifierCore getCore() {
    return core;
  }

  @Override
  public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean isItemValid(ItemStack stack) {
    StaffModifierInstance info = get();
    if (info == null) {
      return false;
    }

    if (info.isApplied()) {
      return false;
    }

    return ItemUtil.equalWithoutSize(stack, info.getStack());
  }

  // TODO
  @Override
  public ItemStack getStack() {
    StaffModifierInstance info = get();
    if (info == null) {
      return ItemStack.EMPTY;
    }
    if (info.isApplied()) {
      return info.getStack();
    } else {
      return ItemStack.EMPTY;
    }
  }

  public boolean isDisabled () {
    StaffModifierInstance info = get();
    if (info == null) {
      return false;
    }
    return !info.isEnabled();
  }

  public boolean isApplied () {
    StaffModifierInstance info = get();
    if (info == null) {
      return false;
    }
    return info.isApplied();
  }

  public boolean isApplicable () {
    StaffModifierInstance info = get();
    return info != null;
  }

  @Override
  public boolean getHasStack() {
    return !getStack().isEmpty();
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
  public boolean isEnabled() {
    return !isHidden.get();
  }

  @Override
  public boolean isSameInventory(Slot other) {
    if (other instanceof SlotLibraryModifierInfo) {
      return ((SlotLibraryModifierInfo) other).core.equals(core);
    }
    return false;
  }

  @FunctionalInterface
  public interface IModifierProvider {
    @Nullable
    StaffModifierInstance get(IModifierCore core);
  }
}
