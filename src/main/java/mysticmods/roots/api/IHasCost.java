package mysticmods.roots.api;

import mysticmods.roots.api.herbs.Cost;

import java.util.List;

public interface IHasCost {
  List<Cost> getCosts();

  void setCosts(List<Cost> costs);
}
