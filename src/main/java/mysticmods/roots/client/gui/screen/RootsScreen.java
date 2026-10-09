package mysticmods.roots.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class RootsScreen extends Screen {
  protected final List<Component> tooltip = new ArrayList<>();
  protected ItemStack tooltipItem = ItemStack.EMPTY;
  protected int leftPos, topPos;
  protected int lastMouseX, lastMouseY;

  protected RootsScreen(Component pTitle) {
    super(pTitle);
  }

  @Override
  protected void init() {
    super.init();
    leftPos = width / 2 - getBackgroundWidth() / 2;
    topPos = height / 2 - getBackgroundHeight() / 2;
  }

  public boolean isMouseInRelativeRange(int mouseX, int mouseY, int x, int y, int w, int h) {
    return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
  }

  // TODO: renderWithTooltip?
  public void drawTooltip(GuiGraphics guiGraphics, int x, int y) {
    if (tooltipItem != null && !tooltipItem.isEmpty()) {
      ItemStack itemstack = this.tooltipItem;
      guiGraphics.renderTooltip(this.font, getTooltipFromItem(minecraft, itemstack), itemstack.getTooltipImage(), itemstack, x, y);
    } else if (!tooltip.isEmpty()) {
      guiGraphics.renderTooltip(this.font, this.tooltip, Optional.empty(), x, y);
    }
  }

  public void resetTooltip() {
    tooltipItem = ItemStack.EMPTY;
    tooltip.clear();
  }

  public void fillTooltip(ItemStack stack) {
    tooltipItem = stack;
    tooltip.clear();
  }

  public void setTooltip(List<Component> tooltip) {
    this.tooltipItem = ItemStack.EMPTY;
    this.tooltip.clear();
    this.tooltip.addAll(tooltip);
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }

  public abstract ResourceLocation getBackground();

  public abstract int getBackgroundWidth();

  public abstract int getBackgroundHeight();

  @Override
  public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    this.renderTransparentBackground(guiGraphics);
    this.drawBackground(guiGraphics, mouseX, mouseY, partialTick);
  }

  // TODO: Massive todo: rewrite all of this
  @Override
  public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
    this.lastMouseX = pMouseX;
    this.lastMouseY = pMouseY;
    PoseStack stack = graphics.pose();
    this.renderBackground(graphics, pMouseX, pMouseY, pPartialTick);
    resetTooltip();
    stack.pushPose();
    RenderSystem.disableDepthTest();
    RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    for (Renderable renderable : this.renderables) {
      renderable.render(graphics, pMouseX, pMouseY, pPartialTick);
    }
    drawForeground(graphics, pMouseX, pMouseY, pPartialTick);
    stack.popPose();
    RenderSystem.enableDepthTest();
    drawTooltip(graphics, pMouseX, pMouseY);
  }

  public void drawBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    int width1 = getBackgroundWidth();
    int height1 = getBackgroundHeight();
    int fileWidth = getBackgroundWidth();
    int fileHeight = getBackgroundHeight();
    drawBackground(graphics, mouseX, mouseY, partialTicks, width1, height1, fileWidth, fileHeight);
  }

  public void drawBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, int uvW, int uvH, int maxW, int maxH) {
    ResourceLocation resourceLocation = getBackground();
    graphics.blit(resourceLocation, leftPos, topPos, 0, 0, uvW, uvH, maxW, maxH);
  }

  public void drawForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
  }
}
