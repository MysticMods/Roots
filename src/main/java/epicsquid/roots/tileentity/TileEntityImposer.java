package epicsquid.roots.tileentity;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.GuiHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.advancements.Advancements;
import epicsquid.roots.container.ContainerImposer;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.ItemDruidKnife;
import epicsquid.roots.modifiers.IModifierCore;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstance;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.spell.info.StaffSpellInfo;
import epicsquid.roots.spell.info.storage.StaffSpellStorage;
import epicsquid.roots.util.SpellUtil;
import epicsquid.roots.world.data.SpellLibraryData;
import epicsquid.roots.world.data.SpellLibraryRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ITickableTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import noobanidus.libs.noobutil.util.ItemUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityImposer extends TileEntity implements ITickableTileEntity {
  public ItemStackHandler inventory = new ItemStackHandler(1) {
    @Override
    protected void onContentsChanged(int slot) {
      TileEntityImposer.this.slot = 0;
      TileEntityImposer.this.markDirty();
      if (!world.isRemote) {
        //TileEntityImposer.this.updatePacketViaState();
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

  public void updateInSlot(PlayerEntity player) {
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
  public CompoundNBT write(CompoundNBT tag) {
    super.write(tag);
    tag.put("handler", inventory.serializeNBT());
    tag.putInt("slot", slot);
    return tag;
  }

  @Override
  public void read(BlockState state, CompoundNBT tag) {
    super.read(state, tag);
    inventory.deserializeNBT(tag.getCompound("handler"));
    slot = tag.getInt("slot");
  }

  @Override
  public CompoundNBT getUpdateTag() {
    return write(new CompoundNBT());
  }

  @Override
  public SUpdateTileEntityPacket getUpdatePacket() {
    return new SUpdateTileEntityPacket(getPos(), 0, getUpdateTag());
  }

  @Override
  public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
    read(ModBlocks.imposer.getDefaultState(), pkt.getNbtCompound());
  }

  public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull Direction side, float hitX, float hitY, float hitZ) {
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
      ItemStack inSlot = inventory.getStackInSlot(0);
      if (inSlot.getItem() == ModItems.staff) {
        if (!world.isRemote) {
          player.openGui(Roots.instance, GuiHandler.IMPOSER_ID, world, pos.getX(), pos.getY(), pos.getZ());
        }
      }
      return true;
    } else if (heldItem.isEmpty() && !world.isRemote && hand == Hand.MAIN_HAND) {
      for (int i = 0; i < inventory.getSlots(); i++) {
        if (this.dropItemInInventory(inventory, i)) {
          return true;
        }
      }
    }
    return false;
  }

  public void breakBlock(World world, BlockPos pos, BlockState state, PlayerEntity player) {
    if (!world.isRemote) {
      // TODO
      //Util.spawnInventoryInWorld(world, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, inventory);
    }
  }

  public StaffSpellInfo getCurrentInfo(StaffSpellStorage storage) {
    if (storage == null) {
      return StaffSpellInfo.EMPTY;
    }

    return storage.getSpellInSlot(slot);
  }

  @Nullable
  public StaffModifierInstance getModifier(StaffSpellStorage storage, IModifierCore core) {
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

  public void addModifier(IModifierCore core, ItemStack stack, ContainerImposer container) {
    if (world != null && !world.isRemote) {
      StaffSpellStorage storage = getSpellStorage();
      StaffModifierInstance modifier = getModifier(storage, core);
      if (modifier == null || storage == null) {
        return;
      }

      if (!ItemUtil.equalWithoutSize(modifier.getStack(), stack)) {
        Roots.logger.error("Attempted to apply modifyCooldown core " + core + " to spell in slot " + slot + ", but it wants " + modifier.getStack() + " and we were passed " + stack + "!");
        return;
      }

      // Why sisn't this updatinfg the library

      modifier.setApplied();
      storage.saveToStack();
      markDirty();
      updatePacketViaState();
      PlayerEntity player = container.getPlayer();
      Advancements.MODIFIER_TRIGGER.trigger((ServerPlayerEntity) player, modifier.getModifier().getCore());
      SpellLibraryData data = SpellLibraryRegistry.getData(player);
      StaffSpellInfo info = storage.getSpellInSlot(slot);
      if (info == null) {
        throw new NullPointerException("StaffSpellInfo for applied modifier was somehow null");
      }
      data.updateSpell(info.toLibrary());
      SpellUtil.updateModifiers(player);
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
  public void tick() {
    ticks++;
  }
}