package mysticmods.roots.api.registry;

import mysticmods.roots.api.modifier.Modifier;
import noobanidus.libs.noobutil.type.LazySupplier;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

// ???????? WHAT IS THIS ???????
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

  default void resolve() {
    T parent = getParent();
    if (parent != null) {
      // TODO: THIS COULD NEVER GO WRONG???
      //noinspection unchecked
      parent.addChild((T) this);
    }
  }

  Supplier<Modifier> NO_PARENT = LazySupplier.of(() -> null);
}
