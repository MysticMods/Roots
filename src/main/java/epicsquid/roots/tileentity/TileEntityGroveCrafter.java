package epicsquid.roots.tileentity;

import epicsquid.mysticallib.tile.TileBase;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.GroveCraftingRecipe;
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

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TileEntityGroveCrafter extends TileBase {
  public ItemStackHandler inventory = new ItemStackHandler(5);

  public TileEntityGroveCrafter() {
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
  public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
    if (!world.isRemote) {
      Util.spawnInventoryInWorld(world, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, inventory);
    }
  }

  public List<ItemStack> craft () {
    GroveCraftingRecipe recipe = getRecipe();
    List<ItemStack> result = new ArrayList<>();
    while (recipe != null) {

      for (int i = 0; i < 5; i++) {
        inventory.extractItem(i, 1, false);
      }

      result.add(recipe.getResult().copy());

      recipe = getRecipe();
    }

    return result;
  }

  public GroveCraftingRecipe getRecipe () {
    return ModRecipes.getGroveCraftingRecipe(getContents());
  }

  public List<ItemStack> getContents () {
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
    if (player.world.isRemote) {
      return true;
    }

    List<ItemStack> items = craft();
    if (items.isEmpty()) {
      return true;
    }

    for (ItemStack stack : items) {
      EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
      world.spawnEntity(item);
    }

    return true;
  }
}

