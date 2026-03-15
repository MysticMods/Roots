package mysticmods.roots.api.client;

import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.modifier.ModifierNode;
import mysticmods.roots.api.modifier.ModifierTree;
import mysticmods.roots.api.modifier.RootModifierNode;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.advancements.AdvancementWidgetType;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public class ModifierWidget<V, T extends Modifier<V, T>> {
  private final ModifierTab<V, T> tab;
  private final ModifierNode<V, T> node;

  private final ItemStack renderStack;
  private final int x, y;

  public ModifierWidget(ModifierTab<V, T> tab, ModifierNode<V, T> node) {
    this.tab = tab;
    this.node = node;
    this.x = Mth.floor(node.x() * 28.0f); // 28?
    this.y = Mth.floor(node.y() * 27.0f); // 27?
    this.renderStack = new ItemStack(ModifierTree.getIcon(tab.getTree(), node.key()));
  }

  @Nullable
  private ModifierWidget<V, T> parent() {
    if (node.parent() == null || node.parent() instanceof RootModifierNode<V, T>) {
      return null;
    }

    return tab.getWidget(node.parent());
  }

  private List<ModifierWidget<V, T>> children = null;

  private Iterable<ModifierWidget<V, T>> children() {
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

  public void drawConnectivity(GuiGraphics guiGraphics, int x, int y, boolean dropShadow) {
    var parent = parent();
    if (parent != null) {
      int i = x + parent.x + 13;
      int j = x + parent.x + 26 + 4;
      int k = y + parent.y + 13;
      int l = x + this.x + 13;
      int i1 = y + this.y + 13;
      int j1 = dropShadow ? -16777216 : -1;
      if (dropShadow) {
        guiGraphics.hLine(j, i, k - 1, j1);
        guiGraphics.hLine(j + 1, i, k, j1);
        guiGraphics.hLine(j, i, k + 1, j1);
        guiGraphics.hLine(l, j - 1, i1 - 1, j1);
        guiGraphics.hLine(l, j - 1, i1, j1);
        guiGraphics.hLine(l, j - 1, i1 + 1, j1);
        guiGraphics.vLine(j - 1, i1, k, j1);
        guiGraphics.vLine(j + 1, i1, k, j1);
      } else {
        guiGraphics.hLine(j, i, k, j1);
        guiGraphics.hLine(l, j, i1, j1);
        guiGraphics.vLine(j, i1, k, j1);
      }
    }

    for (ModifierWidget<V, T> widget : children()) {
      widget.drawConnectivity(guiGraphics, x, y, dropShadow);
    }
  }

  public void draw (GuiGraphics guiGraphics, int x, int y) {
    guiGraphics.blitSprite(AdvancementWidgetType.UNOBTAINED.frameSprite(AdvancementType.TASK), x + this.x + 3, y + this.y, 26, 26);
    guiGraphics.renderFakeItem(renderStack, x + this.x + 8, y + this.y + 5);

    for (ModifierWidget<V, T> widget : children()) {
      widget.draw(guiGraphics, x, y);
    }
  }

  public void drawHover (GuiGraphics guiGraphics, int x, int y, float fade, int width, int height) {

  }

  public boolean isMouseOver (int x, int y, int mouseX, int mouseY) {
    int i = x + this.x;
    int j = i + 26;
    int k = y + this.y;
    int l = k + 26;
    return mouseX >= i && mouseX <= j && mouseY >= k && mouseY <= 1;
  }
}
