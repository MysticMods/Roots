package mysticmods.roots.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public abstract class RootsScreen extends Screen {
  protected int maxScale;
  protected float scaleFactor;
  protected List<Component> tooltip;
  protected ItemStack tooltipItem = ItemStack.EMPTY;
  protected int guiLeft, guiTop, guiRight, guiBottom;

  protected RootsScreen(Component pTitle) {
    super(pTitle);
  }

  @Override
  protected void init() {
    super.init();
    maxScale = getMinecraft().getWindow().calculateScale(0, minecraft.isEnforceUnicode());
    scaleFactor = 1;
    guiLeft = width / 2 - getBackgroundWidth() / 2;
    guiTop = height / 2 - getBackgroundHeight() / 2;
    guiRight = width / 2 + getBackgroundWidth() / 2;
    guiBottom = height / 2 + getBackgroundHeight() / 2;
  }

  public boolean isMouseInRelativeRange(int mouseX, int mouseY, int x, int y, int w, int h) {
    return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
  }

  public void drawTooltip(PoseStack stack, int mouseX, int mouseY) {
    // TODO: render tooltip?
/*    if (tooltip != null && !tooltip.isEmpty()) {
      this.renderComponentTooltip(stack, tooltip, mouseX, mouseY, font);
    } else if (!tooltipItem.isEmpty()) {
      this.renderComponentTooltip(stack, getTooltipFromItem(tooltipItem), mouseX, mouseY, font);
    }*/
  }

  public void resetTooltip() {
    tooltip = null;
    tooltipItem = ItemStack.EMPTY;
  }

  public void fillTooltip(ItemStack stack) {
    tooltipItem = stack;
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }

  public static void drawFromTexture(GuiGraphics graphics, ResourceLocation resourceLocation, int x, int y, int uOffset, int vOffset, int width, int height, int fileWidth, int fileHeight, PoseStack stack) {
    /*    RenderSystem.setShaderTexture(0, resourceLocation);*/
    graphics.blit(resourceLocation, x, y, uOffset, vOffset, width, height, fileWidth, fileHeight);
  }

  public abstract ResourceLocation getBackground();

  public abstract int getBackgroundWidth();

  public abstract int getBackgroundHeight();

  @Override
  public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
    super.render(graphics, pMouseX, pMouseY, pPartialTick);
    graphics.pose().pushPose();
    if (scaleFactor != 1) {
      graphics.pose().scale(scaleFactor, scaleFactor, scaleFactor);
      pMouseX /= scaleFactor;
      pMouseY /= scaleFactor;
    }
    drawScreenAfterScale(graphics, pMouseX, pMouseY, pPartialTick);
    graphics.pose().popPose();
  }

  public void drawScreenAfterScale(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    resetTooltip();
    PoseStack stack = graphics.pose();
    // TODO: check integers
    renderBackground(graphics, 0, 0, 0);
    stack.pushPose();
    stack.translate(guiLeft, guiTop, 0);
    RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    drawBackground(graphics, mouseX, mouseY, partialTicks);
    drawForeground(stack, mouseX, mouseY, partialTicks);
    stack.popPose();
    // TODO: ???
    super.render(graphics, mouseX, mouseY, partialTicks);
    drawTooltip(stack, mouseX, mouseY);
  }

  public void drawBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    drawFromTexture(graphics, getBackground(), 0, 0, 0, 0, getBackgroundWidth(), getBackgroundHeight(), getBackgroundWidth(), getBackgroundHeight(), graphics.pose());
  }

  public void drawForeground(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
  }
}
