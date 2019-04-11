/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.gui.container;

import javax.annotation.Nonnull;

import epicsquid.roots.capability.pouch.PouchItemHandler;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.item.ItemPouch;
import epicsquid.roots.util.PowderInventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerPouch extends Container {

  private PouchItemHandler itemHandler;
  private EntityPlayer player;

  public ContainerPouch(EntityPlayer player) {
    this.player = player;
    ItemStack main = player.getHeldItemMainhand();
    ItemStack off = player.getHeldItemOffhand();
    ItemStack first = PowderInventoryUtil.getPouch(player);

    ItemStack use = ItemStack.EMPTY;
    if (main.getItem() instanceof ItemPouch) {
      use = main;
    } else if (off.getItem() instanceof ItemPouch) {
      use = off;
    } else if (first.getItem() instanceof ItemPouch) {
      use = first;
    }

    itemHandler = (PouchItemHandler) use.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    createPlayerInventory(player.inventory);
    createPouchSlots();
  }

  private void createPouchSlots() {
    if (itemHandler.getHerbSlots() == PouchItemHandler.COMPONENT_POUCH_HERB_SLOTS && itemHandler.getInventorySlots() == PouchItemHandler.COMPONENT_POUCH_INVENTORY_SLOTS) {
      createComponentPouchSlots();
    } else if (itemHandler.getHerbSlots() == PouchItemHandler.APOTHECARY_POUCH_HERB_SLOTS && itemHandler.getInventorySlots() == PouchItemHandler.APOTHECARY_POUCH_INVENTORY_SLOTS) {
      createApothecaryPouchSlots();
    }
  }

  private void createComponentPouchSlots() {
    int xOffset = -13;
    int yOffset = -55;
    for (int i = 0; i < itemHandler.getSlots(); i++ ) {
      // Top Row
      if (i < 5) {
        addSlotToContainer(new SlotItemHandler(itemHandler, i, xOffset + 11 + (i * 21), yOffset + 23));
      }
      // Middle Row
      if (i >= 5 && i < 9) {
        addSlotToContainer(new SlotItemHandler(itemHandler, i, xOffset + 22 + ((i - 5) * 21), yOffset + 44));
      }
      // Bottom Row
      if (i >= 9 && i < 12) {
        addSlotToContainer(new SlotItemHandler(itemHandler, i, xOffset + 33 + ((i - 9) * 21), yOffset + 65));
      }
      // Herb Pouch
      if (i >= 12 && i < 18) {
        // Controls which row the slots appear on
        int yPosOffset = i >= 14 ? i >= 16 ? 21 * 2 : 21 : 0;
        addSlotToContainer(new SlotItemHandler(itemHandler, i, xOffset + 127 + (21 * (i % 2)), yOffset + 23 + yPosOffset));
      }
    }
  }

  private void createApothecaryPouchSlots() {
    int xOffset = -35;
    int yOffset = -63;
    for (int i = 0; i < itemHandler.getSlots(); i++) {
      // Top Row
      if (i < 6) {
        addSlotToContainer(new SlotItemHandler(itemHandler, i, xOffset + 25 + (20 * (i % 6)), yOffset + 19));
      }
      // Middle Slot
      if (i >= 6 && i < 12) {
        addSlotToContainer(new SlotItemHandler(itemHandler, i, xOffset + 25 + (20 * (i % 6)), yOffset + 43));
      }
      // Bottom Slot
      if (i >= 12 && i < 18) {
        addSlotToContainer(new SlotItemHandler(itemHandler, i, xOffset + 25 + (20 * (i % 6)), yOffset + 66));
      }
      // Add Herb Slots
      if (i >= 18 && i < 21) {
        addSlotToContainer(new SlotItemHandler(itemHandler, i, xOffset + 149 + (16 * (i % 3)), yOffset + 16 + (4 * (i % 2))));
      }
      if (i >= 21 && i < 24) {
        addSlotToContainer(new SlotItemHandler(itemHandler, i, xOffset + 149 + (16 * (i % 3)), yOffset + 39 + (4 * ((i + 1) % 2))));
      }
      if (i >= 24 && i < 27) {
        addSlotToContainer(new SlotItemHandler(itemHandler, i, xOffset + 149 + (16 * (i % 3)), yOffset + 64 + (4 * (i % 2))));
      }
    }
  }

  private void createPlayerInventory(InventoryPlayer inventoryPlayer) {

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
  public boolean canInteractWith(@Nonnull EntityPlayer player) {
    return true;
  }

  @Override
  @Nonnull
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
    ItemStack stack = inventorySlots.get(index).getStack();
    if (!stack.isEmpty()) {
      ItemStack copyStack = stack.copy();
      if (index < 36) {
        if (HerbRegistry.containsHerbItem(stack.getItem())) {
          for (int i = itemHandler.getInventorySlots(); i < itemHandler.getInventorySlots() + itemHandler.getHerbSlots(); i++) {
            if (itemHandler.insertItem(i, copyStack, true).isEmpty()) {
              stack.shrink(stack.getCount());
              return itemHandler.insertItem(i, copyStack, false);
            }
          }
        }
        for (int i = 0; i < itemHandler.getInventorySlots(); i++) {
          if (itemHandler.insertItem(i, copyStack, true).isEmpty()) {
            stack.shrink(stack.getCount());
            return itemHandler.insertItem(i, copyStack, false);
          }
        }
      } else {
        for (int i = 0; i < 36; i++) {
          Slot slot = inventorySlots.get(i);
          if (slot.getStack().isEmpty()) {
            slot.putStack(copyStack);
            stack.shrink(stack.getCount());
            return ItemStack.EMPTY;
          }
        }
      }
    }
    return ItemStack.EMPTY;
  }

  @Override
  @Nonnull
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
    if (slotId >= 0) {
      ItemStack stack = getSlot(slotId).getStack();
      if (stack.getItem() instanceof ItemPouch) {
        return ItemStack.EMPTY;
      }
      return super.slotClick(slotId, dragType, clickTypeIn, player);
    }
    return ItemStack.EMPTY;
  }

  public int getHerbSlots() {
    return itemHandler.getHerbSlots();
  }

  public int getInvSlots() {
    return itemHandler.getInventorySlots();
  }
}
