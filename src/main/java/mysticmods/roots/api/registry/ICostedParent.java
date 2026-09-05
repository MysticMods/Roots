package mysticmods.roots.api.registry;

import mysticmods.roots.api.SpellType;

import java.util.Set;

public interface ICostedParent extends ICosted {
  SpellType.Primary getChargeType();

  Set<? extends ICostedChild> getChildren();

  default boolean hasChild(ICostedChild child) {
    return getChildren().contains(child);
  }
}
