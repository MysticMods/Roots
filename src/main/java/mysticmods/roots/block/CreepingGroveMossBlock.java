package mysticmods.roots.block;

import it.unimi.dsi.fastutil.booleans.BooleanPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class CreepingGroveMossBlock extends CarpetBlock {
  public static BooleanProperty RITUAL_PLACED = BooleanProperty.create("ritual_placed");

  public CreepingGroveMossBlock(Properties p_152915_) {
    super(p_152915_);
    this.registerDefaultState(this.defaultBlockState().setValue(RITUAL_PLACED, false));
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

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    super.createBlockStateDefinition(pBuilder);
    pBuilder.add(RITUAL_PLACED);
  }
}
