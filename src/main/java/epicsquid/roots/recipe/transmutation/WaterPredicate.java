package epicsquid.roots.recipe.transmutation;

import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class WaterPredicate extends BlocksPredicate {
  public WaterPredicate() {
    super(Blocks.WATER, Blocks.FLOWING_WATER);
  }

  @Override
  public List<IBlockState> matchingStates() {
    return Collections.singletonList(ModBlocks.fake_water.getDefaultState());
  }

  @Override
  public List<ItemStack> matchingItems() {
    return Collections.singletonList(new ItemStack(ModBlocks.fake_water));
  }
}
