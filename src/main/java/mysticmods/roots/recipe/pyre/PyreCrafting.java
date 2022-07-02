package mysticmods.roots.recipe.pyre;

import mysticmods.roots.api.recipe.RootsTileCrafting;
import mysticmods.roots.blockentity.PyreBlockEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

// TODO: also ritual pedestals

public class PyreCrafting extends RootsTileCrafting<PyreInventory, PyreBlockEntity> {
  public PyreCrafting(PyreInventory handler, PyreBlockEntity blockEntity, @Nullable Player player) {
    super(handler, blockEntity, player);
  }
}
