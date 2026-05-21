package mysticmods.roots.api.registry;

import mysticmods.roots.api.herb.ChildChargeType;

public non-sealed interface ICostedChild extends ICosted {
  ChildChargeType getChargeType();
}
