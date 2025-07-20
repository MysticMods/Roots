package mysticmods.roots.recipe.transmutation;

import mysticmods.roots.api.recipe.crafting.RootsTileCrafting;
import mysticmods.roots.blockentity.FungalTransmuterBlockEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class TransmutationCrafting extends RootsTileCrafting<TransmutationInventory, FungalTransmuterBlockEntity> {
  public TransmutationCrafting(TransmutationInventory handler, FungalTransmuterBlockEntity blockEntity, @Nullable Player player) {
    super(handler, blockEntity, player);
  }
}
