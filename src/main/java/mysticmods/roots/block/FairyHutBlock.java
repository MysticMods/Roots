package mysticmods.roots.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.StateProperties;
import mysticmods.roots.blockentity.GroveStoneBlockEntity;
import mysticmods.roots.blockentity.template.BaseBlockEntity;
import mysticmods.roots.util.VoxelUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;

public class FairyHutBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlock {
  public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
  public static final BooleanProperty ACTIVE = StateProperties.ACTIVE;
  private final Map<BlockState, VoxelShape> shapesCache;

  public FairyHutBlock(Properties properties) {
    super(properties);
    registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false)
        .setValue(HALF, DoubleBlockHalf.LOWER).setValue(ACTIVE, false));
    this.shapesCache = getShapeForEachState(this::getShape);
  }

  private VoxelShape getShape(BlockState blockState) {
    if (blockState.getValue(HALF) == DoubleBlockHalf.LOWER) {
      VoxelShape half = VoxelUtil.multiOr(Block.box(3.5, 4, 4,11.5, 10, 12), Block.box(2.25, 10, 2,14.25, 16, 14), Block.box(2.25, 9.5, 0.5,14.25, 16, 2), Block.box(14.25, 9.5, 2,15.75, 16, 14), Block.box(2.25, 9.5, 14,14.25, 16, 15.5), Block.box(0.75, 9.5, 2,2.25, 16, 14), Block.box(2.5, 2, 3,12.5, 4, 13), Block.box(2, 2, 6,3.5, 7, 10), Block.box(0, 0, 0,16, 2, 16)/*, Block.box(3.5, 4.26339, -1.37885,5.5, 7.26339, 0.62115)*/, Block.box(2, 4, 0,7, 7, 4), Block.box(12, 2, 12,14, 6, 14), Block.box(10.5, 6, 10.5,15.5, 9, 15.5)/*, Block.box(7.75, 3.7222, 15.68541,8.75, 6.7222, 16.68541)*//*, Block.box(6.25, 6.14398, 14.30586,10.25, 9.14398, 18.30586)*/);
      return VoxelUtil.rotateHorizontal(half, blockState.getValue(FACING).getClockWise());
    } else {
      VoxelShape half = VoxelUtil.multiOr(Block.box(2.25, 0, 0.5,14.25, 5.5, 2), Block.box(14.25, 0, 2,15.75, 5.5, 14), Block.box(2.25, 5.5, 2,14.25, 7, 14), Block.box(2.25, 0, 14,14.25, 5.5, 15.5), Block.box(0.75, 0, 2,2.25, 5.5, 14));
      return VoxelUtil.rotateHorizontal(half, blockState.getValue(FACING).getClockWise());
    }
  }

  @Override
  protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return shapesCache.get(state);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(FACING, WATERLOGGED, HALF, ACTIVE);
  }

  @Override
  @Nullable
  public BlockState getStateForPlacement(BlockPlaceContext pContext) {
    BlockPos blockpos = pContext.getClickedPos();
    boolean waterlogged = pContext.getLevel().getFluidState(blockpos).isSourceOfType(Fluids.WATER);
    BlockState newState = blockpos.getY() < pContext.getLevel().getMaxBuildHeight() - 1 && pContext.getLevel()
        .getBlockState(blockpos.above())
        .canBeReplaced(pContext) ? super.getStateForPlacement(pContext) : null;
    if (newState == null) {
      return null;
    }

    newState = newState.setValue(WATERLOGGED, waterlogged);

    for (Direction direction : pContext.getNearestLookingDirections()) {
      if (direction.getAxis().isHorizontal()) {
        return newState.setValue(FACING, direction);
      }
    }

    return newState;
  }

  @Override
  protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
    return simpleCodec(FairyHutBlock::new);
  }

  @Override
  public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
    pLevel.setBlock(pPos.above(), this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER)
        .setValue(FACING, pState.getValue(FACING)), 3);
  }

  @Override
  public BlockState playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
    if (!pLevel.isClientSide) {
      breakLinkedBlocks(pLevel, pPos, pState, pPlayer);
    }
    return super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
    if (state.getValue(WATERLOGGED)) {
      level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
    }

    DoubleBlockHalf doubleblockhalf = state.getValue(HALF);
    if (facing.getAxis() != Direction.Axis.Y
        || doubleblockhalf == DoubleBlockHalf.LOWER != (facing == Direction.UP)
        || facingState.is(this) && facingState.getValue(HALF) != doubleblockhalf) {
      return doubleblockhalf == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !state.canSurvive(level, currentPos)
          ? Blocks.AIR.defaultBlockState()
          : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    } else {
      return Blocks.AIR.defaultBlockState();
    }
  }

  protected void breakLinkedBlocks(LevelAccessor pLevel, BlockPos pPos, BlockState pState, @Nullable Player pPlayer) {
    boolean creative = pPlayer != null && pPlayer.isCreative();
    if (pState.getValue(HALF) == DoubleBlockHalf.LOWER) {
      pLevel.destroyBlock(pPos.above(), false);
    } else {
      pLevel.destroyBlock(pPos.below(), !creative);
    }
  }

  @Override
  public @org.jetbrains.annotations.Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    if (state.getValue(HALF) != DoubleBlockHalf.LOWER) {
      return null;
    }

    return null; //new GroveStoneBlockEntity(pos, state);
  }

  @org.jetbrains.annotations.Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
    if (pLevel.isClientSide()) {
      return BaseBlockEntity::clientTick;
    } else {
      return BaseBlockEntity::serverTick;
    }
  }

  public static int lightLevel (BlockState state) {
    if (state.getValue(ACTIVE)) {
      return 6;
    }
    return 0;
  }
}
