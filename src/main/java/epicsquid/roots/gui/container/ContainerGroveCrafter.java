/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.gui.container;

import epicsquid.roots.tileentity.TileEntityGroveCrafter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ContainerGroveCrafter extends Container {

  private TileEntityGroveCrafter crafter;
  private EntityPlayer player;

  public ContainerGroveCrafter(EntityPlayer player, TileEntityGroveCrafter crafter) {
    this.player = player;
    this.crafter = crafter;

    createPlayerInventory(player.inventory);
    createCrafterSlots();
  }

  private void createCrafterSlots() {
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
    ItemStack slotStack = ItemStack.EMPTY;
    return slotStack;
  }
}
