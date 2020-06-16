/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots;

import epicsquid.roots.client.gui.*;
import epicsquid.roots.container.*;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.tileentity.TileEntityFeyCrafter;
import epicsquid.roots.tileentity.TileEntityImposer;
import epicsquid.roots.util.SpellUtil;
import epicsquid.roots.world.data.SpellLibraryRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {

  public static final int POUCH_ID = 16;
  public static final int QUIVER_ID = 17;
  public static final int CRAFTER_ID = 18;
  public static final int IMPOSER_ID = 19;
  public static final int LIBRARY_ID = 20;

  public static ItemStack getStaff(EntityPlayer player) {
    ItemStack staff = player.getHeldItemMainhand();
    if (!staff.isEmpty() && staff.getItem().equals(ModItems.staff)) {
      return staff;
    }

    staff = player.getHeldItemOffhand();
    if (!staff.isEmpty() && staff.getItem().equals(ModItems.staff)) {
      return staff;
    }

    IItemHandler handler = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
    if (handler != null) {
      for (int i = 0; i < handler.getSlots(); i++) {
        staff = handler.getStackInSlot(i);
        if (!staff.isEmpty() && staff.getItem().equals(ModItems.staff)) {
          return staff;
        }
      }
    }

    return ItemStack.EMPTY;
  }

  @Nullable
  @Override
  public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    switch (id) {
      case POUCH_ID:
        return new ContainerPouch(player, true);
      case QUIVER_ID:
        return new ContainerQuiver(player);
      case CRAFTER_ID:
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof TileEntityFeyCrafter) {
          return new ContainerFeyCrafter(player, (TileEntityFeyCrafter) te);
        }
        break;
      case IMPOSER_ID:
        te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof TileEntityImposer) {
          ((TileEntityImposer) te).updateInSlot(player);
          return new ContainerImposer(player, (TileEntityImposer) te);
        }
        break;
      case LIBRARY_ID:
        ItemStack staff = getStaff(player);
        SpellUtil.updateModifiers(staff, player);
        if (!staff.isEmpty()) {
          return new ContainerLibrary(player, staff, SpellLibraryRegistry.getData(player));
        }
        break;
    }
    return null;
  }

  @Nullable
  @Override
  public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    switch (id) {
      case POUCH_ID:
        return new GuiPouch(new ContainerPouch(player, false));
      case QUIVER_ID:
        return new GuiQuiver(new ContainerQuiver(player));
      case CRAFTER_ID:
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof TileEntityFeyCrafter) {
          return new GuiFeyCrafter(new ContainerFeyCrafter(player, (TileEntityFeyCrafter) te));
        }
        break;
      case IMPOSER_ID:
        te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof TileEntityImposer) {
          return new GuiImposer(new ContainerImposer(player, (TileEntityImposer) te));
        }
        break;
      case LIBRARY_ID:
        ItemStack staff = getStaff(player);
        if (!staff.isEmpty()) {
          return new GuiLibrary(new ContainerLibrary(player, staff, null));
        }
        break;
    }
    return null;
  }
}
