package mysticmods.roots.integration.jei.ingredient.block;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface IBlockType {
  ItemStack stack();

  BlockState state();

  default Block block() {
    return state().getBlock();
  }

  default List<Component> additionalTooltipLines() {
    return List.of();
  }
}
