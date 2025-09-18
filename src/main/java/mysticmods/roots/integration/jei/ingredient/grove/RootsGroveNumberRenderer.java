package mysticmods.roots.integration.jei.ingredient.grove;

import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mysticmods.roots.api.grove.GroveNumber;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class RootsGroveNumberRenderer implements IIngredientRenderer<GroveNumber> {
  @Override
  public void render(GuiGraphics guiGraphics, @Nullable GroveNumber ingredient) {
    render(guiGraphics, ingredient, 0, 0);
  }

  @Override
  public void render(GuiGraphics guiGraphics, @Nullable GroveNumber ingredient, int posX, int posY) {
    if (ingredient != null) {
      RenderSystem.enableDepthTest();

      Minecraft minecraft = Minecraft.getInstance();
      Font font = getFontRenderer(minecraft, ingredient);
      guiGraphics.renderFakeItem(ingredient.grove().getIcon(), posX, posY);
      // TODO: Render number here
      //guiGraphics.renderItemDecorations(font, ingredient.getIcon(), posX, posY);
      guiGraphics.pose().pushPose();
      String s = String.valueOf(ingredient.value());
      guiGraphics.pose().translate(0.0F, 0.0F, 200.0F);
      int color = 16777215;
      if (ingredient.value() < 0) {
        color = 16733525;
      }
      guiGraphics.drawString(font, s, posX + 19 - 2 - font.width(s), posY + 6 + 3, color, true);
      guiGraphics.pose().popPose();
      RenderSystem.disableBlend();
    }
  }

  // TODO:
  @SuppressWarnings("removal")
  @Override
  public List<Component> getTooltip(GroveNumber ingredient, TooltipFlag tooltipFlag) {
    return Collections.emptyList();
/*    Minecraft minecraft = Minecraft.getInstance();
    Player player = minecraft.player;
    Item.TooltipContext tooltipContext = Item.TooltipContext.of(minecraft.level);
    return ingredient.getIcon().getTooltipLines(tooltipContext, player, tooltipFlag);*/
  }

  @Override
  public void getTooltip(ITooltipBuilder tooltip, GroveNumber ingredient, TooltipFlag tooltipFlag) {
/*    Minecraft minecraft = Minecraft.getInstance();
    Player player = minecraft.player;
    Item.TooltipContext tooltipContext = Item.TooltipContext.of(minecraft.level);
    List<Component> tooltipLines = ingredient.getIcon().getTooltipLines(tooltipContext, player, tooltipFlag);
    tooltip.addAll(tooltipLines);*/
  }

  @Override
  public int getWidth() {
    return 16;
  }

  @Override
  public int getHeight() {
    return 16;
  }
}
