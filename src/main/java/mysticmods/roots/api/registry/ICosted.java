package mysticmods.roots.api.registry;

import mysticmods.roots.api.herb.Cost;

import java.util.List;

// TODO: Format costs as a list of components
public interface ICosted {
  List<Cost> getDefaultCosts();

  List<Cost> getCosts();
}
