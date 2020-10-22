package epicsquid.roots.recipe.transmutation;

import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class LavaPredicate extends BlocksPredicate {
  public LavaPredicate() {
    super(Blocks.LAVA, Blocks.FLOWING_LAVA);
  }

  @Override
  public List<IBlockState> matchingStates() {
    return Collections.singletonList(ModBlocks.fake_lava.getDefaultState());
  }

  @Override
  public List<ItemStack> matchingItems() {
    return Collections.singletonList(new ItemStack(ModBlocks.fake_lava));
  }
}
