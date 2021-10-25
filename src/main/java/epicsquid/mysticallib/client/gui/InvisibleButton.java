package epicsquid.mysticallib.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nonnull;
import java.awt.*;

public class InvisibleButton extends GuiButton {

  public static boolean DEBUG_LOCATION = false;

  public InvisibleButton(int buttonId, int x, int y, int widthIn, int heightIn, @Nonnull String buttonText) {
    super(buttonId, x, y, widthIn, heightIn, buttonText);
  }

  @Override
  public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
    if (this.visible) {
      this.mouseDragged(mc, mouseX, mouseY);

      if (DEBUG_LOCATION) {
        GuiContainer.drawRect(x, y, x + width, y + height, Color.BLUE.getRGB());
      }

      if (!displayString.isEmpty()) {
        // largely cribbed from GuiButton::DrawButton
        zLevel = 200;

        FontRenderer fontrenderer = mc.fontRenderer;
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.hovered = mousePressed(mc, mouseX, mouseY);

        GlStateManager.enableDepth();
        GlStateManager.disableBlend();

        this.mouseDragged(mc, mouseX, mouseY);

        int j = 0xFFE0E0E0;

        if (packedFGColour != 0) {
          j = packedFGColour;
        } else if (!this.enabled) {
          j = 0xFFA0A0A0;
        } else if (this.hovered) {
          j = 0xFFFFFFA0;
        }

        this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);

        zLevel = 0;
      }
    }
  }
}
