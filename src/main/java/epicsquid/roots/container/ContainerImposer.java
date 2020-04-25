/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.container;

import epicsquid.roots.tileentity.TileEntityImposer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ContainerImposer extends Container {

  private final TileEntityImposer tile;
  private final EntityPlayer player;

  public ContainerImposer(EntityPlayer player, TileEntityImposer tile) {
    this.tile = tile;
    this.player = player;

    createPlayerInventory(player.inventory);
    createImposerSlots();
  }

  private void createImposerSlots() {
    int xOffset = 47;
    int yOffset = -15;
/*    for (int i = 0; i < quiverHandler.getSlots(); i++) {
      addSlotToContainer(new SlotItemHandler(quiverHandler, i, xOffset + 11 + (((i >= 3) ? i - 3 : i) * 21), yOffset + 23 + ((i >= 3) ? 21 : 0)) {
        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
          return stack.getItem() instanceof ItemArrow;
        }
      });
    }*/
  }

  private void createPlayerInventory(InventoryPlayer inventoryPlayer) {
    int xOffset = 8;
    int yOffset = 67;

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, xOffset + j * 18, yOffset + i * 18));
      }
    }
    for (int i = 0; i < 9; i++) {
      addSlotToContainer(new Slot(inventoryPlayer, i, xOffset + i * 18, yOffset + 58));
    }
  }

  @Override
  public boolean canInteractWith(@Nonnull EntityPlayer player) {
    return true;
  }

  @Override
  @Nonnull
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
    ItemStack slotStack = ItemStack.EMPTY;

    Slot slot = inventorySlots.get(index);

    if (slot != null && slot.getHasStack()) {
      ItemStack stack = slot.getStack();

      boolean isArrow = stack.getItem() instanceof ItemArrow;

      if (isArrow && index < 36 && !mergeItemStack(stack, 36, 42, false)) {
        slot.onSlotChanged();
        //handler.saveToStack();
        return ItemStack.EMPTY;
      } else {
        //handler.saveToStack();
        return ItemStack.EMPTY;
      }
    }

    //handler.saveToStack();
    return slotStack;
  }
}
