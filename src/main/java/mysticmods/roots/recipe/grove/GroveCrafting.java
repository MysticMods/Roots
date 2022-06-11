package mysticmods.roots.recipe.grove;

import mysticmods.roots.api.recipe.RootsTileCrafting;
import mysticmods.roots.block.PedestalBlock;
import mysticmods.roots.block.entity.GroveCrafterBlockEntity;
import mysticmods.roots.block.entity.PedestalBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

// TODO: Pedestal access

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
