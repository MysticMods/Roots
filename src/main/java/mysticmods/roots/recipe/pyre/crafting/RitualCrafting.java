package mysticmods.roots.recipe.pyre.crafting;

import mysticmods.roots.api.recipe.RootsTileCrafting;
import mysticmods.roots.block.entity.PyreBlockEntity;
import mysticmods.roots.recipe.pyre.PyreInventory;
import net.minecraft.entity.player.PlayerEntity;
import noobanidus.libs.noobutil.crafting.Crafting;

import javax.annotation.Nullable;

public class RitualCrafting extends RootsTileCrafting<PyreInventory, PyreBlockEntity> {
  public RitualCrafting(PyreInventory handler, PyreBlockEntity blockEntity, @Nullable PlayerEntity player) {
    super(handler, blockEntity, player);
  }
}
