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

  public ModifierTab(ModifierTree<V, T>.Instance tree, WidgetBuilder<V, T> builder) {
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

  private List<ModifierWidget<V, T>> roots = null;

  public List<ModifierWidget<V, T>> roots() {
    if (roots == null) {
      this.roots = tree.tree().rootNodes().stream().sorted(Comparator.comparing(o -> o.key().location().getPath())).map(node -> widgets.get(node.key())).toList();
    }
    return this.roots;
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

  public List<ModifierWidget<V, T>> children () {
    return children;
  }

  public void drawTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, int width, int height) {
    for (ModifierWidget<V, T> widget : widgets.values()) {
      if (widget.isMouseOver(mouseX, mouseY)) {
        widget.drawHover(guiGraphics, 0, 0, 0, width, height);
      }
    }
  }

  @FunctionalInterface
  public interface WidgetBuilder<V, T extends Modifier<V, T>> {
    ModifierWidget<V, T> create (ModifierTab<V, T> tab, IModifierNode<V, T> node);
  }
}
