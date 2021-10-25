package epicsquid.roots.recipe.transmutation;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStateAbove extends BlockStateBelow {
  public BlockStateAbove(BlockStatePredicate state) {
    super(state);
  }

  @Override
  public boolean test(BlockState state, World world, BlockPos pos) {
    return this.state.test(world.getBlockState(pos.up()));
  }

  @Override
  public StatePosition getPosition() {
    return StatePosition.ABOVE;
  }
}
