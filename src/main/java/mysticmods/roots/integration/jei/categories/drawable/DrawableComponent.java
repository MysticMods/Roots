package mysticmods.roots.integration.jei.categories.drawable;

import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class DrawableComponent implements IDrawable {
  private final FormattedCharSequence resolvedText;
  private final int color;
  private final int width;
  private final int height;
  private final int x, y;

  public DrawableComponent(Component text, int color, int x, int y) {
    Minecraft mc = Minecraft.getInstance();
    this.resolvedText = text.getVisualOrderText();
    this.width = mc.font.width(resolvedText);
    this.height = mc.font.lineHeight;
    this.color = color;
    this.x = x;
    this.y = y;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
    Minecraft minecraft = Minecraft.getInstance();
    Font fontRenderer = minecraft.font;
    guiGraphics.drawString(fontRenderer, resolvedText, xOffset + x, yOffset + y, color);
    RenderSystem.setShaderColor(1, 1, 1, 1);
  }
}
