package mysticmods.roots.api.registry;

import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.CostInstance;

import java.util.List;

// TODO: Format costs as a list of components
public interface ICosted {
  CostInstance getDefaultCosts();

  CostInstance getCosts();

  default CostInstance.ChargeType getChargeType() {
    return getCosts().chargeType();
  }

  default int getMaximumOperations () {
    return 1;
  }
}
