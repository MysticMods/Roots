package epicsquid.roots.tileentity;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.tile.TileBase;
import epicsquid.mysticallib.util.Util;
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
import net.minecraft.client.renderer.GlStateManager;
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
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityImbuer extends TileBase implements ITickable {
  public ItemStackHandler inventory = new ItemStackHandler(2) {
    @Override
    protected void onContentsChanged(int slot) {
      TileEntityImbuer.this.markDirty();
      if (!world.isRemote) {
        TileEntityImbuer.this.updatePacketViaState();
      }
    }
  };
  int progress = 0;
  public float angle = 0;

  public TileEntityImbuer() {
    super();
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    tag.setTag("handler", inventory.serializeNBT());
    tag.setInteger("progress", progress);
    return tag;
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
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
    if (!heldItem.isEmpty()) {
      if (heldItem.getItem() == ModItems.spell_dust || heldItem.getItem() == ModItems.runic_dust) {
        if (inventory.getStackInSlot(0).isEmpty()) {
          ItemStack toInsert = heldItem.copy();
          toInsert.setCount(1);
          ItemStack attemptedInsert = inventory.insertItem(0, toInsert, false);
          if (attemptedInsert.isEmpty()) {
            player.getHeldItem(hand).shrink(1);
            markDirty();
            updatePacketViaState();
            return true;
          }
        }
      } else if (heldItem.getItem() == ModItems.staff || ModuleRegistry.isModule(heldItem)) {
        if (heldItem.getItem() == ModItems.staff) {
          SpellHandler cap = SpellHandler.fromStack(heldItem);
          if (!cap.hasFreeSlot() && inventory.getStackInSlot(0).getItem() != ModItems.runic_dust) {
            if (world.isRemote) {
              player.sendMessage(new TextComponentTranslation("roots.info.staff.no_slots").setStyle(new Style().setColor(TextFormatting.GOLD)));
            }
            return true;
          } else if (inventory.getStackInSlot(0).getItem() == ModItems.runic_dust) {
            if (cap.getSelectedSpell() == null) {
              if (world.isRemote) {
                player.sendMessage(new TextComponentTranslation("roots.info.staff.empty_slot").setStyle(new Style().setColor(TextFormatting.GOLD)));
              }
              return true;
            }
          }
        }
        if (inventory.getStackInSlot(1).isEmpty()) {
          ItemStack toInsert = heldItem.copy();
          toInsert.setCount(1);
          ItemStack attemptedInsert = inventory.insertItem(1, toInsert, true);
          if (attemptedInsert.isEmpty()) {
            inventory.insertItem(1, toInsert, false);
            player.getHeldItem(hand).shrink(1);
            if (player.getHeldItem(hand).getCount() == 0) {
              player.setHeldItem(hand, ItemStack.EMPTY);
            }
            markDirty();
            updatePacketViaState();
            return true;
          }
        }
      } else {
        // Check for a damaged item in the other slot and see if this matches
        if (inventory.getStackInSlot(0).isEmpty() && inventory.getStackInSlot(1).isEmpty()) {
          if (heldItem.isItemStackDamageable()) {
            ItemStack toInsert = heldItem.copy(); // <-- Pretty sure this gets copied anyway?
            ItemStack attemptedInsert = inventory.insertItem(1, toInsert, true);
            if (attemptedInsert.isEmpty()) {
              inventory.insertItem(1, toInsert, false);
              player.getHeldItem(hand).shrink(1);
              if (player.getHeldItem(hand).getCount() == 0) {
                player.setHeldItem(hand, ItemStack.EMPTY);
              }
              markDirty();
              updatePacketViaState();
              return true;
            }
          }
        } else {
          ItemStack toRepair = inventory.getStackInSlot(1);
          if (!toRepair.isEmpty() && toRepair.isItemStackDamageable() && toRepair.getItem().getIsRepairable(toRepair, heldItem)) {
            ItemStack repairItem = heldItem.copy();
            repairItem.setCount(1);
            int repairAmount = Math.min(toRepair.getItemDamage(), toRepair.getMaxDamage() / 4);
            if (repairAmount > 0) {
              ItemStack result = inventory.insertItem(0, repairItem, true);
              if (result.isEmpty()) {
                inventory.insertItem(0, repairItem, false);
                player.getHeldItem(hand).shrink(1);
                if (player.getHeldItem(hand).getCount() == 0) {
                  player.setHeldItem(hand, ItemStack.EMPTY);
                }
                markDirty();
                updatePacketViaState();
                return true;
                // actual repair doesn't occur here
                // toRepair.setItemDamage(toRepair.getItemDamage() - repairAmount);
              }
            }
          }
        }
      }
    }
    if (heldItem.isEmpty() && !world.isRemote && hand == EnumHand.MAIN_HAND) {
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
    angle++;
    if (!inventory.getStackInSlot(0).isEmpty() && !inventory.getStackInSlot(1).isEmpty()) {
      progress++;
      angle += 2.0f;
      ItemStack spellDust = inventory.getStackInSlot(0);
      boolean clearSlot = spellDust.getItem() != ModItems.spell_dust;
      SpellHandler capability = SpellHandler.fromStack(spellDust);
      if ((capability.getSelectedSpell() != null) || clearSlot) {
        SpellBase spell;
        if (clearSlot) {
          spell = new FakeSpellRunicDust();
        } else {
          spell = capability.getSelectedSpell();
        }
        if (world.isRemote) {
          if (Util.rand.nextInt(2) == 0) {
            ParticleUtil.spawnParticleLineGlow(world, getPos().getX() + 0.5f, getPos().getY() + 0.125f, getPos().getZ() + 0.5f,
                getPos().getX() + 0.5f + 0.5f * (Util.rand.nextFloat() - 0.5f), getPos().getY() + 1.0f,
                getPos().getZ() + 0.5f + 0.5f * (Util.rand.nextFloat() - 0.5f), spell.getRed1(), spell.getGreen1(), spell.getBlue1(), 0.25f, 4.0f, 40);
          } else {
            ParticleUtil.spawnParticleLineGlow(world, getPos().getX() + 0.5f, getPos().getY() + 0.125f, getPos().getZ() + 0.5f,
                getPos().getX() + 0.5f + 0.5f * (Util.rand.nextFloat() - 0.5f), getPos().getY() + 1.0f,
                getPos().getZ() + 0.5f + 0.5f * (Util.rand.nextFloat() - 0.5f), spell.getRed2(), spell.getGreen2(), spell.getBlue2(), 0.25f, 4.0f, 40);
          }
        }
      }
      if (progress > 200) {
        progress = 0;
        if (!world.isRemote) {
          if (inventory.getStackInSlot(1).getItem() == ModItems.staff) {
            ItemStack staff = inventory.getStackInSlot(1);
            SpellBase spell;
            if (!clearSlot && capability.getSelectedSpell() != null) {
              ItemStaff.createData(staff, capability);
              spell = capability.getSelectedSpell();
            } else {
              ItemStaff.clearData(staff);
              spell = new FakeSpellRunicDust();
            }
            world.spawnEntity(new EntityItem(world, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, staff));
            inventory.extractItem(0, 1, false);
            inventory.extractItem(1, 1, false);
            markDirty();
            updatePacketViaState();
            PacketHandler.sendToAllTracking(new MessageImbueCompleteFX(spell.getName(), getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5), this);
          } else if (inventory.getStackInSlot(0).getItem() == ModItems.spell_dust) {
            ItemStack stack = inventory.getStackInSlot(1);
            SpellModule module = ModuleRegistry.getModule(stack);
            capability.addModule(module);
            inventory.extractItem(1, 1, false);
            markDirty();
            updatePacketViaState();
            PacketHandler.sendToAllTracking(new MessageImbueCompleteFX(capability.getSelectedSpell().getName(), getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5), this);
          } else {
            // Handle the repair
            ItemStack toRepair = inventory.extractItem(1, 1, false);
            inventory.extractItem(0, 1, false);
            int repairAmount = Math.min(toRepair.getItemDamage(), toRepair.getMaxDamage() / 4);
            if (repairAmount > 0) {
              toRepair.setItemDamage(toRepair.getItemDamage() - repairAmount);
            }
            world.spawnEntity(new EntityItem(world, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, toRepair));
            markDirty();
            updatePacketViaState();
            PacketHandler.sendToAllTracking(new MessageImbueCompleteFX("fake_spell", getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5), this);
          }
        }
      }
      this.markDirty();
      if (!world.isRemote) {
        updatePacketViaState();
      }
    } else {
      if (progress != 0) {
        progress = 0;
        this.markDirty();
        if (!world.isRemote) {
          updatePacketViaState();
        }
      }
    }
  }

}