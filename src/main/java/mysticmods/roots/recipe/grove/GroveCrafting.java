package mysticmods.roots.recipe.grove;

import mysticmods.roots.api.recipe.RootsTileCrafting;
import mysticmods.roots.block.entity.GroveCrafterBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nullable;

// TODO: Pedestal access

public class GroveCrafting extends RootsTileCrafting<GroveInventoryWrapper, GroveCrafterBlockEntity> {
  public GroveCrafting(GroveInventoryWrapper handler, GroveCrafterBlockEntity blockEntity, @Nullable Player player) {
    super(handler, blockEntity, player);
  }
}
