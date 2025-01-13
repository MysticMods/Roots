package mysticmods.roots.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;

@SuppressWarnings({"Duplicates"})
public class WaterloggedBlock extends Block implements SimpleWaterloggedBlock {
  protected static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  public WaterloggedBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false));
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    BlockState state = defaultBlockState();
    FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
    if (fluidState.getType() == Fluids.WATER) {
      return state.setValue(WATERLOGGED, true);
    }

    return state;
  }

  @Override
  public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
    if (stateIn.getValue(WATERLOGGED)) {
      worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
    }

    return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(WATERLOGGED);
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }
}
