package mysticmods.roots.client.gui.buttons;

import com.mojang.blaze3d.vertex.PoseStack;
import mysticmods.roots.api.SpellLike;
import mysticmods.roots.client.RenderUtil;
import mysticmods.roots.client.gui.screen.RootsScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class TypedButton<S extends SpellLike, V extends RootsScreen> extends Button {
  protected final int id;
  protected final V parentScreen;
  protected final Supplier<S> spellSupplier;

  public TypedButton(V parentScreen, @NotNull Supplier<S> spellSupplier, int id, int pX, int pY, int pWidth, int pHeight, OnPress pOnPress) {
    super(pX, pY, pWidth, pHeight, Component.empty(), pOnPress, DEFAULT_NARRATION);
    this.parentScreen = parentScreen;
    this.spellSupplier = spellSupplier;
    this.id = id;
  }

  public int getId() {
    return id;
  }

  @Override
  public void renderWidget(GuiGraphics arg, int pMouseX, int pMouseY, float pPartialTick) {
    if (spellSupplier.get() == null) {
      return;
    }
    if (visible) {
      // Draw the actual spell

      RenderUtil.renderItemAsIcon(spellSupplier.get().asSpell()
          .getIcon(), arg.pose(), this.getX(), this.getY(), 16, isTransparent());

      if (parentScreen.isMouseInRelativeRange(pMouseX, pMouseY, this.getX(), this.getY(), width, height)) {
        // Draw the tooltip
        renderToolTip(arg.pose(), pMouseX, pMouseY);
      }
    }
  }

  public void renderToolTip(PoseStack pPoseStack, int pMouseX, int pMouseY) {
    if (spellSupplier.get() == null) {
      return;
    }
    parentScreen.fillTooltip(spellSupplier.get().asSpell().getIcon());
  }

  public boolean isTransparent() {
    return false;
  }
}
