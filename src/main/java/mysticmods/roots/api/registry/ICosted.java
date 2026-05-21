package mysticmods.roots.api.registry;

import mysticmods.roots.api.herb.ParentChargeType;
import mysticmods.roots.api.herb.CostInstance;

// TODO: Format costs as a list of components
public sealed interface ICosted permits ICostedChild, ICostedParent{
  CostInstance getDefaultCosts();

  CostInstance getCosts();

  default int getMaximumOperations() {
    return 1;
  }
}
