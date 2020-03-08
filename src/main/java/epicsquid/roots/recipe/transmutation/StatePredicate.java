package epicsquid.roots.recipe.transmutation;

import net.minecraft.block.state.IBlockState;

import java.util.Collections;
import java.util.List;

public class StatePredicate implements BlockStatePredicate {
  protected IBlockState state;

  public StatePredicate(IBlockState state) {
    this.state = state;
  }

  @Override
  public List<IBlockState> matchingStates() {
    return Collections.singletonList(state);
  }

  @Override
  public boolean test(IBlockState state) {
    return state.getBlock() == this.state.getBlock();
  }
}
