package epicsquid.roots.tileentity;

import epicsquid.mysticallib.tile.TileBase;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.UUID;

public class TileEntityOffertoryPlate extends TileBase {
  public ItemStackHandler inventory = new ItemStackHandler(1) {
    @Override
    protected void onContentsChanged(int slot) {
      TileEntityOffertoryPlate.this.markDirty();
      if (!world.isRemote) {
        updatePacketViaState();
      }
    }
  };
  private UUID lastPlayer = null;
  private int progress = 0;

  public TileEntityOffertoryPlate() {
    super();
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    tag.setTag("handler", inventory.serializeNBT());
    tag.setInteger("progress", progress);
    if (lastPlayer != null) {
      tag.setTag("lastPlayer", NBTUtil.createUUIDTag(lastPlayer));
    }
    return tag;
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    if (tag.hasKey("lastPlayer")) {
      lastPlayer = NBTUtil.getUUIDFromTag(tag.getCompoundTag("lastPlayer"));
    }
    inventory.deserializeNBT(tag.getCompoundTag("handler"));
    progress = tag.getInteger("progress");
  }

  @Override
  public NBTTagCompound getUpdateTag() {
    return writeToNBT(new NBTTagCompound());
  }

  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
  }

  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    readFromNBT(pkt.getNbtCompound());
  }

  @Override
  public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand,
                          @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack heldItem = player.getHeldItem(hand);
    this.lastPlayer = player.getUniqueID();
    markDirty();

    if (!heldItem.isEmpty()) {
      if (inventory.getStackInSlot(0).isEmpty()) {
        ItemStack toInsert = heldItem.copy();
        ItemStack attemptedInsert = inventory.insertItem(0, toInsert, true);
        if (attemptedInsert.isEmpty()) {
          inventory.insertItem(0, toInsert, false);
          player.getHeldItem(hand).shrink(toInsert.getCount());
          if (player.getHeldItem(hand).getCount() == 0) {
            player.setHeldItem(hand, ItemStack.EMPTY);
          }

          if (!world.isRemote)
            updatePacketViaState();
          return true;
        }
      }
    }
    if (heldItem.isEmpty() && !world.isRemote && hand == EnumHand.MAIN_HAND) {
      if (!inventory.getStackInSlot(0).isEmpty()) {
        ItemStack extracted = inventory.extractItem(0, inventory.getStackInSlot(0).getCount(), false);
        ItemUtil.spawnItem(world, getPos(), extracted);
        if (!world.isRemote)
          updatePacketViaState();
        return true;
      }
    }
    return false;
  }

  public ItemStack getHeldItem() {
    return this.inventory.getStackInSlot(0);
  }

  public void removeItem() {
    ItemStack stack = this.inventory.getStackInSlot(0);
    stack.setCount(stack.getCount() - 1);
    if (stack.getCount() == 0) {
      inventory.setStackInSlot(0, ItemStack.EMPTY);
    } else {
      inventory.setStackInSlot(0, stack);
    }
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
    if (!world.isRemote) {
      Util.spawnInventoryInWorld(world, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, inventory);
    }
  }

  public UUID getLastPlayer() {
    return lastPlayer;
  }
}
