package mysticmods.roots.api.registry;

import mysticmods.roots.api.SpellType;

public interface ICostedChild extends ICosted {
  SpellType.Secondary getChargeType();
}
