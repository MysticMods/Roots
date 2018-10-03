package epicsquid.roots.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.network.MessageTEUpdate;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.tile.TileBase;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.util.Misc;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityBonfire extends TileBase implements ITickable {
  float ticker = 0;
  float pickupDelay = 0;
  int burnTime = 0;
  boolean doBigFlame = false;

  public ItemStackHandler inventory = new ItemStackHandler(5) {
    @Override
    protected void onContentsChanged(int slot) {
      TileEntityBonfire.this.markDirty();
      if (!world.isRemote) {
        PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(TileEntityBonfire.this.getUpdateTag()));
      }
    }
  };

  public TileEntityBonfire() {
    super();
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    tag.setTag("inventory", inventory.serializeNBT());
    tag.setInteger("burnTime", burnTime);
    tag.setBoolean("doBigFlame", doBigFlame);
    return tag;
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    inventory.deserializeNBT(tag.getCompoundTag("inventory"));
    burnTime = tag.getInteger("burnTime");
    doBigFlame = tag.getBoolean("doBigFlame");
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
    if (!heldItem.isEmpty()) {
      if (heldItem.getItem() instanceof ItemFlintAndSteel) {
        List<ItemStack> stacks = new ArrayList<ItemStack>();
        for (int i = 0; i < inventory.getSlots(); i++) {
          stacks.add(inventory.getStackInSlot(i));
        }
        RitualBase ritual = RitualRegistry.getRitual(stacks);
        if (ritual != null) {
          if (ritual.isValidForPos(world, pos)) {
            ritual.doEffect(world, pos);
            this.burnTime = ritual.duration;
            this.doBigFlame = true;
            for (int i = 0; i < inventory.getSlots(); i++) {
              inventory.extractItem(i, 1, false);
            }
            markDirty();
            PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this.getUpdateTag()));
          }
        }
      } else {
        for (int i = 0; i < 5; i++) {
          if (inventory.getStackInSlot(i).isEmpty()) {
            ItemStack toInsert = heldItem.copy();
            toInsert.setCount(1);
            ItemStack attemptedInsert = inventory.insertItem(i, toInsert, true);
            if (attemptedInsert.isEmpty()) {
              inventory.insertItem(i, toInsert, false);
              heldItem.setCount(heldItem.getCount()-1);
              if(heldItem.getCount() <= 0){
                player.setHeldItem(hand, ItemStack.EMPTY);
              }
              else{
                player.setHeldItem(hand, heldItem);
              }
              markDirty();
              PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this.getUpdateTag()));
              return true;
            }
          }
        }
      }
    }
    if (heldItem.isEmpty() && !world.isRemote && hand == EnumHand.MAIN_HAND) {
      for (int i = 4; i >= 0; i--) {
        if (!inventory.getStackInSlot(i).isEmpty()) {
          ItemStack extracted = inventory.extractItem(i, inventory.getStackInSlot(i).getCount(), false);
          if (!world.isRemote) {
            world.spawnEntity(new EntityItem(world, player.posX, player.posY + 0.5, player.posZ, extracted));
          }
          markDirty();
          PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this.getUpdateTag()));
          pickupDelay = 40;
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
    if (!world.isRemote) {
      Misc.spawnInventoryInWorld(world, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, inventory);
    }
  }

  @Override
  public void update() {
    this.ticker += 1.0f;
    if (pickupDelay > 0) {
      pickupDelay--;
    }
    if (world.isRemote && this.doBigFlame) {
      for (int i = 0; i < 40; i++) {
        //todo: fix particle
        //ParticleUtil.spawnParticleFiery(world, getPos().getX()+0.125f+0.75f*Misc.random.nextFloat(), getPos().getY()+0.75f+0.5f*Misc.random.nextFloat(), getPos().getZ()+0.125f+0.75f*Misc.random.nextFloat(), 0.03125f*(Misc.random.nextFloat()-0.5f), 0.125f*Misc.random.nextFloat(), 0.03125f*(Misc.random.nextFloat()-0.5f), 255.0f, 224.0f, 32.0f, 0.75f, 9.0f+9.0f*Misc.random.nextFloat(), 40);
      }
    }
    if (doBigFlame) {
      doBigFlame = false;
      markDirty();
      PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this.getUpdateTag()));
    }
    if (burnTime > 0) {
      burnTime--;
      if (burnTime == 0) {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < inventory.getSlots(); i++) {
          stacks.add(inventory.getStackInSlot(i));
        }
        RitualBase ritual = RitualRegistry.getRitual(stacks);
        if (ritual != null) {
          if (ritual.isValidForPos(world, getPos())) {
            ritual.doEffect(world, pos);
            this.burnTime = ritual.duration;
            for (int i = 0; i < inventory.getSlots(); i++) {
              inventory.extractItem(i, 1, false);
            }
            this.doBigFlame = true;
            markDirty();
            PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this.getUpdateTag()));
          }
        }
      }
      if (world.isRemote) {
        for (int i = 0; i < 2; i++) {
          //todo: fix particle
          //ParticleUtil.spawnParticleFiery(world, getPos().getX()+0.3125f+0.375f*Misc.random.nextFloat(), getPos().getY()+0.625f+0.375f*Misc.random.nextFloat(), getPos().getZ()+0.3125f+0.375f*Misc.random.nextFloat(), 0.03125f*(Misc.random.nextFloat()-0.5f), 0.125f*Misc.random.nextFloat(), 0.03125f*(Misc.random.nextFloat()-0.5f), 255.0f, 96.0f, 32.0f, 0.75f, 7.0f+7.0f*Misc.random.nextFloat(), 40);
        }
      }
    }
    if ((int) this.ticker % 20 == 0 && pickupDelay == 0) {
      List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class,
          new AxisAlignedBB(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX() + 1, getPos().getY() + 1, getPos().getZ() + 1));
      for (int i = 0; i < items.size(); i++) {
        ItemStack stack = items.get(i).getItem();
        boolean isFull = false;
        boolean isEmpty = false;
        while (stack.getCount() > 0 && !isFull && !isEmpty) {
          isFull = true;
          isEmpty = true;
          int minStack = -1;
          for (int j = 0; j < 5; j++) {
            if (stack != ItemStack.EMPTY) {
              isEmpty = false;
              if (stack.getItem() == inventory.getStackInSlot(j).getItem() && stack.getItemDamage() == inventory.getStackInSlot(j).getItemDamage()) {
                if (inventory.getStackInSlot(j).getCount() < inventory.getSlotLimit(j)) {
                  isFull = false;
                  if (minStack == -1) {
                    minStack = j;
                  } else {
                    if (inventory.getStackInSlot(j).getCount() < inventory.getStackInSlot(minStack).getCount()) {
                      minStack = j;
                    }
                  }
                }
              }
            }
          }
          if (minStack != -1 && minStack < 5) {
            if (inventory.getStackInSlot(minStack) != ItemStack.EMPTY && stack != ItemStack.EMPTY) {
              ItemStack toInsert = stack.copy();
              toInsert.setCount(1);
              inventory.insertItem(minStack, toInsert, false);
              markDirty();
              PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this.getUpdateTag()));
              stack.shrink(1);
              if (stack.getCount() == 0) {
                stack = ItemStack.EMPTY;
              }
            }
          }
        }

        items.get(i).setItem(stack);
        markDirty();
        PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this.getUpdateTag()));
      }
    }
  }

}