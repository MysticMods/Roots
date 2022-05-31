package mysticmods.roots.recipe.fey;

import mysticmods.roots.api.recipe.RootsTileCrafting;
import mysticmods.roots.block.entity.GroveCrafterBlockEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class FeyCrafting extends RootsTileCrafting<FeyCraftingInventory, GroveCrafterBlockEntity> {
  public FeyCrafting(FeyCraftingInventory handler, GroveCrafterBlockEntity blockEntity, @Nullable Player player) {
    super(handler, blockEntity, player);
  }
}
