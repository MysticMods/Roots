package mysticmods.roots.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.StateProperties;
import mysticmods.roots.api.reference.Shapes;
import mysticmods.roots.blockentity.GroveStoneBlockEntity;
import mysticmods.roots.blockentity.template.BaseBlockEntity;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.particle.RootsParticleOptions;
import mysticmods.roots.util.VoxelUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
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
      return VoxelUtil.rotateHorizontal(Shapes.HUT_LOWER, blockState.getValue(FACING).getClockWise());
    } else {;
      return VoxelUtil.rotateHorizontal(Shapes.HUT_UPPER, blockState.getValue(FACING).getClockWise());
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

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
      double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 1.2;
      double y = pos.getY() + 0.5 + (random.nextDouble() - 0.5) * 0.5;
      double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 1.2;

      level.addParticle(RootsParticleOptions.builder(ModParticles.SYLVAN_LIGHT).color(ModSpells.SYLVAN_LIGHT).build(), x, y, z, (random.nextDouble() - 0.5) * 0.02, (random.nextDouble() - 0.5) * 0.02, (random.nextDouble() - 0.5) * 0.02);
    }
}
