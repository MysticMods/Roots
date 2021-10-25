package epicsquid.roots.recipe.transmutation;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public interface MatchingStates {
  default List<BlockState> matchingStates() {
    return Collections.emptyList();
  }

  default List<ItemStack> matchingItems() {
    return Collections.emptyList();
  }
}
