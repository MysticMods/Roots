package mysticmods.roots.recipe.grove;

import mysticmods.roots.api.recipe.crafting.RootsTileCrafting;
import mysticmods.roots.blockentity.GroveCrafterBlockEntity;
import mysticmods.roots.blockentity.PedestalBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GroveCrafting extends RootsTileCrafting<GroveInventoryWrapper, GroveCrafterBlockEntity> {
  public GroveCrafting(GroveInventoryWrapper handler, GroveCrafterBlockEntity blockEntity, @Nullable Player player) {
    super(handler, blockEntity, player);
  }

  public List<ItemStack> popItems() {
    List<ItemStack> result = new ArrayList<>();
    for (PedestalBlockEntity pedestal : getHandler().getPedestals()) {
      result.add(pedestal.popOne());
    }
    return result;
  }
}
