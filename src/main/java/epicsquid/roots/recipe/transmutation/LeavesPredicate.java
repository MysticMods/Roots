package epicsquid.roots.recipe.transmutation;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class LeavesPredicate implements BlockStatePredicate {
  public static List<IBlockState> leaves = null;

  @Override
  public boolean test(IBlockState state) {
    return state.getMaterial() == Material.LEAVES;
  }

  @Override
  public List<IBlockState> matchingStates() {
    if (leaves == null) {
      leaves = new ArrayList<>();
      for (ItemStack stack : OreDictionary.getOres("treeLeaves")) {
        if (stack.getItem() instanceof ItemBlock) {
          leaves.add(((ItemBlock) stack.getItem()).getBlock().getDefaultState());
        }
      }
    }
    return leaves;
  }
}
