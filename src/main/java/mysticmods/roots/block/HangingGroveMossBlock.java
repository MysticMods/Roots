package mysticmods.roots.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.RootsTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Map;

public class HangingGroveMossBlock extends Block {
  public static final MapCodec<HangingGroveMossBlock> CODEC = simpleCodec(HangingGroveMossBlock::new);

  public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
  private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(
      ImmutableMap.of(
          Direction.NORTH,
          Block.box(5.5, 3.0, 11.0, 10.5, 13.0, 16.0),
          Direction.SOUTH,
          Block.box(5.5, 3.0, 0.0, 10.5, 13.0, 5.0),
          Direction.WEST,
          Block.box(11.0, 3.0, 5.5, 16.0, 13.0, 10.5),
          Direction.EAST,
          Block.box(0.0, 3.0, 5.5, 5.0, 13.0, 10.5)
      )
  );


  @Override
  public MapCodec<HangingGroveMossBlock> codec() {
    return CODEC;
  }

  public HangingGroveMossBlock(BlockBehaviour.Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
  }

  @Override
  protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return getShape(state);
  }

  public static VoxelShape getShape(BlockState state) {
    return AABBS.get(state.getValue(FACING));
  }

  @Override
  protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
    return canSurvive(level, pos, state.getValue(FACING));
  }

  public static boolean canSurvive(LevelReader level, BlockPos pos, Direction facing) {
    BlockPos blockpos = pos.relative(facing.getOpposite());
    BlockState blockstate = level.getBlockState(blockpos);
    return blockstate.isFaceSturdy(level, blockpos, facing) && blockstate.is(RootsTags.Blocks.SUPPORTS_HANGING_MOSS);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    BlockState blockstate = this.defaultBlockState();
    LevelReader levelreader = context.getLevel();
    BlockPos blockpos = context.getClickedPos();
    Direction[] adirection = context.getNearestLookingDirections();

    for (Direction direction : adirection) {
      if (direction.getAxis().isHorizontal()) {
        Direction direction1 = direction.getOpposite();
        blockstate = blockstate.setValue(FACING, direction1);
        if (blockstate.canSurvive(levelreader, blockpos)) {
          return blockstate;
        }
      }
    }

    return null;
  }

  @Override
  protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
    return facing.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : state;
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
    builder.add(FACING);
  }
}