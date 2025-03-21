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

public class DurabilityWidget implements IRecipeWidget {
  private final Component tooltip;
  private final int x, y;
  private final int cost;

  public DurabilityWidget(int cost, int x, int y, Component tooltip) {
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
    RenderSystem.setShaderColor(1, 1, 1, 1);

    int maxWidth = 13;
    int l = Math.max(1, Math.round((float) cost * maxWidth / 100f));
    int i = 0x00ff00;
    int j = 0;
    int k = 0;
    guiGraphics.fill(RenderType.guiOverlay(), j, k, j + 13, k + 2, -16777216);
    guiGraphics.fill(RenderType.guiOverlay(), j, k, j + l, k + 1, i | 0xFF000000);
  }
}
