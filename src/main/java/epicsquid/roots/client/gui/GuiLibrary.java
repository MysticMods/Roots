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
import epicsquid.roots.container.ContainerLibrary;
import epicsquid.roots.container.slots.SlotLibraryInfo;
import epicsquid.roots.container.slots.SlotLibraryModifierInfo;
import epicsquid.roots.container.slots.SlotSpellInfo;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstance;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.MessageResetLibraryScreen;
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
import java.util.StringJoiner;

public class GuiLibrary extends GuiContainer {

  private ContainerLibrary container;
  private InvisibleButton backButton;
  private InvisibleButton resetButton;

  public GuiLibrary(@Nonnull ContainerLibrary container) {
    super(container);
    this.container = container;
    xSize = 256;
    ySize = 152;
  }

  private boolean isSelectSpell() {
    return container.isSelectSpell();
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    if (!isSelectSpell()) {
      FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
      StaffSpellStorage storage = container.getSpellStorage();
      if (storage != null) {
        StaffSpellInfo info = storage.getSpellInSlot(container.getSpellSlot());
        if (info != null) {
          String name = info.getNonNullSpell().getTextColor() + "" + TextFormatting.BOLD + I18n.format("roots.spell." + info.getNonNullSpell().getName() + ".name");
          RenderHelper.enableGUIStandardItemLighting();
          this.drawCenteredString(renderer, name, this.width / 2, guiTop, 0xFFFFFFFF);
        }
      }
    } else {
      FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
      ItemStack staff = container.getStaff();
      if (!staff.isEmpty() && staff.hasDisplayName()) {
        RenderHelper.enableGUIStandardItemLighting();
        this.drawCenteredString(renderer, staff.getDisplayName(), this.width / 2, guiTop - (guiTop/3), 0xffffffff);
      }
    }
    this.renderHoveredToolTip(mouseX, mouseY);
  }

  @Override
  public void initGui() {
    super.initGui();

    this.buttonList.clear();
    this.backButton = new InvisibleButton(0, guiLeft + 183, guiTop + 136, 32, 22, I18n.format("roots.imposer.back"));
    this.resetButton = new InvisibleButton(0, guiLeft + 183, guiTop + 136, 32, 22, I18n.format("roots.library.reset"));
    this.buttonList.add(this.backButton);
    this.buttonList.add(this.resetButton);
  }

  @Override
  protected void renderHoveredToolTip(int x, int y) {
    if (this.mc.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null) {
      List<String> tooltip = new ArrayList<>();
      FontRenderer font = null;
      boolean hasStack = this.hoveredSlot.getHasStack();
      ItemStack stack = this.hoveredSlot.getStack();
      SlotLibraryModifierInfo info = null;
      if (hoveredSlot instanceof SlotLibraryModifierInfo) {
        info = (SlotLibraryModifierInfo) hoveredSlot;
        if (stack.isEmpty()) {
          stack = info.getCore().getStack();
          hasStack = true;
        }
        StaffModifierInstanceList modifiers = container.getModifiers();
        if (info.isDisabled() && info.isApplicable() && info.isApplied() && !info.isConflicting(modifiers)) {
          tooltip.add(TextFormatting.BOLD + I18n.format("roots.tooltip.modifier.not_enabled"));
        } else if (!info.isApplied() && info.isApplicable() && !info.isConflicting(modifiers)) {
          tooltip.add(TextFormatting.BOLD + I18n.format("roots.tooltip.modifier.not_applied"));
        } else if (!info.isApplicable() && !info.isConflicting(modifiers)) {
          tooltip.add(TextFormatting.BOLD + I18n.format("roots.tooltip.modifier.not_applicable"));
          stack = ItemStack.EMPTY;
          hasStack = false;
        } else if (info.isConflicting(modifiers)) {
          List<StaffModifierInstance> conflicts = info.getConflicts(modifiers);
          StringJoiner joiner = new StringJoiner(", ");
          conflicts.forEach(o -> joiner.add(I18n.format(o.getTranslationKey())));
          tooltip.add(TextFormatting.BOLD + I18n.format("roots.tooltip.modifier.conflicting1", joiner.toString()));
          tooltip.add(TextFormatting.BOLD + I18n.format("roots.tooltip.modifier.conflicting2"));
        }
      } else if (hoveredSlot instanceof SlotSpellInfo) {
        SlotSpellInfo spellInfo = (SlotSpellInfo) hoveredSlot;
        if (spellInfo.getHasStack()) {
          tooltip.add(TextFormatting.BOLD + I18n.format("roots.tooltip.modifier.activate"));
        }
      }
      if (hasStack) {
        GuiUtils.preItemToolTip(stack);
        tooltip.addAll(getItemToolTip(stack));
        font = stack.getItem().getFontRenderer(stack);
      }
      if (info != null) {
        StaffModifierInstance instance = info.get();
        if (instance != null && !instance.isDisabled()) {
          tooltip.add("");
          tooltip.add(instance.describeName());
          tooltip.add(instance.describeFunction());
          tooltip.add("");
          tooltip.addAll(instance.describeCost());
        }
      }
      if (!tooltip.isEmpty()) {
        this.drawHoveringText(tooltip, x, y, (font == null ? fontRenderer : font));
        if (hasStack) {
          GuiUtils.postItemToolTip();
        }
      }
    }
  }

