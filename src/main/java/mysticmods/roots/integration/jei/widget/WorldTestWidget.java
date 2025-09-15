package mysticmods.roots.integration.jei.widget;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.widgets.IRecipeWidget;
import mysticmods.roots.client.RenderUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Map;

public record WorldTestWidget(int xOffset, int yOffset, int width, int height, BlockState state,
                              ItemStack asItem) implements IRecipeWidget {
  @Override
  public void getTooltip(ITooltipBuilder tooltip, double mouseX, double mouseY) {
    if (mouseX > 0 && mouseX <= width && mouseY > 0 && mouseY <= height) {
      // TODO:
      tooltip.addAll(asItem.getTooltipLines(Item.TooltipContext.of(Minecraft.getInstance().level), Minecraft.getInstance().player, Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL));
      for (Map.Entry<Property<?>, Comparable<?>> v : state.getValues().entrySet()) {
        tooltip.add(getPropertyValueString(v));
      }
    }
  }

  @Override
  public void drawWidget(GuiGraphics guiGraphics, double mouseX, double mouseY) {
    RenderUtil.renderBlock(guiGraphics, state, 12, 9, 0, 45f, 10f);
  }

  @Override
  public ScreenPosition getPosition() {
    return new ScreenPosition(xOffset, yOffset);
  }

  private Component getPropertyValueString(Map.Entry<Property<?>, Comparable<?>> entry) {
    Property<?> property = entry.getKey();
    Comparable<?> comparable = entry.getValue();
    String s = Util.getPropertyName(property, comparable);
    if (Boolean.TRUE.equals(comparable)) {
      s = ChatFormatting.GREEN + s;
    } else if (Boolean.FALSE.equals(comparable)) {
      s = ChatFormatting.RED + s;
    }

    return Component.literal(property.getName() + ": " + s);
  }
}
