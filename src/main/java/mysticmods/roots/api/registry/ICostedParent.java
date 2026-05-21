package mysticmods.roots.api.registry;

import mysticmods.roots.api.herb.ChargeType;

import java.util.Set;

public interface ICostedParent extends ICosted {
  Set<? extends ICostedChild> getChildren();

  default boolean hasChild(ICostedChild child) {
    return getChildren().contains(child);
  }
}
