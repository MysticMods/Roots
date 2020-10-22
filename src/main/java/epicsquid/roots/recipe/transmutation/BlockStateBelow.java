package epicsquid.roots.recipe.transmutation;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

@SuppressWarnings("deprecation")
public class BlockStateBelow implements WorldBlockStatePredicate {
  protected BlockStatePredicate state;

  public BlockStateBelow(BlockStatePredicate state) {
    this.state = state;
  }

  @Override
  public boolean test(IBlockState state, World world, BlockPos pos) {
    return this.state.test(world.getBlockState(pos.down()));
  }

  @Override
  public List<IBlockState> matchingStates() {
    return this.state.matchingStates();
  }

  @Override
  public List<ItemStack> matchingItems() {
    return this.state.matchingItems();
  }

  @Override
  public StatePosition getPosition() {
    return StatePosition.BELOW;
  }
}
