package epicsquid.roots.recipe.transmutation;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class StatePredicate implements BlockStatePredicate {
  protected IBlockState state;
  protected ItemStack stack;

  public StatePredicate(IBlockState state) {
    this.state = state;
    this.stack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
  }

  @Override
  public List<IBlockState> matchingStates() {
    return Collections.singletonList(state);
  }

  @Override
  public List<ItemStack> matchingItems() {
    return Collections.singletonList(stack);
  }

  @Override
  public boolean test(IBlockState state) {
    return state.getBlock() == this.state.getBlock();
  }
}
