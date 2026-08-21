package mysticmods.roots.client.gui.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.gui.screen.fake.StaffScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.CommonComponents;

public class ModifierButton extends Button {
  private final WidgetSprites SPRITES = new WidgetSprites(
      RootsAPI.rl("buttons/modify_icon_normal"),
      RootsAPI.rl("buttons/modify_icon_disabled"),
      RootsAPI.rl("buttons/modify_icon_hover"),
      RootsAPI.rl("buttons/modify_icon_disabled")
  );

  public ModifierButton(StaffScreen parentScreen, int x, int y) {
    super(x, y, 10, 10, CommonComponents.EMPTY, parentScreen::buttonClicked, DEFAULT_NARRATION);
  }

  @Override
  protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
    RenderSystem.enableBlend();
    RenderSystem.enableDepthTest();
    guiGraphics.blitSprite(SPRITES.get(this.active, this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
    guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
  }
}
