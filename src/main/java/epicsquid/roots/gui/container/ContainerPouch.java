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
  }

  private void createPouchSlots() {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        addSlotToContainer(new SlotItemHandler(itemHandler, j + i * 9 + 9, j * 18, i * 18));
      }
    }
  }

  private void createPlayerInventory(InventoryPlayer inventoryPlayer) {

    int xOffset = 0;
    int yOffset = 0;

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
