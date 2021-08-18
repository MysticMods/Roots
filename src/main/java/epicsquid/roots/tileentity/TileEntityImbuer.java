package epicsquid.roots.tileentity;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.tile.TileBase;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.network.fx.MessageImbueCompleteFX;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.FakeSpell;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.info.LibrarySpellInfo;
import epicsquid.roots.spell.info.storage.DustSpellStorage;
import epicsquid.roots.world.data.SpellLibraryData;
import epicsquid.roots.world.data.SpellLibraryRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.UUID;

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
  private int progress = 0;
  public float angle = 0;
  private UUID inserter = null;

  public TileEntityImbuer() {
    super();
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    tag.setTag("handler", inventory.serializeNBT());
    tag.setInteger("progress", progress);
    if (inserter != null) {
      tag.setUniqueId("inserter", inserter);
    }
    return tag;
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    inventory.deserializeNBT(tag.getCompoundTag("handler"));
    progress = tag.getInteger("progress");
    if (tag.hasUniqueId("inserter")) {
      inserter = tag.getUniqueId("inserter");
    } else {
      inserter = null;
    }
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

  private boolean checkExists(int slot, ItemStack heldItem, EntityPlayer player, World world) {
    if (!world.isRemote) {
      ItemStack inSlot = inventory.getStackInSlot(slot == 0 ? 1 : 0);
      if (inSlot.isEmpty()) {
        return false;
      }
      ItemStack spellDust = ItemStack.EMPTY;
      if (heldItem.getItem() == ModItems.gramary && inSlot.getItem() == ModItems.spell_dust) {
        spellDust = inSlot;
      } else if (heldItem.getItem() == ModItems.spell_dust && inSlot.getItem() == ModItems.gramary) {
        spellDust = heldItem;
      }
      if (spellDust.isEmpty()) {
        player.sendStatusMessage(new TextComponentTranslation("roots.message.spell_invalid").setStyle(new Style().setColor(FakeSpell.INSTANCE.getTextColor()).setBold(true)), true);
        return true;
      }
      DustSpellStorage cap = DustSpellStorage.fromStack(spellDust);
      SpellBase spell = cap != null && cap.getSelectedInfo() != null ? cap.getSelectedInfo().getSpell() : FakeSpell.INSTANCE;
      if (spell == null) {
        spell = FakeSpell.INSTANCE;
      }
      SpellLibraryData library = SpellLibraryRegistry.getData(player);
      if (spell == FakeSpell.INSTANCE) {
        player.sendStatusMessage(new TextComponentTranslation("roots.message.spell_invalid").setStyle(new Style().setColor(spell.getTextColor()).setBold(true)), true);
        return true;
      }
      LibrarySpellInfo lib = library.getData(spell);
      if (lib.isObtained()) {
        player.sendStatusMessage(new TextComponentTranslation("roots.message.spell_already", new TextComponentTranslation(spell.getTranslationKey() + ".name").setStyle(new Style().setColor(spell.getTextColor()).setBold(true))), true);
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack heldItem = player.getHeldItem(hand);
    if (!heldItem.isEmpty()) {
      int slot = -1;
      if (heldItem.getItem() == ModItems.spell_dust) {
        slot = 0;
      } else if (heldItem.getItem() == ModItems.gramary) {
        slot = 1;
      }
      if (slot != -1) {
        if (inventory.getStackInSlot(slot).isEmpty()) {
          if (world.isRemote) {
            return true;
          }
          ItemStack toInsert = heldItem.copy();
          toInsert.setCount(1);
          if (checkExists(slot, heldItem, player, world)) {
            return true;
          }
          ItemStack attemptedInsert = inventory.insertItem(slot, toInsert, false);
          if (attemptedInsert.isEmpty()) {
            inserter = player.getUniqueID();
            player.getHeldItem(hand).shrink(1);
            markDirty();
            updatePacketViaState();
            world.playSound(null, getPos(), ModSounds.Events.IMBUER_ADD_ITEM, SoundCategory.BLOCKS, 1f, 1f);
            return true;
          }
        }
      } else {
        // Check for a damaged item in the other slot and see if this matches
        if (inventory.getStackInSlot(0).isEmpty() && inventory.getStackInSlot(1).isEmpty()) {
          if (heldItem.isItemDamaged() || (heldItem.isItemEnchanted() && heldItem.getItem() != Items.ENCHANTED_BOOK)) {
            ItemStack toInsert = heldItem.copy();
            ItemStack attemptedInsert = inventory.insertItem(1, toInsert, true);
            if (attemptedInsert.isEmpty()) {
              inventory.insertItem(1, toInsert, false);
              player.getHeldItem(hand).shrink(1);
              if (player.getHeldItem(hand).getCount() == 0) {
                player.setHeldItem(hand, ItemStack.EMPTY);
              }
              markDirty();
              updatePacketViaState();
              world.playSound(null, getPos(), ModSounds.Events.IMBUER_ADD_ITEM, SoundCategory.BLOCKS, 1f, 1f);
              return true;
            }
          }
        } else {
          ItemStack toRepair = inventory.getStackInSlot(1);
          if (GeneralConfig.AllowImbuerRepair && !toRepair.isEmpty() && toRepair.isItemDamaged() && toRepair.getItem().getIsRepairable(toRepair, heldItem) && inventory.getStackInSlot(0).isEmpty()) {
            ItemStack repairItem = heldItem.copy();
            repairItem.setCount(1);
            int repairAmount = Math.min(toRepair.getItemDamage(), toRepair.getMaxDamage() / GeneralConfig.MaxDamageDivisor);
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
                world.playSound(null, getPos(), ModSounds.Events.IMBUER_ADD_ITEM, SoundCategory.BLOCKS, 1f, 1f);
                return true;
              }
            }
          } else if (GeneralConfig.AllowImbuerDisenchant && !toRepair.isEmpty() && toRepair.isItemEnchanted() && heldItem.getItem() == ModItems.runic_dust) {
            ItemStack runicDust = heldItem.copy();
            runicDust.setCount(1);
            ItemStack result = inventory.insertItem(0, runicDust, true);
            if (result.isEmpty()) {
              inventory.insertItem(0, runicDust, false);
              player.getHeldItem(hand).shrink(1);
              if (player.getHeldItem(hand).getCount() == 0) {
                player.setHeldItem(hand, ItemStack.EMPTY);
              }
              markDirty();
              updatePacketViaState();
              world.playSound(null, getPos(), ModSounds.Events.IMBUER_ADD_ITEM, SoundCategory.BLOCKS, 1f, 1f);
              return true;
            }
          }
        }
      }
    }
    if (heldItem.isEmpty() && !world.isRemote && hand == EnumHand.MAIN_HAND) {
      for (int i = inventory.getSlots() - 1; i >= 0; i--) {
        if (this.dropItemInInventory(inventory, i)) {
          world.playSound(null, getPos(), ModSounds.Events.IMBUER_REMOVE_ITEM, SoundCategory.BLOCKS, 1f, 1f);
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
      DustSpellStorage capability = DustSpellStorage.fromStack(spellDust);
      SpellBase spell;
      if (capability != null && capability.getSelectedInfo() != null) {
        spell = capability.getSelectedInfo().getSpell();
        if (spell == null) {
          spell = FakeSpell.INSTANCE;
        }
      } else {
        spell = FakeSpell.INSTANCE;
      }
      if (world.isRemote) {
        BlockPos pos = getPos();
        float x = pos.getX();
        float y = pos.getY();
        float z = pos.getZ();
        if (Util.rand.nextInt(2) == 0) {
          ParticleUtil.spawnParticleLineGlow(world, x + 0.5f, y + 0.125f, z + 0.5f, x + 0.5f + 0.5f * (Util.rand.nextFloat() - 0.5f), y + 1.0f, z + 0.5f + 0.5f * (Util.rand.nextFloat() - 0.5f), spell.getFirstColours(0.25f), 4.0f, 40);
        } else {
          ParticleUtil.spawnParticleLineGlow(world, x + 0.5f, y + 0.125f, z + 0.5f, x + 0.5f + 0.5f * (Util.rand.nextFloat() - 0.5f), y + 1.0f, z + 0.5f + 0.5f * (Util.rand.nextFloat() - 0.5f), spell.getSecondColours(0.25f), 4.0f, 40);
        }
      } else {
        if (progress % 20 == 0) {
          world.playSound(null, getPos(), ModSounds.Events.IMBUER_USE, SoundCategory.BLOCKS, 1f, 1f);
        }
      }
      if (progress > 200) {
        progress = 0;
        if (!world.isRemote) {
          ItemStack inSlot = inventory.getStackInSlot(1);
          if (inSlot.getItem() == ModItems.gramary && spell != FakeSpell.INSTANCE) {
            if (inserter == null) {
              Util.spawnInventoryInWorld(world, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, inventory);
            } else {
              SpellLibraryData library = SpellLibraryRegistry.getData(inserter);
              library.addSpell(spell);
              PacketHandler.sendToAllTracking(new MessageImbueCompleteFX(spell.getName(), getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5), this);
              if (inserter != null) {
                EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(inserter);
                player.sendStatusMessage(new TextComponentTranslation("roots.message.spell_imbued", new TextComponentTranslation(spell.getTranslationKey() + ".name").setStyle(new Style().setColor(spell.getTextColor()).setBold(true))), true);
              }
            }
            inventory.extractItem(0, 1, false);
            world.playSound(null, getPos(), ModSounds.Events.IMBUER_FINISHED, SoundCategory.BLOCKS, 1f, 1f);
            /*if (ejectItem) {
              inventory.extractItem(1, 1, false);*/

            markDirty();
            updatePacketViaState();
          } else {
            // Handle the repair
            ItemStack repairItem = inventory.extractItem(0, 1, false);
            ItemStack toRepair = inventory.extractItem(1, 1, false);
            if (repairItem.getItem() == ModItems.runic_dust) {
              NBTTagCompound tag = ItemUtil.getOrCreateTag(toRepair);
              if (tag.hasKey("ench")) {
                tag.removeTag("ench");
                toRepair.setTagCompound(tag);
                if (GeneralConfig.AllowImbuerDisenchantReduceCost) {
                  int rep = toRepair.getRepairCost();
                  if (rep > 0) {
                    toRepair.setRepairCost(rep - (rep / 3));
                  }
                }
              }
            } else {
              int repairAmount = Math.min(toRepair.getItemDamage(), toRepair.getMaxDamage() / 4);
              if (repairAmount > 0) {
                toRepair.setItemDamage(toRepair.getItemDamage() - repairAmount);
              }
            }
            world.spawnEntity(new EntityItem(world, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, toRepair));
            world.playSound(null, getPos(), ModSounds.Events.IMBUER_FINISHED, SoundCategory.BLOCKS, 1f, 1f);
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