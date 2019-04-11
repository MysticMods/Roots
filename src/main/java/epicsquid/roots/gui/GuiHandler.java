/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.gui;

import javax.annotation.Nullable;

import epicsquid.roots.gui.client.GuiPouch;
import epicsquid.roots.gui.container.ContainerPouch;
import epicsquid.roots.util.PowderInventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

  public static final int POUCH_ID = 16;

  @Nullable
  @Override
  public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    switch (id) {
    case POUCH_ID:
      return new ContainerPouch(player);
    default:
      return null;
    }
  }

  @Nullable
  @Override
  public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    switch (id) {
    case POUCH_ID:
      return new GuiPouch(new ContainerPouch(player));
    default:
      return null;
    }
  }
}
