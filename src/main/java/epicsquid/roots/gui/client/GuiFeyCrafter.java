/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.gui.client;

import epicsquid.roots.Roots;
import epicsquid.roots.gui.container.ContainerFeyCrafter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class GuiFeyCrafter extends GuiContainer {

  private ContainerFeyCrafter container;

  public GuiFeyCrafter(@Nonnull ContainerFeyCrafter container) {
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
    this.mc.getTextureManager().bindTexture(new ResourceLocation(Roots.MODID, "textures/gui/fey_crafting_gui.png"));
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(i, j, 0, 0, 176, 207);
    int j3 = -1;
    int i3 = -1;
    if (container.getRecipe() != null) {
      RenderHelper.enableGUIStandardItemLighting();
      ItemStack result = container.getRecipe().getResult();
      i3 = i + ((this.xSize / 4) * 3);
      j3 = j + (this.ySize / 5);
      mc.getRenderItem().renderItemIntoGUI(result, i3, j3);
      mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, result, i3, j3, null);
      RenderHelper.disableStandardItemLighting();
    }
    this.mc.getTextureManager().bindTexture(new ResourceLocation(Roots.MODID, "textures/gui/fey_crafting_gui.png"));
    if (!container.getValidStone()) {
      int i2 = i + 54;
      int j2 = j + 33;
      this.drawTexturedModalRect(i2, j2, 176, 0, 16, 22);
      if (container.getRecipe() != null) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 150f);
        this.drawTexturedModalRect(i3 + 1, j3 + 1, 176 + 16, 0, 15, 15);
        GlStateManager.popMatrix();
      }
    }
  }
}
