package mysticmods.roots.client.gui.screen;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.inventory.pouch.sylvan.SylvanPouchContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SylvanPouchScreen extends AbstractContainerScreen<SylvanPouchContainer> {
  private static final ResourceLocation BACKGROUND = RootsAPI.rl("textures/gui/sylvan_pouch_gui.png");

  public SylvanPouchScreen(SylvanPouchContainer menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
    imageHeight = 231;
    imageWidth = 239;
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    super.render(guiGraphics, mouseX, mouseY, partialTick);
    this.renderTooltip(guiGraphics, mouseX, mouseY);
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
    int i = (this.width - this.imageWidth) / 2;
    int j = (this.height - this.imageHeight) / 2;
    guiGraphics.blit(BACKGROUND, i, j, 0, 0, this.imageWidth, this.imageHeight);
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    // NO-OP
  }
}
