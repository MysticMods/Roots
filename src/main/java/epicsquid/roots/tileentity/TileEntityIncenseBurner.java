package epicsquid.roots.tileentity;

import epicsquid.mysticallib.tile.TileBase;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.api.Herb;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.util.ItemSpawnUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class TileEntityIncenseBurner extends TileBase implements ITickable {

  public static final int BURN_TICKS = 1200;

  public ItemStackHandler inventory = new ItemStackHandler(1) {
    @Override
    protected void onContentsChanged(int slot) {
      TileEntityIncenseBurner.this.markDirty();
      if (!world.isRemote) {
        TileEntityIncenseBurner.this.updatePacketViaState();
        //PacketHandler.sendToAllTracking(new MessageTEUpdate(TileEntityIncenseBurner.this.getUpdateTag()), TileEntityIncenseBurner.this);
      }
    }
  };

  private int burnTick;
  private boolean lit;

  public TileEntityIncenseBurner() {
    burnTick = 0;
    lit = false;
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger("burnTick", this.burnTick);
    compound.setBoolean("lit", this.lit);
    compound.setTag("handler", inventory.serializeNBT());
    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    this.burnTick = compound.getInteger("burnTick");
    this.lit = compound.getBoolean("lit");
    inventory.deserializeNBT(compound.getCompoundTag("handler"));
    super.readFromNBT(compound);
  }

  @Override
  public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
    if (hand != EnumHand.MAIN_HAND) {
      return false;
    }
    ItemStack stack = player.getHeldItem(hand);
    ItemStack inventoryStack = this.inventory.getStackInSlot(0);
    if (stack.isEmpty()) {
      if (lit) {
        this.lit = false;
        markDirty();
        if (!world.isRemote)
          updatePacketViaState();
        //PacketHandler.sendToAllTracking(new MessageTEUpdate(this.getUpdateTag()), this);
        return false;
      }
      ItemStack extracted = inventory.extractItem(0, inventory.getStackInSlot(0).getCount(), false);
      if (!world.isRemote) {
        ItemSpawnUtil.spawnItem(world, getPos(), extracted);
        updatePacketViaState();
        //PacketHandler.sendToAllTracking(new MessageTEUpdate(this.getUpdateTag()), this);
      }
      return false;
    }
    if (stack.getItem() == Items.FLINT_AND_STEEL) {
      if (!inventoryStack.isEmpty()) {
        this.lit = true;
        markDirty();
        if (!world.isRemote)
          updatePacketViaState();
        //PacketHandler.sendToAllTracking(new MessageTEUpdate(this.getUpdateTag()), this);
        return false;
      }
    }
    if (!isHerb(stack)) {
      return false;
    }
    if (!inventoryStack.isEmpty()) {
      if (inventoryStack.getItem() == stack.getItem()) {
        int remainingHerbs = 64 - inventoryStack.getCount();
        if (stack.getCount() <= remainingHerbs) {
          inventoryStack.setCount(inventoryStack.getCount() + stack.getCount());
          player.setHeldItem(hand, ItemStack.EMPTY);
        } else {
          inventoryStack.setCount(inventoryStack.getCount() + remainingHerbs);
          stack.setCount(stack.getCount() - remainingHerbs);
        }
      }
    } else {
      this.inventory.insertItem(0, stack, false);
      player.setHeldItem(hand, ItemStack.EMPTY);
    }

    markDirty();
    if (!world.isRemote)
      updatePacketViaState();
    //PacketHandler.sendToAllTracking(new MessageTEUpdate(this.getUpdateTag()), this);

    return false;
  }

  private boolean isHerb(@Nonnull ItemStack stack) {
    for (Herb herb : HerbRegistry.REGISTRY.getValuesCollection()) {
      if (stack.getItem() == herb.getItem()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void update() {
    if (this.lit) {
      double d3 = (double) pos.getX() + 0.5 + Util.rand.nextDouble() * 0.10000000149011612D;
      double d8 = (double) pos.getY() + 0.6;
      double d13 = (double) pos.getZ() + 0.5;
      world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d8, d13, 0.0D, 0.0D, 0.0D);

      burnTick++;

      if (this.burnTick >= BURN_TICKS) {
        this.inventory.extractItem(0, 1, false);
        burnTick = 0;

        if (this.inventory.getStackInSlot(0).isEmpty()) {
          this.lit = false;
        }

        markDirty();
        if (!world.isRemote)
          updatePacketViaState();
        //PacketHandler.sendToAllTracking(new MessageTEUpdate(this.getUpdateTag()), this);
      }


    }
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
    if (!world.isRemote) {
      Util.spawnInventoryInWorld(world, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, inventory);
    }
  }

  public boolean isLit() {
    return lit;
  }

  public Item burningItem() {
    if (this.lit) {
      return this.inventory.getStackInSlot(0).getItem();
    } else {
      return null;
    }
  }
}
