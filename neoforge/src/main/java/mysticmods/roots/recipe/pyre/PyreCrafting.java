package mysticmods.roots.recipe.pyre;

import mysticmods.roots.api.recipe.crafting.RootsTileCrafting;
import mysticmods.roots.blockentity.PyreBlockEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class PyreCrafting extends RootsTileCrafting<PyreInventory, PyreBlockEntity> {
  public PyreCrafting(PyreInventory handler, PyreBlockEntity blockEntity, @Nullable Player player) {
    super(handler, blockEntity, player);
  }
}
