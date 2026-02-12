package mysticmods.roots.client.gui.screen;

import mysticmods.roots.integration.ClientIntegrationUtil;
import mysticmods.roots.inventory.fake.mortar.MortarContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class MortarScreen extends AbstractContainerScreen<MortarContainer> {

  public MortarScreen(MortarContainer menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
    imageHeight = 0;
    imageWidth = 0;
  }

  private boolean shown = false;

  @Override
  protected void init() {
    super.init();
    if (!shown) {
      // TODO: If there's no JEI or no recipes are shown this screen never closes
      ClientIntegrationUtil.showRecipesFor(MortarContainer.class);
      shown = true;
    }
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
  }

  @Override
  protected void containerTick() {
    super.containerTick();
    if (menu.hasRecipe()) {
      this.onClose();
    }
  }
}
