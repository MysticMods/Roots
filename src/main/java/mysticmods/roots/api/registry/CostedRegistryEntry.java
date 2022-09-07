package mysticmods.roots.api.registry;

import mysticmods.roots.api.herbs.Cost;

import java.util.List;

// TODO: Format costs as a list of components
public interface CostedRegistryEntry {
  List<Cost> getCosts();

  void setCosts(List<Cost> costs);
}
