package epicsquid.roots.recipe.transmutation;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LeavesPredicate implements BlockStatePredicate {
	public static List<IBlockState> leaves = null;
	public static List<ItemStack> leafItems = null;
	
	@Override
	public boolean test(IBlockState state) {
		return state.getMaterial() == Material.LEAVES;
	}
	
	@Override
	public List<IBlockState> matchingStates() {
		if (leaves == null) {
			Set<IBlockState> leafBlocks = new HashSet<>();
			leafItems = new ArrayList<>();
			for (ItemStack stack : OreDictionary.getOres("treeLeaves")) {
				if (stack.getItem() instanceof ItemBlock) {
					Block block = ((ItemBlock) stack.getItem()).getBlock();
					for (IBlockState state : block.getBlockState().getValidStates()) {
						if (state.getPropertyKeys().contains(BlockLeaves.CHECK_DECAY) && state.getValue(BlockLeaves.CHECK_DECAY)) {
							continue;
						}
						if (state.getPropertyKeys().contains(BlockLeaves.DECAYABLE) && state.getValue(BlockLeaves.CHECK_DECAY)) {
							continue;
						}
						leafBlocks.add(state);
						leafItems.add(stack);
					}
				}
			}
			leaves = new ArrayList<>(leafBlocks);
		}
		return leaves;
	}
	
	@Override
	public List<ItemStack> matchingItems() {
		if (leafItems == null) {
			matchingStates();
		}
		return leafItems;
	}
}
