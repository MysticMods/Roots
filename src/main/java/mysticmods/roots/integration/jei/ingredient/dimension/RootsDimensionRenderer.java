package mysticmods.roots.integration.jei.ingredient.dimension;

import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class RootsDimensionRenderer implements IIngredientRenderer<DimensionType> {
  @Override
  public int getWidth() {
    return 16;
  }

  @Override
  public int getHeight() {
    return 16;
  }

  @Override
  public void render(GuiGraphics guiGraphics, DimensionType ingredient) {
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
  public List<Component> getTooltip(DimensionType ingredient, TooltipFlag tooltipFlag) {
    Minecraft minecraft = Minecraft.getInstance();
    Player player = minecraft.player;
    Item.TooltipContext tooltipContext = Item.TooltipContext.of(minecraft.level);
    return ingredient.icon().getTooltipLines(tooltipContext, player, tooltipFlag);
  }

  @Override
  public void getTooltip(ITooltipBuilder tooltip, DimensionType ingredient, TooltipFlag tooltipFlag) {
    Minecraft minecraft = Minecraft.getInstance();
    Player player = minecraft.player;
    Item.TooltipContext tooltipContext = Item.TooltipContext.of(minecraft.level);
    List<Component> tooltipLines = ingredient.icon().getTooltipLines(tooltipContext, player, tooltipFlag);
    tooltip.addAll(tooltipLines);
  }
}
