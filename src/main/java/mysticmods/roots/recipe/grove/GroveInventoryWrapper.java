package mysticmods.roots.recipe.grove;

import com.mojang.datafixers.util.Pair;
import mysticmods.roots.blockentity.PedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import java.util.List;

public class GroveInventoryWrapper extends CombinedInvWrapper {
  public GroveInventoryWrapper(List<Pair<BlockPos, PedestalBlockEntity>> pairs) {
    super(pairs.stream().map(Pair::getSecond).map(PedestalBlockEntity::getInventory).toArray(IItemHandlerModifiable[]::new));
  }
}
