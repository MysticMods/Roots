package mysticmods.roots.block;

import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.StateProperties;
import mysticmods.roots.blockentity.FungalTransmuterBlockEntity;
import mysticmods.roots.blockentity.template.BaseBlockEntity;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FungalTransmuterBlock extends UseDelegatedBlock implements EntityBlock {
  public static final MapCodec<FungalTransmuterBlock> CODEC = simpleCodec(FungalTransmuterBlock::new);
  public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
  public static final BooleanProperty ACTIVE = StateProperties.ACTIVE;

  protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0);

  @Override
  public MapCodec<FungalTransmuterBlock> codec() {
    return CODEC;
  }

  public FungalTransmuterBlock(BlockBehaviour.Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ACTIVE, false));
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
  }

  @Override
  protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return SHAPE;
  }

  @Override
  protected boolean useShapeForLightOcclusion(BlockState state) {
    return true;
  }

  @Override
  protected RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  @Override
  protected BlockState rotate(BlockState state, Rotation rotation) {
    return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
  }

  @Override
  protected BlockState mirror(BlockState state, Mirror mirror) {
    return state.rotate(mirror.getRotation(state.getValue(FACING)));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, ACTIVE);
  }

  @Override
  protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
    return false;
  }

  @Override
  public @org.jetbrains.annotations.Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new FungalTransmuterBlockEntity(pos, state);
  }

  @Override
  public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
    if (!level.isClientSide()) {
      return BaseBlockEntity::serverTick;
    } else {
      return BaseBlockEntity::clientTick;
    }
  }

  @Override
  protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
    super.onRemove(state, level, pos, newState, movedByPiston);
    if (!newState.is(state.getBlock())) {
      level.getData(ModAttachments.GROVE_CONSUMERS).remove(pos);
    }
  }
}
