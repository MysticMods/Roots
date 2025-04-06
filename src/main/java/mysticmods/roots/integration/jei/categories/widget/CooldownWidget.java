package mysticmods.roots.integration.jei.categories.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.widgets.IRecipeWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;

public class CooldownWidget implements IRecipeWidget {
  private final Component tooltip;
  private final int x, y;
  private final int cost;

  public CooldownWidget(int ticks, int x, int y, Component tooltip) {
    this.cost = ticks / 20;
    this.y = y;
    this.x = x;
    this.tooltip = tooltip;
  }

  @Override
  public void getTooltip(ITooltipBuilder tooltip, double mouseX, double mouseY) {
    if (mouseX > 0 && mouseX <= 16 && mouseY > 0 && mouseY <= 16) {
      tooltip.add(this.tooltip);
    }
  }

  @Override
  public ScreenPosition getPosition() {
    return new ScreenPosition(x, y);
  }

  @Override
  public void drawWidget(GuiGraphics guiGraphics, double mouseX, double mouseY) {
    Minecraft minecraft = Minecraft.getInstance();
    Font fontRenderer = minecraft.font;
    guiGraphics.drawString(fontRenderer, cost + "s", 0, 4, 0xe0e0e0);
    RenderSystem.setShaderColor(1, 1, 1, 1);
  }
}
