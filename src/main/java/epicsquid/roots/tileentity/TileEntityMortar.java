package epicsquid.roots.tileentity;

import epicsquid.mysticallib.tile.TileBase;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.recipe.MortarRecipe;
import epicsquid.roots.spell.SpellBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NullableProblems")
public class TileEntityMortar extends TileBase {

  public ItemStackHandler inventory = new ItemStackHandler(5) {
    @Override
    protected void onContentsChanged(int slot) {
      TileEntityMortar.this.markDirty();
      if (!world.isRemote) {
        updatePacketViaState();
        TileEntityMortar.this.world.updateComparatorOutputLevel(pos, ModBlocks.mortar);
      }
    }

    @Override
    public int getSlotLimit(int slot) {
      return 1;
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
      return 1;
    }
  };

  public TileEntityMortar() {
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
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == EnumFacing.UP;
  }

  @SuppressWarnings("unchecked")
  @Nullable
  @Override
  public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == EnumFacing.UP) {
      return (T) inventory;
    }

    return super.getCapability(capability, facing);
  }

  public boolean handleCraft(BlockPos crafterPos) {
    List<ItemStack> ingredients = getIngredients();
    SpellBase spell = ModRecipes.getSpellRecipe(ingredients);
    if (spell != null) {
      if (world.isRemote) {
        for (int i = 0; i < 8; i++) {
          int chance = Util.rand.nextInt(3);
          if (chance == 0) {
            ParticleUtil.spawnParticleSmoke(world, getPos().getX() + 0.5f + 0.25f * (Util.rand.nextFloat() - 0.5f),
                getPos().getY() + 0.4375f + 0.125f * (Util.rand.nextFloat() - 0.5f), getPos().getZ() + 0.5f + 0.25f * (Util.rand.nextFloat() - 0.5f),
                0.0125f * (Util.rand.nextFloat() - 0.5f), 0.0125f * (Util.rand.nextFloat() - 0.5f), 0.0125f * (Util.rand.nextFloat() - 0.5f), spell.getRed1(),
                spell.getGreen1(), spell.getBlue1(), 0.25f, 1.5f, 24, false);
          } else if (chance == 1) {
            ParticleUtil.spawnParticleSmoke(world, getPos().getX() + 0.5f + 0.25f * (Util.rand.nextFloat() - 0.5f),
                getPos().getY() + 0.4375f + 0.125f * (Util.rand.nextFloat() - 0.5f), getPos().getZ() + 0.5f + 0.25f * (Util.rand.nextFloat() - 0.5f),
                0.0125f * (Util.rand.nextFloat() - 0.5f), 0.0125f * (Util.rand.nextFloat() - 0.5f), 0.0125f * (Util.rand.nextFloat() - 0.5f), spell.getRed2(),
                spell.getGreen2(), spell.getBlue2(), 0.25f, 1.5f, 24, false);
          } else if (chance == 2) {
            ParticleUtil.spawnParticleSmoke(world, getPos().getX() + 0.5f + 0.25f * (Util.rand.nextFloat() - 0.5f),
                getPos().getY() + 0.4375f + 0.125f * (Util.rand.nextFloat() - 0.5f), getPos().getZ() + 0.5f + 0.25f * (Util.rand.nextFloat() - 0.5f),
                0.0125f * (Util.rand.nextFloat() - 0.5f), 0.0125f * (Util.rand.nextFloat() - 0.5f), 0.0125f * (Util.rand.nextFloat() - 0.5f), 0.5f, 0.5f,
                0.5f, 0.5f, 2.5f, 24, false);
          }
        }
      }
      ItemStack dust = spell.getResult();
      if (!world.isRemote) {
        ItemUtil.spawnItem(world, crafterPos.add(0, 1, 0), dust);
        markDirty();
        updatePacketViaState();
      }
      List<ItemStack> items = new ArrayList<>();
      for (int i = 0; i < inventory.getSlots(); i++) {
        items.add(inventory.extractItem(i, 1, false));
      }
      if (!world.isRemote) {
        for (ItemStack stack : spell.recipe.transformIngredients(items, this)) {
          ItemUtil.spawnItem(world, crafterPos.add(0, 1, 0), stack);
        }
      }
      return true;
    }
    MortarRecipe mortarRecipe = ModRecipes.getMortarRecipe(ingredients);

    if (mortarRecipe != null) {
      if (world.isRemote) {
        for (int i = 0; i < 8; i++) {
          int chance = Util.rand.nextInt(3);
          if (chance == 0) {
            ParticleUtil.spawnParticleSmoke(world, getPos().getX() + 0.5f + 0.25f * (Util.rand.nextFloat() - 0.5f),
                getPos().getY() + 0.4375f + 0.125f * (Util.rand.nextFloat() - 0.5f), getPos().getZ() + 0.5f + 0.25f * (Util.rand.nextFloat() - 0.5f),
                0.0125f * (Util.rand.nextFloat() - 0.5f), 0.0125f * (Util.rand.nextFloat() - 0.5f), 0.0125f * (Util.rand.nextFloat() - 0.5f),
                mortarRecipe.getR1(), mortarRecipe.getG1(), mortarRecipe.getB1(), 0.25f, 1.5f, 24, false);
          } else if (chance == 1) {
            ParticleUtil.spawnParticleSmoke(world, getPos().getX() + 0.5f + 0.25f * (Util.rand.nextFloat() - 0.5f),
                getPos().getY() + 0.4375f + 0.125f * (Util.rand.nextFloat() - 0.5f), getPos().getZ() + 0.5f + 0.25f * (Util.rand.nextFloat() - 0.5f),
                0.0125f * (Util.rand.nextFloat() - 0.5f), 0.0125f * (Util.rand.nextFloat() - 0.5f), 0.0125f * (Util.rand.nextFloat() - 0.5f),
                mortarRecipe.getR2(), mortarRecipe.getG2(), mortarRecipe.getB2(), 0.25f, 1.5f, 24, false);
          } else if (chance == 2) {
            ParticleUtil.spawnParticleSmoke(world, getPos().getX() + 0.5f + 0.25f * (Util.rand.nextFloat() - 0.5f),
                getPos().getY() + 0.4375f + 0.125f * (Util.rand.nextFloat() - 0.5f), getPos().getZ() + 0.5f + 0.25f * (Util.rand.nextFloat() - 0.5f),
                0.0125f * (Util.rand.nextFloat() - 0.5f), 0.0125f * (Util.rand.nextFloat() - 0.5f), 0.0125f * (Util.rand.nextFloat() - 0.5f), 0.5f, 0.5f,
                0.5f, 0.5f, 2.5f, 24, false);
          }
        }
      }
      if (!world.isRemote) {
        ItemUtil.spawnItem(world, crafterPos.add(0, 1, 0), mortarRecipe.getResult().copy());
        markDirty();
        updatePacketViaState();
      }
      List<ItemStack> items = new ArrayList<>();
      for (int i = 0; i < inventory.getSlots(); i++) {
        items.add(inventory.extractItem(i, 1, false));
      }
      if (!world.isRemote) {
        for (ItemStack stack : mortarRecipe.transformIngredients(items, this)) {
          ItemUtil.spawnItem(world, crafterPos.add(0, 1, 0), stack);
        }
      }
      return true;
    }
    return false;
  }

  public List<ItemStack> getIngredients() {
    List<ItemStack> ingredients = new ArrayList<>();
    for (int i = 0; i < inventory.getSlots(); i++) {
      ItemStack stack = inventory.getStackInSlot(i);
      if (!stack.isEmpty()) {
        ingredients.add(stack);
      }
    }
    return ingredients;
  }

  @Override
  public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
    if (hand != EnumHand.MAIN_HAND) {
      return true;
    }
    ItemStack heldItem = player.getHeldItem(hand);
    ItemStack offHand = player.getHeldItemOffhand();
    List<ItemStack> ingredients = getIngredients();
    if (!heldItem.isEmpty()) {
      ItemStack slot0 = inventory.getStackInSlot(0);
      if (heldItem.getItem() != ModItems.pestle && !(ingredients.size() == 5 && offHand.getItem() == ModItems.pestle)) {
        if (!slot0.isEmpty() && slot0.getItem() == ModItems.pestle) return true;
        for (int i = 0; i < inventory.getSlots(); i++) {
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
              updatePacketViaState();
              return true;
            }
          }
        }
      } else {
        if (ingredients.isEmpty()) {
          ItemStack mortar = inventory.insertItem(0, heldItem, false);
          if (mortar.isEmpty()) {
            player.setHeldItem(hand, ItemStack.EMPTY);
            markDirty();
            updatePacketViaState();
            return true;
          }
        }
        return handleCraft(player.getPosition());
      }
    }
    if (heldItem.isEmpty() && !world.isRemote) {
      for (int i = inventory.getSlots() - 1; i >= 0; i--) {
        if (this.dropItemInInventory(inventory, i)) {
          return true;
        }
      }
    }
    return !heldItem.isEmpty();
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
    if (!world.isRemote) {
      Util.spawnInventoryInWorld(world, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, inventory);
    }
  }
}

