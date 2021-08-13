package epicsquid.roots.recipe.transmutation;

import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class WaterPredicate extends BlocksPredicate {
  public WaterPredicate() {
    super(GeneralConfig.getWaterBlocks().toArray(new Block[0]));
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
