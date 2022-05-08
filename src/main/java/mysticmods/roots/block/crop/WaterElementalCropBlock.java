package mysticmods.roots.block.crop;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class WaterElementalCropBlock extends ElementalCropBlock implements LiquidBlockContainer {
  public WaterElementalCropBlock(Properties builder, Supplier<Supplier<? extends ItemLike>> seedProvider) {
    super(builder, seedProvider);
  }

  @Override
  public boolean canPlaceLiquid(BlockGetter pLevel, BlockPos pPos, BlockState pState, Fluid pFluid) {
    return false;
  }

  @Override
  public boolean placeLiquid(LevelAccessor pLevel, BlockPos pPos, BlockState pState, FluidState pFluidState) {
    return false;
  }

  @Nullable
  // TODO: Add check for elemental soils
  public BlockState getStateForPlacement(BlockPlaceContext pContext) {
    FluidState fluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos());
    return fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8 ? super.getStateForPlacement(pContext) : null;
  }

  @Override
  public FluidState getFluidState(BlockState pState) {
    return Fluids.WATER.getSource(false);
  }
}
