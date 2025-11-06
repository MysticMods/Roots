package mysticmods.roots.api.modifier;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.resources.ResourceKey;

import javax.annotation.Nullable;
import java.util.Set;

public class ModifierNode<V, T extends Modifier<V, T>> {
  private static final Interner<ModifierNode<?, ?>> VALUES = Interners.newWeakInterner();

  private final ResourceKey<T> modifier;
  @Nullable
  private ModifierNode<V, T> parent;
  private final Set<ModifierNode<V, T>> children = new ReferenceOpenHashSet<>();

  protected ModifierNode(ResourceKey<T> modifier, @Nullable ModifierNode<V, T> parent) {
    this.modifier = modifier;
    this.parent = parent;
  }

  protected ModifierNode(ResourceKey<T> modifier) {
    this(modifier, null);
  }

  @SuppressWarnings("unchecked")
  public static <V, T extends Modifier<V, T>> ModifierNode<V, T> create(ResourceKey<T> modifier) {
    return (ModifierNode<V, T>) VALUES.intern(new ModifierNode<>(modifier));
  }

  public static <V, T extends Modifier<V, T>> ModifierNode<V, T> create(ResourceKey<T> modifier, ModifierNode<V, T> parent) {
    return create(modifier).parent(parent);
  }

  public ResourceKey<T> modifier() {
    return modifier;
  }

  @Nullable
  public ModifierNode<V, T> parent() {
    return parent;
  }

  public ModifierNode<V, T> parent(ModifierNode<V, T> parent) {
    this.parent = parent;
    return this;
  }

  public ModifierNode<V, T> root() {
    return getRoot(this);
  }

  public Iterable<ModifierNode<V, T>> children() {
    return this.children;
  }

  public void addChild(ModifierNode<V, T> child) {
    this.children.add(child);
  }

  public static <V, T extends Modifier<V, T>> ModifierNode<V, T> getRoot(ModifierNode<V, T> node) {
    ModifierNode<V, T> modifierNode = node;

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
      if (o instanceof ModifierNode<?, ?> other) {
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
