package mysticmods.roots.client.gui.buttons;

import com.mojang.blaze3d.vertex.PoseStack;
import mysticmods.roots.api.SpellLike;
import mysticmods.roots.client.ItemCache;
import mysticmods.roots.client.RenderUtil;
import mysticmods.roots.client.gui.SpellSupplier;
import mysticmods.roots.client.gui.screen.RootsScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class TypedButton<S extends SpellLike, T extends SpellSupplier<S>, V extends RootsScreen> extends Button {
  protected final int id;
  protected final V parentScreen;
  protected final T spellSupplier;

  public TypedButton(V parentScreen, T spellSupplier, int id, int pX, int pY, int pWidth, int pHeight, OnPress pOnPress) {
    super(pX, pY, pWidth, pHeight, Component.empty(), pOnPress);
    this.parentScreen = parentScreen;
    this.spellSupplier = spellSupplier;
    this.id = id;
  }

  public int getId() {
    return id;
  }

  @Override
  public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
    if (visible) {
      // Draw the actual spell

      RenderUtil.renderItemAsIcon(spellSupplier.getAsItemStack(), pPoseStack, x, y, 16, false);

      if (parentScreen.isMouseInRelativeRange(pMouseX, pMouseY, x, y, width, height)) {
        // Draw the tooltip
      }
    }
  }
}
