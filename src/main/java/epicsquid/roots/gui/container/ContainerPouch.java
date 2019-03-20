/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.gui.container;

import javax.annotation.Nonnull;

import epicsquid.roots.capability.pouch.PouchItemHandler;
import epicsquid.roots.item.ItemPouch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerPouch extends Container {

  private PouchItemHandler itemHandler;
  private EntityPlayer player;

  public ContainerPouch(@Nonnull ItemStack stack, @Nonnull InventoryPlayer playerInv) {
    this.player = playerInv.player;
    if (stack.getItem() instanceof ItemPouch) {
      itemHandler = (PouchItemHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    }
    createPouchSlots();
    createPlayerInventory(playerInv);
  }

  private void createPouchSlots() {
    if (itemHandler.getHerbSlots() == PouchItemHandler.COMPONENT_POUCH_HERB_SLOTS && itemHandler.getInventorySlots() == PouchItemHandler.COMPONENT_POUCH_INVENTORY_SLOTS) {
      createComponentPouchSlots();
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
}
