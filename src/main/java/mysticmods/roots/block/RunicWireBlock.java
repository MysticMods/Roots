package mysticmods.roots.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RunicWireBlock extends Block {
  public static final EnumProperty<RedstoneSide> NORTH = BlockStateProperties.NORTH_REDSTONE;
  public static final EnumProperty<RedstoneSide> EAST = BlockStateProperties.EAST_REDSTONE;
  public static final EnumProperty<RedstoneSide> SOUTH = BlockStateProperties.SOUTH_REDSTONE;
  public static final EnumProperty<RedstoneSide> WEST = BlockStateProperties.WEST_REDSTONE;
  protected static final VoxelShape[] SHAPES = {Block.box(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 16.0D), Block.box(0.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D), Block.box(0.0D, 0.0D, 3.0D, 13.0D, 1.0D, 16.0D), Block.box(3.0D, 0.0D, 0.0D, 13.0D, 1.0D, 13.0D), Block.box(3.0D, 0.0D, 0.0D, 13.0D, 1.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 13.0D, 1.0D, 13.0D), Block.box(0.0D, 0.0D, 0.0D, 13.0D, 1.0D, 16.0D), Block.box(3.0D, 0.0D, 3.0D, 16.0D, 1.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 16.0D, 1.0D, 16.0D), Block.box(0.0D, 0.0D, 3.0D, 16.0D, 1.0D, 13.0D), Block.box(0.0D, 0.0D, 3.0D, 16.0D, 1.0D, 16.0D), Block.box(3.0D, 0.0D, 0.0D, 16.0D, 1.0D, 13.0D), Block.box(3.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 13.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D)};

  public RunicWireBlock(BlockBehaviour.Properties properties) {
    super(properties);
    this.registerDefaultState(this.defaultBlockState().setValue(RunicWireBlock.NORTH, RedstoneSide.NONE)
        .setValue(RunicWireBlock.EAST, RedstoneSide.NONE).setValue(RunicWireBlock.SOUTH, RedstoneSide.NONE)
        .setValue(RunicWireBlock.WEST, RedstoneSide.NONE));
  }

  private static int getShapeIndex(BlockState state) {
    var i = 0;
    var n = state.getValue(RunicWireBlock.NORTH) != RedstoneSide.NONE;
    var e = state.getValue(RunicWireBlock.EAST) != RedstoneSide.NONE;
    var s = state.getValue(RunicWireBlock.SOUTH) != RedstoneSide.NONE;
    var w = state.getValue(RunicWireBlock.WEST) != RedstoneSide.NONE;

    if (n || s && !n && !e && !w) {
      i |= 1 << Direction.NORTH.get2DDataValue();
    }
    if (e || w && !n && !e && !s) {
      i |= 1 << Direction.EAST.get2DDataValue();
    }
    if (s || n && !e && !s && !w) {
      i |= 1 << Direction.SOUTH.get2DDataValue();
    }
    if (w || e && !n && !s && !w) {
      i |= 1 << Direction.WEST.get2DDataValue();
    }
    return i;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(RunicWireBlock.NORTH, RunicWireBlock.EAST, RunicWireBlock.SOUTH, RunicWireBlock.WEST);
  }

  @Override
  @SuppressWarnings("deprecation")
  public VoxelShape getShape(BlockState state, BlockGetter levelIn, BlockPos pos, CollisionContext context) {
    return RunicWireBlock.SHAPES[RunicWireBlock.getShapeIndex(state)];
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    BlockGetter iblockreader = context.getLevel();
    var blockpos = context.getClickedPos();
    return this.defaultBlockState().setValue(RunicWireBlock.WEST, this.getSide(iblockreader, blockpos, Direction.WEST))
        .setValue(RunicWireBlock.EAST, this.getSide(iblockreader, blockpos, Direction.EAST))
        .setValue(RunicWireBlock.NORTH, this.getSide(iblockreader, blockpos, Direction.NORTH))
        .setValue(RunicWireBlock.SOUTH, this.getSide(iblockreader, blockpos, Direction.SOUTH));
  }

  @Override
  @SuppressWarnings("deprecation")
  public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor levelIn, BlockPos currentPos, BlockPos facingPos) {
    if (facing == Direction.DOWN) {
      return stateIn;
    } else {
      return facing == Direction.UP ? stateIn.setValue(RunicWireBlock.WEST, this.getSide(levelIn, currentPos, Direction.WEST))
          .setValue(RunicWireBlock.EAST, this.getSide(levelIn, currentPos, Direction.EAST))
          .setValue(RunicWireBlock.NORTH, this.getSide(levelIn, currentPos, Direction.NORTH))
          .setValue(RunicWireBlock.SOUTH, this.getSide(levelIn, currentPos, Direction.SOUTH)) : stateIn.setValue(RedStoneWireBlock.PROPERTY_BY_DIRECTION.get(facing), this.getSide(levelIn, currentPos, facing));
    }
  }

  private RedstoneSide getSide(BlockGetter levelIn, BlockPos pos, Direction face) {
    var blockpos = pos.relative(face);
    var blockstate = levelIn.getBlockState(blockpos);
    var blockpos1 = pos.above();
    var blockstate1 = levelIn.getBlockState(blockpos1);
    if (!blockstate1.isCollisionShapeFullBlock(levelIn, blockpos1)) {
      var flag = blockstate.isFaceSturdy(levelIn, blockpos, Direction.UP) || blockstate.getBlock() == Blocks.HOPPER;
      if (flag && this.canConnectTo(levelIn.getBlockState(blockpos.above()))) {
        if (blockstate.isCollisionShapeFullBlock(levelIn, blockpos)) {
          return RedstoneSide.UP;
        }

        return RedstoneSide.SIDE;
      }
    }

    return !this.canConnectTo(blockstate) && (blockstate.isCollisionShapeFullBlock(levelIn, blockpos) || !this.canConnectTo(levelIn.getBlockState(blockpos.below()))) ? RedstoneSide.NONE : RedstoneSide.SIDE;
  }

  protected boolean canConnectTo(BlockState blockState) {
    var block = blockState.getBlock();
    return block == this;
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean canSurvive(BlockState state, LevelReader levelIn, BlockPos pos) {
    var blockpos = pos.below();
    var blockstate = levelIn.getBlockState(blockpos);
    return blockstate.isFaceSturdy(levelIn, blockpos, Direction.UP) || blockstate.getBlock() == Blocks.HOPPER;
  }

  @Override
  @SuppressWarnings("deprecation")
  public VoxelShape getCollisionShape(BlockState state, BlockGetter levelIn, BlockPos pos, CollisionContext context) {
    return Shapes.empty();
  }

  @Override
  @SuppressWarnings("deprecation")
  public void onPlace(BlockState state, Level levelIn, BlockPos pos, BlockState oldState, boolean isMoving) {
    if (oldState.getBlock() != state.getBlock() && !levelIn.isClientSide) {
      for (var direction : Direction.Plane.VERTICAL) {
        levelIn.updateNeighborsAt(pos.relative(direction), this);
      }
      for (var direction1 : Direction.Plane.HORIZONTAL) {
        this.notifyWireNeighborsOfStateChange(levelIn, pos.relative(direction1));
      }
      for (var direction2 : Direction.Plane.HORIZONTAL) {
        var blockpos = pos.relative(direction2);
        if (levelIn.getBlockState(blockpos).isCollisionShapeFullBlock(levelIn, blockpos)) {
          this.notifyWireNeighborsOfStateChange(levelIn, blockpos.above());
        } else {
          this.notifyWireNeighborsOfStateChange(levelIn, blockpos.below());
        }
      }

    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public void onRemove(BlockState state, Level levelIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (!isMoving && state.getBlock() != newState.getBlock()) {
      super.onRemove(state, levelIn, pos, newState, isMoving);
      if (!levelIn.isClientSide) {
        for (var direction : Direction.values()) {
          levelIn.updateNeighborsAt(pos.relative(direction), this);
        }
        for (var direction1 : Direction.Plane.HORIZONTAL) {
          this.notifyWireNeighborsOfStateChange(levelIn, pos.relative(direction1));
        }
        for (var direction2 : Direction.Plane.HORIZONTAL) {
          var blockpos = pos.relative(direction2);
          if (levelIn.getBlockState(blockpos).isCollisionShapeFullBlock(levelIn, blockpos)) {
            this.notifyWireNeighborsOfStateChange(levelIn, blockpos.above());
          } else {
            this.notifyWireNeighborsOfStateChange(levelIn, blockpos.below());
          }
        }

      }
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public void neighborChanged(BlockState state, Level levelIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    if (!levelIn.isClientSide) {
      if (!state.canSurvive(levelIn, pos)) {
        Block.dropResources(state, levelIn, pos);
        levelIn.removeBlock(pos, false);
      }
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public void updateIndirectNeighbourShapes(BlockState state, LevelAccessor levelIn, BlockPos pos, int flags, int recursionLeft) {
    var pool = new BlockPos.MutableBlockPos();
    for (var direction : Direction.Plane.HORIZONTAL) {
      var redstoneside = state.getValue(RedStoneWireBlock.PROPERTY_BY_DIRECTION.get(direction));
      if (redstoneside != RedstoneSide.NONE && levelIn.getBlockState(pool.set(pos).move(direction))
          .getBlock() != this) {
        pool.move(Direction.DOWN);
        var blockstate = levelIn.getBlockState(pool);
        if (blockstate.getBlock() != Blocks.OBSERVER) {
          var blockpos = pool.relative(direction.getOpposite());
          var blockstate1 = blockstate.updateShape(direction.getOpposite(), levelIn.getBlockState(blockpos), levelIn, pool, blockpos);
          Block.updateOrDestroy(blockstate, blockstate1, levelIn, pool, flags);
        }

        pool.set(pos).move(direction).move(Direction.UP);
        var blockstate3 = levelIn.getBlockState(pool);
        if (blockstate3.getBlock() != Blocks.OBSERVER) {
          var blockpos1 = pool.relative(direction.getOpposite());
          var blockstate2 = blockstate3.updateShape(direction.getOpposite(), levelIn.getBlockState(blockpos1), levelIn, pool, blockpos1);
          Block.updateOrDestroy(blockstate3, blockstate2, levelIn, pool, flags);
        }
      }
    }

  }

  private void notifyWireNeighborsOfStateChange(Level levelIn, BlockPos pos) {
    if (levelIn.getBlockState(pos).getBlock() == this) {
      levelIn.updateNeighborsAt(pos, this);

      for (var direction : Direction.values()) {
        levelIn.updateNeighborsAt(pos.relative(direction), this);
      }
    }
  }
}
