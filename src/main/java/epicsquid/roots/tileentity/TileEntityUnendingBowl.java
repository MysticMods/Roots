package epicsquid.roots.tileentity;

import epicsquid.mysticallib.tile.TileBase;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityUnendingBowl extends TileBase {
  public static UnendingBowlFluidHandler HANDLER = new UnendingBowlFluidHandler();

  public TileEntityUnendingBowl(TileEntityType<?> type) {
    super(type);
  }

  @Override
  public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull Direction side, float hitX, float hitY, float hitZ) {

    if (!world.isRemote) {
      return FluidUtil.interactWithFluidHandler(player, hand, HANDLER);
    }

    return true;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return LazyOptional.of(() -> HANDLER).cast();
    }

    return LazyOptional.empty();
  }

  public static class UnendingBowlFluidHandler extends FluidTank {
    public static final String FLUID_NAME = "water";

    public UnendingBowlFluidHandler() {
      super(Integer.MAX_VALUE);
      // TODO: Also handle potential different fluid names in the item stack handler
      this.fluid = new FluidStack(Fluids.WATER, Integer.MAX_VALUE);
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
      return new FluidStack(Fluids.WATER, Integer.MAX_VALUE);
    }

    @Override
    public int getFluidAmount() {
      return Integer.MAX_VALUE;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
      if (resource == null || resource.getAmount() <= 0) {
        return 0;
      }

      if (!fluid.isFluidEqual(resource)) {
        return 0;
      }

      if (action == FluidAction.SIMULATE) {
        return capacity;
      }

      return capacity;
    }

    @Override
    @Nullable
    public FluidStack drain(int maxDrain, FluidAction action) {
      return new FluidStack(fluid, maxDrain);
    }

    @Override
    public boolean isFluidValid(FluidStack fluid) {
      return true;
    }

    @Override
    protected void onContentsChanged() {

    }
  }
}
