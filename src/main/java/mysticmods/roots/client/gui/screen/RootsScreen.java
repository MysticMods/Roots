package mysticmods.roots.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public abstract class RootsScreen extends Screen {
  protected int maxScale;
  protected float scaleFactor;
  protected List<Component> tooltip;
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
    if (tooltip != null && !tooltip.isEmpty()) {
      this.renderComponentTooltip(stack, tooltip, mouseX, mouseY, font);
    }
  }

  public void resetTooltip() {
    tooltip = null;
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }

  public static void drawFromTexture (ResourceLocation resourceLocation, int x, int y, int uOffset, int vOffset, int width, int height, int fileWidth, int fileHeight, PoseStack stack) {
    RenderSystem.setShaderTexture(0, resourceLocation);
    blit(stack, x, y, uOffset, vOffset, width, height, fileWidth, fileHeight);
  }

  public abstract ResourceLocation getBackground ();

  public abstract int getBackgroundWidth ();
  public abstract int getBackgroundHeight ();

  @Override
  public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
    super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    pPoseStack.pushPose();
    if (scaleFactor != 1) {
      pPoseStack.scale(scaleFactor, scaleFactor, scaleFactor);
      pMouseX /= scaleFactor;
      pMouseY /= scaleFactor;
    }
    drawScreenAfterScale(pPoseStack, pMouseX, pMouseY, pPartialTick);
    pPoseStack.popPose();
  }

  public void drawScreenAfterScale (PoseStack stack, int mouseX, int mouseY, float partialTicks) {
    resetTooltip();
    renderBackground(stack, 0);
    stack.pushPose();
    stack.translate(guiLeft, guiTop, 0);
    RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    drawBackground(stack, mouseX, mouseY, partialTicks);
    drawForeground(stack, mouseX, mouseY, partialTicks);
    stack.popPose();
    super.render(stack, mouseX, mouseY, partialTicks);
    drawTooltip(stack, mouseX, mouseY);
  }

  public void drawBackground (PoseStack stack, int mouseX, int mouseY, float partialTicks) {
    drawFromTexture(getBackground(), 0, 0, 0, 0, getBackgroundWidth(), getBackgroundHeight(), getBackgroundWidth(), getBackgroundHeight(), stack);
  }

  public void drawForeground (PoseStack stack, int mouseX, int mouseY, float partialTicks) {
  }
}
