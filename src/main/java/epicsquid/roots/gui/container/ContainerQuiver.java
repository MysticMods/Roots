/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.gui.container;

import epicsquid.roots.handler.QuiverHandler;
import epicsquid.roots.item.ItemQuiver;
import epicsquid.roots.util.QuiverInventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerQuiver extends Container {

  private ItemStackHandler quiverHandler;
  private QuiverHandler handler;
  private EntityPlayer player;
  private ItemStack quiver;

  public ContainerQuiver(EntityPlayer player) {
    this.player = player;
    ItemStack main = player.getHeldItemMainhand();
    ItemStack off = player.getHeldItemOffhand();
    ItemStack first = QuiverInventoryUtil.getQuiver(player);

    ItemStack use = ItemStack.EMPTY;
    if (main.getItem() instanceof ItemQuiver) {
      use = main;
    } else if (off.getItem() instanceof ItemQuiver) {
      use = off;
    } else if (first.getItem() instanceof ItemQuiver) {
      use = first;
    }

    handler = QuiverHandler.getHandler(use);
    quiverHandler = handler.getInventory();

    this.quiver = use;

    createPlayerInventory(player.inventory);
    createComponentPouchSlots();
  }

  private void createComponentPouchSlots() {
    int xOffset = 47;
    int yOffset = -15;
    for (int i = 0; i < quiverHandler.getSlots(); i++) {
      addSlotToContainer(new SlotItemHandler(quiverHandler, i, xOffset + 11 + (((i >= 3) ? i - 3 : i) * 21), yOffset + 23 + ((i >= 3) ? 21 : 0)) {
        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
          return stack.getItem() instanceof ItemArrow;
        }
      });
    }
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
      slotStack = stack.copy();

      boolean isArrow = stack.getItem() instanceof ItemArrow;

      if (isArrow && index < 36) { // Player Inventory -> Quiver
        if (!mergeItemStack(stack, 36, 42, false)) {
          handler.saveToStack();
          return ItemStack.EMPTY;
        }
      } else {
        if (!mergeItemStack(stack, 0, 36, false)) {
          handler.saveToStack();
          return ItemStack.EMPTY;
        }
      }

      if (stack.isEmpty()) {
        handler.saveToStack();
        slot.putStack(ItemStack.EMPTY);
      }

      slot.onSlotChanged();
    }

    handler.saveToStack();
    return slotStack;
  }

  @Override
  @Nonnull
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
    if (slotId >= 0) {
      ItemStack stack = getSlot(slotId).getStack();
      if (stack.getItem() instanceof ItemQuiver) {
        return ItemStack.EMPTY;
      }
    }

    return super.slotClick(slotId, dragType, clickTypeIn, player);
  }
}
