package mysticmods.roots.recipe.summon;

import mysticmods.roots.api.recipe.RootsTileCrafting;
import mysticmods.roots.block.entity.PyreBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import noobanidus.libs.noobutil.crafting.Crafting;

import javax.annotation.Nullable;

public class SummonCreaturesCrafting extends RootsTileCrafting<SummonCreaturesInventory, PyreBlockEntity> {
  public SummonCreaturesCrafting(SummonCreaturesInventory handler, PyreBlockEntity blockEntity, @Nullable PlayerEntity player) {
    super(handler, blockEntity, player);
  }
}
