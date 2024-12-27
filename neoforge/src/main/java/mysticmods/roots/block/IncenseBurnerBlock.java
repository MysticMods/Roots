package mysticmods.roots.block;

import mysticmods.roots.api.reference.Shapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class IncenseBurnerBlock extends Block {
  public IncenseBurnerBlock(Properties builder) {
    super(builder);
  }

  @Override
  public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
    return Shapes.INCENSE_BURNER;
  }
}
