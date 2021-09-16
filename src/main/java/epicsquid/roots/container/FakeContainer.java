package epicsquid.roots.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class FakeContainer extends Container {
  private final static NonNullList<ItemStack> INVENTORY = NonNullList.create();
  private static IInventory emptyInventory = new InventoryBasic("[Null]", true, 0);
  private final static Slot SLOT = new Slot(emptyInventory, 0, 0, 0);

  @Override
  public boolean canInteractWith(EntityPlayer entityPlayer) {
    return true;
  }

  public FakeContainer() {
    super();
  }

  @Override
  protected Slot addSlotToContainer(Slot slotIn) {
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
  public void removeListener(IContainerListener listener) {
  }

  @Override
  public void detectAndSendChanges() {
  }

  @Override
  public boolean enchantItem(EntityPlayer playerIn, int id) {
    return false;
  }

  @Nullable
  @Override
  public Slot getSlotFromInventory(IInventory inv, int slotIn) {
    return SLOT;
  }

  @Override
  public Slot getSlot(int slotId) {
    return SLOT;
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
    return ItemStack.EMPTY;
  }

  @Override
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
    return false;
  }

  @Override
  public void onContainerClosed(EntityPlayer playerIn) {
  }

  @Override
  protected void clearContainer(EntityPlayer playerIn, World worldIn, IInventory inventoryIn) {
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
  public boolean getCanCraft(EntityPlayer player) {
    return false;
  }

  @Override
  public void setCanCraft(EntityPlayer player, boolean canCraft) {
  }

  @Override
  protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
    return false;
  }

  @Override
  protected void resetDrag() {
  }

  @Override
  public boolean canDragIntoSlot(Slot slotIn) {
    return false;
  }

  @Override
  protected void slotChangedCraftingGrid(World p_192389_1_, EntityPlayer p_192389_2_, InventoryCrafting p_192389_3_, InventoryCraftResult p_192389_4_) {
  }
}
