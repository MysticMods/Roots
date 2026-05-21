package mysticmods.roots.api.registry;

import mysticmods.roots.api.herb.ParentChargeType;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.spell.Spell;

// TODO: Format costs as a list of components
public interface ICosted {
  CostInstance getDefaultCosts();

  CostInstance getCosts();

  default int getMaximumOperations() {
    return 1;
  }
}
