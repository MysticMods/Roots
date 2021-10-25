package epicsquid.roots.recipe.transmutation;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class MultiStatePredicate implements BlockStatePredicate {
  protected List<BlockState> states;
  protected List<ItemStack> stacks = null;

  public MultiStatePredicate(BlockState... states) {
    this.states = Lists.newArrayList(states);
  }

  @Override
  public List<BlockState> matchingStates() {
    return states;
  }

  @Override
  public List<ItemStack> matchingItems() {
    if (stacks == null) {
      stacks = states.stream().map(o -> new ItemStack(o.getBlock(), 1, o.getBlock().getMetaFromState(o))).collect(Collectors.toList());
    }
    return null;
  }

  @Override
  public boolean test(BlockState state) {
    return states.stream().anyMatch(o -> o.getBlock() == state.getBlock());
  }
}
