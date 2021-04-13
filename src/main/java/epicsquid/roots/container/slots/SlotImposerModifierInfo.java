package epicsquid.roots.container.slots;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.container.ContainerImposer;
import epicsquid.roots.modifiers.IModifierCore;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstance;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.tileentity.TileEntityImposer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class SlotImposerModifierInfo extends Slot {
  private static IInventory emptyInventory = new InventoryBasic("[Null]", true, 0);
  private final IModifierProvider info;
  private final IModifierCore core;
  private final IBooleanProvider isHidden;
  private final TileEntityImposer tile;
  private final ContainerImposer container;

  public SlotImposerModifierInfo(ContainerImposer container, IBooleanProvider isHidden, IModifierProvider info, IModifierCore core, TileEntityImposer tile, int xPosition, int yPosition) {
    super(emptyInventory, 0, xPosition, yPosition);
    this.info = info;
    this.isHidden = isHidden;
    this.core = core;
    this.tile = tile;
    this.container = container;
  }

  @Nullable
  public StaffModifierInstance get() {
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

  public boolean isDisabled() {
    StaffModifierInstance info = get();
    if (info == null) {
      return false;
    }
    return !info.isEnabled();
  }

  public boolean isConflicting(StaffModifierInstanceList modifiers) {
    if (modifiers == null) {
      return false;
    }

    StaffModifierInstance info = get();
    if (info == null) {
      return false;
    }
    return info.isConflicting(modifiers);
  }

  public List<StaffModifierInstance> getConflicts(StaffModifierInstanceList modifiers) {
    if (modifiers == null) {
      return Collections.emptyList();
    }

    StaffModifierInstance info = get();
    if (info == null) {
      return Collections.emptyList();
    }

    return info.getConflicts(modifiers);
  }

  public boolean isApplied() {
    StaffModifierInstance info = get();
    if (info == null) {
      return false;
    }
    return info.isApplied();
  }

  public boolean isApplicable() {
    StaffModifierInstance info = get();
    return info != null;
  }

  @Override
  public boolean getHasStack() {
    return !getStack().isEmpty();
  }

  @Override
  public void putStack(ItemStack stack) {
    if (!stack.isEmpty()) {
      // Consume the item on the server and the client side!
      tile.addModifier(core, stack, container);
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
  public boolean isEnabled() {
    return !isHidden.get(); // && get() != null;
  }

  @Override
  public boolean isSameInventory(Slot other) {
    if (other instanceof SlotImposerModifierInfo) {
      return ((SlotImposerModifierInfo) other).core.equals(core);
    }
    return false;
  }

  @FunctionalInterface
  public interface IModifierProvider {
    @Nullable
    StaffModifierInstance get(IModifierCore core);
  }
}
