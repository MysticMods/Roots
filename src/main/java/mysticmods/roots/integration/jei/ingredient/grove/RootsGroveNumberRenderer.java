package mysticmods.roots.integration.jei.ingredient.grove;

import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.grove.GroveNumber;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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
      TextureAtlasSprite sprite = switch(ingredient.type()) {
        case POWER -> minecraft.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
            .apply(RootsAPI.rl("gui/grove_power_symbol"));
        case REPUTATION -> minecraft.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
            .apply(RootsAPI.rl("gui/grove_reputation_symbol"));
      };
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
      guiGraphics.pose().translate(0, 0, 200);
      guiGraphics.blit(posX, posY, 0, 16, 16, sprite);
      guiGraphics.pose().popPose();
      RenderSystem.disableBlend();
    }
  }

  // TODO:
  @SuppressWarnings("removal")
  @Override
  public List<Component> getTooltip(GroveNumber ingredient, TooltipFlag tooltipFlag) {
    List<Component> result = new ArrayList<>();
    result.add(ingredient.grove().getStyledName());
    result.add(Component.literal(String.valueOf(ingredient.value())));
    if (tooltipFlag.isAdvanced()) {
      result.add(Component.literal(RootsRegistries.GROVES.getKey(ingredient.grove()).toString())
          .withStyle(ChatFormatting.DARK_GRAY));
    }
    return result;
  }

  @Override
  public void getTooltip(ITooltipBuilder tooltip, GroveNumber ingredient, TooltipFlag tooltipFlag) {
    tooltip.addAll(getTooltip(ingredient, tooltipFlag));
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
