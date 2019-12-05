/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.gui.client;

import com.mojang.blaze3d.platform.GlStateManager;
import epicsquid.roots.Roots;
import epicsquid.roots.gui.container.QuiverContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class QuiverScreen extends ContainerScreen<QuiverContainer> {

  private QuiverContainer containerQuiver;

  public QuiverScreen(QuiverContainer containerScreen, PlayerInventory inv, ITextComponent titleIn) {
    super(containerScreen, inv, titleIn);
    this.containerQuiver = containerScreen;
    xSize = 176;
    ySize = 149;
  }

/*  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
  }*/

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.minecraft.getTextureManager().bindTexture(new ResourceLocation(Roots.MODID, "textures/gui/quiver_gui.png"));
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    this.blit(i, j, 0, 0, 176, 149);
  }
}
