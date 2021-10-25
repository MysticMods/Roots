package epicsquid.mysticallib.tile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.util.Util;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class TileBase extends TileEntity implements ITile {
  public boolean dirty = false;

  @Override
  public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand,
      @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
    return false;
  }

  @Override
  public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nullable EntityPlayer player) {
    invalidate();
  }

  @Override
  @Nonnull
  public NBTTagCompound getUpdateTag() {
    return writeToNBT(new NBTTagCompound());
  }

  @Override
  @Nullable
  public SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
  }

  @Override
  public void onDataPacket(@Nonnull NetworkManager net, @Nonnull SPacketUpdateTileEntity pkt) {
    readFromNBT(pkt.getNbtCompound());
  }

  @Nonnull
  public static String getTileName(@Nonnull Class<? extends TileEntity> teClass) {
    return Util.getLowercaseClassName(teClass);
  }

  // TODO: I literally can't think of a better name for this function
  public void updatePacketViaState() {
    if (world != null && !world.isRemote) {
      IBlockState state = world.getBlockState(getPos());
      world.notifyBlockUpdate(getPos(), state, state, 8);
    }
  }

  protected boolean dropItemInInventory(ItemStackHandler inventory, int slot) {
    if (!inventory.getStackInSlot(slot).isEmpty()) {
      ItemStack extracted = inventory.extractItem(slot, 1, false);
      if (!world.isRemote) {
        world.spawnEntity(new EntityItem(world, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, extracted));
      }
      markDirty();
      updatePacketViaState();
      return true;
    }
    return false;
  }
}