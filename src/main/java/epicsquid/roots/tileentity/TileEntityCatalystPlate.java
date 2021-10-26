package epicsquid.roots.tileentity;

import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import noobanidus.libs.noobutil.util.ItemUtil;

import javax.annotation.Nonnull;
import java.util.UUID;

public class TileEntityCatalystPlate extends TileEntity {
  public ItemStackHandler inventory = new ItemStackHandler(1) {
    @Override
    protected void onContentsChanged(int slot) {
      TileEntityCatalystPlate.this.markDirty();
      if (!world.isRemote) {
        //TODO: //TODO: updatePacketViaState();
      }
    }
  };
  private UUID lastPlayer = null;
  private int progress = 0;

  public TileEntityCatalystPlate() {
    super(null);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    super.write(tag);
    tag.put("handler", inventory.serializeNBT());
    tag.putInt("progress", progress);
    if (lastPlayer != null) {
      tag.put("lastPlayer", NBTUtil.func_240626_a_(lastPlayer));
    }
    return tag;
  }

  @Override
  public void read(BlockState state, CompoundNBT tag) {
    super.read(state, tag);
    if (tag.contains("lastPlayer")) {
      lastPlayer = NBTUtil.readUniqueId(tag.getCompound("lastPlayer"));
    }
    inventory.deserializeNBT(tag.getCompound("handler"));
    progress = tag.getInt("progress");
  }

  @Override
  public CompoundNBT getUpdateTag() {
    return write(new CompoundNBT());
  }

  @Override
  public SUpdateTileEntityPacket getUpdatePacket() {
    return new SUpdateTileEntityPacket(getPos(), 9, getUpdateTag());
  }

  @Override
  public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
    read(ModBlocks.catalyst_plate.getDefaultState(), pkt.getNbtCompound());
  }

  public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull Direction side, float hitX, float hitY, float hitZ) {
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

            //TODO: //TODO: updatePacketViaState();
          }
          return true;
        }
      }
    }
    ItemStack inSlot = inventory.getStackInSlot(0);
    if ((heldItem.isEmpty() || ItemUtil.equalWithoutSize(heldItem, inSlot)) && !world.isRemote && hand == Hand.MAIN_HAND) {
      if (!inSlot.isEmpty()) {
        count = inSlot.getCount();
        if (player.isSneaking()) {
          count = 1;
        }
        ItemStack extracted = inventory.extractItem(0, count, false);
        ItemUtil.Spawn.spawnItem(world, getPos(), extracted);
        //TODO: updatePacketViaState();
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
      if (stack.attemptDamageItem(1, world.rand, null)) {
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

  public void breakBlock(World world, BlockPos pos, BlockState state, PlayerEntity player) {
    if (!world.isRemote) {
      // TODO:
      //Util.spawnInventoryInWorld(world, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, inventory);
    }
  }

  public UUID getLastPlayer() {
    return lastPlayer;
  }
}
