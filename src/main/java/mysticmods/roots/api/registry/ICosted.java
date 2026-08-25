package mysticmods.roots.api.registry;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import mysticmods.roots.api.herb.CostInstance;

// TODO: Format costs as a list of components
public interface ICosted {
  CostInstance getDefaultCosts();

  CostInstance getCosts();

  default int getBaseMaximumOperations() {
    return 1;
  }

  default int getMaximumOperations(Object2BooleanMap<ICosted> childMap) {
    return getBaseMaximumOperations();
  }
}
