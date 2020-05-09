/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.client.gui;

import epicsquid.roots.Roots;
import epicsquid.roots.container.ContainerFeyCrafter;
import epicsquid.roots.container.ContainerImposer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class GuiImposer extends GuiContainer {

  private ContainerImposer container;

  public GuiImposer(@Nonnull ContainerImposer container) {
    super(container);
    this.container = container;
    xSize = 176;
    ySize = 243;
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
  }

  @Override
  protected void renderHoveredToolTip(int x, int y) {
    super.renderHoveredToolTip(x, y);

/*    if (container.getRecipe() != null) {
      final int minX = ((this.width - this.xSize) / 2) + ((this.xSize / 4) * 3) - 2;
      final int minY = ((this.height - this.ySize) / 2) + (this.ySize / 5) - 2;
      final int maxX = minX + 20;
      final int maxY = minY + 20;

      if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
        this.renderToolTip(container.getRecipe().getResult(), x, y);
      }
    }*/
  }

  private static ResourceLocation SPELL_SELECT = new ResourceLocation(Roots.MODID, "textures/gui/imposer_spell_select.png");
  private static ResourceLocation MODIFIER_EDIT = new ResourceLocation(Roots.MODID, "textures/gui/imposer_modifier_edit.png");

  protected ResourceLocation getTexture () {
    return container.isSelectSpell() ? SPELL_SELECT : MODIFIER_EDIT;
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(getTexture());
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(i, j, 0, 0, 176, 243);
  }
}
