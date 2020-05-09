/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots;

import epicsquid.roots.client.gui.GuiFeyCrafter;
import epicsquid.roots.client.gui.GuiImposer;
import epicsquid.roots.client.gui.GuiPouch;
import epicsquid.roots.client.gui.GuiQuiver;
import epicsquid.roots.container.ContainerFeyCrafter;
import epicsquid.roots.container.ContainerImposer;
import epicsquid.roots.container.ContainerPouch;
import epicsquid.roots.container.ContainerQuiver;
import epicsquid.roots.tileentity.TileEntityFeyCrafter;
import epicsquid.roots.tileentity.TileEntityImposer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {

  public static final int POUCH_ID = 16;
  public static final int QUIVER_ID = 17;
  public static final int CRAFTER_ID = 18;
  public static final int IMPOSER_ID = 19;

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
          return new ContainerImposer(player, (TileEntityImposer) te);
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
    }
    return null;
  }
}
