package mysticmods.roots.integration.jei.ingredient.cooldown;

import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mysticmods.roots.api.recipe.output.Cooldown;
import mysticmods.roots.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RootsCooldownIngredientRenderer implements IIngredientRenderer<Cooldown> {
  @Override
  public void render(GuiGraphics guiGraphics, @Nullable Cooldown ingredient) {
    render(guiGraphics, ingredient, 0, 0);
  }

  @Override
  public void render(GuiGraphics guiGraphics, @Nullable Cooldown ingredient, int posX, int posY) {
    if (ingredient != null) {
      RenderSystem.enableDepthTest();

      Minecraft minecraft = Minecraft.getInstance();
      Font font = getFontRenderer(minecraft, ingredient);
      guiGraphics.renderFakeItem(new ItemStack(ModItems.COOLDOWN.get()), posX, posY);
      guiGraphics.pose().pushPose();
      guiGraphics.pose().translate(0.0F, 0.0F, 200.0F);
      Component text = ingredient.asNumber();
      guiGraphics.drawString(font, text, posX + 17 - font.width(text), posY + 6 + 3, 16777215, false);
      guiGraphics.pose().popPose();
      RenderSystem.disableBlend();
    }
  }

  // TODO:
  @SuppressWarnings("removal")
  @Override
  public List<Component> getTooltip(Cooldown ingredient, TooltipFlag tooltipFlag) {
    List<Component> result = new ArrayList<>();
    result.add(ingredient.asComponent());
    result.add(Component.empty());
    return result;
  }

  @Override
  public void getTooltip(ITooltipBuilder tooltip, Cooldown ingredient, TooltipFlag tooltipFlag) {
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
