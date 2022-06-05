package mysticmods.roots.recipe.grove;

import mysticmods.roots.api.recipe.RootsTileCrafting;
import mysticmods.roots.block.entity.GroveCrafterBlockEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

// TODO: Pedestal access

public class GroveCrafting extends RootsTileCrafting<GroveCrafterInventory, GroveCrafterBlockEntity> {
  public GroveCrafting(GroveCrafterInventory handler, GroveCrafterBlockEntity blockEntity, @Nullable Player player) {
    super(handler, blockEntity, player);
  }
}
