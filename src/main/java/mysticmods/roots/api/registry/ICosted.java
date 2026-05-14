package mysticmods.roots.api.registry;

import mysticmods.roots.api.herb.ChargeType;
import mysticmods.roots.api.herb.CostInstance;

// TODO: Format costs as a list of components
public interface ICosted {
  CostInstance getDefaultCosts();

  CostInstance getCosts();

  ChargeType getChargeType();

  default int getMaximumOperations() {
    return 1;
  }
}
