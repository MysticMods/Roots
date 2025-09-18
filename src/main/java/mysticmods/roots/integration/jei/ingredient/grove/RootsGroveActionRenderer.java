package mysticmods.roots.integration.jei.ingredient.grove;

import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.datamap.DataMaps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class RootsGroveActionRenderer implements IIngredientRenderer<GroveAction> {
  @Override
  public void render(GuiGraphics guiGraphics, @Nullable GroveAction ingredient) {
    render(guiGraphics, ingredient, 0, 0);
  }

  @Override
  public void render(GuiGraphics guiGraphics, @Nullable GroveAction ingredient, int posX, int posY) {
    if (ingredient != null) {
      RenderSystem.enableDepthTest();

      Minecraft minecraft = Minecraft.getInstance();
      Font font = getFontRenderer(minecraft, ingredient);
      Item item = ingredient.builtInRegistryHolder().getData(DataMaps.GROVE_ACTION_ICONS);
      ItemStack stack = new ItemStack(item);
      guiGraphics.renderFakeItem(stack, posX, posY);
      //guiGraphics.renderItemDecorations(font, ingredient.getIcon(), posX, posY);
      RenderSystem.disableBlend();
    }
  }

  // TODO:
  @SuppressWarnings("removal")
  @Override
  public List<Component> getTooltip(GroveAction ingredient, TooltipFlag tooltipFlag) {
    return Collections.emptyList();
/*    Minecraft minecraft = Minecraft.getInstance();
    Player player = minecraft.player;
    Item.TooltipContext tooltipContext = Item.TooltipContext.of(minecraft.level);
    return ingredient.getIcon().getTooltipLines(tooltipContext, player, tooltipFlag);*/
  }

  @Override
  public void getTooltip(ITooltipBuilder tooltip, GroveAction ingredient, TooltipFlag tooltipFlag) {
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
