package mysticmods.roots.api;


import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TreeNodePosition<T extends TreeNodeDisplayable<T>> {
  private final T node;
  @Nullable
  private final TreeNodePosition<T> parent;
  @Nullable
  private final TreeNodePosition<T> previousSibling;
  private final int childIndex;
  private final List<TreeNodePosition<T>> children = new ArrayList<>();
  private TreeNodePosition<T> ancestor;
  @Nullable
  private TreeNodePosition<T> thread;
  private int x;
  private float y;
  private float mod;
  private float change;
  private float shift;

  protected TreeNodePosition(T pT, @Nullable TreeNodePosition<T> pParent, @Nullable TreeNodePosition<T> pPreviousSibling, int pChildIndex, int pX) {
    if (!pT.isVisible()) {
      throw new IllegalStateException("Can't position an invisible node!");
    }
    this.node = pT;
    this.parent = pParent;
    this.previousSibling = pPreviousSibling;
    this.childIndex = pChildIndex;
    this.ancestor = this;
    this.x = pX;
    this.y = -1.0F;
    TreeNodePosition<T> treenodeposition = null;

    for (T childNode : pT.getChildren()) {
      treenodeposition = this.addChild(childNode, treenodeposition);
    }
  }

  @Nullable
  private TreeNodePosition<T> addChild(T pT, @Nullable TreeNodePosition<T> pPrevious) {
    if (pT.isVisible()) {
      pPrevious = new TreeNodePosition<>(pT, this, pPrevious, this.children.size() + 1, this.x + 1);
      this.children.add(pPrevious);
    } else {
      for (T childNode : pT.getChildren()) {
        pPrevious = this.addChild(childNode, pPrevious);
      }
    }

    return pPrevious;
  }

  private void firstWalk() {
    if (this.children.isEmpty()) {
      if (this.previousSibling != null) {
        this.y = this.previousSibling.y + 1.0F;
      } else {
        this.y = 0.0F;
      }

    } else {
      TreeNodePosition<T> treenodeposition = null;

      for (TreeNodePosition<T> childNode : this.children) {
        childNode.firstWalk();
        treenodeposition = childNode.apportion(treenodeposition == null ? childNode : treenodeposition);
      }

      this.executeShifts();
      float f = ((this.children.get(0)).y + (this.children.get(this.children.size() - 1)).y) / 2.0F;
      if (this.previousSibling != null) {
        this.y = this.previousSibling.y + 1.0F;
        this.mod = this.y - f;
      } else {
        this.y = f;
      }

    }
  }

  private float secondWalk(float pOffsetY, int pColumnX, float pSubtreeTopY) {
    this.y += pOffsetY;
    this.x = pColumnX;
    if (this.y < pSubtreeTopY) {
      pSubtreeTopY = this.y;
    }

    for (TreeNodePosition<T> childNode : this.children) {
      pSubtreeTopY = childNode.secondWalk(pOffsetY + this.mod, pColumnX + 1, pSubtreeTopY);
    }

    return pSubtreeTopY;
  }

  private void thirdWalk(float pY) {
    this.y += pY;

    for (TreeNodePosition<T> childNode : this.children) {
      childNode.thirdWalk(pY);
    }

  }

  private void executeShifts() {
    float f = 0.0F;
    float f1 = 0.0F;

    for (int i = this.children.size() - 1; i >= 0; --i) {
      TreeNodePosition<T> treenodeposition = this.children.get(i);
      treenodeposition.y += f;
      treenodeposition.mod += f;
      f1 += treenodeposition.change;
      f += treenodeposition.shift + f1;
    }

  }

  @Nullable
  private TreeNodePosition<T> previousOrThread() {
    if (this.thread != null) {
      return this.thread;
    } else {
      return !this.children.isEmpty() ? this.children.get(0) : null;
    }
  }

  @Nullable
  private TreeNodePosition<T> nextOrThread() {
    if (this.thread != null) {
      return this.thread;
    } else {
      return !this.children.isEmpty() ? this.children.get(this.children.size() - 1) : null;
    }
  }

  private TreeNodePosition<T> apportion(TreeNodePosition<T> pNode) {
     if (this.previousSibling != null) {
        TreeNodePosition<T> treenodeposition = this;
        TreeNodePosition<T> treenodeposition1 = this;
        TreeNodePosition<T> treenodeposition2 = this.previousSibling;
        TreeNodePosition<T> treenodeposition3 = this.parent.children.get(0);
        float f = this.mod;
        float f1 = this.mod;
        float f2 = treenodeposition2.mod;

        float f3;
        for (f3 = treenodeposition3.mod; treenodeposition2.nextOrThread() != null && treenodeposition.previousOrThread() != null; f1 += treenodeposition1.mod) {
           treenodeposition2 = treenodeposition2.nextOrThread();
           treenodeposition = treenodeposition.previousOrThread();
           treenodeposition3 = treenodeposition3.previousOrThread();
           treenodeposition1 = treenodeposition1.nextOrThread();
           treenodeposition1.ancestor = this;
           float f4 = treenodeposition2.y + f2 - (treenodeposition.y + f) + 1.0F;
           if (f4 > 0.0F) {
              treenodeposition2.getAncestor(this, pNode).moveSubtree(this, f4);
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

           pNode = this;
        }

     }
     return pNode;
  }

  private void moveSubtree(TreeNodePosition<T> pNode, float pShift) {
    float f = (float) (pNode.childIndex - this.childIndex);
    if (f != 0.0F) {
      pNode.change -= pShift / f;
      this.change += pShift / f;
    }

    pNode.shift += pShift;
    pNode.y += pShift;
    pNode.mod += pShift;
  }

  private TreeNodePosition<T> getAncestor(TreeNodePosition<T> pSelf, TreeNodePosition<T> pOther) {
    return this.ancestor != null && pSelf.parent.children.contains(this.ancestor) ? this.ancestor : pOther;
  }

  private void finalizePosition() {
      this.node.setLocation((float) this.x, this.y);

    if (!this.children.isEmpty()) {
      for (TreeNodePosition<T> treenodeposition : this.children) {
        treenodeposition.finalizePosition();
      }
    }

  }

  public static <T extends TreeNodeDisplayable<T>> void run(T pRoot) {
    if (!pRoot.isVisible()) {
      throw new IllegalArgumentException("Can't position children of an invisible root!");
    } else {
      TreeNodePosition<T> treenodeposition = new TreeNodePosition<>(pRoot, null,  null, 1, 0);
      treenodeposition.firstWalk();
      float f = treenodeposition.secondWalk(0.0F, 0, treenodeposition.y);
      if (f < 0.0F) {
        treenodeposition.thirdWalk(-f);
      }

      treenodeposition.finalizePosition();
    }
  }
}
