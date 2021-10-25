package epicsquid.roots.recipe.transmutation;

import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class LavaPredicate extends BlocksPredicate {
  public LavaPredicate() {
    super(net.minecraft.block.Blocks.LAVA, Blocks.FLOWING_LAVA);
  }

  @Override
  public List<BlockState> matchingStates() {
    return Collections.singletonList(ModBlocks.fake_lava.getDefaultState());
  }

  @Override
  public List<ItemStack> matchingItems() {
    return Collections.singletonList(new ItemStack(ModBlocks.fake_lava));
  }
}
