package mysticmods.roots.recipe.mortar;

import mysticmods.roots.api.recipe.RootsTileCrafting;
import mysticmods.roots.blockentity.MortarBlockEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class MortarCrafting extends RootsTileCrafting<MortarInventory, MortarBlockEntity> {
  public MortarCrafting(MortarInventory handler, MortarBlockEntity blockEntity, @Nullable Player player) {
    super(handler, blockEntity, player);
  }
}
