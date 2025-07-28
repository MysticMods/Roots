package mysticmods.roots.api.registry;

import java.util.Set;

public interface ICostedParent extends ICosted {
  Set<ICosted> getChildren();

  default boolean hasChild(ICosted child) {
    return getChildren().contains(child);
  }
}
