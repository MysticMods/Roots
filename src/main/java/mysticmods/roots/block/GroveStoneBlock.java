package mysticmods.roots.block;

import mysticmods.roots.api.reference.Shapes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import noobanidus.libs.noobutil.block.BaseBlocks;

import javax.annotation.Nullable;

public class GroveStoneBlock extends BaseBlocks.HorizontalBlock {
  public static final DirectionProperty FACING = BaseBlocks.HorizontalBlock.FACING;
  public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);
  public static final BooleanProperty VALID = BooleanProperty.create("valid");

  public GroveStoneBlock(Properties builder) {
    super(builder);
    this.registerDefaultState(defaultBlockState().setValue(VALID, false).setValue(PART, Part.BOTTOM));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
    super.createBlockStateDefinition(pBuilder);
    pBuilder.add(PART, VALID);
  }

  // TODO: ROTATION
  @Override
  public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
    switch (p_220053_1_.getValue(PART)) {
      case TOP:
        return Shapes.GROVE_STONE_TOP;
      case MIDDLE:
        return Shapes.GROVE_STONE_MIDDLE;
      default:
      case BOTTOM:
        return Shapes.GROVE_STONE_BOTTOM;
    }
  }

  @Override
  @Nullable
  public BlockState getStateForPlacement(BlockItemUseContext pContext) {
    BlockPos blockpos = pContext.getClickedPos();
    return blockpos.getY() < 255 && pContext.getLevel().getBlockState(blockpos.above()).canBeReplaced(pContext) && pContext.getLevel().getBlockState(blockpos.above().above()).canBeReplaced(pContext) ? super.getStateForPlacement(pContext) : null;
  }

  @Override
  public void setPlacedBy(World pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
    pLevel.setBlock(pPos.above(), this.defaultBlockState().setValue(PART, Part.MIDDLE).setValue(FACING, pState.getValue(FACING)), 3);
    pLevel.setBlock(pPos.above().above(), this.defaultBlockState().setValue(PART, Part.TOP).setValue(FACING, pState.getValue(FACING)), 3);
  }

  @Override
  public void playerWillDestroy(World pLevel, BlockPos pPos, BlockState pState, PlayerEntity pPlayer) {
    if (!pLevel.isClientSide) {
      preventDrops(pLevel, pPos, pState, pPlayer);
      if (!pPlayer.isCreative()) {
        dropResources(pState, pLevel, pPos, (TileEntity) null, pPlayer, pPlayer.getMainHandItem());
      }
    }

    super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
  }

  @Override
  public void playerDestroy(World pLevel, PlayerEntity pPlayer, BlockPos pPos, BlockState pState, @Nullable TileEntity pTe, ItemStack pStack) {
    super.playerDestroy(pLevel, pPlayer, pPos, Blocks.AIR.defaultBlockState(), pTe, pStack);
  }

  protected static void preventDrops(World pLevel, BlockPos pPos, BlockState pState, PlayerEntity pPlayer) {
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

  public enum Part implements IStringSerializable {
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
