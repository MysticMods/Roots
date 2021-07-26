/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.container;

import epicsquid.roots.recipe.FeyCraftingRecipe;
import epicsquid.roots.tileentity.TileEntityFeyCrafter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class ContainerFeyCrafter extends Container {

  private TileEntityFeyCrafter crafter;
  private EntityPlayer player;

  private FeyCraftingRecipe recipe;

  private final InventoryCraftResult craftResult;

  public ContainerFeyCrafter(EntityPlayer player, TileEntityFeyCrafter crafter) {
    this.player = player;
    this.crafter = crafter;

    craftResult = new InventoryCraftResult();

    createCrafterSlots();
    createPlayerInventory(player.inventory);
  }

  private void createCrafterSlots() {
    addSlotToContainer(new SlotGrid(crafter.inventory, this, 0, 54, 12));
    addSlotToContainer(new SlotGrid(crafter.inventory, this, 1, 29, 30));
    addSlotToContainer(new SlotGrid(crafter.inventory, this, 2, 79, 30));
    addSlotToContainer(new SlotGrid(crafter.inventory, this, 3, 38, 59));
    addSlotToContainer(new SlotGrid(crafter.inventory, this, 4, 70, 59));
  }

  private void createPlayerInventory(InventoryPlayer inventoryPlayer) {
    int xOffset = 8;
    int yOffset = 105;

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        addSlotToContainer(new Slot(inventoryPlayer, (j + i * 9 + 9), xOffset + j * 18, yOffset + i * 18));
      }
    }
    for (int i = 0; i < 9; i++) {
      addSlotToContainer(new Slot(inventoryPlayer, i, xOffset + i * 18, yOffset + 58));
    }
  }

  @Nonnull
  @Override
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
    Slot slot = this.inventorySlots.get(index);

    if (slot == null || !slot.getHasStack()) {
      return ItemStack.EMPTY;
    }

    ItemStack original = slot.getStack().copy();
    ItemStack itemstack = slot.getStack().copy();

    // slot that was clicked on not empty?
    int end = this.inventorySlots.size();

    // Is it a slot in the main inventory? (aka not player inventory)
    if (index < 5) {
      // try to put it into the player inventory (if we have a player inventory)
      if (!this.mergeItemStack(itemstack, 5, end, true)) {
        return ItemStack.EMPTY;
      }
    }
    // Slot is in the player inventory (if it exists), transfer to main inventory
    else if (!this.mergeItemStack(itemstack, 0, 5, false)) {
      return ItemStack.EMPTY;
    }

    slot.onSlotChanged();

    if (itemstack.getCount() == original.getCount()) {
      return ItemStack.EMPTY;
    }

    // update slot we pulled from
    slot.putStack(itemstack);
    slot.onTake(player, itemstack);

    if (slot.getHasStack() && slot.getStack().isEmpty()) {
      slot.putStack(ItemStack.EMPTY);
    }

    return original;
  }

  // only refills items that are already present
  protected boolean mergeItemStackRefill(@Nonnull ItemStack stack, int startIndex, int endIndex, boolean useEndIndex) {
    if (stack.getCount() <= 0) {
      return false;
    }

    boolean flag1 = false;
    int k = startIndex;

    if (useEndIndex) {
      k = endIndex - 1;
    }

    Slot slot;
    ItemStack itemstack1;

    if (stack.isStackable()) {
      while (stack.getCount() > 0 && (!useEndIndex && k < endIndex || useEndIndex && k >= startIndex)) {
        slot = this.inventorySlots.get(k);
        itemstack1 = slot.getStack();

        if (!itemstack1.isEmpty() && itemstack1.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == itemstack1.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, itemstack1) && this.canMergeSlot(stack, slot)) {
          int l = itemstack1.getCount() + stack.getCount();
          int limit = Math.min(stack.getMaxStackSize(), slot.getItemStackLimit(stack));

          if (l <= limit) {
            stack.setCount(0);
            itemstack1.setCount(l);
            slot.onSlotChanged();
            flag1 = true;
          } else if (itemstack1.getCount() < limit) {
            stack.shrink(limit - itemstack1.getCount());
            itemstack1.setCount(limit);
            slot.onSlotChanged();
            flag1 = true;
          }
        }

        if (useEndIndex) {
          --k;
        } else {
          ++k;
        }
      }
    }

    return flag1;
  }

  // only moves items into empty slots
  protected boolean mergeItemStackMove(@Nonnull ItemStack stack, int startIndex, int endIndex, boolean useEndIndex) {
    if (stack.getCount() <= 0) {
      return false;
    }

    boolean flag1 = false;
    int k;

    if (useEndIndex) {
      k = endIndex - 1;
    } else {
      k = startIndex;
    }

    while (!useEndIndex && k < endIndex || useEndIndex && k >= startIndex) {
      Slot slot = this.inventorySlots.get(k);
      ItemStack itemstack1 = slot.getStack();

      if (itemstack1.isEmpty() && slot.isItemValid(stack) && this.canMergeSlot(stack, slot)) // Forge: Make sure to respect isItemValid in the slot.
      {
        int limit = slot.getItemStackLimit(stack);
        ItemStack stack2 = stack.copy();
        if (stack2.getCount() > limit) {
          stack2.setCount(limit);
          stack.shrink(limit);
        } else {
          stack.setCount(0);
        }
        slot.putStack(stack2);
        slot.onSlotChanged();
        flag1 = true;

        if (stack.isEmpty()) {
          break;
        }
      }

      if (useEndIndex) {
        --k;
      } else {
        ++k;
      }
    }


    return flag1;
  }

  @Override
  public boolean canMergeSlot(ItemStack p_94530_1_, Slot p_94530_2_) {
    return p_94530_2_.inventory != this.craftResult && super.canMergeSlot(p_94530_1_, p_94530_2_);
  }

  @Override
  public void onCraftMatrixChanged(IInventory inventoryIn) {
    this.slotChangedCraftingGrid(this.player.world, this.player, null, this.craftResult);
  }

  // Fix for a vanilla bug: doesn't take Slot.getMaxStackSize into account
  @Override
  protected boolean mergeItemStack(@Nonnull ItemStack stack, int startIndex, int endIndex, boolean useEndIndex) {
    boolean ret = mergeItemStackRefill(stack, startIndex, endIndex, useEndIndex);
    if (!stack.isEmpty() && stack.getCount() > 0) {
      ret |= mergeItemStackMove(stack, startIndex, endIndex, useEndIndex);
    }
    return ret;
  }

  @Override
  protected void slotChangedCraftingGrid(World world, EntityPlayer player, InventoryCrafting inv, InventoryCraftResult result) {
    List<ItemStack> items = crafter.getContents();

    if (recipe == null || !recipe.matches(items)) {
      recipe = crafter.getRecipe();
    }
  }

  public boolean getValidStone() {
    return crafter.hasValidGroveStone();
  }

  public FeyCraftingRecipe getRecipe() {
    return recipe;
  }

  @Override
  public boolean canInteractWith(@Nonnull EntityPlayer player) {
    BlockPos pos = this.crafter.getPos();
    if (this.crafter.getWorld().getTileEntity(pos) != this.crafter) {
      return false;
    } else {
      return player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
    }
  }

  public static class SlotGrid extends SlotItemHandler {
    Container eventHandler;

    public SlotGrid(IItemHandler itemHandler, Container eventHandler, int index, int xPosition, int yPosition) {
      super(itemHandler, index, xPosition, yPosition);
      this.eventHandler = eventHandler;
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int amount) {
      ItemStack result = super.decrStackSize(amount);
      eventHandler.onCraftMatrixChanged(null);
      return result;
    }

    @Override
    public void putStack(@Nonnull ItemStack stack) {
      super.putStack(stack);
      eventHandler.onCraftMatrixChanged(null);
    }
  }
}
