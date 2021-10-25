/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.client.gui;

import epicsquid.roots.Roots;
import epicsquid.roots.container.ContainerPouch;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class GuiPouch extends ContainerScreen {

  private ContainerPouch containerPouch;

  public GuiPouch(@Nonnull ContainerPouch containerPouch) {
    super(containerPouch);
    this.containerPouch = containerPouch;
    if (!isComponentPouch() && !isHerbPouch()) {
      xSize = 170;
    } else {
      xSize = 150;
    }
    ySize = 100;
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
    ResourceLocation rl;
    if (containerPouch.isApothecary()) {
      rl = new ResourceLocation(Roots.MODID, "textures/gui/apothecary_pouch_gui.png");
    } else if (containerPouch.isHerb()) {
      rl = new ResourceLocation(Roots.MODID, "textures/gui/herb_pouch_gui.png");
    } else if (containerPouch.isFey()) {
      rl = new ResourceLocation(Roots.MODID, "textures/gui/fey_pouch_gui.png");
    } else {
      rl = new ResourceLocation(Roots.MODID, "textures/gui/component_pouch_gui.png");
    }
    this.mc.getTextureManager().bindTexture(rl);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    if (containerPouch.isApothecary()) {
      this.drawTexturedModalRect(i - 35, j - 63, 0, 0, 213, 215);
    } else {
      this.drawTexturedModalRect(i - 13, j - 55, 0, 0, 176, 207);
    }
  }

  private boolean isComponentPouch() {
    return !containerPouch.isApothecary() && !containerPouch.isHerb() && !containerPouch.isFey();
  }

  private boolean isHerbPouch() {
    return containerPouch.isHerb();
  }
}
