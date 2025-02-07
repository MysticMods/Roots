package mysticmods.roots.recipe.pyre;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.recipe.PedestalInventoryWrapper;
import mysticmods.roots.recipe.TaggedPedestalCrafting;
import net.minecraft.world.entity.player.Player;

public class PyrePedestalCrafting extends TaggedPedestalCrafting<PedestalInventoryWrapper, PyreBlockEntity> {
  public PyrePedestalCrafting(PedestalInventoryWrapper handler, PyreBlockEntity blockEntity, Player player) {
    super(RootsTags.Blocks.RITUAL_PEDESTALS, RootsTags.Blocks.DISPLAY_PEDESTALS, handler, blockEntity, player);
  }
}
