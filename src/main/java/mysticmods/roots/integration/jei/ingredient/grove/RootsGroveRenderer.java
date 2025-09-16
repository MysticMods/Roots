package mysticmods.roots.integration.jei.ingredient.grove;

import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mysticmods.roots.api.grove.Grove;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class RootsGroveRenderer implements IIngredientRenderer<Grove> {
  @Override
  public void render(GuiGraphics guiGraphics, @Nullable Grove ingredient) {
    render(guiGraphics, ingredient, 0, 0);
  }

  @Override
  public void render(GuiGraphics guiGraphics, @Nullable Grove ingredient, int posX, int posY) {
    if (ingredient != null) {
      RenderSystem.enableDepthTest();

      Minecraft minecraft = Minecraft.getInstance();
      Font font = getFontRenderer(minecraft, ingredient);
      guiGraphics.renderFakeItem(ingredient.getIcon(), posX, posY);
      guiGraphics.renderItemDecorations(font, ingredient.getIcon(), posX, posY);
      RenderSystem.disableBlend();
    }
  }

  // TODO:
  @SuppressWarnings("removal")
  @Override
  public List<Component> getTooltip(Grove ingredient, TooltipFlag tooltipFlag) {
    return Collections.emptyList();
/*    Minecraft minecraft = Minecraft.getInstance();
    Player player = minecraft.player;
    Item.TooltipContext tooltipContext = Item.TooltipContext.of(minecraft.level);
    return ingredient.getIcon().getTooltipLines(tooltipContext, player, tooltipFlag);*/
  }

  @Override
  public void getTooltip(ITooltipBuilder tooltip, Grove ingredient, TooltipFlag tooltipFlag) {
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
