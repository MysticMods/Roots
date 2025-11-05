package mysticmods.roots.api.modifier;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import org.openjdk.nashorn.internal.objects.annotations.Function;

import javax.annotation.Nullable;
import java.util.Set;

public class ModifierNode {
  private final ResourceKey<Modifier> modifier;
  @Nullable
  private final ModifierNode parent;
  private final Set<ModifierNode> children = new ReferenceOpenHashSet<>();

  public ModifierNode(ResourceKey<Modifier> modifier, @Nullable ModifierNode parent) {
    this.modifier = modifier;
    this.parent = parent;
  }

  public ModifierNode(ResourceKey<Modifier> modifier) {
    this(modifier, null);
  }

  public ResourceKey<Modifier> modifier() {
    return modifier;
  }

  @Nullable
  public ModifierNode parent() {
    return parent;
  }

  public ModifierNode root() {
    return getRoot(this);
  }

  public Iterable<ModifierNode> children() {
    return this.children;
  }

  public void addChild(ModifierNode child) {
    this.children.add(child);
  }

  public static ModifierNode getRoot(ModifierNode node) {
    ModifierNode modifierNode = node;

    while (modifierNode != null && modifierNode.parent() != null) {
      modifierNode = modifierNode.parent();
    }

    return modifierNode;
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    } else {
      if (o instanceof ModifierNode other) {
        return other.modifier.equals(this.modifier);
      }
    }

    return false;
  }

  @Override
  public int hashCode() {
    return this.modifier.hashCode();
  }

  @Override
  public String toString() {
    return this.modifier.toString();
  }
}
