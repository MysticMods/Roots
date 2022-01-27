package mysticmods.roots.recipe.pyre.crafting;

import mysticmods.roots.block.entity.PyreBlockEntity;
import mysticmods.roots.recipe.pyre.PyreInventory;
import net.minecraft.entity.player.PlayerEntity;
import noobanidus.libs.noobutil.crafting.Crafting;

import javax.annotation.Nullable;

public class RitualCrafting extends Crafting<PyreInventory, PyreBlockEntity> {
  public RitualCrafting(PyreBlockEntity blockentity, PyreInventory handler, @Nullable PlayerEntity player) {
    super(blockentity, handler, player);
  }
}
