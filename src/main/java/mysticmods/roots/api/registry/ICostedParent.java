package mysticmods.roots.api.registry;

import mysticmods.roots.api.herb.ChargeType;

import java.util.Set;

public interface ICostedParent extends ICosted {
  Set<? extends ICosted> getChildren();

  default boolean hasChild(ICosted child) {
    return getChildren().contains(child);
  }
}
