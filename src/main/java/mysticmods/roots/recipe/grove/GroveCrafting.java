package mysticmods.roots.recipe.grove;

import com.mojang.datafixers.util.Pair;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.recipe.crafting.RootsTileCrafting;
import mysticmods.roots.blockentity.GroveCrafterBlockEntity;
import mysticmods.roots.blockentity.PedestalBlockEntity;
import net.minecraft.core.BlockPos;
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
    if (getBlockEntity() == null) {
      return result;
    }
    for (Pair<BlockPos, PedestalBlockEntity> entry : getBlockEntity().pedestals(RootsTags.Blocks.GROVE_PEDESTALS, RootsTags.Blocks.DISPLAY_PEDESTALS)) {
      result.add(entry.getSecond().popOne());
    }
    return result;
  }
}
