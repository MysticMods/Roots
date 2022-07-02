package mysticmods.roots.recipe.grove;

import mysticmods.roots.blockentity.PedestalBlockEntity;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.List;

public class GroveInventoryWrapper extends CombinedInvWrapper {
  private final List<PedestalBlockEntity> pedestals;

  public GroveInventoryWrapper(List<PedestalBlockEntity> pedestals) {
    super(pedestals.stream().map(PedestalBlockEntity::getInventory).toArray(IItemHandlerModifiable[]::new));
    this.pedestals = pedestals;
  }

  public List<PedestalBlockEntity> getPedestals() {
    return pedestals;
  }
}
