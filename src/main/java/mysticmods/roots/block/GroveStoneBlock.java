package mysticmods.roots.block;

import mysticmods.roots.api.reference.Shapes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import noobanidus.libs.noobutil.block.BaseBlocks;
import noobanidus.libs.noobutil.util.VoxelUtil;

import javax.annotation.Nullable;

// TODO: Activation with right-click wildroot? (or just right-click)
public class GroveStoneBlock extends BaseBlocks.HorizontalBlock {
  public static final DirectionProperty FACING = BaseBlocks.HorizontalBlock.FACING;
  public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);
  public static final BooleanProperty VALID = BooleanProperty.create("valid");

  public static final VoxelShape[] EAST_WEST = {VoxelUtil.rotateHorizontal(Shapes.GROVE_STONE_TOP, Direction.EAST), VoxelUtil.rotateHorizontal(Shapes.GROVE_STONE_MIDDLE, Direction.EAST), VoxelUtil.rotateHorizontal(Shapes.GROVE_STONE_BOTTOM, Direction.EAST)};
  public static final VoxelShape[] NORTH_SOUTH = {Shapes.GROVE_STONE_TOP, Shapes.GROVE_STONE_MIDDLE, Shapes.GROVE_STONE_BOTTOM};

  public GroveStoneBlock(Properties builder) {
    super(builder);
    this.registerDefaultState(defaultBlockState().setValue(VALID, false).setValue(PART, Part.BOTTOM));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    super.createBlockStateDefinition(pBuilder);
    pBuilder.add(PART, VALID);
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
    return blockpos.getY() < 255 && pContext.getLevel().getBlockState(blockpos.above()).canBeReplaced(pContext) && pContext.getLevel().getBlockState(blockpos.above().above()).canBeReplaced(pContext) ? super.getStateForPlacement(pContext) : null;
  }

  @Override
  public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
    pLevel.setBlock(pPos.above(), this.defaultBlockState().setValue(PART, Part.MIDDLE).setValue(FACING, pState.getValue(FACING)), 3);
    pLevel.setBlock(pPos.above().above(), this.defaultBlockState().setValue(PART, Part.TOP).setValue(FACING, pState.getValue(FACING)), 3);
  }

  @Override
  public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
    if (!pLevel.isClientSide) {
      preventDrops(pLevel, pPos, pState, pPlayer);
      if (!pPlayer.isCreative()) {
        dropResources(pState, pLevel, pPos, null, pPlayer, pPlayer.getMainHandItem());
      }
    }

    super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
  }

  @Override
  public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, @Nullable BlockEntity pTe, ItemStack pStack) {
    super.playerDestroy(pLevel, pPlayer, pPos, Blocks.AIR.defaultBlockState(), pTe, pStack);
  }

  protected static void preventDrops(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
    Part part = pState.getValue(PART);
    if (part == Part.TOP) {
      BlockPos blockpos = pPos.below();
      BlockState blockstate = pLevel.getBlockState(blockpos);
      if (blockstate.getBlock() == pState.getBlock() && blockstate.getValue(PART) == Part.MIDDLE) {
        pLevel.destroyBlock(blockpos, false);
      }
      blockpos = blockpos.below();
      blockstate = pLevel.getBlockState(blockpos);
      if (blockstate.getBlock() == pState.getBlock() && blockstate.getValue(PART) == Part.BOTTOM) {
        pLevel.destroyBlock(blockpos, false);
      }
    } else if (part == Part.MIDDLE) {
      BlockPos blockPos = pPos.above();
      BlockState blockState = pLevel.getBlockState(blockPos);
      if (blockState.getBlock() == pState.getBlock() && blockState.getValue(PART) == Part.TOP) {
        pLevel.destroyBlock(blockPos, false);
      }
      blockPos = pPos.below();
      blockState = pLevel.getBlockState(blockPos);
      if (blockState.getBlock() == pState.getBlock() && blockState.getValue(PART) == Part.BOTTOM) {
        pLevel.destroyBlock(blockPos, false);
      }
    } else if (part == Part.BOTTOM) {
      BlockPos blockPos = pPos.above();
      BlockState blockState = pLevel.getBlockState(blockPos);
      if (blockState.getBlock() == pState.getBlock() && blockState.getValue(PART) == Part.MIDDLE) {
        pLevel.destroyBlock(blockPos, false);
      }
      blockPos = blockPos.above();
      blockState = pLevel.getBlockState(blockPos);
      if (blockState.getBlock() == pState.getBlock() && blockState.getValue(PART) == Part.TOP) {
        pLevel.destroyBlock(blockPos, false);
      }
    }
  }

  public enum Part implements StringRepresentable {
    TOP("top"),
    MIDDLE("middle"),
    BOTTOM("bottom");

    private final String partName;

    Part(String partName) {
      this.partName = partName;
    }

    @Override
    public String getSerializedName() {
      return this.partName;
    }
  }
}
