package mysticmods.roots.recipe.mortar;

import mysticmods.roots.block.entity.MortarBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import noobanidus.libs.noobutil.crafting.Crafting;

import javax.annotation.Nullable;

public class MortarCrafting extends Crafting<MortarInventory, MortarBlockEntity> {
  public MortarCrafting(MortarBlockEntity blockentity, MortarInventory handler, @Nullable PlayerEntity player) {
    super(blockentity, handler, player);
  }
}
