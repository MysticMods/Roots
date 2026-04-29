package mysticmods.roots.api.client;

import mysticmods.roots.api.RootsItemCallbacks;
import mysticmods.roots.api.modifier.IModifierNode;
import mysticmods.roots.api.modifier.Modifier;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public class RootModifierWidget<V, T extends Modifier<V, T>> extends ModifierWidget<V, T> {
  public RootModifierWidget(ModifierTab<V, T> tab, IModifierNode<V, T> node) {
    super(tab, node);
    this.renderStack = RootsItemCallbacks.getItemStackGeneric(tab.getTree().getObject().getKey());
  }

  @Nullable
  @Override
  public RootModifierWidget<V, T> parent() {
    return null;
  }

  private List<ModifierWidget<V, T>> children = null;

  @Override
  public Iterable<ModifierWidget<V, T>> children() {
    if (children == null) {
      if (!node.children().isEmpty()) {
        children = node.children().stream().sorted(Comparator.comparing(object -> object.key().location()))
            .map(tab::getWidget).toList();
      } else {
        children = List.of();
      }
    }
    return children;
  }

  @Override
  public void drawConnectivity(GuiGraphics guiGraphics, int x, int y, boolean dropShadow) {
    for (ModifierWidget<V, T> widget : children()) {
      widget.drawConnectivity(guiGraphics, x, y, dropShadow);
    }
  }

  public void drawHover(GuiGraphics guiGraphics, int x, int y, float fade, int width, int height) {

  }

  public boolean isMouseOver(int x, int y, int mouseX, int mouseY) {
    int i = x + this.x;
    int j = i + 26;
    int k = y + this.y;
    int l = k + 26;
    return mouseX >= i && mouseX <= j && mouseY >= k && mouseY <= 1;
  }
}
