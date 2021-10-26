package mysticmods.roots.blocks.crops;

import net.minecraft.block.BlockState;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class WaterElementalCropBlock extends ElementalCropBlock implements ILiquidContainer {
  public WaterElementalCropBlock(Properties builder, Supplier<? extends IItemProvider> seedProvider) {
    super(builder, seedProvider);
  }

  @Override
  public boolean canPlaceLiquid(IBlockReader pLevel, BlockPos pPos, BlockState pState, Fluid pFluid) {
    return false;
  }

  @Override
  public boolean placeLiquid(IWorld pLevel, BlockPos pPos, BlockState pState, FluidState pFluidState) {
    return false;
  }

  @Nullable
  // TODO: Add check for elemental soils
  public BlockState getStateForPlacement(BlockItemUseContext pContext) {
    FluidState fluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos());
    return fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8 ? super.getStateForPlacement(pContext) : null;
  }

  @Override
  public FluidState getFluidState(BlockState pState) {
    return Fluids.WATER.getSource(false);
  }
}
