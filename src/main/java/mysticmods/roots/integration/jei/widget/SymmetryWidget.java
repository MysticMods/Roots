package mysticmods.roots.integration.jei.widget;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.widgets.IRecipeWidget;
import mysticmods.roots.api.grove.GrovePowerGenerator;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class SymmetryWidget implements IRecipeWidget {
  private final Component tooltip;
  private final int x, y;
  private final GrovePowerGenerator.Symmetry symmetry;

  public SymmetryWidget(int x, int y, GrovePowerGenerator.Symmetry symmetry) {
    this.y = y;
    this.x = x;
    this.tooltip = symmetry.getTooltip();
    this.symmetry = symmetry;
  }

  @Override
  public void getTooltip(ITooltipBuilder tooltip, double mouseX, double mouseY) {
    if (mouseX > 0 && mouseX <= 16 && mouseY > 0 && mouseY <= 16) {
      tooltip.add(symmetry.getName());
      tooltip.add(CommonComponents.EMPTY);
      tooltip.add(symmetry.getTooltip());
    }
  }

  @Override
  public ScreenPosition getPosition() {
    return new ScreenPosition(x, y);
  }

  @Override
  public void drawWidget(GuiGraphics guiGraphics, double mouseX, double mouseY) {
    RootsJEIPlugin.GROVE_POWER_SYMMETRY_DRAWABLES.get(symmetry).draw(guiGraphics, 0, 0);
  }
}