package epicsquid.roots.recipe.transmutation;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class MultiStatePredicate implements BlockStatePredicate {
	protected List<IBlockState> states;
	protected List<ItemStack> stacks = null;
	
	public MultiStatePredicate(IBlockState... states) {
		this.states = Lists.newArrayList(states);
	}
	
	@Override
	public List<IBlockState> matchingStates() {
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
	public boolean test(IBlockState state) {
		return states.stream().anyMatch(o -> o.getBlock() == state.getBlock());
	}
}
