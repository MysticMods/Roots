package mysticmods.roots.api.modifier;

import com.google.common.collect.MapMaker;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class ModifierNode<V, T extends Modifier<V, T>> implements IModifierNode<V, T> {
  private static final ConcurrentMap<ResourceKey<?>, ModifierNode<?, ?>> VALUES = new MapMaker().weakValues().makeMap();

  protected final ResourceKey<T> modifier;
  @Nullable
  protected IModifierNode<V, T> parent;
  protected final List<IModifierNode<V, T>> children = new ArrayList<>();
  protected float x, y;

  private ModifierNode(ResourceKey<T> modifier, @Nullable IModifierNode<V, T> parent) {
    this.modifier = modifier;
    this.parent = parent;
  }

  private ModifierNode(ResourceKey<T> modifier) {
    this(modifier, null);
  }

  @SuppressWarnings("unchecked")
  public static <V, T extends Modifier<V, T>> IModifierNode<V, T> create(ResourceKey<T> modifier) {
    return (IModifierNode<V, T>) VALUES.computeIfAbsent(modifier, (k) -> new ModifierNode<>((ResourceKey<T>) k));
  }

  public static <V, T extends Modifier<V, T>> IModifierNode<V, T> create(ResourceKey<T> modifier, IModifierNode<V, T> parent) {
    return create(modifier).setParent(parent);
  }

  @NotNull
  public ResourceKey<T> modifier() {
    return modifier;
  }

  @Nullable
  public IModifierNode<V, T> parent() {
    return parent;
  }

  public IModifierNode<V, T> setParent(IModifierNode<V, T> parent) {
    this.parent = parent;
    return this;
  }

  public List<IModifierNode<V, T>> children() {
    return this.children;
  }

  public void addChild(IModifierNode<V, T> child) {
    this.children.add(child);
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

  @Override
  public void setLocation(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public float x() {
    return x;
  }

  public float y  () {
    return y;
  }
}
