package epicsquid.roots.recipe.transmutation;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LeavesPredicate implements BlockStatePredicate {
  public static List<BlockState> leaves = null;
  public static List<ItemStack> leafItems = null;

  @Override
  public boolean test(BlockState state) {
    return state.getMaterial() == Material.LEAVES;
  }

  @Override
  public List<BlockState> matchingStates() {
    if (leaves == null) {
      Set<BlockState> leafBlocks = new HashSet<>();
      leafItems = new ArrayList<>();
      for (ItemStack stack : OreDictionary.getOres("treeLeaves")) {
        if (stack.getItem() instanceof BlockItem) {
          Block block = ((BlockItem) stack.getItem()).getBlock();
          for (BlockState state : block.getBlockState().getValidStates()) {
            if (state.getPropertyKeys().contains(LeavesBlock.CHECK_DECAY) && state.getValue(LeavesBlock.CHECK_DECAY)) {
              continue;
            }
            if (state.getPropertyKeys().contains(LeavesBlock.DECAYABLE) && state.getValue(LeavesBlock.CHECK_DECAY)) {
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
