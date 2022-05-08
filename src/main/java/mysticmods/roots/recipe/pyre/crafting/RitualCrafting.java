package mysticmods.roots.recipe.pyre.crafting;

import mysticmods.roots.api.recipe.RootsTileCrafting;
import mysticmods.roots.block.entity.PyreBlockEntity;
import mysticmods.roots.recipe.pyre.PyreInventory;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class RitualCrafting extends RootsTileCrafting<PyreInventory, PyreBlockEntity> {
  public RitualCrafting(PyreInventory handler, PyreBlockEntity blockEntity, @Nullable Player player) {
    super(handler, blockEntity, player);
  }
}
