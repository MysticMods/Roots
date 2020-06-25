/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.container;

import epicsquid.roots.container.slots.SlotLibraryInfo;
import epicsquid.roots.container.slots.SlotLibraryModifierInfo;
import epicsquid.roots.container.slots.SlotSpellInfo;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstance;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.modifiers.modifier.IModifierCore;
import epicsquid.roots.modifiers.modifier.ModifierCores;
import epicsquid.roots.spell.info.LibrarySpellInfo;
import epicsquid.roots.spell.info.StaffSpellInfo;
import epicsquid.roots.spell.info.storage.StaffSpellStorage;
import epicsquid.roots.util.PlayerSyncUtil;
import epicsquid.roots.world.data.SpellLibraryData;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ContainerLibrary extends Container {

  private SpellLibraryData data;
  private final Supplier<ItemStack> staff;
  private int staffSlot = -1;
  private int librarySlot = -1;
  private boolean isServer;

  private int slot;

  public ContainerLibrary(EntityPlayer player, Supplier<ItemStack> staff, SpellLibraryData data) {
    this.data = data;

    if (staff == null) {
      throw new IllegalArgumentException("ContainerLibrary initialization: staff cannot be null.");
    }

    this.staff = staff;

    this.isServer = data == null;

    createStaffSlots();
    createLibrarySlots();
    createModifierSlots();
  }

  @Nullable
  public StaffSpellStorage getSpellStorage() {
    return StaffSpellStorage.fromStack(staff.get());
  }

  public int getSpellSlot() {
    return slot;
  }

  public boolean isSelectSpell() {
    return slot == 0;
  }

  public void reset () {
    staffSlot = -1;
    librarySlot = -1;
  }

  public void setSelectSpell() {
    slot = 0;
  }

  public int getStaffSlot() {
    return staffSlot;
  }

  public int getLibrarySlot() {
    return librarySlot;
  }

  private StaffSpellInfo getInfoFor(int slot) {
    StaffSpellStorage storage = getSpellStorage();
    if (storage == null) {
      return null;
    }
    return storage.getSpellInSlot(slot);
  }

  @Nullable
  private StaffModifierInstance getInstanceFor(IModifierCore core) {
    if (slot == 0) {
      return null;
    }
    StaffSpellStorage storage = getSpellStorage();
    if (storage == null) {
      return null;
    }
    StaffSpellInfo info = storage.getSpellInSlot(slot);
    if (info == null) {
      return null;
    }
    StaffModifierInstanceList mods = info.getModifiers();
    if (mods == null) {
      return null;
    }
    return mods.getByCore(core);
  }

  private void createModifierSlots() {
    addSlotToContainer(new SlotLibraryModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.PERESKIA, 120, 18));
    addSlotToContainer(new SlotLibraryModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.WILDROOT, 120, 43));
    addSlotToContainer(new SlotLibraryModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.TERRA_MOSS, 120, 68));
    addSlotToContainer(new SlotLibraryModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.INFERNAL_BULB, 120, 93));
    addSlotToContainer(new SlotLibraryModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.DEWGONIA, 120, 118));
    addSlotToContainer(new SlotLibraryModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.WILDEWHEET, 95, 38));
    addSlotToContainer(new SlotLibraryModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.SPIRIT_HERB, 95, 73));
    addSlotToContainer(new SlotLibraryModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.CLOUD_BERRY, 95, 108));
    addSlotToContainer(new SlotLibraryModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.REDSTONE, 70, 38));
    addSlotToContainer(new SlotLibraryModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.GUNPOWDER, 70, 73));
    addSlotToContainer(new SlotLibraryModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.GLOWSTONE, 70, 108));
    addSlotToContainer(new SlotLibraryModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.MOONGLOW_LEAF, 145, 38));
    addSlotToContainer(new SlotLibraryModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.BAFFLE_CAP, 145, 73));
    addSlotToContainer(new SlotLibraryModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.STALICRIPE, 145, 108));
    addSlotToContainer(new SlotLibraryModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.NETHER_WART, 170, 38));
    addSlotToContainer(new SlotLibraryModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.BLAZE_POWDER, 170, 73));
    addSlotToContainer(new SlotLibraryModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.RUNIC_DUST, 170, 108));
  }

  private void createStaffSlots() {
    addSlotToContainer(new SlotSpellInfo(this::getInfoFor, this::isSelectSpell, 1, 2, 33)); // Spot 1
    addSlotToContainer(new SlotSpellInfo(this::getInfoFor, this::isSelectSpell, 2, 7, 9)); // Spot 2
    addSlotToContainer(new SlotSpellInfo(this::getInfoFor, this::isSelectSpell, 3, 31, 4)); // Spot 3
    addSlotToContainer(new SlotSpellInfo(this::getInfoFor, this::isSelectSpell, 4, 55, 9)); // Spot 4
    addSlotToContainer(new SlotSpellInfo(this::getInfoFor, this::isSelectSpell, 5, 60, 33)); // Spot 5
  }

  private void createLibrarySlots() {
    int offsetX = 98;
    int offsetY = 15;

    int i = 0;

    for (int y = 0; y < 5; y++) {
      for (int x = 0; x < 8; x++) {
        addSlotToContainer(SlotLibraryInfo.create(data, this::isSelectSpell, i, offsetX + x * 18, offsetY + y * 18));
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
    Slot slot = inventorySlots.get(index);

    if (slot != null && slot.getHasStack()) {
      return slot.getStack();
    }

    return ItemStack.EMPTY;
  }

  @Override
  @Nonnull
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
    if (slotId != -999) {
      Slot slot = getSlot(slotId);
      if (isSelectSpell()) {
        if (slot instanceof SlotLibraryInfo) {
          SlotLibraryInfo info = (SlotLibraryInfo) slot;
          if (info.getHasStack()) {
            librarySlot = info.getSlot() + 5;
            if (staffSlot != -1) {
              StaffSpellStorage storage = getSpellStorage();
              LibrarySpellInfo libInfo = info.getInfo();
              if (storage == null || libInfo == null) {
                reset();
                return ItemStack.EMPTY;
              }
              storage.setSpellToSlot(staffSlot, libInfo.toStaff());
              storage.saveToStack();
              reset();
              PlayerSyncUtil.syncPlayer(player);
            }
          }
        } else if (slot instanceof SlotSpellInfo) {
          SlotSpellInfo info = (SlotSpellInfo) slot;
          if (GuiScreen.isShiftKeyDown() || GuiScreen.isAltKeyDown() || GuiScreen.isShiftKeyDown()) {
            if (info.getHasStack()) {
              this.slot = info.getSlot();
            }
            return ItemStack.EMPTY;
          }
          if (info.getHasStack()) {
            StaffSpellInfo newSpell = null;
            int swap = -1;
            if (staffSlot == -1) {
              staffSlot = info.getSlot();
            }
            if (librarySlot != -1) {
              LibrarySpellInfo libInfo = ((SlotLibraryInfo) getSlot(librarySlot)).getInfo();
              if (libInfo == null) {
                librarySlot = -1;
              } else {
                newSpell = libInfo.toStaff();
              }
            } else {
              newSpell = info.getInfo();
              swap = info.getSlot();
            }
            if (newSpell != null) {
              StaffSpellStorage storage = getSpellStorage();
              if (storage == null) {
                reset();
                return ItemStack.EMPTY;
              }
              if (swap == -1) {
                storage.setSpellToSlot(staffSlot, newSpell);
                storage.saveToStack();
                reset();
                PlayerSyncUtil.syncPlayer(player);
              } else if (staffSlot != -1 && swap != staffSlot) {
                StaffSpellInfo oldSpell = storage.getSpellInSlot(staffSlot);
                storage.setSpellToSlot(staffSlot, newSpell);
                storage.setSpellToSlot(swap, oldSpell);
                storage.saveToStack();
                reset();
                PlayerSyncUtil.syncPlayer(player);
              }
            }
          } else {
            StaffSpellStorage storage = null;
            if (staffSlot != -1 || librarySlot != -1) {
              storage = getSpellStorage();
              if (storage == null) {
                reset();
                return ItemStack.EMPTY;
              }
            }
            if (staffSlot != -1) {
              StaffSpellInfo newSpell = ((SlotSpellInfo) getSlot(librarySlot)).getInfo();
              if (newSpell == null) {
                reset();
                return ItemStack.EMPTY;
              }
              storage.setSpellToSlot(info.getSlot(), newSpell);
              storage.clearSlot(staffSlot);
              storage.saveToStack();
              reset();
              PlayerSyncUtil.syncPlayer(player);
            } else if (librarySlot != -1) {
              LibrarySpellInfo newSpell = ((SlotLibraryInfo) getSlot(librarySlot)).getInfo();
              if (newSpell == null) {
                reset();
                return ItemStack.EMPTY;
              }
              storage.setSpellToSlot(info.getSlot(), newSpell.toStaff());
              storage.saveToStack();
              reset();
              PlayerSyncUtil.syncPlayer(player);
            } else {
              staffSlot = info.getSlot();
            }
          }
        }
      } else {
        // Editing modifiers
        SlotLibraryModifierInfo info = (SlotLibraryModifierInfo) slot;
        if (info.isApplicable() && info.isApplied()) {
          StaffSpellStorage storage = getSpellStorage();
          if (storage == null) {
            return ItemStack.EMPTY;
          }
          StaffSpellInfo staffInfo = storage.getSpellInSlot(this.slot);
          if (staffInfo == null) {
            return ItemStack.EMPTY;
          }

          StaffModifierInstance modifier = staffInfo.getModifiers().getByCore(info.getCore());
          if (modifier == null) {
            return ItemStack.EMPTY;
          }

          modifier.setEnabled(!modifier.isEnabled());
          storage.saveToStack();
          PlayerSyncUtil.syncPlayer(player);
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
