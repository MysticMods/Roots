package epicsquid.roots.tileentity;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.tile.TileBase;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.block.groves.BlockGroveStone;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.network.fx.MessageGrowthCrafterVisualFX;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.recipe.FeyCraftingRecipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileEntityFeyCrafter extends TileBase {
  public static int GROVE_STONE_RADIUS = 10;

  private Random random = new Random();

  public ItemStackHandler inventory = new ItemStackHandler(5) {
    @Override
    protected void onContentsChanged(int slot) {
      TileEntityFeyCrafter.this.markDirty();
      if (!world.isRemote) {
        TileEntityFeyCrafter.this.updatePacketViaState();
      }
    }
  };

  private BlockPos groveStone = null;

  public TileEntityFeyCrafter() {
    super();
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    tag.setTag("handler", inventory.serializeNBT());
    tag.setLong("groveStone", groveStone == null ? -1 : groveStone.toLong());

    return tag;
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    inventory.deserializeNBT(tag.getCompoundTag("handler"));
    long gpos = tag.getLong("groveStone");
    if (gpos == -1) groveStone = null;
    else groveStone = BlockPos.fromLong(gpos);
  }

  @Nonnull
  @Override
  public NBTTagCompound getUpdateTag() {
    return writeToNBT(new NBTTagCompound());
  }

  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
  }

  @Override
  public void onDataPacket(@Nonnull NetworkManager net, SPacketUpdateTileEntity pkt) {
    readFromNBT(pkt.getNbtCompound());
  }

  @Override
  public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, EntityPlayer player) {
    if (!world.isRemote) {
      Util.spawnInventoryInWorld(world, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, inventory);
    }
  }

  public boolean hasValidGroveStone() {
    if (groveStone != null) {
      IBlockState grove = world.getBlockState(groveStone);
      if (grove.getBlock() == ModBlocks.grove_stone && grove.getValue(BlockGroveStone.VALID)) {
        return true;
      } else {
        groveStone = null;
      }
    }

    List<BlockPos> potentials = Util.getBlocksWithinRadius(world, pos, GROVE_STONE_RADIUS, GROVE_STONE_RADIUS, GROVE_STONE_RADIUS, ModBlocks.grove_stone);
    if (potentials.isEmpty()) return false;

    for (BlockPos pos : potentials) {
      IBlockState grove = world.getBlockState(pos);
      if (grove.getValue(BlockGroveStone.VALID)) {
        groveStone = pos;
        return true;
      }
    }

    return false;
  }

  public List<ItemStack> craft() {
    FeyCraftingRecipe recipe = getRecipe();
    List<ItemStack> result = new ArrayList<>();
    ItemStack current = ItemStack.EMPTY;
    List<ItemStack> inputItems = new ArrayList<>();
    while (recipe != null) {
      inputItems.clear();
      boolean singleStack = false;
      for (int i = 0; i < 5; i++) {
        inputItems.add(inventory.extractItem(i, 1, false));
      }

      if (current.isEmpty()) {
        current = recipe.getResult().copy();
        if (current.getMaxStackSize() == 1) singleStack = true;
        recipe.postCraft(current, inputItems);
      } else {
        // TODO: If this ever becomes a problem in the future I will laugh
        // TODO: But technically if you run out of ingredients for one recipe and then shift down to another, the result could change
        if (current.getCount() + recipe.getResult().getCount() < current.getMaxStackSize()) {
          current.grow(recipe.getResult().getCount());
        } else {
          result.add(current);
          current = recipe.getResult().copy();
        }
      }

      recipe = getRecipe();
      if (singleStack) break;
    }

    if (!current.isEmpty()) {
      result.add(current);
    }

    return result;
  }

  public FeyCraftingRecipe getRecipe() {
    return ModRecipes.getFeyCraftingRecipe(getContents());
  }

  public List<ItemStack> getContents() {
    List<ItemStack> result = new ArrayList<>();
    for (int i = 0; i < inventory.getSlots(); i++) {
      result.add(inventory.getStackInSlot(i));
    }
    return result;
  }

  @Override
  public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand,
                          @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
    // Knife detection is already handled
    if (!hasValidGroveStone()) return false;

    if (player.world.isRemote) {
      return true;
    }

    List<ItemStack> items = craft();
    if (items.isEmpty()) {
      return true;
    }

    for (EnumFacing facing : EnumFacing.values()) {
      TileEntity te = world.getTileEntity(getPos().offset(facing));
      if (te != null) {
        IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (cap != null) {
          List<ItemStack> newItems = new ArrayList<>();
          for (ItemStack toPut : items) {
            ItemStack result = ItemHandlerHelper.insertItemStacked(cap, toPut, false);
            if (!result.isEmpty()) {
              newItems.add(result);
            }
          }
          items = newItems;
        }
      }
    }

    for (ItemStack stack : items) {
      EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5, stack);
      world.spawnEntity(item);
    }

    MessageGrowthCrafterVisualFX packet = new MessageGrowthCrafterVisualFX(getPos(), world.provider.getDimension());
    PacketHandler.sendToAllTracking(packet, this);
    world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.CHIMES, SoundCategory.NEUTRAL, 1f, 1f);

    return true;
  }

  public void doVisual() {
    if (world.isRemote) {
      for (int i = 0; i < 40; i++) {
        // TODO: Use whirlwind of leaf particles!
        ParticleUtil.spawnParticleFiery(world, getPos().getX() + 0.125f + 0.75f * random.nextFloat(), getPos().getY() + 1.25f + 0.5f * random.nextFloat(),
            getPos().getZ() + 0.125f + 0.75f * random.nextFloat(), 0.03125f * (random.nextFloat() - 0.5f), 0.125f * random.nextFloat(),
            0.03125f * (random.nextFloat() - 0.5f), 255.0f, 224.0f, 32.0f, 0.75f, 9.0f + 9.0f * random.nextFloat(), 40);
      }
    }
  }
}

