package mysticmods.roots.integration.jei.widget;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.widgets.IRecipeWidget;
import mysticmods.roots.client.RenderUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public record LevelConditionWidget(int xOffset, int yOffset, int width, int height, List<BlockState> states,
                                   Component tooltip, @Nullable Component description) implements IRecipeWidget {
  @Override
  public void getTooltip(ITooltipBuilder tooltip, double mouseX, double mouseY) {
    if (mouseX > 0 && mouseX <= width && mouseY > 0 && mouseY <= height) {
      tooltip.add(this.tooltip);
      if (description != null) {
        tooltip.add(Component.empty());
        tooltip.add(description);
      }
    }
  }

  @Override
  public void drawWidget(GuiGraphics guiGraphics, double mouseX, double mouseY) {
    int row = 0;
    int count = states.size();
    for (BlockState state : states) {
      RenderUtil.renderBlock(guiGraphics, state, 10, (count - row) * 5.2f, row * 3, 45f, 6f);
      row++;
    }
  }

  @Override
  public ScreenPosition getPosition() {
    return new ScreenPosition(xOffset, yOffset);
  }
}
