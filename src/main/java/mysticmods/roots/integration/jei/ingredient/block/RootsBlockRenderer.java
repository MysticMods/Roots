package mysticmods.roots.integration.jei.ingredient.block;

import mezz.jei.api.ingredients.IIngredientRenderer;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;

public class RootsBlockRenderer<T extends IBlockType> implements IIngredientRenderer<T> {
  public RootsBlockRenderer() {
  }

  @Override
  public int getWidth() {
    return 16;
  }

  @Override
  public int getHeight() {
    return 16;
  }

  @Override
  public void render(GuiGraphics guiGraphics, T ingredient) {
    render(guiGraphics, ingredient, 0, 0);
  }

  @Override
  public void render(GuiGraphics guiGraphics, T ingredient, int posX, int posY) {
    if (ingredient != null) {
      RenderUtil.renderBlock(guiGraphics, ingredient.state(), posX + 8, posY + 5, 0, 45f, 10f);
    }
  }

  @SuppressWarnings("removal")
  @Override
  public List<Component> getTooltip(T ingredient, TooltipFlag tooltipFlag) {
    Minecraft minecraft = Minecraft.getInstance();
    Player player = minecraft.player;
    Item.TooltipContext tooltipContext = Item.TooltipContext.of(minecraft.level);
    List<Component> tooltip;
    if (ingredient.stack().isEmpty()) {
      RootsAPI.LOG.error("Empty itemstack for block {}, needs a custom tooltip", ingredient.block());
      // TODO: Determine from the blockstate
      tooltip = new ArrayList<>();
    } else {
      tooltip = ingredient.stack().getTooltipLines(tooltipContext, player, tooltipFlag);
    }
    tooltip.addAll(ingredient.additionalTooltipLines());
    return tooltip;
  }
}
