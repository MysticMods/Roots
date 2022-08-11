package epicsquid.roots.recipe.transmutation;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlocksPredicate implements BlockStatePredicate {
	private Set<Block> blocks;
	private List<IBlockState> states;
	private List<ItemStack> items;
	
	public BlocksPredicate(Block... blocks) {
		this.blocks = Sets.newHashSet(blocks);
		this.states = Stream.of(blocks).map(Block::getDefaultState).collect(Collectors.toList());
		this.items = states.stream().map(o -> new ItemStack(o.getBlock(), 1, o.getBlock().getMetaFromState(o))).collect(Collectors.toList());
	}
	
	@Override
	public boolean test(IBlockState state) {
		return blocks.contains(state.getBlock());
	}
	
	@Override
	public List<IBlockState> matchingStates() {
		return states;
	}
	
	@Override
	public List<ItemStack> matchingItems() {
		return items;
	}
}
