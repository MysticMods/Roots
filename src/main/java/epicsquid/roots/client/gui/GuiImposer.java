/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package epicsquid.roots.client.gui;

import epicsquid.mysticallib.client.gui.InvisibleButton;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.container.ContainerImposer;
import epicsquid.roots.container.slots.SlotModifierInfo;
import epicsquid.roots.container.slots.SlotSpellInfo;
import epicsquid.roots.network.MessageSetImposerSlot;
import epicsquid.roots.spell.info.StaffSpellInfo;
import epicsquid.roots.spell.info.storage.StaffSpellStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiImposer extends GuiContainer {

  private ContainerImposer container;
  private InvisibleButton backButton;

  public GuiImposer(@Nonnull ContainerImposer container) {
    super(container);
    this.container = container;
    xSize = 176;
    ySize = 243;
  }

  @Override
  public void initGui() {
    super.initGui();

    this.buttonList.clear();
    this.backButton = new InvisibleButton(0, guiLeft + 143, guiTop + 126, 32, 22, I18n.format("roots.imposer.back"));
    this.buttonList.add(this.backButton);
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    if (container.tile.getSlot() != 0) {
      FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
      StaffSpellStorage storage = container.tile.getSpellStorage();
      if (storage != null) {
        StaffSpellInfo info = storage.getSpellInSlot(container.tile.getSlot());
        if (info != null) {
          String name = info.getSpell().getTextColor() + "" + TextFormatting.BOLD + I18n.format("roots.spell." + info.getSpell().getName() + ".name");
          RenderHelper.enableGUIStandardItemLighting();
          this.drawCenteredString(renderer, name, this.width / 2, guiTop + 5, 0xFFFFFFFF);
        }
      }
    }
    this.renderHoveredToolTip(mouseX, mouseY);
  }

  @Override
  protected void renderHoveredToolTip(int x, int y) {
    if (this.mc.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null) {
      List<String> tooltip = new ArrayList<>();
      FontRenderer font = null;
      boolean hasStack = this.hoveredSlot.getHasStack();
      if (hasStack) {
        ItemStack stack = this.hoveredSlot.getStack();
        GuiUtils.preItemToolTip(stack);
        tooltip.addAll(getItemToolTip(stack));
        font = stack.getItem().getFontRenderer(stack);
      }
      if (hoveredSlot instanceof SlotModifierInfo) {
        // Add/manipulate modifyCooldown info here
        tooltip.add("");
      }
      if (!tooltip.isEmpty()) {
        this.drawHoveringText(tooltip, x, y, (font == null ? fontRenderer : font));
        if (hasStack) {
          GuiUtils.postItemToolTip();
        }
      }
    }
  }

  private static ResourceLocation SPELL_SELECT = new ResourceLocation(Roots.MODID, "textures/gui/imposer_spell_select.png");
  private static ResourceLocation MODIFIER_EDIT = new ResourceLocation(Roots.MODID, "textures/gui/imposer_modifier_edit.png");

  protected ResourceLocation getTexture() {
    return container.isSelectSpell() ? SPELL_SELECT : MODIFIER_EDIT;
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    ResourceLocation tex = getTexture();
    backButton.visible = tex != SPELL_SELECT;
    this.mc.getTextureManager().bindTexture(tex);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(i, j, 0, 0, 176, 243);
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id == backButton.id) {
      MessageSetImposerSlot packet = new MessageSetImposerSlot(Minecraft.getMinecraft().player.world.provider.getDimension(), container.tile.getPos(), 0);
      PacketHandler.INSTANCE.sendToServer(packet);
    }

    super.actionPerformed(button);
  }

  @Override
  public void drawSlot(Slot slot) {
    int i2 = slot.xPos - 2;
    int j2 = slot.yPos - 2;
    if (slot instanceof SlotModifierInfo) {
      SlotModifierInfo modInfo = (SlotModifierInfo) slot;
      if (!modInfo.isApplicable()) { // There is no modifyCooldown existant for this slot
        this.mc.getTextureManager().bindTexture(getTexture());
        this.drawTexturedModalRect(i2, j2, 176, 40, 20, 20);
      } else if (!modInfo.isApplied()) { // There is a modifyCooldown but it isn't applied
        this.mc.getTextureManager().bindTexture(getTexture());
        this.drawTexturedModalRect(i2, j2, 176, 20, 20, 20);
      } else if (modInfo.isDisabled()) { // There is a modifyCooldown and it is applied, but it's disabled
        this.mc.getTextureManager().bindTexture(getTexture());
        this.drawTexturedModalRect(i2, j2, 176, 0, 20, 20);
      }
    }
    super.drawSlot(slot);
    if (slot instanceof SlotSpellInfo) {
      SlotSpellInfo infoSlot = (SlotSpellInfo) slot;
      StaffSpellInfo info = infoSlot.getInfo();
      if (info == null || info == StaffSpellInfo.EMPTY) {
        this.mc.getTextureManager().bindTexture(getTexture());
        this.drawTexturedModalRect(i2 + 2, j2 + 2, 176, 16, 16, 16);
      }
    }
  }
}
