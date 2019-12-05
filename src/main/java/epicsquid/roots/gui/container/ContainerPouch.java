/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.gui.container;

import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.handler.PouchHandler;
import epicsquid.roots.item.Pouch;
import epicsquid.roots.util.PowderInventoryUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerPouch extends Container {

  private PouchHandler.PouchItemHandler inventoryHandler;
  private PouchHandler.PouchItemHandler herbsHandler;
  private PouchHandler handler;
  private PlayerEntity player;
  private ItemStack pouch;

  public boolean componentPouch = false;

  private int inventoryEnd;
  private int herbsEnd;

  public ContainerPouch(PlayerEntity player) {
    this.player = player;
    ItemStack main = player.getHeldItemMainhand();
    ItemStack off = player.getHeldItemOffhand();
    ItemStack first = PowderInventoryUtil.getPouch(player);

    ItemStack use = ItemStack.EMPTY;
    if (main.getItem() instanceof Pouch) {
      use = main;
    } else if (off.getItem() instanceof Pouch) {
      use = off;
    } else if (first.getItem() instanceof Pouch) {
      use = first;
    }

    handler = PouchHandler.getHandler(use);
    inventoryHandler = handler.getInventory();
    herbsHandler = handler.getHerbs();

    this.pouch = use;

    createPlayerInventory(player.inventory);
    createPouchSlots();
  }

  private void createPouchSlots() {
    if (pouch.getItem() == ModItems.component_pouch) {
      createComponentPouchSlots();
      componentPouch = true;
    } else {
      createApothecaryPouchSlots();
    }
  }

  private void createComponentPouchSlots() {
    int xOffset = -13;
    int yOffset = -55;
    int q = 0;
    for (int i = 0; i < inventoryHandler.getSlots(); i++) {
      // Top Row
      if (i < 5) {
        addSlotToContainer(new SlotItemHandler(inventoryHandler, q++, xOffset + 11 + (i * 21), yOffset + 23));
      }
      // Middle Row
      if (i >= 5 && i < 9) {
        addSlotToContainer(new SlotItemHandler(inventoryHandler, q++, xOffset + 22 + ((i - 5) * 21), yOffset + 44));
      }
      // Bottom Row
      if (i >= 9 && i < 12) {
        addSlotToContainer(new SlotItemHandler(inventoryHandler, q++, xOffset + 33 + ((i - 9) * 21), yOffset + 65));
      }
      // Herb Pouch
    }
    inventoryEnd = q;
    for (int i = 0; i < herbsHandler.getSlots(); i++) {
      if (q >= 12 && q < 18) {
        // Controls which row the slots appear on
        int yPosOffset = q >= 14 ? q >= 16 ? 21 * 2 : 21 : 0;
        addSlotToContainer(new SlotItemHandler(herbsHandler, i, xOffset + 127 + (21 * (q % 2)), yOffset + 23 + yPosOffset));
        q++;
      }
    }
    herbsEnd = q;
  }

  private void createApothecaryPouchSlots() {
    int xOffset = -35;
    int yOffset = -63;
    int q = 0;
    for (int i = 0; i < inventoryHandler.getSlots(); i++) {
      // Top Row
      if (i < 6) {
        addSlotToContainer(new SlotItemHandler(inventoryHandler, q, xOffset + 25 + (20 * (q % 6)), yOffset + 19));
      }
      // Middle Slot
      if (i >= 6 && i < 12) {
        addSlotToContainer(new SlotItemHandler(inventoryHandler, q, xOffset + 25 + (20 * (q % 6)), yOffset + 43));
      }
      // Bottom Slot
      if (i >= 12 && i < 18) {
        addSlotToContainer(new SlotItemHandler(inventoryHandler, q, xOffset + 25 + (20 * (q % 6)), yOffset + 66));
      }
      q++;
    }
    inventoryEnd = q;
    for (int i = 0; i < herbsHandler.getSlots(); i++) {
      // Add Herb Slots
      q = inventoryEnd + i;
      if (q >= 18 && q < 21) {
        addSlotToContainer(new SlotItemHandler(herbsHandler, i, xOffset + 149 + (16 * (q % 3)), yOffset + 16 + (4 * (q % 2))));
      }
      if (q >= 21 && q < 24) {
        addSlotToContainer(new SlotItemHandler(herbsHandler, i, xOffset + 149 + (16 * (q % 3)), yOffset + 39 + (4 * ((q + 1) % 2))));
      }
      if (q >= 24 && q < 27) {
        addSlotToContainer(new SlotItemHandler(herbsHandler, i, xOffset + 149 + (16 * (q % 3)), yOffset + 64 + (4 * (q % 2))));
      }
    }
    herbsEnd = q + 1;
  }

  private void createPlayerInventory(PlayerInventory inventoryPlayer) {

    int xOffset = -5;
    int yOffset = 70;

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
  public boolean canInteractWith(@Nonnull PlayerEntity player) {
    return true;
  }

  @Override
  @Nonnull
  public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
    ItemStack slotStack = ItemStack.EMPTY;

    Slot slot = inventorySlots.get(index);
    int herbStart = inventoryEnd + 36;
    int herbStop = herbsEnd + 36;

    if (slot != null && slot.getHasStack()) {
      ItemStack stack = slot.getStack();
      slotStack = stack.copy();

      boolean herb = HerbRegistry.isHerb(slotStack.getItem());

      if (index < 36) { // Player Inventory -> Inventory/herbs
        if (herb && !mergeItemStack(stack, herbStart, herbStop, false)) {
          handler.saveToStack();
          return ItemStack.EMPTY;
        } else if (!herb && !mergeItemStack(stack, 36, herbStart, false)) {
          handler.saveToStack();
          return ItemStack.EMPTY;
        }
      } else {
        if (!mergeItemStack(stack, 27, 36, false) && !mergeItemStack(stack, 0, 27, false)) {
          handler.saveToStack();
          return ItemStack.EMPTY;
        }
      }

      if (stack.isEmpty()) {
        slot.putStack(ItemStack.EMPTY);
        handler.saveToStack();
      }

      slot.onSlotChanged();
    }

    handler.saveToStack();
    return slotStack;
  }

  @Override
  @Nonnull
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
    if (slotId >= 0) {
      ItemStack stack = getSlot(slotId).getStack();
      if (stack.getItem() instanceof Pouch) {
        return ItemStack.EMPTY;
      }
    }

    return super.slotClick(slotId, dragType, clickTypeIn, player);
  }
}
