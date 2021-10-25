package epicsquid.roots.recipe.transmutation;

import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class WaterPredicate extends BlocksPredicate {
  public WaterPredicate() {
    super(GeneralConfig.getWaterBlocks().toArray(new Block[0]));
  }

  @Override
  public List<BlockState> matchingStates() {
    return Collections.singletonList(ModBlocks.fake_water.getDefaultState());
  }

  @Override
  public List<ItemStack> matchingItems() {
    return Collections.singletonList(new ItemStack(ModBlocks.fake_water));
  }
}
