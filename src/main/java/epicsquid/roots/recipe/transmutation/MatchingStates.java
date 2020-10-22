package epicsquid.roots.recipe.transmutation;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public interface MatchingStates {
  default List<IBlockState> matchingStates() {
    return Collections.emptyList();
  }

  default List<ItemStack> matchingItems() {
    return Collections.emptyList();
  }
}
