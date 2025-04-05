package mysticmods.roots.recipe.grove;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.blockentity.GroveCrafterBlockEntity;
import mysticmods.roots.recipe.TaggedPedestalCrafting;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class GroveCrafting extends TaggedPedestalCrafting<GroveCrafterBlockEntity> {
  public GroveCrafting(GroveCrafterBlockEntity blockEntity, @Nullable Player player) {
    super(RootsTags.Blocks.GROVE_PEDESTALS, RootsTags.Blocks.DISPLAY_PEDESTALS, blockEntity, player);
  }
}
