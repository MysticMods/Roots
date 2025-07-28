package mysticmods.roots.integration.jei.categories.widget;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.widgets.IRecipeWidget;
import mysticmods.roots.api.condition.IPlayerCondition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.network.chat.Component;

public record PlayerConditionWidget(int xOffset, int yOffset, int width, int height, Component name,
                                    Component description) implements IRecipeWidget {
  public static PlayerConditionWidget create(int maxWidth, int yOffset, IPlayerCondition condition) {
    Minecraft mc = Minecraft.getInstance();
    int width = mc.font.width(condition.getName());
    return new PlayerConditionWidget(
        maxWidth - width,
        yOffset,
        width,
        18,
        condition.getNameComponent(),
        condition.getDescriptionComponent()
    );
  }

  @Override
  public void getTooltip(ITooltipBuilder tooltip, double mouseX, double mouseY) {
    if (mouseX > 0 && mouseX <= width && mouseY > 0 && mouseY <= height) {
      tooltip.add(name);
      tooltip.add(Component.empty());
      tooltip.add(description);
    }
  }

  @Override
  public void drawWidget(GuiGraphics guiGraphics, double mouseX, double mouseY) {
    Minecraft minecraft = Minecraft.getInstance();
    guiGraphics.drawString(minecraft.font, name, 0, 0, 0xFFFFFFFF, true);
  }

  @Override
  public ScreenPosition getPosition() {
    return new ScreenPosition(xOffset, yOffset);
  }
}
