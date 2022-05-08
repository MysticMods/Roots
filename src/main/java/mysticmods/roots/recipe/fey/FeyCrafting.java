package mysticmods.roots.recipe.fey;

import mysticmods.roots.api.recipe.RootsTileCrafting;
import mysticmods.roots.block.entity.FeyCrafterBlockEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class FeyCrafting extends RootsTileCrafting<FeyCraftingInventory, FeyCrafterBlockEntity> {
  public FeyCrafting(FeyCraftingInventory handler, FeyCrafterBlockEntity blockEntity, @Nullable Player player) {
    super(handler, blockEntity, player);
  }
}
