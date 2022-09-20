package mysticmods.roots.api.registry;

import mysticmods.roots.api.herb.Cost;

import java.util.List;

// TODO: Format costs as a list of components
public interface ICostedRegistryEntry {
  List<Cost> getCosts();

  void setCosts(List<Cost> costs);
}
