package mysticmods.roots.client.gui.screen;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.inventory.pouch.apothecary.ApothecaryPouchContainer;
import mysticmods.roots.inventory.pouch.herb.HerbPouchContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ApothecaryPouchScreen extends AbstractContainerScreen<ApothecaryPouchContainer> {
  private static final ResourceLocation BACKGROUND = RootsAPI.rl("textures/gui/herb_pouch_gui.png");

  public ApothecaryPouchScreen(ApothecaryPouchContainer menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
    imageHeight = 207;
    imageWidth = 176;
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
