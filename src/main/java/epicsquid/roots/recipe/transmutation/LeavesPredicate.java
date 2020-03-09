package epicsquid.roots.recipe.transmutation;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LeavesPredicate implements BlockStatePredicate {
  public static List<IBlockState> leaves = null;

  @Override
  public boolean test(IBlockState state) {
    return state.getMaterial() == Material.LEAVES;
  }

  @Override
  public List<IBlockState> matchingStates() {
    if (leaves == null) {
      Set<IBlockState> leafBlocks = new HashSet<>();
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
          }
        }
      }
      leaves = new ArrayList<>(leafBlocks);
    }
    return leaves;
  }
}
