package mysticmods.roots.recipe.fey;

import mysticmods.roots.block.entity.FeyCrafterBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import noobanidus.libs.noobutil.crafting.Crafting;

import javax.annotation.Nullable;

public class FeyCrafting extends Crafting<FeyCraftingInventory, FeyCrafterBlockEntity> {
  public FeyCrafting(FeyCrafterBlockEntity blockentity, FeyCraftingInventory handler, @Nullable PlayerEntity player) {
    super(blockentity, handler, player);
  }
}
