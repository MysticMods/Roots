package mysticmods.roots.api.registry;

import mysticmods.roots.api.herb.ChildChargeType;

public interface ICostedChild extends ICosted {
  ChildChargeType getChargeType();
}
