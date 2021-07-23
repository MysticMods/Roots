/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.container;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.container.slots.SlotImposerModifierInfo;
import epicsquid.roots.container.slots.SlotImposerSpellInfo;
import epicsquid.roots.modifiers.IModifierCore;
import epicsquid.roots.modifiers.ModifierCores;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstance;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.MessageInvalidateContainer;
import epicsquid.roots.spell.info.StaffSpellInfo;
import epicsquid.roots.spell.info.storage.StaffSpellStorage;
import epicsquid.roots.tileentity.TileEntityImposer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContainerImposer extends Container implements IInvalidatingContainer, IModifierContainer {

  public final TileEntityImposer tile;
  private final Map<IModifierCore, Slot> coreSlotMap = new HashMap<>();
  private List<SlotImposerSpellInfo> spellSlots = new ArrayList<>();
  private boolean shiftDown = false;
  private boolean altDown = false;
  private boolean ctrlDown = false;
  private final EntityPlayer player;

  public ContainerImposer(EntityPlayer player, TileEntityImposer tile) {
    this.tile = tile;
    this.player = player;

    createPlayerInventory(player.inventory);
    createModifierSlots();
    createSpellSlots();
  }

  public EntityPlayer getPlayer() {
    return player;
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
    addSpellSlot(new SlotImposerSpellInfo(this::isSelectSpell, this::getInfoFor, 1, 51, 37)); // Spot 1
    addSpellSlot(new SlotImposerSpellInfo(this::isSelectSpell, this::getInfoFor, 2, 56, 13)); // Spot 2
    addSpellSlot(new SlotImposerSpellInfo(this::isSelectSpell, this::getInfoFor, 3, 80, 8)); // Spot 3
    addSpellSlot(new SlotImposerSpellInfo(this::isSelectSpell, this::getInfoFor, 4, 104, 13)); // Spot 4
    addSpellSlot(new SlotImposerSpellInfo(this::isSelectSpell, this::getInfoFor, 5, 109, 37)); // Spot 5
  }

  private void addSpellSlot(SlotImposerSpellInfo slot) {
    spellSlots.add(slot);
    addSlotToContainer(slot);
  }

  @Override
  public void invalidate() {
    spellSlots.forEach(SlotImposerSpellInfo::invalidate);
  }

  private void addModifierSlot(IModifierCore core, TileEntityImposer imposer, int x, int y) {
    SlotImposerModifierInfo slot = new SlotImposerModifierInfo(this, this::isSelectSpell, this::getInstanceFor, core, imposer, x, y);
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
    addModifierSlot(ModifierCores.MOONGLOW_LEAF, tile, 105, 38);
    addModifierSlot(ModifierCores.BAFFLE_CAP, tile, 105, 73);
    addModifierSlot(ModifierCores.STALICRIPE, tile, 105, 108);
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
    if (slotId >= 0 && !player.world.isRemote) {
      Slot slot = getSlot(slotId);
      if (slot instanceof SlotImposerSpellInfo) {
        StaffSpellInfo info = ((SlotImposerSpellInfo) slot).getInfo();
        if (info != null && info != StaffSpellInfo.EMPTY) {
          tile.setSlot(((SlotImposerSpellInfo) slot).getSlot());
          invalidate();
          MessageInvalidateContainer message = new MessageInvalidateContainer();
          PacketHandler.INSTANCE.sendTo(message, (EntityPlayerMP) player);
        }
      } else if (slot instanceof SlotImposerModifierInfo) {
        SlotImposerModifierInfo info = (SlotImposerModifierInfo) slot;
        if (info.isApplicable() && info.isApplied()) {
          if (tile.getWorld() != null && !tile.getWorld().isRemote) {
            StaffSpellStorage storage = tile.getSpellStorage();
            StaffSpellInfo staffInfo = tile.getCurrentInfo(storage);
            if (staffInfo != null) {
              StaffModifierInstanceList modifiers = staffInfo.getModifiers();
              StaffModifierInstance modifier = modifiers.getByCore(info.getCore());
              if (modifier != null) {
                if (modifier.isEnabled() || (!modifier.isConflicting(modifiers) && !isAltDown() && !isControlDown())) {
                  modifier.setEnabled(!modifier.isEnabled());
                } else if (!modifier.isEnabled() && modifier.isConflicting(modifiers) && (isAltDown() || isControlDown())) {
                  for (StaffModifierInstance conflictingMod : modifier.getConflicts(modifiers)) {
                    conflictingMod.setEnabled(false);
                  }
                  modifier.setEnabled(true);
                }
                storage.saveToStack();
                tile.markDirty();
                tile.updatePacketViaState();
                invalidate();
                MessageInvalidateContainer message = new MessageInvalidateContainer();
                PacketHandler.INSTANCE.sendTo(message, (EntityPlayerMP) player);
              }
            }
          }
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
    Slot slot = this.inventorySlots.get(index);

    if (slot == null || !slot.getHasStack()) {
      return ItemStack.EMPTY;
    }

    ItemStack inSlot = slot.getStack();
    IModifierCore core = ModifierCores.fromItemStack(inSlot);
    if (core == null) {
      return ItemStack.EMPTY;
    }

    Slot coreSlot = coreSlotMap.get(core);
    if (coreSlot == null) {
      throw new IllegalStateException("slot for modifier core " + core + " must exist and cannot be null");
    }

    // This slot already has a modifier in it, so do nothing
    if (coreSlot.getHasStack()) {
      return ItemStack.EMPTY;
    }

    if (inSlot.getCount() > 1) {
      inSlot.shrink(1);
      slot.putStack(inSlot);
      inSlot = inSlot.copy();
      inSlot.setCount(1);
    } else {
      slot.putStack(ItemStack.EMPTY);
    }
    coreSlot.putStack(inSlot);

    return ItemStack.EMPTY;
  }

  @Override
  public void setModifierStatus(int modifier, boolean status) {
    if (modifier == 0) { // shift
      this.shiftDown = status;
    } else if (modifier == 1) { // ctrl
      this.ctrlDown = status;
    } else if (modifier == 2) { // alt
      this.altDown = status;
    }
  }

  @Override
  public boolean getModifierStatus(int modifier) {
    if (modifier == 0) { // shift
      return this.shiftDown;
    } else if (modifier == 1) { // ctrl
      return this.ctrlDown;
    } else if (modifier == 2) { // alt
      return this.altDown;
    } else {
      return false;
    }
  }
}