  private static ResourceLocation SPELL_SELECT = new ResourceLocation(Roots.MODID, "textures/gui/staff_gui.png");
  private static ResourceLocation SPELL_MODIFY = new ResourceLocation(Roots.MODID, "textures/gui/staff_gui_modifiers.png");

  protected ResourceLocation getTexture() {
    return container.isSelectSpell() ? SPELL_SELECT : SPELL_MODIFY;
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    ResourceLocation tex = getTexture();
    backButton.visible = tex != SPELL_SELECT;
    resetButton.visible = tex == SPELL_SELECT;
    this.mc.getTextureManager().bindTexture(tex);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(i, j, 0, 0, 256, 152);
  }

  @Override
  public void drawSlot(Slot slot) {
    int i2 = slot.xPos - 2;
    int j2 = slot.yPos - 2;
    if (slot instanceof SlotLibraryModifierInfo) {
      SlotLibraryModifierInfo modInfo = (SlotLibraryModifierInfo) slot;
      if (!modInfo.isApplicable() || !modInfo.isApplied() || modInfo.isDisabled() || modInfo.isConflicting(container.getModifiers())) {
        int v = -1;
        if (!modInfo.isApplicable()) { // There is no modifier existant for this slot
          v = 40;
        } else if (!modInfo.isApplied()) { // There is a modifier but it isn't applied
          v = 20;
        } else if (modInfo.isConflicting(container.getModifiers())) { // There is a modifier but it conflicts with other enabled modifiers
          v = 60;
        } else if (modInfo.isDisabled()) { // There is a modifier and it is applied, but it's disabled
          v = 0;
        }

        if (v != -1) {
          this.mc.getTextureManager().bindTexture(getTexture());
          GlStateManager.disableLighting();
          GlStateManager.enableAlpha();
          this.drawTexturedModalRect(i2, j2, 0, 152 + v, 20, 20);
          GlStateManager.disableAlpha();
          GlStateManager.enableLighting();
        }
      }
    }
    super.drawSlot(slot);
    if (slot instanceof SlotSpellInfo) {
      // Staff
      if (((SlotSpellInfo) slot).getSlot() == container.getStaffSlot()) {
        this.mc.getTextureManager().bindTexture(getTexture());
        this.drawTexturedModalRect(i2, j2, 0, 154 + 16, 20, 20);
      }
    } else if (slot instanceof SlotLibraryInfo) {
      // Library
      if (((SlotLibraryInfo) slot).getSlot() == container.getLibrarySlot() - 5) {
        this.mc.getTextureManager().bindTexture(getTexture());
        this.drawTexturedModalRect(i2 + 1, j2 + 1, 0, 152, 18, 18);
      }
    }
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id == backButton.id || button.id == resetButton.id) {
      MessageResetLibraryScreen packet = new MessageResetLibraryScreen();
      PacketHandler.INSTANCE.sendToServer(packet);
      container.setSelectSpell();
      container.reset();
    }

    super.actionPerformed(button);
  }
}
