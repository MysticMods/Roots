package epicsquid.roots.block.blockitem;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UnendingBowlBlockItem extends BlockItem {
  public UnendingBowlBlockItem(Block block) {
    super(block);
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
      return new FluidStack(FluidRegistry.getFluid("water"), Integer.MAX_VALUE);
    }

    @Override
    protected void setFluid(FluidStack fluid) {
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
      return 0;
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
      return resource.copy();
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
      return new FluidStack(FluidRegistry.getFluid("water"), maxDrain);
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid) {
      return false;
    }

    @Override
    public boolean canDrainFluidType(FluidStack fluid) {
      return fluid.getFluid() == FluidRegistry.getFluid("water");

    }

    @Override
    protected void setContainerToEmpty() {
    }
  }
}
