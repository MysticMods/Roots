package mysticmods.roots.integration.jei.categories.widget;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.widgets.IRecipeWidget;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.network.chat.Component;

public class InfoWidget implements IRecipeWidget {
  private final Component tooltip;
  private final int x, y;

  public InfoWidget(int x, int y, Component tooltip) {
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
    RootsJEIPlugin.INFO_DRAWABLE.draw(guiGraphics, 0, 0);
  }
}