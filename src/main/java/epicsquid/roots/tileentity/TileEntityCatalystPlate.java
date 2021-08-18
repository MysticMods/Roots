package epicsquid.roots.tileentity;

import epicsquid.mysticallib.tile.TileBase;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.UUID;

public class TileEntityCatalystPlate extends TileBase {
  public ItemStackHandler inventory = new ItemStackHandler(1) {
    @Override
    protected void onContentsChanged(int slot) {
      TileEntityCatalystPlate.this.markDirty();
      if (!world.isRemote) {
        updatePacketViaState();
      }
    }
  };
  private UUID lastPlayer = null;
  private int progress = 0;

  public TileEntityCatalystPlate() {
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
  public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack heldItem = player.getHeldItem(hand);
    this.lastPlayer = player.getUniqueID();
    markDirty();
    int count = heldItem.getCount();

    if (!heldItem.isEmpty()) {
      ItemStack inSlot = inventory.getStackInSlot(0);
      boolean doInsert = false;
      if (!inSlot.isEmpty() && ItemUtil.equalWithoutSize(inSlot, heldItem) && inSlot.getCount() < inSlot.getMaxStackSize()) {
        if (player.isSneaking()) {
          count = Math.min(inSlot.getMaxStackSize() - inSlot.getCount(), heldItem.getCount());
        }
        doInsert = true;
      } else if (inSlot.isEmpty()) {
        doInsert = true;
      }
      if (doInsert) {
        ItemStack toInsert = heldItem.copy();
        toInsert.setCount(count);
        ItemStack attemptedInsert = inventory.insertItem(0, toInsert, true);
        if (attemptedInsert.isEmpty()) {
          if (!world.isRemote) {
            inventory.insertItem(0, toInsert, false);
            player.getHeldItem(hand).shrink(count);
            if (player.getHeldItem(hand).getCount() == 0) {
              player.setHeldItem(hand, ItemStack.EMPTY);
            }

            updatePacketViaState();
          }
          return true;
        }
      }
    }
    ItemStack inSlot = inventory.getStackInSlot(0);
    if ((heldItem.isEmpty() || ItemUtil.equalWithoutSize(heldItem, inSlot)) && !world.isRemote && hand == EnumHand.MAIN_HAND) {
      if (!inSlot.isEmpty()) {
        count = inSlot.getCount();
        if (player.isSneaking()) {
          count = 1;
        }
        ItemStack extracted = inventory.extractItem(0, count, false);
        ItemUtil.spawnItem(world, getPos(), extracted);
        updatePacketViaState();
        return true;
      }
    }
    return false;
  }

  public ItemStack getHeldItem() {
    return this.inventory.getStackInSlot(0).copy();
  }

  public ItemStack removeItem() {
    ItemStack stack = this.inventory.getStackInSlot(0);
    if (stack.getItem() == ModItems.life_essence) {
      if (stack.attemptDamageItem(1, Util.rand, null)) {
        inventory.setStackInSlot(0, ItemStack.EMPTY);
      }
      return ItemStack.EMPTY;
    } else {
      ItemStack ingredient = stack.copy();
      ingredient.setCount(1);
      stack.shrink(1);
      if (stack.getCount() == 0 || stack.isEmpty()) {
        inventory.setStackInSlot(0, ItemStack.EMPTY);
      } else {
        inventory.setStackInSlot(0, stack);
      }
      return ingredient;
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
