package mysticmods.roots.recipe.summon;

import mysticmods.roots.block.entity.PyreBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import noobanidus.libs.noobutil.crafting.Crafting;

import javax.annotation.Nullable;

public class SummonCreaturesCrafting extends Crafting<SummonCreaturesInventory, PyreBlockEntity> {
  public SummonCreaturesCrafting(PyreBlockEntity blockentity, SummonCreaturesInventory handler, @Nullable PlayerEntity player) {
    super(blockentity, handler, player);
  }
}
