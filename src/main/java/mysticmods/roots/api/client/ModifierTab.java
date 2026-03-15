package mysticmods.roots.api.client;

import mysticmods.roots.api.modifier.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ModifierTab<V, T extends Modifier<V, T>> {
  private final Minecraft minecraft;
  private final Map<ResourceKey<T>, ModifierWidget<V, T>> widgets = new HashMap<>();
  private final ModifierTree<V, T> tree;

  public ModifierTab(Minecraft minecraft, ModifierTree<V, T> tree) {
    this.minecraft = minecraft;
    this.tree = tree;
  }

  public ModifierTree<V, T> getTree () {
    return tree;
  }

  @Nullable
  public ModifierWidget<V, T> getWidget (IModifierNode<V, T> node) {
    if (node == null) {
      return null;
    }
    return widgets.get(node.key());
  }


  public void drawTooltips (GuiGraphics guiGraphics, int mouseX, int mouseY, int width, int height) {
    for (ModifierWidget<V, T> widget : widgets.values()) {
      if (widget.isMouseOver(0, 0, mouseX, mouseY)) {
        widget.drawHover(guiGraphics, 0, 0, 0, width, height);
      }
    }
  }
}
