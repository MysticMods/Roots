package mysticmods.roots.api.registry;

import mysticmods.roots.api.modifier.ChildChargeType;

public interface ICostedChild extends ICosted {
  ChildChargeType getChargeType();
}
