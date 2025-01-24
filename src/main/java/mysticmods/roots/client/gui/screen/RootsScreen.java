package mysticmods.roots.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public abstract class RootsScreen extends Screen {
  protected int maxScale;
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
    guiLeft = width / 2 - getBackgroundWidth() / 2;
    guiTop = height / 2 - getBackgroundHeight() / 2;
    guiRight = width / 2 + getBackgroundWidth() / 2;
    guiBottom = height / 2 + getBackgroundHeight() / 2;
  }

  public boolean isMouseInRelativeRange(int mouseX, int mouseY, int x, int y, int w, int h) {
    return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
  }

  public void drawTooltip(GuiGraphics guiGraphics, int x, int y) {
    if (tooltipItem != null && !tooltipItem.isEmpty()) {
      ItemStack itemstack = this.tooltipItem;
      guiGraphics.renderTooltip(this.font, getTooltipFromItem(minecraft, itemstack), itemstack.getTooltipImage(), itemstack, x, y);
    }
  }

  public void resetTooltip() {
    tooltipItem = ItemStack.EMPTY;
  }

  public void fillTooltip(ItemStack stack) {
    tooltipItem = stack;
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }

  public abstract ResourceLocation getBackground();

  public abstract int getBackgroundWidth();

  public abstract int getBackgroundHeight();

  @Override
  public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
    this.renderBackground(graphics, pMouseX, pMouseY, pPartialTick);
    PoseStack stack = graphics.pose();
    resetTooltip();
    stack.pushPose();
    stack.translate(guiLeft, guiTop, 0);
    RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    drawBackground(graphics, pMouseX, pMouseY, pPartialTick);
    drawForeground(stack, pMouseX, pMouseY, pPartialTick);
    stack.popPose();
    for (Renderable renderable : this.renderables) {
      renderable.render(graphics, pMouseX, pMouseY, pPartialTick);
    }
    drawTooltip(graphics, pMouseX, pMouseY);
  }

  public void drawBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    ResourceLocation resourceLocation = getBackground();
    int width1 = getBackgroundWidth();
    int height1 = getBackgroundHeight();
    int fileWidth = getBackgroundWidth();
    int fileHeight = getBackgroundHeight();
    graphics.blit(resourceLocation, 0, 0, 0, 0, width1, height1, fileWidth, fileHeight);
  }

  public void drawForeground(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
  }
}
