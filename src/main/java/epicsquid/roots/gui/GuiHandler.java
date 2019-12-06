/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 *//*

package epicsquid.roots.gui;

import javax.annotation.Nullable;

import epicsquid.roots.gui.client.PouchScreen;
import epicsquid.roots.gui.client.QuiverScreen;
import epicsquid.roots.gui.client.FeyCrafterScreen;
import epicsquid.roots.gui.container.PouchContainer;
import epicsquid.roots.gui.container.QuiverContainer;
import epicsquid.roots.gui.container.FeyCrafterContainer;
import epicsquid.roots.tileentity.TileEntityFeyCrafter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

  public static final int POUCH_ID = 16;
  public static final int QUIVER_ID = 17;
  public static final int CRAFTER_ID = 18;

  @Nullable
  @Override
  public Object getServerGuiElement(int id, PlayerEntity player, World world, int x, int y, int z) {
    switch (id) {
      case POUCH_ID:
        return new PouchContainer(player);
      case QUIVER_ID:
        return new QuiverContainer(player);
      case CRAFTER_ID:
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof TileEntityFeyCrafter) {
          return new FeyCrafterContainer(player, (TileEntityFeyCrafter) te);
        }
      default:
        return null;
    }
  }

  @Nullable
  @Override
  public Object getClientGuiElement(int id, PlayerEntity player, World world, int x, int y, int z) {
    switch (id) {
      case POUCH_ID:
        return new PouchScreen(new PouchContainer(player));
      case QUIVER_ID:
        return new QuiverScreen(new QuiverContainer(player));
      case CRAFTER_ID:
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof TileEntityFeyCrafter) {
          return new FeyCrafterScreen(new FeyCrafterContainer(player, (TileEntityFeyCrafter) te));
        }
      default:
        return null;
    }
  }
}*/
