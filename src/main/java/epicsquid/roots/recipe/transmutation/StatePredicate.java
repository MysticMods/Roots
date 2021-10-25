package epicsquid.roots.recipe.transmutation;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class StatePredicate implements BlockStatePredicate {
  protected BlockState state;
  protected ItemStack stack;

  public StatePredicate(BlockState state) {
    this.state = state;
    this.stack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
  }

  @Override
  public List<BlockState> matchingStates() {
    return Collections.singletonList(state);
  }

  @Override
  public List<ItemStack> matchingItems() {
    return Collections.singletonList(stack);
  }

  @Override
  public boolean test(BlockState state) {
    return state.getBlock() == this.state.getBlock();
  }
}
