package mysticmods.roots.block;

import mysticmods.roots.api.RootsTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WildRootsBlock extends DirectionalBlock implements SimpleWaterloggedBlock {
  public static BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  public static BooleanProperty MOSSY = BooleanProperty.create("mossy");
  private static final int mossyChance = 30;

  //private static final VoxelShape UP_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
  //private static final VoxelShape DOWN_AABB = Block.box(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);

  private static final VoxelShape UP_AABB = Block.box(3, 0, 2, 13, 4, 14);
  private static final VoxelShape DOWN_AABB = Block.box(3, 12, 2, 13, 16, 14);

  private static final VoxelShape EAST_AABB = Block.box(0, 2, 3, 4, 14, 13);
  private static final VoxelShape WEST_AABB = Block.box(12, 2, 3, 16, 14, 13);
  private static final VoxelShape SOUTH_AABB = Block.box(3, 2, 0, 13, 14, 4);
  private static final VoxelShape NORTH_AABB = Block.box(3, 2, 12, 13, 14, 16);

  public WildRootsBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false).setValue(MOSSY, false));
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext pContext) {
    Level level = pContext.getLevel();
    BlockPos pos = pContext.getClickedPos();
    FluidState fluidstate = level.getFluidState(pos);

    BlockState blockstate = this.defaultBlockState().setValue(FACING, pContext.getNearestLookingDirection().getOpposite());
    if (fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8) {
      return blockstate.setValue(WATERLOGGED, true);
    }

    return blockstate;
  }

  @Override
  public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
    return switch (pState.getValue(FACING)) {
      case NORTH -> NORTH_AABB;
      case SOUTH -> SOUTH_AABB;
      case WEST -> WEST_AABB;
      case EAST -> EAST_AABB;
      case UP -> UP_AABB;
      case DOWN -> DOWN_AABB;
    };
  }

  @Override
  public RenderShape getRenderShape(BlockState pState) {
    return RenderShape.MODEL;
  }

  @Override
  public BlockState rotate(BlockState pState, Rotation pRotation) {
    return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
  }

  @Override
  public BlockState mirror(BlockState pState, Mirror pMirror) {
    return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    pBuilder.add(FACING, WATERLOGGED, MOSSY);
  }

  @Override
  public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
    Direction facing = pState.getValue(FACING);
    BlockPos relativePos = pPos.relative(facing.getOpposite());
    BlockState target = pLevel.getBlockState(relativePos);
    return target.isFaceSturdy(pLevel, relativePos, facing) && target.is(RootsTags.Blocks.SUPPORTS_WILD_ROOTS);
  }

  @Override
  public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
    if (!this.canSurvive(pState, pLevel, pCurrentPos)) {
      return Blocks.AIR.defaultBlockState();
    }
    return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }
}
