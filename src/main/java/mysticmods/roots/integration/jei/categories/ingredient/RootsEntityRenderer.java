package mysticmods.roots.integration.jei.categories.ingredient;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mysticmods.roots.client.RenderUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class RootsEntityRenderer implements IIngredientRenderer<RootsEntityType> {
  private final int size;

  public RootsEntityRenderer(int size) {
    this.size = size;
  }

  @Override
  public int getWidth() {
    return size;
  }

  @Override
  public int getHeight() {
    return size;
  }

  @Override
  public void render(GuiGraphics guiGraphics, RootsEntityType ingredient) {
    RenderUtil.renderEntity(guiGraphics, ingredient.entity(), this.size);
  }

  @SuppressWarnings("removal")
  @Override
  public List<Component> getTooltip(RootsEntityType ingredient, TooltipFlag tooltipFlag) {
    return List.of();
  }

  @Override
  public void getTooltip(ITooltipBuilder tooltip, RootsEntityType ingredient, TooltipFlag tooltipFlag) {
    tooltip.addAll(RenderUtil.getMobTooltip(ingredient.entity()));
  }
}
