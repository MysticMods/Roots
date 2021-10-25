package epicsquid.roots.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.*;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class FakeContainer extends Container {
  private final static NonNullList<ItemStack> INVENTORY = NonNullList.create();
  private static IInventory emptyInventory = new Inventory("[Null]", true, 0);
  private final static net.minecraft.inventory.container.Slot SLOT = new net.minecraft.inventory.container.Slot(emptyInventory, 0, 0, 0);

  @Override
  public boolean canInteractWith(PlayerEntity entityPlayer) {
    return true;
  }

  public FakeContainer() {
    super();
  }

  @Override
  protected net.minecraft.inventory.container.Slot addSlotToContainer(net.minecraft.inventory.container.Slot slotIn) {
    return slotIn;
  }

  @Override
  public void addListener(IContainerListener listener) {
  }

  @Override
  public NonNullList<ItemStack> getInventory() {
    return INVENTORY;
  }

  @Override
  public void removeListener(net.minecraft.inventory.container.IContainerListener listener) {
  }

  @Override
  public void detectAndSendChanges() {
  }

  @Override
  public boolean enchantItem(PlayerEntity playerIn, int id) {
    return false;
  }

  @Nullable
  @Override
  public Slot getSlotFromInventory(IInventory inv, int slotIn) {
    return SLOT;
  }

  @Override
  public net.minecraft.inventory.container.Slot getSlot(int slotId) {
    return SLOT;
  }

  @Override
  public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
    return ItemStack.EMPTY;
  }

  @Override
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canMergeSlot(ItemStack stack, net.minecraft.inventory.container.Slot slotIn) {
    return false;
  }

  @Override
  public void onContainerClosed(PlayerEntity playerIn) {
  }

  @Override
  protected void clearContainer(PlayerEntity playerIn, World worldIn, IInventory inventoryIn) {
  }

  @Override
  public void onCraftMatrixChanged(IInventory inventoryIn) {
  }

  @Override
  public void putStackInSlot(int slotID, ItemStack stack) {
  }

  @Override
  public void setAll(List<ItemStack> p_190896_1_) {
  }

  @Override
  public void updateProgressBar(int id, int data) {
  }

  @Override
  public boolean getCanCraft(PlayerEntity player) {
    return false;
  }

  @Override
  public void setCanCraft(PlayerEntity player, boolean canCraft) {
  }

  @Override
  protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
    return false;
  }

  @Override
  protected void resetDrag() {
  }

  @Override
  public boolean canDragIntoSlot(net.minecraft.inventory.container.Slot slotIn) {
    return false;
  }

  @Override
  protected void slotChangedCraftingGrid(World p_192389_1_, PlayerEntity p_192389_2_, CraftingInventory p_192389_3_, CraftResultInventory p_192389_4_) {
  }
}
