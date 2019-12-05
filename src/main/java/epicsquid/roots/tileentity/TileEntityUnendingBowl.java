package epicsquid.roots.tileentity;

import epicsquid.mysticallib.tile.TileBase;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityUnendingBowl extends TileBase {
  public static UnendingBowlFluidHandler HANDLER = new UnendingBowlFluidHandler();

  @Override
  public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull Direction side, float hitX, float hitY, float hitZ) {

    if (!world.isRemote) {
      return FluidUtil.interactWithFluidHandler(player, hand, HANDLER);
    }

    return true;
  }

  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable Direction facing) {
    return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
  }

  @Nullable
  @Override
  public <T> T getCapability(Capability<T> capability, @Nullable Direction facing) {
    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(HANDLER);
    }

    return null;
  }

  public static class UnendingBowlFluidHandler extends FluidTank {
    public static final String FLUID_NAME = "water";

    public UnendingBowlFluidHandler() {
      super(Integer.MAX_VALUE);
      // TODO: Also handle potential different fluid names in the item stack handler
      this.fluid = FluidRegistry.getFluidStack(FLUID_NAME, Integer.MAX_VALUE);
    }

    @Override
    public FluidTank readFromNBT(CompoundNBT nbt) {
      return this;
    }

    @Override
    public CompoundNBT writeToNBT(CompoundNBT nbt) {
      return nbt;
    }

    @Override
    public FluidStack getFluid() {
      return FluidRegistry.getFluidStack(FLUID_NAME, Integer.MAX_VALUE);
    }

    @Override
    public int getFluidAmount() {
      return Integer.MAX_VALUE;
    }

    @Override
    public int fillInternal(FluidStack resource, boolean doFill) {
      if (resource == null || resource.amount <= 0) {
        return 0;
      }

      if (!fluid.isFluidEqual(resource)) {
        return 0;
      }

      if (!doFill) {
        return capacity;
      }

      if (tile != null) {
        FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluid, tile.getWorld(), tile.getPos(), this, capacity));
      }

      return capacity;
    }

    @Override
    @Nullable
    public FluidStack drainInternal(int maxDrain, boolean doDrain) {
      if (doDrain) {
        if (tile != null) {
          FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(fluid, tile.getWorld(), tile.getPos(), this, maxDrain));
        }
      }
      return new FluidStack(fluid, maxDrain);
    }

    @Override
    public boolean canFill() {
      return true;
    }

    @Override
    public boolean canDrain() {
      return true;
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid) {
      return true;
    }

    @Override
    public boolean canDrainFluidType(@Nullable FluidStack fluid) {
      return fluid.getFluid() == FluidRegistry.getFluid(FLUID_NAME);
    }

    @Override
    protected void onContentsChanged() {

    }
  }
}
