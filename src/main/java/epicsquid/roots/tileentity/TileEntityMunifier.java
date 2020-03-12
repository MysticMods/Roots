package epicsquid.roots.tileentity;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.tile.TileBase;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.handler.SpellHandler;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.ItemStaff;
import epicsquid.roots.network.fx.MessageImbueCompleteFX;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.FakeSpellRunicDust;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.modules.ModuleRegistry;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class TileEntityMunifier extends TileBase implements ITickable {
  public ItemStackHandler inventory = new ItemStackHandler(1) {
    @Override
    protected void onContentsChanged(int slot) {
      TileEntityMunifier.this.markDirty();
      if (!world.isRemote) {
        TileEntityMunifier.this.updatePacketViaState();
      }
    }
  };
  int ticks = 0;
  public float angle = 0;

  public TileEntityMunifier() {
    super();
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    tag.setTag("handler", inventory.serializeNBT());
    return tag;
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    inventory.deserializeNBT(tag.getCompoundTag("handler"));
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
    if (!heldItem.isEmpty() && heldItem.getItem() == ModItems.staff) {
      if (inventory.getStackInSlot(0).isEmpty()) {
        ItemStack toInsert = heldItem.copy();
        toInsert.setCount(1);
        ItemStack attemptedInsert = inventory.insertItem(0, toInsert, true);
        if (attemptedInsert.isEmpty()) {
          inventory.insertItem(0, toInsert, false);
          player.getHeldItem(hand).shrink(1);
          if (player.getHeldItem(hand).getCount() == 0) {
            player.setHeldItem(hand, ItemStack.EMPTY);
          }
          markDirty();
          updatePacketViaState();
          return true;
        }
      }
    } else if (heldItem.isEmpty() && !world.isRemote && hand == EnumHand.MAIN_HAND) {
      for (int i = inventory.getSlots() - 1; i >= 0; i--) {
        if (this.dropItemInInventory(inventory, i)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
    if (!world.isRemote) {
      Util.spawnInventoryInWorld(world, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, inventory);
    }
  }

  @Override
  public void update() {
    ticks++;
  }
}