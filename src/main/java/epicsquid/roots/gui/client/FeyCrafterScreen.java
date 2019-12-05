/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.gui.client;

import com.mojang.blaze3d.platform.GlStateManager;
import epicsquid.roots.Roots;
import epicsquid.roots.gui.container.FeyCrafterContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class FeyCrafterScreen extends ContainerScreen<FeyCrafterContainer> {

  private FeyCrafterContainer container;

  public FeyCrafterScreen(FeyCrafterContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.container = screenContainer;
    xSize = 176;
    ySize = 187;
  }

  // TODO: What's the new value?

/*  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
  }*/


  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.minecraft.getTextureManager().bindTexture(new ResourceLocation(Roots.MODID, "textures/gui/fey_crafting_gui.png"));
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    this.blit(i, j, 0, 0, 176, 207);
    int j3 = -1;
    int i3 = -1;
    if (container.getRecipe() != null) {
      RenderHelper.enableGUIStandardItemLighting();
      ItemStack result = container.getRecipe().getResult();
      i3 = i + ((this.xSize / 4) * 3);
      j3 = j + (this.ySize / 5);
      minecraft.getItemRenderer().renderItemIntoGUI(result, i3, j3);
      minecraft.getItemRenderer().renderItemOverlayIntoGUI(minecraft.fontRenderer, result, i3, j3, null);
      RenderHelper.disableStandardItemLighting();
    }
    this.minecraft.getTextureManager().bindTexture(new ResourceLocation(Roots.MODID, "textures/gui/fey_crafting_gui.png"));
    if (!container.getValidStone()) {
      int i2 = i + 54;
      int j2 = j + 33;
      this.blit(i2, j2, 176, 0, 16, 22);
      if (container.getRecipe() != null) {
        GlStateManager.pushMatrix();
        GlStateManager.translatef(0, 0, 150f);
        this.blit(i3 + 1, j3 + 1, 176 + 16, 0, 15, 15);
        GlStateManager.popMatrix();
      }
    }
  }
}
