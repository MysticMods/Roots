package mysticmods.roots.integration.jei.categories.widget;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.widgets.IRecipeWidget;
import mezz.jei.api.ingredients.ITypedIngredient;
import mysticmods.roots.client.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.state.BlockState;

public record WorldTestWidget(int xOffset, int yOffset, int width, int height, BlockState state,
                              ITypedIngredient<ItemStack> ingredient) implements IRecipeWidget {
  @Override
  public void getTooltip(ITooltipBuilder tooltip, double mouseX, double mouseY) {
    if (mouseX > 0 && mouseX <= width && mouseY > 0 && mouseY <= height) {
      // TODO:
      if (ingredient != null) {
        tooltip.setIngredient(ingredient);
      }
    }
  }

  @Override
  public void drawWidget(GuiGraphics guiGraphics, double mouseX, double mouseY) {
    RenderUtil.renderBlock(guiGraphics, state, 12, 10, 0, 45f, 10f);
  }

  @Override
  public ScreenPosition getPosition() {
    return new ScreenPosition(xOffset, yOffset);
  }
}
