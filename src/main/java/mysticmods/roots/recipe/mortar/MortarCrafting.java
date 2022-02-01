package mysticmods.roots.recipe.mortar;

import mysticmods.roots.api.recipe.RootsTileCrafting;
import mysticmods.roots.block.entity.MortarBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import noobanidus.libs.noobutil.crafting.Crafting;

import javax.annotation.Nullable;

public class MortarCrafting extends RootsTileCrafting<MortarInventory, MortarBlockEntity> {
  public MortarCrafting(MortarInventory handler, MortarBlockEntity blockEntity, @Nullable PlayerEntity player) {
    super(handler, blockEntity, player);
  }
}
