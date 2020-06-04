/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.container;

import epicsquid.roots.container.slots.SlotLibraryInfo;
import epicsquid.roots.container.slots.SlotSpellInfo;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.ItemQuiver;
import epicsquid.roots.spell.info.StaffSpellInfo;
import epicsquid.roots.spell.info.storage.StaffSpellStorage;
import epicsquid.roots.world.data.SpellLibraryData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ContainerLibrary extends Container {

  private SpellLibraryData data;
  private ItemStack staff;
  private int staffSlot = -1;
  private int librarySlot = -1;

  public ContainerLibrary(EntityPlayer player, ItemStack staff, SpellLibraryData data) {
    this.data = data;

    if (staff.isEmpty()) {
      if (player.getHeldItemMainhand().getItem().equals(ModItems.staff)) {
        this.staff = player.getHeldItemMainhand();
      } else if (player.getHeldItemOffhand().getItem().equals(ModItems.staff)) {
        this.staff = player.getHeldItemOffhand();
      }
    } else {
      this.staff = staff;
    }

    createStaffSlots();
    createLibrarySlots();
  }

  public int getStaffSlot() {
    return staffSlot;
  }

  public int getLibrarySlot() {
    return librarySlot;
  }

  private StaffSpellInfo getInfoFor(int slot) {
    StaffSpellStorage storage = StaffSpellStorage.fromStack(staff);
    return storage.getSpellInSlot(slot);
  }

  private void createStaffSlots() {
    addSlotToContainer(new SlotSpellInfo(this::getInfoFor, 1, 2, 33)); // Spot 1
    addSlotToContainer(new SlotSpellInfo(this::getInfoFor, 2, 7, 9)); // Spot 2
    addSlotToContainer(new SlotSpellInfo(this::getInfoFor, 3, 31, 4)); // Spot 3
    addSlotToContainer(new SlotSpellInfo(this::getInfoFor, 4, 55, 9)); // Spot 4
    addSlotToContainer(new SlotSpellInfo(this::getInfoFor, 5, 60, 33)); // Spot 5
  }

  private void createLibrarySlots() {
    int offsetX = 98;
    int offsetY = 15;

    int i = 0;

    for (int y = 0; y < 5; y++) {
      for (int x = 0; x < 8; x++) {
        addSlotToContainer(new SlotLibraryInfo(data, i, offsetX + x * 18, offsetY + y * 18));
        i++;
      }
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

  @Override
  @Nonnull
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
    if (slotId != -999) {
      Slot slot = getSlot(slotId);
      if (slot instanceof SlotLibraryInfo) {
        SlotLibraryInfo info = (SlotLibraryInfo) slot;
        if (staffSlot == -1) {
          librarySlot = info.getSlot();
        } else {
          // Do the swappy-swappy
        }
      } else if (slot instanceof SlotSpellInfo) {
        SlotSpellInfo info = (SlotSpellInfo) slot;

        if (librarySlot == -1) {
          staffSlot = info.getSlot();
        } else {
          // Do the swappy-swappy
        }
      }
    }

    return super.slotClick(slotId, dragType, clickTypeIn, player);
  }

  @Override
  public void detectAndSendChanges() {
    super.detectAndSendChanges();
  }
}
