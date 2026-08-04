package mysticmods.roots.integration.jei.ingredient.grove;

import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RootsGroveActionRenderer implements IIngredientRenderer<GroveAction> {
  public RootsGroveActionRenderer() {
  }

  @Override
  public void render(GuiGraphics guiGraphics, @Nullable GroveAction ingredient) {
    render(guiGraphics, ingredient, 0, 0);
  }

  @Override
  public void render(GuiGraphics guiGraphics, @Nullable GroveAction ingredient, int posX, int posY) {
    if (ingredient != null) {
      RenderSystem.enableDepthTest();

      Minecraft minecraft = Minecraft.getInstance();
      TextureAtlasSprite sprite = minecraft.getTextureAtlas(RootsAPI.OVERLAYS_ATLAS_FILE)
          .apply(RootsAPI.rl("gui/grove_action_symbol"));
      Font font = getFontRenderer(minecraft, ingredient);
      // TODO: Cache this
      Item item = ingredient.builtInRegistryHolder().getData(DataMaps.GROVE_ACTION_ICONS);
      if (item == null) {
        RootsAPI.LOG.error("No item associated with grove action {}.", ingredient);
      }
      ItemStack stack = new ItemStack(item);
      guiGraphics.renderFakeItem(stack, posX, posY);
      guiGraphics.pose().pushPose();
      guiGraphics.pose().translate(0, 0, 200);
      guiGraphics.blit(posX, posY, 0, 16, 16, sprite);
      guiGraphics.pose().popPose();
      //guiGraphics.renderItemDecorations(font, ingredient.getIcon(), posX, posY);
      RenderSystem.disableBlend();
    }
  }

  // TODO:
  @SuppressWarnings("removal")
  @Override
  public List<Component> getTooltip(GroveAction ingredient, TooltipFlag tooltipFlag) {
    List<Component> result = new ArrayList<>();
    result.add(ingredient.getName());
    if (tooltipFlag.isAdvanced()) {
      result.add(Component.literal(RootsRegistries.GROVE_ACTIONS.getKey(ingredient).toString())
          .withStyle(ChatFormatting.DARK_GRAY));
    }
    return result;
  }

  @Override
  public void getTooltip(ITooltipBuilder tooltip, GroveAction ingredient, TooltipFlag tooltipFlag) {
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
