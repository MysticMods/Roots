package epicsquid.roots.recipe.transmutation;

import net.minecraft.block.state.IBlockState;

import java.util.Collections;
import java.util.List;

public interface MatchingStates {
  default List<IBlockState> matchingStates() {
    return Collections.emptyList();
  }
}
