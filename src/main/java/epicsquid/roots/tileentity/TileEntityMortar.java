package epicsquid.roots.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.network.MessageTEUpdate;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.tile.TileBase;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.MortarRecipe;
import epicsquid.roots.recipe.RecipeRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityMortar extends TileBase {

  public ItemStackHandler inventory = new ItemStackHandler(5) {
    @Override
    protected void onContentsChanged(int slot) {
      TileEntityMortar.this.markDirty();
      if (!world.isRemote) {
        PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(TileEntityMortar.this.getUpdateTag()));
      }
    }
  };

  public TileEntityMortar() {
    super();
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    tag.setTag("inventory", inventory.serializeNBT());
    return tag;
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    inventory.deserializeNBT(tag.getCompoundTag("inventory"));
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
    if (heldItem != ItemStack.EMPTY && heldItem.getCount() > 0) {
      if (heldItem.getItem() != ModItems.pestle) {
        for (int i = 0; i < 5; i++) {
          if (inventory.getStackInSlot(i).isEmpty()) {
            ItemStack toInsert = heldItem.copy();
            toInsert.setCount(1);
            ItemStack attemptedInsert = inventory.insertItem(i, toInsert, true);
            if (attemptedInsert.isEmpty()) {
              inventory.insertItem(i, toInsert, false);
              player.getHeldItem(hand).shrink(1);
              if (player.getHeldItem(hand).getCount() == 0) {
                player.setHeldItem(hand, ItemStack.EMPTY);
              }
              markDirty();
              PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this.getUpdateTag()));
              return true;
            }
          }
        }
      } else {
        List<ItemStack> ingredients = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
          ingredients.add(inventory.getStackInSlot(i));
        }
        //        SpellRecipe recipe = RecipeRegistry.getSpellRecipe(ingredients);
        //        if (recipe != null) {
        //          SpellBase spell = SpellRegistry.spellRegistry.get(recipe.result);
        //          if (world.isRemote) {
        //            for (int i = 0; i < 8; i++) {
        //              int chance = Misc.random.nextInt(3);
        //              if (chance == 0) {
        //                ParticleUtil.spawnParticleSmoke(world, getPos().getX() + 0.5f + 0.25f * (Misc.random.nextFloat() - 0.5f),
        //                    getPos().getY() + 0.4375f + 0.125f * (Misc.random.nextFloat() - 0.5f), getPos().getZ() + 0.5f + 0.25f * (Misc.random.nextFloat() - 0.5f),
        //                    0.0125f * (Misc.random.nextFloat() - 0.5f), 0.0125f * (Misc.random.nextFloat() - 0.5f), 0.0125f * (Misc.random.nextFloat() - 0.5f),
        //                    spell.red1, spell.green1, spell.blue1, 0.25f, 1.5f, 24, false);
        //              }
        //              if (chance == 1) {
        //                ParticleUtil.spawnParticleSmoke(world, getPos().getX() + 0.5f + 0.25f * (Misc.random.nextFloat() - 0.5f),
        //                    getPos().getY() + 0.4375f + 0.125f * (Misc.random.nextFloat() - 0.5f), getPos().getZ() + 0.5f + 0.25f * (Misc.random.nextFloat() - 0.5f),
        //                    0.0125f * (Misc.random.nextFloat() - 0.5f), 0.0125f * (Misc.random.nextFloat() - 0.5f), 0.0125f * (Misc.random.nextFloat() - 0.5f),
        //                    spell.red2, spell.green2, spell.blue2, 0.25f, 1.5f, 24, false);
        //              }
        //              if (chance == 2) {
        //                ParticleUtil.spawnParticleSmoke(world, getPos().getX() + 0.5f + 0.25f * (Misc.random.nextFloat() - 0.5f),
        //                    getPos().getY() + 0.4375f + 0.125f * (Misc.random.nextFloat() - 0.5f), getPos().getZ() + 0.5f + 0.25f * (Misc.random.nextFloat() - 0.5f),
        //                    0.0125f * (Misc.random.nextFloat() - 0.5f), 0.0125f * (Misc.random.nextFloat() - 0.5f), 0.0125f * (Misc.random.nextFloat() - 0.5f), 0.5f,
        //                    0.5f, 0.5f, 0.5f, 2.5f, 24, false);
        //              }
        //            }
        //          }
        //          ItemStack dust = new ItemStack(RegistryManager.petal_dust, 1);
        //          ItemPetalDust.createData(dust, recipe.result);
        //          if (!world.isRemote) {
        //            world.spawnEntity(new EntityItem(world, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, dust));
        //            markDirty();
        //            PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this));
        //          }
        //          for (int i = 0; i < 5; i++) {
        //            inventory.extractItem(i, 1, false);
        //          }
        //          return true;
        //        }
        MortarRecipe mortarRecipe = RecipeRegistry.getMortarRecipe(ingredients);

        if (mortarRecipe != null) {
          //todo: fix particle
          //          if (world.isRemote) {
          //            for (int i = 0; i < 8; i++) {
          //              int chance = Misc.random.nextInt(3);
          //              if (chance == 0) {
          //                ParticleUtil.spawnParticleSmoke(world, getPos().getX() + 0.5f + 0.25f * (Misc.random.nextFloat() - 0.5f),
          //                    getPos().getY() + 0.4375f + 0.125f * (Misc.random.nextFloat() - 0.5f), getPos().getZ() + 0.5f + 0.25f * (Misc.random.nextFloat() - 0.5f),
          //                    0.0125f * (Misc.random.nextFloat() - 0.5f), 0.0125f * (Misc.random.nextFloat() - 0.5f), 0.0125f * (Misc.random.nextFloat() - 0.5f),
          //                    mortarRecipe.r1, mortarRecipe.g1, mortarRecipe.b1, 0.25f, 1.5f, 24, false);
          //              }
          //              if (chance == 1) {
          //                ParticleUtil.spawnParticleSmoke(world, getPos().getX() + 0.5f + 0.25f * (Misc.random.nextFloat() - 0.5f),
          //                    getPos().getY() + 0.4375f + 0.125f * (Misc.random.nextFloat() - 0.5f), getPos().getZ() + 0.5f + 0.25f * (Misc.random.nextFloat() - 0.5f),
          //                    0.0125f * (Misc.random.nextFloat() - 0.5f), 0.0125f * (Misc.random.nextFloat() - 0.5f), 0.0125f * (Misc.random.nextFloat() - 0.5f),
          //                    mortarRecipe.r2, mortarRecipe.g2, mortarRecipe.b2, 0.25f, 1.5f, 24, false);
          //              }
          //              if (chance == 2) {
          //                ParticleUtil.spawnParticleSmoke(world, getPos().getX() + 0.5f + 0.25f * (Misc.random.nextFloat() - 0.5f),
          //                    getPos().getY() + 0.4375f + 0.125f * (Misc.random.nextFloat() - 0.5f), getPos().getZ() + 0.5f + 0.25f * (Misc.random.nextFloat() - 0.5f),
          //                    0.0125f * (Misc.random.nextFloat() - 0.5f), 0.0125f * (Misc.random.nextFloat() - 0.5f), 0.0125f * (Misc.random.nextFloat() - 0.5f), 0.5f,
          //                    0.5f, 0.5f, 0.5f, 2.5f, 24, false);
          //              }
          //            }
          //          }
          if (!world.isRemote) {
            world.spawnEntity(new EntityItem(world, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, mortarRecipe.getResult()));
            markDirty();
            PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this.getUpdateTag()));
          }
          for (int i = 0; i < 5; i++) {
            inventory.extractItem(i, 1, false);
          }
          return true;
        }
      }

    }
    if (heldItem.isEmpty() && !world.isRemote && hand == EnumHand.MAIN_HAND) {
      for (int i = 4; i >= 0; i--) {
        if (!inventory.getStackInSlot(i).isEmpty()) {
          ItemStack extracted = inventory.extractItem(i, 1, false);
          if (!world.isRemote) {
            world.spawnEntity(new EntityItem(world, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, extracted));
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
    if (!world.isRemote) {
      Util.spawnInventoryInWorld(world, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, inventory);
    }
  }

}

