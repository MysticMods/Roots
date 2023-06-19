package mysticmods.roots.block.crop;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public class WaterElementalCropBlock extends ElementalCropBlock implements SimpleWaterloggedBlock {
  public static BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  public WaterElementalCropBlock(Properties builder, Supplier<Supplier<? extends ItemLike>> seedProvider) {
    super(builder, seedProvider);
    this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false));
  }

  @Override
  public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {

    return super.canSurvive(pState, pLevel, pPos);
  }

  @Nullable
  // TODO: Add check for elemental soils
  public BlockState getStateForPlacement(BlockPlaceContext pContext) {
    Level level = pContext.getLevel();
    BlockPos pos = pContext.getClickedPos();
    if (level.getBlockState(pos.below()).is(RootsAPI.Tags.Blocks.ELEMENTAL_SOIL)) {
      return super.getStateForPlacement(pContext);
    } else {
      FluidState fluidstate = level.getFluidState(pos);
      if (fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8) {
        BlockState newState = super.getStateForPlacement(pContext);
        if (newState != null) {
          return newState.setValue(WATERLOGGED, true);
        }
      }

      return null;
    }
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(WATERLOGGED);
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }
}
