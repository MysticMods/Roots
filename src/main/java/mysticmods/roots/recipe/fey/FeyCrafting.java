package mysticmods.roots.recipe.fey;

import mysticmods.roots.api.recipe.RootsTileCrafting;
import mysticmods.roots.block.entity.FeyCrafterBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import noobanidus.libs.noobutil.crafting.Crafting;

import javax.annotation.Nullable;

public class FeyCrafting extends RootsTileCrafting<FeyCraftingInventory, FeyCrafterBlockEntity> {
  public FeyCrafting(FeyCraftingInventory handler, FeyCrafterBlockEntity blockEntity, @Nullable PlayerEntity player) {
    super(handler, blockEntity, player);
  }
}
