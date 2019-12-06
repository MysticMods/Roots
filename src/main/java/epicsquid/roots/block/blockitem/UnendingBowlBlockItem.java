package epicsquid.roots.block.blockitem;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UnendingBowlBlockItem extends BlockItem {
  public UnendingBowlBlockItem(Block blockIn, Properties builder) {
    super(blockIn, builder);
  }

  @Nullable
  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
    return new UnendingBowlItemHandler(stack);
  }

  public static class UnendingBowlItemHandler extends FluidHandlerItemStack {
    public UnendingBowlItemHandler(@Nonnull ItemStack container) {
      super(container, Integer.MAX_VALUE);
    }

    @Nullable
    @Override
    public FluidStack getFluid() {
      return new FluidStack(Fluids.WATER, Integer.MAX_VALUE);
    }

    @Override
    protected void setFluid(FluidStack fluid) {
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
      return 0;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction doDrain) {
      return resource.copy();
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction drain) {
      return new FluidStack(Fluids.WATER, maxDrain);
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid) {
      return false;
    }

    @Override
    public boolean canDrainFluidType(FluidStack fluid) {
      return fluid.getFluid() == Fluids.WATER;
    }

    @Override
    protected void setContainerToEmpty() {
    }
  }
}
