package epicsquid.roots.tileentity;

import epicsquid.mysticallib.tile.TileBase;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.GuiHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.ItemDruidKnife;
import epicsquid.roots.modifiers.IModifierCore;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstance;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.spell.info.StaffSpellInfo;
import epicsquid.roots.spell.info.storage.StaffSpellStorage;
import epicsquid.roots.util.SpellUtil;
import net.minecraft.block.state.IBlockState;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityImposer extends TileBase implements ITickable {
  public ItemStackHandler inventory = new ItemStackHandler(1) {
    @Override
    protected void onContentsChanged(int slot) {
      TileEntityImposer.this.slot = 0;
      TileEntityImposer.this.markDirty();
      if (!world.isRemote) {
        TileEntityImposer.this.updatePacketViaState();
      }
    }
  };
  public int ticks = 0;
  public float angle = 0;
  private int slot = 0;

  public TileEntityImposer() {
    super();
  }

  public int getSlot() {
    return slot;
  }

  public void updateInSlot(EntityPlayer player) {
    if (SpellUtil.isStaff(inventory.getStackInSlot(0))) {
      ItemStack staff = inventory.getStackInSlot(0);
      SpellUtil.updateModifiers(staff, player);
      inventory.setStackInSlot(0, staff);
      markDirty();
      updatePacketViaState();
    }
  }

  public void setSlot(int slot) {
    this.slot = slot;
    markDirty();
    updatePacketViaState();
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    tag.setTag("handler", inventory.serializeNBT());
    tag.setInteger("slot", slot);
    return tag;
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    inventory.deserializeNBT(tag.getCompoundTag("handler"));
    slot = tag.getInteger("slot");
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
        SpellUtil.updateModifiers(heldItem, player);
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
    } else if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemDruidKnife) {
      if (!world.isRemote) {
        player.openGui(Roots.instance, GuiHandler.IMPOSER_ID, world, pos.getX(), pos.getY(), pos.getZ());
      }
      return true;
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

  public void toggleModifier(IModifierCore core) {
    if (world != null && !world.isRemote) {
      StaffSpellStorage storage = getSpellStorage();
      StaffModifierInstance modifier = getModifier(storage, core);
      if (modifier == null) {
        return;
      }

      modifier.setEnabled(!modifier.isEnabled());
      storage.saveToStack();
      markDirty();
      updatePacketViaState();
    }
  }

  @Nullable
  private StaffModifierInstance getModifier(StaffSpellStorage storage, IModifierCore core) {
    if (storage == null) {
      return null;
    }
    StaffSpellInfo info = storage.getSpellInSlot(slot);
    if (info == null) {
      return null;
    }
    StaffModifierInstanceList modifiers = info.getModifiers();
    return modifiers.getByCore(core);
  }

  public void addModifier(IModifierCore core, ItemStack stack) {
    if (world != null && !world.isRemote) {
      StaffSpellStorage storage = getSpellStorage();
      StaffModifierInstance modifier = getModifier(storage, core);
      if (modifier == null) {
        return;
      }

      if (!ItemUtil.equalWithoutSize(modifier.getStack(), stack)) {
        Roots.logger.error("Attempted to apply modifyCooldown core " + core + " to spell in slot " + slot + ", but it wants " + modifier.getStack() + " and we were passed " + stack + "!");
        return;
      }

      modifier.setApplied();
      storage.saveToStack();
      markDirty();
      updatePacketViaState();
    }
  }

  @Nullable
  public StaffSpellStorage getSpellStorage() {
    ItemStack staff = inventory.getStackInSlot(0);
    if (staff.isEmpty()) {
      return null;
    }

    return StaffSpellStorage.fromStack(staff);
  }

  @Override
  public void update() {
    ticks++;
  }
}