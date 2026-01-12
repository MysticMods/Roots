package mysticmods.roots.block;

import com.mojang.serialization.MapCodec;
import mysticmods.roots.blockentity.WildwoodChestBlockEntity;
import mysticmods.roots.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WildwoodChestBlock extends AbstractChestBlock<WildwoodChestBlockEntity> implements SimpleWaterloggedBlock {
  public static final MapCodec<WildwoodChestBlock> CODEC = simpleCodec(WildwoodChestBlock::new);
  public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  protected static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);
  private static final Component CONTAINER_TITLE = Component.translatable("container.wildwoodchest");

  @Override
  public MapCodec<WildwoodChestBlock> codec() {
    return CODEC;
  }

  public WildwoodChestBlock(BlockBehaviour.Properties p_53121_) {
    super(p_53121_, ModBlockEntities.WILDWOOD_CHEST::get);
    this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH)
        .setValue(WATERLOGGED, Boolean.valueOf(false)));
  }

  @Override
  public DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(
      BlockState state, Level level, BlockPos pos, boolean override
  ) {
    return DoubleBlockCombiner.Combiner::acceptNone;
  }

  @Override
  protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return SHAPE;
  }

  @Override
  protected RenderShape getRenderShape(BlockState state) {
    return RenderShape.ENTITYBLOCK_ANIMATED;
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
    return this.defaultBlockState()
        .setValue(FACING, context.getHorizontalDirection().getOpposite())
        .setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
    if (level.isClientSide()) {
      return InteractionResult.SUCCESS;
    } else {
      MenuProvider provider = this.getMenuProvider(state, level, pos);
      if (provider != null) {
        player.openMenu(provider);
        player.awardStat(Stats.CUSTOM.get(Stats.OPEN_CHEST));
        PiglinAi.angerNearbyPiglins(player, false);
      }
      return InteractionResult.CONSUME;
    }
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new WildwoodChestBlockEntity(pos, state);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
    return level.isClientSide ? createTickerHelper(blockEntityType, ModBlockEntities.WILDWOOD_CHEST.get(), WildwoodChestBlockEntity::lidAnimateTick) : null;
  }

  @Override
  protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
    Containers.dropContentsOnDestroy(state, newState, level, pos);
    super.onRemove(state, level, pos, newState, movedByPiston);
  }

  @Override
  protected BlockState rotate(BlockState state, Rotation rot) {
    return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
  }

  @Override
  protected BlockState mirror(BlockState state, Mirror mirror) {
    return state.rotate(mirror.getRotation(state.getValue(FACING)));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, WATERLOGGED);
  }

  @Override
  protected FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
    if (state.getValue(WATERLOGGED)) {
      level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
    }

    return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
  }

  @Override
  protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
    return false;
  }

  @Override
  protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    BlockEntity blockentity = level.getBlockEntity(pos);
    if (blockentity instanceof WildwoodChestBlockEntity wb) {
      wb.recheckOpen();
    }
  }

  @Override
  protected boolean hasAnalogOutputSignal(BlockState state) {
    return true;
  }

  @Override
  protected int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
    if (level.getBlockEntity(pos) instanceof WildwoodChestBlockEntity wb) {
      return AbstractContainerMenu.getRedstoneSignalFromContainer(wb);
    }
    return 0;
  }
}