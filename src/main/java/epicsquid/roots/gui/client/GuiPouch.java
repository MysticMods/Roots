/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.gui.client;

import javax.annotation.Nonnull;

import epicsquid.roots.Roots;
import epicsquid.roots.capability.pouch.PouchItemHandler;
import epicsquid.roots.gui.container.ContainerPouch;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiPouch extends GuiContainer {

  private ContainerPouch containerPouch;

  public GuiPouch(@Nonnull ContainerPouch containerPouch) {
    super(containerPouch);
    this.containerPouch = containerPouch;
    xSize = 150;
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
    this.mc.getTextureManager().bindTexture(new ResourceLocation(Roots.MODID, isComponentPouch() ? "textures/gui/component_pouch_gui.png" : "textures/gui/apothecary_pouch_gui.png"));
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(i - (isComponentPouch() ? 13 : 35), j - (isComponentPouch() ? 55 : 63), 0, 0, isComponentPouch() ? 176 : 213, isComponentPouch() ? 207 : 215);
  }

  private boolean isComponentPouch() {
    return containerPouch.getInvSlots() == PouchItemHandler.COMPONENT_POUCH_INVENTORY_SLOTS && containerPouch.getHerbSlots() == PouchItemHandler.COMPONENT_POUCH_HERB_SLOTS;
  }
}
