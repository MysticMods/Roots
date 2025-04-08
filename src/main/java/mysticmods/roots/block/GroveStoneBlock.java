package mysticmods.roots.block;

import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.StateProperties;
import mysticmods.roots.api.reference.Shapes;
import mysticmods.roots.util.VoxelUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class GroveStoneBlock extends HorizontalDirectionalBlock {
  public static final DirectionProperty FACING = StateProperties.GroveStone.FACING;
  public static final EnumProperty<StateProperties.Part> PART = StateProperties.GroveStone.PART;
  public static final BooleanProperty ACTIVE = StateProperties.ACTIVE;

  public static final VoxelShape[] EAST_WEST = {VoxelUtil.rotateHorizontal(Shapes.GROVE_STONE_TOP, Direction.EAST), VoxelUtil.rotateHorizontal(Shapes.GROVE_STONE_MIDDLE, Direction.EAST), VoxelUtil.rotateHorizontal(Shapes.GROVE_STONE_BOTTOM, Direction.EAST)};
  public static final VoxelShape[] NORTH_SOUTH = {Shapes.GROVE_STONE_TOP, Shapes.GROVE_STONE_MIDDLE, Shapes.GROVE_STONE_BOTTOM};

  public GroveStoneBlock(Properties builder) {
    super(builder);
    this.registerDefaultState(defaultBlockState().setValue(ACTIVE, false).setValue(PART, StateProperties.Part.BOTTOM));
  }

  @Override
  protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
    return simpleCodec(GroveStoneBlock::new);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    super.createBlockStateDefinition(pBuilder);
    pBuilder.add(PART, ACTIVE, FACING);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
    VoxelShape[] parts;
    Direction facing = state.getValue(FACING);

    if (facing == Direction.SOUTH || facing == Direction.NORTH) {
      parts = NORTH_SOUTH;
    } else {
      parts = EAST_WEST;
    }

    return switch (state.getValue(PART)) {
      case TOP -> parts[0];
      case MIDDLE -> parts[1];
      case BOTTOM -> parts[2];
    };
  }

  @Override
  @Nullable
  public BlockState getStateForPlacement(BlockPlaceContext pContext) {
    BlockPos blockpos = pContext.getClickedPos();
    BlockState newState = blockpos.getY() < pContext.getLevel().getMaxBuildHeight() - 1 && pContext.getLevel().getBlockState(blockpos.above())
        .canBeReplaced(pContext) && pContext.getLevel().getBlockState(blockpos.above().above())
        .canBeReplaced(pContext) ? super.getStateForPlacement(pContext) : null;
    if (newState == null) {
      return null;
    }

    for (Direction direction : pContext.getNearestLookingDirections()) {
      if (direction.getAxis().isHorizontal()) {
        return newState.setValue(FACING, direction);
      }
    }

    return newState;
  }

  @Override
  public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
    pLevel.setBlock(pPos.above(), this.defaultBlockState().setValue(PART, StateProperties.Part.MIDDLE)
        .setValue(FACING, pState.getValue(FACING)), 3);
    pLevel.setBlock(pPos.above().above(), this.defaultBlockState().setValue(PART, StateProperties.Part.TOP)
        .setValue(FACING, pState.getValue(FACING)), 3);
  }

  @Override
  public BlockState playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
    if (!pLevel.isClientSide) {
      breakLinkedBlocks(pLevel, pPos, pState, pPlayer);
    }
    return super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
  }

  protected void breakLinkedBlocks(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
    if (pState.getValue(PART) == StateProperties.Part.BOTTOM) {
      pLevel.destroyBlock(pPos.above(), false);
      pLevel.destroyBlock(pPos.above().above(), false);
    } else if (pState.getValue(PART) == StateProperties.Part.MIDDLE) {
      pLevel.destroyBlock(pPos.below(), !pPlayer.isCreative());
      pLevel.destroyBlock(pPos.above(), false);
    } else {
      pLevel.destroyBlock(pPos.below(), false);
      pLevel.destroyBlock(pPos.below().below(), !pPlayer.isCreative());
    }
  }
}
