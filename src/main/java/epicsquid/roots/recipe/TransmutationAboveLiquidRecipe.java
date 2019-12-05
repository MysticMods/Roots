package epicsquid.roots.recipe;

import epicsquid.roots.util.types.WorldPosStatePredicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;

public class TransmutationAboveLiquidRecipe extends TransmutationRecipe {
  private final Fluid below;
  private boolean water = false;
  private boolean lava = false;

  public TransmutationAboveLiquidRecipe(ResourceLocation name, Block startBlock, ItemStack endState, Fluid below) {
    super(name, startBlock, endState, null);
    this.below = below;
    this.water = below == FluidRegistry.WATER;
    this.lava = below == FluidRegistry.LAVA;
  }

  public TransmutationAboveLiquidRecipe(ResourceLocation name, Block startBlock, BlockState endState, Fluid below) {
    super(name, startBlock, endState, null);
    this.below = below;
    this.water = below == FluidRegistry.WATER;
    this.lava = below == FluidRegistry.LAVA;
  }

  public TransmutationAboveLiquidRecipe(ResourceLocation name, BlockState startState, BlockState endState, Fluid below) {
    super(name, startState, endState, null);
    this.below = below;
    this.water = below == FluidRegistry.WATER;
    this.lava = below == FluidRegistry.LAVA;
  }

  public TransmutationAboveLiquidRecipe(ResourceLocation name, BlockState startState, ItemStack endState, Fluid below) {
    super(name, startState, endState, null);
    this.below = below;
    this.water = below == FluidRegistry.WATER;
    this.lava = below == FluidRegistry.LAVA;
  }

  public WorldPosStatePredicate getCondition() {
    return (t, u, v) -> {
      BlockState down = t.getBlockState(u.down());
      Block block = down.getBlock();
      if ((water || lava) && block instanceof BlockLiquid) {
        if ((block == Blocks.LAVA || block == Blocks.FLOWING_LAVA) && lava) {
          return true;
        }
        if ((block == Blocks.WATER || block == Blocks.FLOWING_WATER) && water) {
          return true;
        }
      } else if (block instanceof IFluidBlock) {
        return ((IFluidBlock) block).getFluid() == below;
      }
      return false;
    };
  }
}
