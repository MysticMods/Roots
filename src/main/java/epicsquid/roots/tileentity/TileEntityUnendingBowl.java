package epicsquid.roots.tileentity;

import epicsquid.mysticallib.tile.TileBase;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.config.GeneralConfig;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Potions;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
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

    ItemStack stack = player.getHeldItem(hand);
    if (stack.getItem() == net.minecraft.item.Items.GLASS_BOTTLE) {
      if (!world.isRemote) {
        stack.shrink(1);
        ItemStack potion = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), Potions.WATER);
        if (!player.addItemStackToInventory(potion)) {
          ItemUtil.spawnItem(world, player.getPosition(), potion);
        }
      }

      return true;
    }
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
    public UnendingBowlFluidHandler() {
      super(Integer.MAX_VALUE);
      // TODO: Also handle potential different fluid names in the item stack handler
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
      if (this.fluid == null) {
        this.fluid = FluidRegistry.getFluidStack(GeneralConfig.FluidName, Integer.MAX_VALUE);
      }
      return this.fluid;
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

      if (!getFluid().isFluidEqual(resource)) {
        return 0;
      }

      if (!doFill) {
        return capacity;
      }

      if (tile != null) {
        FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(getFluid(), tile.getWorld(), tile.getPos(), this, capacity));
      }

      return capacity;
    }

    @Override
    @Nullable
    public FluidStack drainInternal(int maxDrain, boolean doDrain) {
      if (doDrain) {
        if (tile != null) {
          FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(getFluid(), tile.getWorld(), tile.getPos(), this, maxDrain));
        }
      }
      return new FluidStack(getFluid(), maxDrain);
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
      return getFluid() != null && fluid.isFluidEqual(getFluid());
    }

    @Override
    public boolean canDrainFluidType(@Nullable FluidStack fluid) {
      return fluid == null || fluid.getFluid() == FluidRegistry.getFluid(GeneralConfig.FluidName);
    }

    @Override
    protected void onContentsChanged() {

    }
  }
}
