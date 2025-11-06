package mysticmods.roots.api.modifier;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.resources.ResourceKey;

import javax.annotation.Nullable;
import java.util.Set;

public class ModifierNode<T> {
  private static final Interner<ModifierNode<?>> VALUES = Interners.newWeakInterner();

  private final ResourceKey<Modifier<T>> modifier;
  @Nullable
  private ModifierNode<T> parent;
  private final Set<ModifierNode<T>> children = new ReferenceOpenHashSet<>();

  protected ModifierNode(ResourceKey<Modifier<T>> modifier, @Nullable ModifierNode<T> parent) {
    this.modifier = modifier;
    this.parent = parent;
  }

  protected ModifierNode(ResourceKey<Modifier<T>> modifier) {
    this(modifier, null);
  }

  @SuppressWarnings("unchecked")
  public static <T> ModifierNode<T> create(ResourceKey<Modifier<T>> modifier) {
    return (ModifierNode<T>) VALUES.intern(new ModifierNode<>(modifier));
  }

  public static <T> ModifierNode<T> create(ResourceKey<Modifier<T>> modifier, ModifierNode<T> parent) {
    return create(modifier).parent(parent);
  }

  public ResourceKey<Modifier<T>> modifier() {
    return modifier;
  }

  @Nullable
  public ModifierNode<T> parent() {
    return parent;
  }

  public ModifierNode<T> parent(ModifierNode<T> parent) {
    this.parent = parent;
    return this;
  }

  public ModifierNode<T> root() {
    return getRoot(this);
  }

  public Iterable<ModifierNode<T>> children() {
    return this.children;
  }

  public void addChild(ModifierNode<T> child) {
    this.children.add(child);
  }

  public static <T> ModifierNode<T> getRoot(ModifierNode<T> node) {
    ModifierNode<T> modifierNode = node;

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
      if (o instanceof ModifierNode<?> other) {
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
