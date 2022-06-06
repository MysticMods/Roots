package mysticmods.roots.recipe.grove;

import mysticmods.roots.block.entity.PedestalBlockEntity;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.List;

public class GroveInventoryWrapper extends CombinedInvWrapper {
  protected GroveInventoryWrapper(IItemHandlerModifiable... itemHandler) {
    super(itemHandler);
  }

  public static GroveInventoryWrapper of (List<PedestalBlockEntity> pedestals) {
    return new GroveInventoryWrapper(pedestals.stream().map(PedestalBlockEntity::getInventory).toArray(IItemHandlerModifiable[]::new));
  }
}
