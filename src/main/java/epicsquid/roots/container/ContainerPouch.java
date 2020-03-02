/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.container;

import epicsquid.roots.handler.ClientPouchHandler;
import epicsquid.roots.handler.IPouchHandler;
import epicsquid.roots.handler.PouchHandler;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.ItemPouch;
import epicsquid.roots.util.ServerHerbUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ContainerPouch extends Container {

  private IItemHandlerModifiable inventoryHandler;
  private IItemHandlerModifiable herbsHandler;
  private IPouchHandler handler;
  private EntityPlayer player;
  private ItemStack pouch;

  public boolean componentPouch = false;

  private int inventoryEnd;
  private int herbsEnd;

  private boolean isServerSide;

  private SlotSupplier supplier;

  public ContainerPouch(EntityPlayer player, boolean isServerSide) {
    this.player = player;
    this.isServerSide = isServerSide;
    ItemStack main = player.getHeldItemMainhand();
    ItemStack off = player.getHeldItemOffhand();
    ItemStack first = ServerHerbUtil.getFirstPouch(player);

    ItemStack use = ItemStack.EMPTY;
    if (main.getItem() instanceof ItemPouch) {
      use = main;
    } else if (off.getItem() instanceof ItemPouch) {
      use = off;
    } else if (first.getItem() instanceof ItemPouch) {
      use = first;
    }

    if (isServerSide) {
      handler = PouchHandler.getHandler(use);
    } else {
      handler = new ClientPouchHandler(use);
    }
    inventoryHandler = handler.getInventory();
    herbsHandler = handler.getHerbs();
    supplier = PouchSlot::new;

    pouch = use;

    createPlayerInventory(player.inventory);
    createPouchSlots();
  }

  private void createPouchSlots() {
    if (pouch.getItem() == ModItems.component_pouch) {
      createComponentPouchSlots();
      componentPouch = true;
    } else {
      createApothecaryPouchSlots();
    }
  }

  private void createComponentPouchSlots() {
    int xOffset = -13;
    int yOffset = -55;
    int q = 0;
    for (int i = 0; i < inventoryHandler.getSlots(); i++) {
      // Top Row
      if (i < 5) {
        addSlotToContainer(supplier.create(inventoryHandler, q++, xOffset + 11 + (i * 21), yOffset + 23));
      }
      // Middle Row
      if (i >= 5 && i < 9) {
        addSlotToContainer(supplier.create(inventoryHandler, q++, xOffset + 22 + ((i - 5) * 21), yOffset + 44));
      }
      // Bottom Row
      if (i >= 9 && i < 12) {
        addSlotToContainer(supplier.create(inventoryHandler, q++, xOffset + 33 + ((i - 9) * 21), yOffset + 65));
      }
      // Herb Pouch
    }
    inventoryEnd = q;
    for (int i = 0; i < herbsHandler.getSlots(); i++) {
      if (q >= 12 && q < 18) {
        // Controls which row the slots appear on
        int yPosOffset = q >= 14 ? q >= 16 ? 21 * 2 : 21 : 0;
        addSlotToContainer(supplier.create(herbsHandler, i, xOffset + 127 + (21 * (q % 2)), yOffset + 23 + yPosOffset));
        q++;
      }
    }
    herbsEnd = q;
  }

  private void createApothecaryPouchSlots() {
    int xOffset = -35;
    int yOffset = -63;
    int q = 0;
    for (int i = 0; i < inventoryHandler.getSlots(); i++) {
      // Top Row
      if (i < 6) {
        addSlotToContainer(supplier.create(inventoryHandler, q, xOffset + 25 + (20 * (q % 6)), yOffset + 19));
      }
      // Middle Slot
      if (i >= 6 && i < 12) {
        addSlotToContainer(supplier.create(inventoryHandler, q, xOffset + 25 + (20 * (q % 6)), yOffset + 43));
      }
      // Bottom Slot
      if (i >= 12 && i < 18) {
        addSlotToContainer(supplier.create(inventoryHandler, q, xOffset + 25 + (20 * (q % 6)), yOffset + 66));
      }
      q++;
    }
    inventoryEnd = q;
    for (int i = 0; i < herbsHandler.getSlots(); i++) {
      // Add Herb Slots
      q = inventoryEnd + i;
      if (q >= 18 && q < 21) {
        addSlotToContainer(supplier.create(herbsHandler, i, xOffset + 149 + (16 * (q % 3)), yOffset + 16 + (4 * (q % 2))));
      }
      if (q >= 21 && q < 24) {
        addSlotToContainer(supplier.create(herbsHandler, i, xOffset + 149 + (16 * (q % 3)), yOffset + 39 + (4 * ((q + 1) % 2))));
      }
      if (q >= 24 && q < 27) {
        addSlotToContainer(supplier.create(herbsHandler, i, xOffset + 149 + (16 * (q % 3)), yOffset + 64 + (4 * (q % 2))));
      }
    }
    herbsEnd = q + 1;
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
    ItemStack main = player.getHeldItemMainhand();
    ItemStack off = player.getHeldItemOffhand();
    ItemStack first = ServerHerbUtil.getFirstPouch(player);
    return (main.equals(pouch) || off.equals(pouch) || first.equals(pouch));
  }

  @Override
  @Nonnull
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
    ItemStack slotStack = ItemStack.EMPTY;

    Slot slot = inventorySlots.get(index);
    int herbStart = inventoryEnd + 36;
    int herbStop = herbsEnd + 36;

    if (slot != null && slot.getHasStack()) {
      ItemStack stack = slot.getStack();
      slotStack = stack.copy();

      boolean herb = HerbRegistry.isHerb(slotStack.getItem());

      if (index < 36) { // Player Inventory -> Inventory/herbs
        if (herb && !mergeItemStack(stack, herbStart, herbStop, false)) {
          return ItemStack.EMPTY;
        } else if (!herb && !mergeItemStack(stack, 36, herbStart, false)) {
          return ItemStack.EMPTY;
        }
      } else {
        if (!mergeItemStack(stack, 27, 36, false) && !mergeItemStack(stack, 0, 27, false)) {
          return ItemStack.EMPTY;
        }
      }

      if (stack.isEmpty()) {
        slot.putStack(ItemStack.EMPTY);
      }

      slot.onSlotChanged();
    }

    return slotStack;
  }

  @Override
  @Nonnull
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
    if (slotId >= 0) {
      ItemStack stack = getSlot(slotId).getStack();
      if (stack.getItem() instanceof ItemPouch) {
        return ItemStack.EMPTY;
      }
    }

    return super.slotClick(slotId, dragType, clickTypeIn, player);
  }

  @Override
  public void detectAndSendChanges() {
    super.detectAndSendChanges();

    handler.markDirty();
  }

  @Override
  public void onContainerClosed(EntityPlayer playerIn) {
    super.onContainerClosed(playerIn);

    if (!player.world.isRemote) {
      Objects.requireNonNull(player.world.getMapStorage()).saveAllData();
    }
  }

  @FunctionalInterface
  public interface SlotSupplier {
    Slot create(IItemHandler itemHandler, int index, int xPosition, int yPosition);
  }

  public class PouchSlot extends SlotItemHandler {
    public PouchSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
      super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
      if (stack.getItem() instanceof ItemPouch) {
        return false;
      }

      return super.isItemValid(stack);
    }

    @Override
    public void onSlotChange(@Nonnull ItemStack stack1, @Nonnull ItemStack stack2) {
      super.onSlotChange(stack1, stack2);
      handler.markDirty();
    }

    @Override
    public void onSlotChanged() {
      super.onSlotChanged();
      handler.markDirty();
    }
  }
}
