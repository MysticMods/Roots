package mysticmods.roots.client.gui.buttons;

import com.mojang.blaze3d.vertex.PoseStack;
import mysticmods.roots.api.SpellLike;
import mysticmods.roots.client.RenderUtil;
import mysticmods.roots.client.gui.SpellSupplier;
import mysticmods.roots.client.gui.screen.RootsScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class TypedButton<S extends SpellLike, T extends SpellSupplier<S>, V extends RootsScreen> extends Button {
  protected final int id;
  protected final V parentScreen;
  protected final T spellSupplier;

  //     protected Button(int i, int j, int k, int l, Component arg, OnPress arg2, CreateNarration arg3) {

  public TypedButton(V parentScreen, T spellSupplier, int id, int pX, int pY, int pWidth, int pHeight, OnPress pOnPress, CreateNarration narration) {
    super(pX, pY, pWidth, pHeight, Component.empty(), pOnPress, narration);
    this.parentScreen = parentScreen;
    this.spellSupplier = spellSupplier;
    this.id = id;
  }

  public int getId() {
    return id;
  }

  @Override
  public void renderWidget(GuiGraphics arg, int pMouseX, int pMouseY, float pPartialTick) {
    if (visible) {
      // Draw the actual spell

      RenderUtil.renderItemAsIcon(spellSupplier.getAsItemStack(), arg.pose(), this.getX(), this.getY(), 16, isTransparent());

      if (parentScreen.isMouseInRelativeRange(pMouseX, pMouseY, this.getX(), this.getY(), width, height)) {
        // Draw the tooltip
        renderToolTip(arg.pose(), pMouseX, pMouseY);
      }
    }
  }

  // TODO: Tooltips have changed
  public void renderToolTip(PoseStack pPoseStack, int pMouseX, int pMouseY) {
    parentScreen.fillTooltip(spellSupplier.getAsItemStack());
  }

  public boolean isTransparent() {
    return false;
  }
}
