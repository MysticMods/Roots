/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.container;

import epicsquid.roots.handler.QuiverHandler;
import epicsquid.roots.item.ItemQuiver;
import epicsquid.roots.util.QuiverInventoryUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerQuiver extends Container {

  private ItemStackHandler quiverHandler;
  private QuiverHandler handler;
  private PlayerEntity player;
  private ItemStack quiver;

  public ContainerQuiver(PlayerEntity player) {
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
          return stack.getItem() instanceof ArrowItem;
        }
      });
    }
  }

  private void createPlayerInventory(PlayerInventory inventoryPlayer) {
    int xOffset = 8;
    int yOffset = 67;

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        addSlotToContainer(new net.minecraft.inventory.container.Slot(inventoryPlayer, j + i * 9 + 9, xOffset + j * 18, yOffset + i * 18));
      }
    }
    for (int i = 0; i < 9; i++) {
      addSlotToContainer(new net.minecraft.inventory.container.Slot(inventoryPlayer, i, xOffset + i * 18, yOffset + 58));
    }
  }

  @Override
  public boolean canInteractWith(@Nonnull PlayerEntity player) {
    return true;
  }

  @Override
  @Nonnull
  public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
    ItemStack slotStack = ItemStack.EMPTY;

    Slot slot = inventorySlots.get(index);

    if (slot != null && slot.getHasStack()) {
      ItemStack stack = slot.getStack();

      boolean isArrow = stack.getItem() instanceof ArrowItem;

      if (isArrow && index < 36 && !mergeItemStack(stack, 36, 42, false)) {
        slot.onSlotChanged();
        handler.saveToStack();
        return ItemStack.EMPTY;
      } else {
        handler.saveToStack();
        return ItemStack.EMPTY;
      }
    }

    handler.saveToStack();
    return slotStack;
  }

  @Override
  @Nonnull
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
    if (slotId >= 0) {
      ItemStack stack = getSlot(slotId).getStack();
      if (stack.getItem() instanceof ItemQuiver) {
        return ItemStack.EMPTY;
      }
    }

    return super.slotClick(slotId, dragType, clickTypeIn, player);
  }
}
