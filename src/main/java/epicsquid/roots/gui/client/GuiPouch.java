/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.gui.client;

import java.awt.Color;

import javax.annotation.Nonnull;

import epicsquid.roots.gui.container.ContainerPouch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import sun.plugin2.util.ColorUtil;

public class GuiPouch extends GuiContainer {

  private ContainerPouch containerPouch;

  public GuiPouch(@Nonnull ContainerPouch containerPouch) {
    super(containerPouch);
    this.containerPouch = containerPouch;
    xSize = 150;
    ySize = 100;
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    bindGuiTexture();
    int sx = (width - xSize) / 2;
    int sy = (height - ySize) / 2;
    drawTexturedModalRect(sx, sy, 0, 0, xSize, ySize);

    FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
    fr.drawString(getUnlocalisedNameForHeading(), getGuiLeft() + 28, getGuiTop() + 13, ColorUtil.getRGB(Color.DARK_GRAY));

    renderCustomOptions(getGuiTop() + 13, par1, mouseX, mouseY);
    super.drawGuiContainerBackgroundLayer(par1, mouseX, mouseY);
  }

  private void bindGuiTexture() {
    
  }
}
