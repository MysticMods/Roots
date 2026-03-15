package mysticmods.roots.client.gui.screen;

import mysticmods.roots.api.client.ModifierWidget;
import mysticmods.roots.api.modifier.IModifierNode;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.modifier.ModifierTree;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModifierTab<V, T extends Modifier<V, T>> {
  private final Map<ResourceKey<T>, ModifierWidget<V, T>> widgets = new HashMap<>();
  private final ModifierTree<V, T>.Instance tree;

  public ModifierTab(ModifierTree<V, T>.Instance tree) {
    this.tree = tree;

    for (IModifierNode<V, T> node : tree.tree().all()) {
      widgets.put(node.key(), new ModifierWidget<>(this, node));
    }
  }

  private List<ModifierWidget<V, T>> roots = null;

  public List<ModifierWidget<V, T>> roots () {
    if (roots == null) {
      this.roots = tree.tree().rootNodes().stream().map(node -> widgets.get(node.key())).toList();
    }
    return this.roots;
  }

  public ModifierTree<V, T> getTree() {
    return tree.tree();
  }

  @Nullable
  public ModifierWidget<V, T> getWidget(IModifierNode<V, T> node) {
    if (node == null) {
      return null;
    }
    return widgets.get(node.key());
  }

  public void drawTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, int width, int height) {
    for (ModifierWidget<V, T> widget : widgets.values()) {
      if (widget.isMouseOver(0, 0, mouseX, mouseY)) {
        widget.drawHover(guiGraphics, 0, 0, 0, width, height);
      }
    }
  }
}
