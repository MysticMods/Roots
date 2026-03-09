package mysticmods.roots.api.modifier;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("MagicNumber")
public class ModifierNodePosition<V, C extends Modifier<V, C>> {
  private final IModifierNode<V, C> node;
  @Nullable
  private final ModifierNodePosition<V, C> parent;
  @Nullable
  private final ModifierNodePosition<V, C> previousSibling;
  private final int childIndex;
  private final List<ModifierNodePosition<V, C>> children = Lists.newArrayList();
  private ModifierNodePosition<V, C> ancestor;
  @Nullable
  private ModifierNodePosition<V, C> thread;
  private int x;
  private float y;
  private float mod;
  private float change;
  private float shift;

  protected ModifierNodePosition(IModifierNode<V, C> node, @Nullable ModifierNodePosition<V, C> parent, @Nullable ModifierNodePosition<V, C> previousSibling, int childIndex, int x) {
    this.node = node;
    this.parent = parent;
    this.previousSibling = previousSibling;
    this.childIndex = childIndex;
    this.ancestor = this;
    this.x = x;
    this.y = -1.0F;
    ModifierNodePosition<V, C> treenodeposition = null;

    for (IModifierNode<V, C> advancementnode : node.children()) {
      treenodeposition = this.addChild(advancementnode, treenodeposition);
    }
  }

  private ModifierNodePosition<V, C> addChild(IModifierNode<V, C> child, @Nullable ModifierNodePosition<V, C> incomingSibling) {
    incomingSibling = new ModifierNodePosition<>(child, this, incomingSibling, this.children.size() + 1, this.x + 1);
    this.children.add(incomingSibling);

    return incomingSibling;
  }

  private void firstWalk() {
    if (this.children.isEmpty()) {
      if (this.previousSibling != null) {
        this.y = this.previousSibling.y + 1.0F;
      } else {
        this.y = 0.0F;
      }
    } else {
      ModifierNodePosition<V, C> treenodeposition = null;

      for (ModifierNodePosition<V, C> treenodeposition1 : this.children) {
        treenodeposition1.firstWalk();
        treenodeposition = treenodeposition1.apportion(treenodeposition == null ? treenodeposition1 : treenodeposition);
      }

      this.executeShifts();
      float f = (this.children.getFirst().y + this.children.getLast().y) / 2.0F;
      if (this.previousSibling != null) {
        this.y = this.previousSibling.y + 1.0F;
        this.mod = this.y - f;
      } else {
        this.y = f;
      }
    }
  }

  private float secondWalk(float offsetY, int columnX, float subtreeTopY) {
    this.y += offsetY;
    this.x = columnX;
    if (this.y < subtreeTopY) {
      subtreeTopY = this.y;
    }

    for (ModifierNodePosition<V, C> treenodeposition : this.children) {
      subtreeTopY = treenodeposition.secondWalk(offsetY + this.mod, columnX + 1, subtreeTopY);
    }

    return subtreeTopY;
  }

  private void thirdWalk(float y) {
    this.y += y;

    for (ModifierNodePosition<V, C> treenodeposition : this.children) {
      treenodeposition.thirdWalk(y);
    }
  }

  private void executeShifts() {
    float f = 0.0F;
    float f1 = 0.0F;

    for (int i = this.children.size() - 1; i >= 0; i--) {
      ModifierNodePosition<V, C> treenodeposition = this.children.get(i);
      treenodeposition.y += f;
      treenodeposition.mod += f;
      f1 += treenodeposition.change;
      f += treenodeposition.shift + f1;
    }
  }

  @Nullable
  private ModifierNodePosition<V, C> previousOrThread() {
    if (this.thread != null) {
      return this.thread;
    } else {
      return !this.children.isEmpty() ? this.children.getFirst() : null;
    }
  }

  @Nullable
  private ModifierNodePosition<V, C> nextOrThread() {
    if (this.thread != null) {
      return this.thread;
    } else {
      return !this.children.isEmpty() ? this.children.getLast() : null;
    }
  }

  private ModifierNodePosition<V, C> apportion(ModifierNodePosition<V, C> node) {
    if (this.previousSibling != null) {
      ModifierNodePosition<V, C> treenodeposition = this;
      ModifierNodePosition<V, C> treenodeposition1 = this;
      ModifierNodePosition<V, C> treenodeposition2 = this.previousSibling;
      ModifierNodePosition<V, C> treenodeposition3 = this.parent.children.getFirst();
      float f = this.mod;
      float f1 = this.mod;
      float f2 = treenodeposition2.mod;

      float f3;
      for (f3 = treenodeposition3.mod;
           treenodeposition2.nextOrThread() != null && treenodeposition.previousOrThread() != null;
           f1 += treenodeposition1.mod
      ) {
        treenodeposition2 = treenodeposition2.nextOrThread();
        treenodeposition = treenodeposition.previousOrThread();
        treenodeposition3 = treenodeposition3.previousOrThread();
        treenodeposition1 = treenodeposition1.nextOrThread();
        treenodeposition1.ancestor = this;
        float f4 = treenodeposition2.y + f2 - (treenodeposition.y + f) + 1.0F;
        if (f4 > 0.0F) {
          treenodeposition2.getAncestor(this, node).moveSubtree(this, f4);
          f += f4;
          f1 += f4;
        }

        f2 += treenodeposition2.mod;
        f += treenodeposition.mod;
        f3 += treenodeposition3.mod;
      }

      if (treenodeposition2.nextOrThread() != null && treenodeposition1.nextOrThread() == null) {
        treenodeposition1.thread = treenodeposition2.nextOrThread();
        treenodeposition1.mod += f2 - f1;
      } else {
        if (treenodeposition.previousOrThread() != null && treenodeposition3.previousOrThread() == null) {
          treenodeposition3.thread = treenodeposition.previousOrThread();
          treenodeposition3.mod += f - f3;
        }

        node = this;
      }

    }
    return node;
  }

  private void moveSubtree(ModifierNodePosition<V, C> node, float shift) {
    float f = (float) (node.childIndex - this.childIndex);
    if (f != 0.0F) {
      node.change -= shift / f;
      this.change += shift / f;
    }

    node.shift += shift;
    node.y += shift;
    node.mod += shift;
  }

  private ModifierNodePosition<V, C> getAncestor(ModifierNodePosition<V, C> self, ModifierNodePosition<V, C> other) {
    return this.ancestor != null && self.parent.children.contains(this.ancestor) ? this.ancestor : other;
  }

  private void finalizePosition() {
    this.node.setLocation(this.x, this.y);
    if (!this.children.isEmpty()) {
      for (ModifierNodePosition<V, C> treenodeposition : this.children) {
        treenodeposition.finalizePosition();
      }
    }
  }

  protected static <V, C extends Modifier<V, C>> void run(ModifierTree<V, C> rootNode) {
    ModifierNodePosition<V, C> treenodeposition = new ModifierNodePosition<>(rootNode, null, null, 1, 0);
    treenodeposition.firstWalk();
    float f = treenodeposition.secondWalk(0.0F, 0, treenodeposition.y);
    if (f < 0.0F) {
      treenodeposition.thirdWalk(-f);
    }

    treenodeposition.finalizePosition();
  }
}
