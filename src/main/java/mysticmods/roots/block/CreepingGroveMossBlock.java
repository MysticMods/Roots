package mysticmods.roots.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CreepingGroveMossBlock extends CarpetBlock {
  public CreepingGroveMossBlock(Properties p_152915_) {
    super(p_152915_);
  }

  @Override
  public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
    BlockPos relativePos = pPos.below();
    BlockState target = pLevel.getBlockState(relativePos);
    return target.isFaceSturdy(pLevel, relativePos, Direction.UP);
  }

  @Override
  public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
    if (!this.canSurvive(pState, pLevel, pCurrentPos)) {
      return Blocks.AIR.defaultBlockState();
    }
    return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
  }
}
