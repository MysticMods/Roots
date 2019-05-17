/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.gui.client;

import epicsquid.roots.Roots;
import epicsquid.roots.gui.container.ContainerWildCrafter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class GuiWildCrafter extends GuiContainer {

  private ContainerWildCrafter container;

  public GuiWildCrafter(@Nonnull ContainerWildCrafter container) {
    super(container);
    this.container = container;
    xSize = 176;
    ySize = 187;
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(new ResourceLocation(Roots.MODID, "textures/gui/wild_crafter.png"));
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(i - 13, j - 55, 0, 0, 176, 207);
  }
}
