package epicsquid.roots.tileentity;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.particle.particles.ParticleLeaf;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.network.fx.MessageGrowthCrafterVisualFX;
import epicsquid.roots.recipe.FeyCraftingRecipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class TileEntityRunicCrafter extends TileEntityFeyCrafter implements ITickable {
  protected FeyCraftingRecipe currentRecipe;

  public ItemStackHandler pedestal = new ItemStackHandler(1) {
    @Override
    protected void onContentsChanged(int slot) {
      TileEntityRunicCrafter.this.markDirty();
      if (!world.isRemote) {
        TileEntityRunicCrafter.this.updatePacketViaState();
        TileEntityRunicCrafter.this.world.updateComparatorOutputLevel(pos, ModBlocks.runic_crafter);
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

  public TileEntityRunicCrafter() {
    super();
  }

  @Override
  public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
    if (player.world.isRemote) {
      return true;
    }

    if (!hasValidGroveStone()) {
      return true;
    }

    ItemStack heldItem = player.getHeldItem(hand);
    if (heldItem.isEmpty() || !ModItems.knives.contains(heldItem.getItem())) {
      boolean update = false;
      if (heldItem.isEmpty() && !pedestal.getStackInSlot(0).isEmpty()) {
        player.setHeldItem(hand, pedestal.getStackInSlot(0).copy());
        pedestal.setStackInSlot(0, ItemStack.EMPTY);
        update = true;
        // pop it off
      } else if (pedestal.getStackInSlot(0).isEmpty() && !heldItem.isEmpty()) {
        ItemStack pedestalStack = heldItem.copy();
        pedestalStack.setCount(1);
        pedestal.setStackInSlot(0, pedestalStack);
        heldItem.shrink(1);
        update = true;
        // pop it in
      }
      if (update) {
        currentRecipe = ModRecipes.getFeyCraftingRecipe(pedestal.getStackInSlot(0));
      }
    } else {
      this.storedItems = craft(player);
      if (!this.storedItems.isEmpty()) {
        this.countdown = COUNTDOWN;

        MessageGrowthCrafterVisualFX packet = new MessageGrowthCrafterVisualFX(getPos(), world.provider.getDimension());
        PacketHandler.sendToAllTracking(packet, this);
        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.WHIRLWIND, SoundCategory.NEUTRAL, 1f, 1f);
      }
    }
    return true;
  }

  @Override
  @Nullable
  public FeyCraftingRecipe getRecipe() {
    if (ItemUtil.equalWithoutSize(currentRecipe.getResult(), pedestal.getStackInSlot(0))) {
      return currentRecipe;
    } else {
      currentRecipe = ModRecipes.getFeyCraftingRecipe(pedestal.getStackInSlot(0));
      return currentRecipe;
    }
  }

  @Override
  public void update() {
    if (!world.isRemote) {
      if (world.getTotalWorldTime() % 2 == 0 && getRecipe() != null) {
        ClientProxy.particleRenderer.spawnParticle(
            world,
            ParticleLeaf.class,
            (double) pos.getX() + 0.5D,
            (double) pos.getY() + 0.75D,
            (double) pos.getZ() + 0.5D,
            (Util.rand.nextDouble() - 0.5) * 0.005,
            (Util.rand.nextDouble() * 0.02) * 0.5,
            (Util.rand.nextDouble() - 0.5) * 0.005,
            100,
            63 / 255.0,
            (143 / 255.0) + Util.rand.nextDouble() * 0.05,
            74 / 255.0,
            1, //0.785,
            1,
            1
        );
      }
      if (countdown > 0) {
        countdown--;
      } else {
        countdown = -1;
        for (EnumFacing facing : EnumFacing.values()) {
          TileEntity te = world.getTileEntity(getPos().offset(facing));
          if (te != null) {
            IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if (cap != null) {
              List<ItemStack> newItems = new ArrayList<>();
              for (ItemStack toPut : storedItems) {
                ItemStack result = ItemHandlerHelper.insertItemStacked(cap, toPut, false);
                if (!result.isEmpty()) {
                  newItems.add(result);
                }
              }
              storedItems = newItems;
            }
          }
        }
        for (ItemStack stack : storedItems) {
          EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5, stack);
          world.spawnEntity(item);
        }
        this.storedItems.clear();
      }
    }
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag) {
    tag = super.writeToNBT(tag);
    tag.setTag("pedestal", pedestal.serializeNBT());
    return tag;
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    pedestal.deserializeNBT(tag.getCompoundTag("pedestal"));
  }
}

