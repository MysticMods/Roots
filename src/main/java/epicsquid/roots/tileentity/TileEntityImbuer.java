package epicsquid.roots.tileentity;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.network.MessageTEUpdate;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.tile.TileBase;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.ItemStaff;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityImbuer extends TileBase implements ITickable {
  public ItemStackHandler inventory = new ItemStackHandler(2){
    @Override
    protected void onContentsChanged(int slot) {
      TileEntityImbuer.this.markDirty();
      if (!world.isRemote){
        PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(TileEntityImbuer.this.getUpdateTag()));
      }
    }
  };
  int progress = 0;
  public float angle = 0;

  public TileEntityImbuer(){
    super();
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag){
    super.writeToNBT(tag);
    tag.setTag("inventory", inventory.serializeNBT());
    tag.setInteger("progress", progress);
    return tag;
  }

  @Override
  public void readFromNBT(NBTTagCompound tag){
    super.readFromNBT(tag);
    inventory.deserializeNBT(tag.getCompoundTag("inventory"));
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
    if (!heldItem.isEmpty()){
      if (heldItem.getItem() == ModItems.petal_dust){
        if (inventory.getStackInSlot(0).isEmpty()){
          ItemStack toInsert = heldItem.copy();
          toInsert.setCount(1);
          ItemStack attemptedInsert = inventory.insertItem(0, toInsert, true);
          if (attemptedInsert.isEmpty()){
            inventory.insertItem(0, toInsert, false);
            player.getHeldItem(hand).shrink(1);
            if (player.getHeldItem(hand).getCount() == 0){
              player.setHeldItem(hand, ItemStack.EMPTY);
            }
            markDirty();
            PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this.getUpdateTag()));
            return true;
          }
        }
      }
      else if (heldItem.getItem() == ModItems.staff){
        if (inventory.getStackInSlot(1).isEmpty()){
          ItemStack toInsert = heldItem.copy();
          toInsert.setCount(1);
          ItemStack attemptedInsert = inventory.insertItem(1, toInsert, true);
          if (attemptedInsert == ItemStack.EMPTY){
            inventory.insertItem(1, toInsert, false);
            player.getHeldItem(hand).shrink(1);
            if (player.getHeldItem(hand).getCount() == 0){
              player.setHeldItem(hand, ItemStack.EMPTY);
            }
            markDirty();
            PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this.getUpdateTag()));
            return true;
          }
        }
      }
    }
    if (heldItem.isEmpty() && !world.isRemote && hand == EnumHand.MAIN_HAND){
      for (int i = 1; i >= 0; i --){
        if (!inventory.getStackInSlot(i).isEmpty()){
          ItemStack extracted = inventory.extractItem(i, 1, false);
          if (!world.isRemote){
            world.spawnEntity(new EntityItem(world,getPos().getX()+0.5,getPos().getY()+0.5,getPos().getZ()+0.5,extracted));
          }
          markDirty();
          PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this.getUpdateTag()));
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
    if (!world.isRemote){
      Util.spawnInventoryInWorld(world, getPos().getX()+0.5,getPos().getY()+0.5,getPos().getZ()+0.5, inventory);
    }
  }

  @Override
  public void update() {
    angle ++;
    if (!inventory.getStackInSlot(0).isEmpty() && !inventory.getStackInSlot(1).isEmpty()){
      progress ++;
      angle += 2.0f;
      ItemStack spellDust = inventory.getStackInSlot(0);
      if (spellDust.hasTagCompound()){
        SpellBase spell = SpellRegistry.spellRegistry.get(spellDust.getTagCompound().getString("spell"));
        if (world.isRemote){
          if (Util.rand.nextInt(2) == 0){
            ParticleUtil.spawnParticleLineGlow(world, getPos().getX()+0.5f, getPos().getY()+0.125f, getPos().getZ()+0.5f, getPos().getX()+0.5f+0.5f*(Util.rand.nextFloat()-0.5f), getPos().getY()+1.0f, getPos().getZ()+0.5f+0.5f*(Util.rand.nextFloat()-0.5f), spell.red1, spell.green1, spell.blue1, 0.25f, 4.0f, 40);
          }
          else {
            ParticleUtil.spawnParticleLineGlow(world, getPos().getX()+0.5f, getPos().getY()+0.125f, getPos().getZ()+0.5f, getPos().getX()+0.5f+0.5f*(Util.rand.nextFloat()-0.5f), getPos().getY()+1.0f, getPos().getZ()+0.5f+0.5f*(Util.rand.nextFloat()-0.5f), spell.red2, spell.green2, spell.blue2, 0.25f, 4.0f, 40);
          }
        }
      }
      if (progress > 200){
        progress = 0;
        if (!world.isRemote){
          ItemStack staff = inventory.getStackInSlot(1);
          if (spellDust.hasTagCompound()){
            if (SpellRegistry.spellRegistry.containsKey(spellDust.getTagCompound().getString("spell"))){
              ItemStaff.createData(staff, spellDust.getTagCompound().getString("spell"));
              world.spawnEntity(new EntityItem(world, getPos().getX()+0.5,getPos().getY()+0.5,getPos().getZ()+0.5,staff));
              inventory.extractItem(0, 1, false);
              inventory.extractItem(1, 1, false);
              markDirty();
              PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this.getUpdateTag()));
              //todo: fix when particles work PacketHandler.INSTANCE.sendToAll(new MessageImbueCompleteFX(spellDust.getTagCompound().getString("spell"),getPos().getX()+0.5,getPos().getY()+0.5,getPos().getZ()+0.5));
            }
          }
        }
      }
      this.markDirty();
      if (!world.isRemote){
        PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this.getUpdateTag()));
      }
    }
    else {
      if (progress != 0){
        progress = 0;
        this.markDirty();
        if (!world.isRemote){
          PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this.getUpdateTag()));
        }
      }
    }
  }

}