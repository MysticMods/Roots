package epicsquid.roots.recipe.transmutation;

import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

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
}
