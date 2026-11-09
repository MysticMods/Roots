package mysticmods.roots.integration.jei.ingredient.grove;

import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mysticmods.roots.api.reference.Keys;
import mysticmods.roots.client.RootsClientHooks;
import mysticmods.roots.condition.GroveRankReputation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RootsGroveRankRenderer implements IIngredientRenderer<GroveRankReputation> {
  @Override
  public void render(GuiGraphics guiGraphics, @Nullable GroveRankReputation ingredient) {
    render(guiGraphics, ingredient, 0, 0);
  }

  @Override
  public void render(GuiGraphics guiGraphics, @Nullable GroveRankReputation ingredient, int posX, int posY) {
    if (ingredient != null) {
      RenderSystem.enableDepthTest();

      Minecraft minecraft = Minecraft.getInstance();
      Font font = getFontRenderer(minecraft, ingredient);
      guiGraphics.renderFakeItem(ingredient.grove().getIcon(), posX, posY);
      guiGraphics.pose().pushPose();
      guiGraphics.pose().translate(0.0F, 0.0F, 200.0F);
      Component text = ingredient.minimumRank().getShortName();
      guiGraphics.drawString(font, text, posX + 19 - 2 - font.width(text), posY + 6 + 3, 16777215, true);
      guiGraphics.pose().popPose();
      RenderSystem.disableBlend();
    }
  }

  // TODO:
  @SuppressWarnings("removal")
  @Override
  public List<Component> getTooltip(GroveRankReputation ingredient, TooltipFlag tooltipFlag) {
    List<Component> result = new ArrayList<>();
    result.add(Component.translatable("roots.grove.ranked_grove", ingredient.grove().getStyledName(), ingredient.minimumRank().getName()));
    result.add(Component.empty());
    result.add(Component.translatable("roots.tooltip.reputation.open_reputation", RootsClientHooks.getKey(tooltipFlag, Keys.OPEN_REPUTATION)));
    return result;
  }

  @Override
  public void getTooltip(ITooltipBuilder tooltip, GroveRankReputation ingredient, TooltipFlag tooltipFlag) {
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
