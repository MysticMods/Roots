package mysticmods.roots.block;

import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.block.StateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public abstract class PillarBlock extends Block implements SimpleWaterloggedBlock {
  public static final EnumProperty<StateProperties.Part> PART = StateProperties.GroveStone.PART;
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  public PillarBlock(Properties builder) {
    super(builder);
    this.registerDefaultState(defaultBlockState().setValue(PART, StateProperties.Part.BOTTOM)
        .setValue(WATERLOGGED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    super.createBlockStateDefinition(pBuilder);
    pBuilder.add(PART, WATERLOGGED);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    VoxelShape[] parts = getParts();

    return switch (state.getValue(PART)) {
      case TOP -> parts[2];
      case MIDDLE -> parts[1];
      case BOTTOM -> parts[0];
    };
  }

  protected abstract VoxelShape[] getParts();

  @Override
  @Nullable
  public BlockState getStateForPlacement(BlockPlaceContext pContext) {
    BlockPos blockpos = pContext.getClickedPos();
    boolean waterlogged = pContext.getLevel().getFluidState(blockpos).isSourceOfType(Fluids.WATER);
    BlockState newState = blockpos.getY() < pContext.getLevel().getMaxBuildHeight() - 1 && pContext.getLevel()
        .getBlockState(blockpos.above())
        .canBeReplaced(pContext) && pContext.getLevel().getBlockState(blockpos.above().above())
        .canBeReplaced(pContext) ? super.getStateForPlacement(pContext) : null;
    if (newState == null) {
      return null;
    }

    newState = newState.setValue(WATERLOGGED, waterlogged);

    return newState;
  }

  @Override
  public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
    pLevel.setBlock(pPos.above(), this.defaultBlockState().setValue(PART, StateProperties.Part.MIDDLE), 3);
    pLevel.setBlock(pPos.above().above(), this.defaultBlockState().setValue(PART, StateProperties.Part.TOP), 3);
  }

  @Override
  public BlockState playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
    if (!pLevel.isClientSide) {
      breakLinkedBlocks(pLevel, pPos, pState, pPlayer);
    }
    return super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
  }

  @Override
  protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
    if (state.getValue(WATERLOGGED)) {
      level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
    }

    StateProperties.Part part = state.getValue(PART);

    if (facing.getAxis() == Direction.Axis.Y) {
      if (part == StateProperties.Part.BOTTOM && facing == Direction.UP) {
        if (!facingState.is(this) || facingState.getValue(PART) != StateProperties.Part.MIDDLE) {
          return Blocks.AIR.defaultBlockState();
        }
      } else if (part == StateProperties.Part.MIDDLE) {
        if (facing == Direction.UP) {
          if (!facingState.is(this) || facingState.getValue(PART) != StateProperties.Part.TOP) {
            return Blocks.AIR.defaultBlockState();
          }
        } else if (facing == Direction.DOWN) {
          if (!facingState.is(this) || facingState.getValue(PART) != StateProperties.Part.BOTTOM) {
            return Blocks.AIR.defaultBlockState();
          }
        }
      } else if (part == StateProperties.Part.TOP && facing == Direction.DOWN) {
        if (!facingState.is(this) || facingState.getValue(PART) != StateProperties.Part.MIDDLE) {
          return Blocks.AIR.defaultBlockState();
        }
      }
    }

    return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  protected void breakLinkedBlocks(LevelAccessor pLevel, BlockPos pPos, BlockState pState, @Nullable Player pPlayer) {
    boolean creative = pPlayer != null && pPlayer.isCreative();
    if (pState.getValue(PART) == StateProperties.Part.BOTTOM) {
      pLevel.destroyBlock(pPos.above(), false);
      pLevel.destroyBlock(pPos.above().above(), false);
    } else if (pState.getValue(PART) == StateProperties.Part.MIDDLE) {
      pLevel.destroyBlock(pPos.below(), !creative);
      pLevel.destroyBlock(pPos.above(), false);
    } else {
      pLevel.destroyBlock(pPos.below(), false);
      pLevel.destroyBlock(pPos.below().below(), !creative);
    }
  }

  public static class Short extends PillarBlock {
    public static final MapCodec<Block> CODEC = simpleCodec(Short::new);

    private static final VoxelShape[] SHAPES = new VoxelShape[]{
        Block.box(3, 0, 3, 13, 16, 13),
        Block.box(3, 0, 3, 13, 16, 13),
        Block.box(3, 0, 3, 13, 3, 13)
    };

    public Short(Properties builder) {
      super(builder);
    }

    @Override
    protected VoxelShape[] getParts() {
      return SHAPES;
    }

    @Override
    protected MapCodec<? extends Block> codec() {
      return CODEC;
    }
  }

  public static class Tall extends PillarBlock {
    public static final MapCodec<Block> CODEC = simpleCodec(Tall::new);

    private static final VoxelShape[] SHAPES = new VoxelShape[]{
        Block.box(3, 0, 3, 13, 16, 13),
        Block.box(3, 0, 3, 13, 16, 13),
        Block.box(3, 0, 3, 13, 11, 13)
    };

    public Tall(Properties builder) {
      super(builder);
    }

    @Override
    protected VoxelShape[] getParts() {
      return SHAPES;
    }

    @Override
    protected MapCodec<? extends Block> codec() {
      return CODEC;
    }
  }
}
