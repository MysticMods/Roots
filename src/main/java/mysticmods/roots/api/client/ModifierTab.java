package mysticmods.roots.api.client;

import mysticmods.roots.api.modifier.IModifierNode;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.modifier.ModifierTree;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ModifierTab<V, T extends Modifier<V, T>> {
  private final Map<ResourceKey<T>, ModifierWidget<V, T>> widgets = new HashMap<>();
  private final ModifierTree<V, T>.Instance tree;
  private final RootModifierWidget<V, T> root;
  private final List<ModifierWidget<V, T>> children = new ArrayList<>();
  public final int x, y;

  public ModifierTab(ModifierTree<V, T>.Instance tree, WidgetBuilder<V, T> builder, int x, int y) {
    this.x = x;
    this.y = y;
    this.tree = tree;

    for (IModifierNode<V, T> node : tree.tree().all()) {
      var child = builder.create(this, node);
      children.add(child);
      widgets.put(node.key(), child);
    }
    this.root = new RootModifierWidget<>(this, tree.tree().root());
    children.add(this.root);
    children.sort(Comparator.comparing(o -> o.node.key().location().getPath()));
  }

  public List<ModifierWidget<V, T>> roots() {
    return children;
  }

  public RootModifierWidget<V, T> root() {
    return root;
  }

  public ModifierTree<V, T> getTree() {
    return tree.tree();
  }

  public ModifierTree<V, T>.Instance getInstance() {
    return tree;
  }

  @Nullable
  public ModifierWidget<V, T> getWidget(IModifierNode<V, T> node) {
    if (node == null) {
      return null;
    }
    if (node == root.node) {
      return root;
    }
    return widgets.get(node.key());
  }

  public void drawTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, int width, int height) {
    for (ModifierWidget<V, T> widget : widgets.values()) {
      if (widget.isMouseOver(mouseX, mouseY)) {
        widget.drawTooltip(guiGraphics, mouseX, mouseY, 0, width, height);
      }
    }
  }

  @FunctionalInterface
  public interface WidgetBuilder<V, T extends Modifier<V, T>> {
    ModifierWidget<V, T> create (ModifierTab<V, T> tab, IModifierNode<V, T> node);
  }
}
