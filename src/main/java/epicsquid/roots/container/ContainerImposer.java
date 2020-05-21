/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.container;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.container.slots.SlotModifierInfo;
import epicsquid.roots.container.slots.SlotSpellInfo;
import epicsquid.roots.modifiers.instance.ModifierInstance;
import epicsquid.roots.modifiers.instance.ModifierInstanceList;
import epicsquid.roots.modifiers.modifier.IModifierCore;
import epicsquid.roots.modifiers.modifier.ModifierCores;
import epicsquid.roots.network.MessageSetImposerSlot;
import epicsquid.roots.spell.info.StaffSpellInfo;
import epicsquid.roots.spell.info.storage.StaffSpellStorage;
import epicsquid.roots.tileentity.TileEntityImposer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ContainerImposer extends Container {

  public final TileEntityImposer tile;
  private final EntityPlayer player;

  public ContainerImposer(EntityPlayer player, TileEntityImposer tile) {
    this.tile = tile;
    this.player = player;

    createPlayerInventory(player.inventory);
    createModifierSlots();
    createSpellSlots();
  }

  public boolean isSelectSpell() {
    return tile.getSlot() == 0;
  }

  private StaffSpellInfo getInfoFor(int slot) {
    StaffSpellStorage storage = tile.getSpellStorage();
    if (storage == null) {
      return StaffSpellInfo.EMPTY;
    } else {
      return storage.getSpellInSlot(slot);
    }
  }

  @Nullable
  private ModifierInstance getInstanceFor (IModifierCore core) {
    StaffSpellStorage storage = tile.getSpellStorage();
    if (storage == null) {
      return null;
    }
    int slot = tile.getSlot();
    if (slot == 0) {
      return null;
    }
    StaffSpellInfo info = storage.getSpellInSlot(slot);
    if (info == null) {
      return null;
    }
    ModifierInstanceList mods = info.getModifiers();
    if (mods == null) {
      return null;
    }
    return mods.getByCore(core);
  }

  private void createSpellSlots() {
    addSlotToContainer(new SlotSpellInfo(this::isSelectSpell, this::getInfoFor, 1, 51, 37)); // Spot 1
    addSlotToContainer(new SlotSpellInfo(this::isSelectSpell, this::getInfoFor, 2, 56, 13)); // Spot 2
    addSlotToContainer(new SlotSpellInfo(this::isSelectSpell, this::getInfoFor, 3, 80, 8)); // Spot 3
    addSlotToContainer(new SlotSpellInfo(this::isSelectSpell, this::getInfoFor, 4, 104, 13)); // Spot 4
    addSlotToContainer(new SlotSpellInfo(this::isSelectSpell, this::getInfoFor, 5, 109, 37)); // Spot 5
  }

  private void createModifierSlots() {
    addSlotToContainer(new SlotModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.PERESKIA, 80, 18));
    addSlotToContainer(new SlotModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.WILDROOT, 80, 43));
    addSlotToContainer(new SlotModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.TERRA_MOSS, 80, 68));
    addSlotToContainer(new SlotModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.INFERNAL_BULB, 80, 93));
    addSlotToContainer(new SlotModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.DEWGONIA, 80, 118));
    addSlotToContainer(new SlotModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.WILDEWHEET, 55, 38));
    addSlotToContainer(new SlotModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.SPIRIT_HERB, 55, 73));
    addSlotToContainer(new SlotModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.CLOUD_BERRY, 55, 108));
    addSlotToContainer(new SlotModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.REDSTONE, 30, 38));
    addSlotToContainer(new SlotModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.GUNPOWDER, 30, 73));
    addSlotToContainer(new SlotModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.GLOWSTONE, 30, 108));
    addSlotToContainer(new SlotModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.MOONGLOW_LEAF, 105, 38));
    addSlotToContainer(new SlotModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.BAFFLE_CAP, 105, 73));
    addSlotToContainer(new SlotModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.STALICRIPE, 105, 108));
    addSlotToContainer(new SlotModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.LAPIS, 130, 38));
    addSlotToContainer(new SlotModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.RUNIC_DUST, 130, 73));
    addSlotToContainer(new SlotModifierInfo(this::isSelectSpell, this::getInstanceFor, ModifierCores.BLAZE_POWDER, 130, 108));
  }

  @Override
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
    if (isSelectSpell() && slotId != -999) {
      Slot slot = getSlot(slotId);
      if (slot instanceof SlotSpellInfo) {
        StaffSpellInfo info = ((SlotSpellInfo) slot).getInfo();
        if (info != null && info != StaffSpellInfo.EMPTY) {
          MessageSetImposerSlot packet = new MessageSetImposerSlot(player.world.provider.getDimension(), tile.getPos(), ((SlotSpellInfo) slot).getSlot());
          PacketHandler.INSTANCE.sendToServer(packet);
        }
      }
    }
    return super.slotClick(slotId, dragType, clickTypeIn, player);
  }

  private void createPlayerInventory(InventoryPlayer inventoryPlayer) {
    int xOffset = 8;
    int yOffset = 160;

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
