/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.capability.pouch;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.roots.init.HerbRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class PouchItemHandler extends ItemStackHandler implements ICapabilityProvider {

  public static final int COMPONENT_POUCH_HERB_SLOTS = 6;
  public static final int COMPONENT_POUCH_INVENTORY_SLOTS = 12;

  // Marks the start of the herb slots as well
  private int inventorySlots;
  private int herbSlots;

  public PouchItemHandler(int inventorySlots, int herbSlots) {
    super(inventorySlots + herbSlots);
    this.inventorySlots = inventorySlots;
    this.herbSlots = herbSlots;
  }

  @Override
  public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
    if (slot >= inventorySlots && !HerbRegistry.containsHerbItem(stack.getItem())) {
      return;
    }
    super.setStackInSlot(slot, stack);
  }

  @Nonnull
  @Override
  public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
    if (slot >= inventorySlots && !HerbRegistry.containsHerbItem(stack.getItem())) {
      return stack;
    }
    return super.insertItem(slot, stack, simulate);
  }

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
  }

  @Nullable
  @Override
  @SuppressWarnings("unchecked")
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) this : null;
  }

  public int getHerbSlots() {
    return herbSlots;
  }

  public int getInventorySlots() {
    return inventorySlots;
  }
}
