package mysticmods.roots.api.herb;


import net.neoforged.neoforge.items.IItemHandler;

import java.util.List;

public interface IInventoryAccessible {
  List<IItemHandler> getContainers();

/*  static IInventoryAccessible of (Player player) {
    return new IInventoryAccessible() {
      @Override
      public List<Container> getContainers() {
        return List.of();
      }
    };
  }*/

  class PlayerAccessible {

  }
}
