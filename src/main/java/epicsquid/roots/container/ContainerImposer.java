/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.container;

import epicsquid.roots.container.slots.SlotImposerModifierInfo;
import epicsquid.roots.container.slots.SlotImposerSpellInfo;
import epicsquid.roots.modifiers.IModifierCore;
import epicsquid.roots.modifiers.ModifierCores;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstance;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
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
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ContainerImposer extends Container {

  public final TileEntityImposer tile;
  private final EntityPlayer player;
  private final Map<IModifierCore, Slot> coreSlotMap = new HashMap<>();

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
  private StaffModifierInstance getInstanceFor(IModifierCore core) {
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
    StaffModifierInstanceList mods = info.getModifiers();
    if (mods == null) {
      return null;
    }
    return mods.getByCore(core);
  }

  @Nullable
  public StaffModifierInstanceList getModifiers() {
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
    StaffModifierInstanceList mods = info.getModifiers();
    if (mods == null) {
      return null;
    }

    return mods;
  }

  private void createSpellSlots() {
    addSlotToContainer(new SlotImposerSpellInfo(this::isSelectSpell, this::getInfoFor, 1, 51, 37)); // Spot 1
    addSlotToContainer(new SlotImposerSpellInfo(this::isSelectSpell, this::getInfoFor, 2, 56, 13)); // Spot 2
    addSlotToContainer(new SlotImposerSpellInfo(this::isSelectSpell, this::getInfoFor, 3, 80, 8)); // Spot 3
    addSlotToContainer(new SlotImposerSpellInfo(this::isSelectSpell, this::getInfoFor, 4, 104, 13)); // Spot 4
    addSlotToContainer(new SlotImposerSpellInfo(this::isSelectSpell, this::getInfoFor, 5, 109, 37)); // Spot 5
  }

  private void addModifierSlot(IModifierCore core, TileEntityImposer imposer, int x, int y) {
    SlotImposerModifierInfo slot = new SlotImposerModifierInfo(this::isSelectSpell, this::getInstanceFor, core, imposer, x, y);
    coreSlotMap.put(core, slot);
    addSlotToContainer(slot);
  }

  private void createModifierSlots() {
    addModifierSlot(ModifierCores.PERESKIA, tile, 80, 18);
    addModifierSlot(ModifierCores.WILDROOT, tile, 80, 43);
    addModifierSlot(ModifierCores.TERRA_MOSS, tile, 80, 68);
    addModifierSlot(ModifierCores.INFERNAL_BULB, tile, 80, 93);
    addModifierSlot(ModifierCores.DEWGONIA, tile, 80, 118);
    addModifierSlot(ModifierCores.WILDEWHEET, tile, 55, 38);
    addModifierSlot(ModifierCores.SPIRIT_HERB, tile, 55, 73);
    addModifierSlot(ModifierCores.CLOUD_BERRY, tile, 55, 108);
    addModifierSlot(ModifierCores.REDSTONE, tile, 30, 38);
    addModifierSlot(ModifierCores.GUNPOWDER, tile, 30, 73);
    addModifierSlot(ModifierCores.GLOWSTONE, tile, 30, 108);
    addModifierSlot(ModifierCores.MOONGLOW_LEAF, tile, 105, 38);
    addModifierSlot(ModifierCores.BAFFLE_CAP, tile, 105, 73);
    addModifierSlot(ModifierCores.STALICRIPE, tile, 105, 108);
    addModifierSlot(ModifierCores.NETHER_WART, tile, 130, 38);
    addModifierSlot(ModifierCores.BLAZE_POWDER, tile, 130, 73);
    addModifierSlot(ModifierCores.RUNIC_DUST, tile, 130, 108);
  }

  @Override
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
    if (slotId != -999 && !player.world.isRemote) {
      Slot slot = getSlot(slotId);
      if (slot instanceof SlotImposerSpellInfo) {
        StaffSpellInfo info = ((SlotImposerSpellInfo) slot).getInfo();
        if (info != null && info != StaffSpellInfo.EMPTY) {
          tile.setSlot(((SlotImposerSpellInfo) slot).getSlot());
        }
      } else if (slot instanceof SlotImposerModifierInfo) {
        SlotImposerModifierInfo info = (SlotImposerModifierInfo) slot;
        if (info.isApplicable() && info.isApplied()) {
          tile.toggleModifier(info.getCore());
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
    BlockPos pos = this.tile.getPos();
    if (this.tile.getWorld().getTileEntity(pos) != this.tile) {
      return false;
    } else {
      return player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
    }
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
