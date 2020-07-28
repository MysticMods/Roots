package epicsquid.roots.recipe.transmutation;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;

import java.util.List;

public class MultiStatePredicate implements BlockStatePredicate {
  protected List<IBlockState> states;

  public MultiStatePredicate(IBlockState... states) {
    this.states = Lists.newArrayList(states);
  }

  @Override
  public List<IBlockState> matchingStates() {
    return states;
  }

  @Override
  public boolean test(IBlockState state) {
    return states.stream().anyMatch(o -> o.getBlock() == state.getBlock());
  }
}
