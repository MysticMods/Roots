package mysticmods.roots.integration.jei.categories.widget;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.widgets.IRecipeWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.network.chat.Component;

public class GrovePowerWidget implements IRecipeWidget {
  private final Component tooltip;
  private final int x, y;
  private final int cost;

  public GrovePowerWidget(int cost, int x, int y, Component tooltip) {
    this.cost = cost;
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
    guiGraphics.drawString(fontRenderer, String.valueOf(cost), 0, 4, 0xe0e0e0);
  }
}
