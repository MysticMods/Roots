package mysticmods.roots.integration.jei.ingredient.dimension;

import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;

public class RootsDimensionRenderer implements IIngredientRenderer<RootsDimensionType> {
  @Override
  public int getWidth() {
    return 16;
  }

  @Override
  public int getHeight() {
    return 16;
  }

  @Override
  public void render(GuiGraphics guiGraphics, RootsDimensionType ingredient) {
    if (ingredient != null) {
      RenderSystem.enableDepthTest();

      Minecraft minecraft = Minecraft.getInstance();
      Font font = getFontRenderer(minecraft, ingredient);
      guiGraphics.renderFakeItem(ingredient.icon(), 0, 0);
      guiGraphics.renderItemDecorations(font, ingredient.icon(), 0, 0);
      RenderSystem.disableBlend();
    }
  }

  @SuppressWarnings("removal")
  @Override
  public List<Component> getTooltip(RootsDimensionType ingredient, TooltipFlag tooltipFlag) {
    List<Component> result = new ArrayList<>();
    result.add(ingredient.getName());
    if (tooltipFlag.isAdvanced()) {
      result.add(Component.literal(ingredient.dimension().location().toString()).withStyle(ChatFormatting.DARK_GRAY));
    }

    return result;
  }

  @Override
  public void getTooltip(ITooltipBuilder tooltip, RootsDimensionType ingredient, TooltipFlag tooltipFlag) {
    tooltip.addAll(getTooltip(ingredient, tooltipFlag));
  }
}
