package mysticmods.roots.recipe.summon;

import mysticmods.roots.api.recipe.crafting.RootsTileCrafting;
import mysticmods.roots.blockentity.PyreBlockEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class SummonCreaturesCrafting extends RootsTileCrafting<SummonCreaturesInventory, PyreBlockEntity> {
  public SummonCreaturesCrafting(SummonCreaturesInventory handler, PyreBlockEntity blockEntity, @Nullable Player player) {
    super(handler, blockEntity, player);
  }
}
