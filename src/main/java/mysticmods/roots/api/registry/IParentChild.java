package mysticmods.roots.api.registry;

import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public interface IParentChild<T extends IParentChild<T>> {
  @Nullable
  T getParent();

  default boolean hasParent() {
    return getParent() != null;
  }

  Set<T> getChildren();

  default Set<T> getDescendants() {
    Set<T> result = new HashSet<>();
    for (T child : getChildren()) {
      result.add(child);
      result.addAll(child.getDescendants());
    }

    return result;
  }

  default Set<T> getAntecedents() {
    Set<T> result = new HashSet<>();
    T parent = getParent();
    while (parent != null) {
      result.add(parent);
      parent = parent.getParent();
    }
    return result;
  }

  default boolean hasChildren() {
    return !getChildren().isEmpty();
  }

  default void addChild(T child) {
    getChildren().add(child);
  }
}
