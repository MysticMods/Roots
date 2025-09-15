package mysticmods.roots.integration.jei.ingredient.ritual;

import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mysticmods.roots.api.ritual.Ritual;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RootsRitualRenderer implements IIngredientRenderer<Ritual> {
  @Override
  public void render(GuiGraphics guiGraphics, @Nullable Ritual ingredient) {
    render(guiGraphics, ingredient, 0, 0);
  }

  @Override
  public void render(GuiGraphics guiGraphics, @Nullable Ritual ingredient, int posX, int posY) {
    if (ingredient != null) {
      RenderSystem.enableDepthTest();

      Minecraft minecraft = Minecraft.getInstance();
      Font font = getFontRenderer(minecraft, ingredient);
      guiGraphics.renderFakeItem(ingredient.getIcon(), posX, posY);
      guiGraphics.renderItemDecorations(font, ingredient.getIcon(), posX, posY);
      RenderSystem.disableBlend();
    }
  }

  @SuppressWarnings("removal")
  @Override
  public List<Component> getTooltip(Ritual ingredient, TooltipFlag tooltipFlag) {
    Minecraft minecraft = Minecraft.getInstance();
    Player player = minecraft.player;
    Item.TooltipContext tooltipContext = Item.TooltipContext.of(minecraft.level);
    return ingredient.getIcon().getTooltipLines(tooltipContext, player, tooltipFlag);
  }

  @Override
  public void getTooltip(ITooltipBuilder tooltip, Ritual ingredient, TooltipFlag tooltipFlag) {
    Minecraft minecraft = Minecraft.getInstance();
    Player player = minecraft.player;
    Item.TooltipContext tooltipContext = Item.TooltipContext.of(minecraft.level);
    List<Component> tooltipLines = ingredient.getIcon().getTooltipLines(tooltipContext, player, tooltipFlag);
    tooltip.addAll(tooltipLines);
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
