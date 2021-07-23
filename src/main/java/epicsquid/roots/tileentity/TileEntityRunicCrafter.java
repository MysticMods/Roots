package epicsquid.roots.tileentity;

import com.google.common.collect.Lists;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.particle.particles.ParticleLeaf;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.network.fx.MessageGrowthCrafterVisualFX;
import epicsquid.roots.particle.ParticleStar;
import epicsquid.roots.recipe.FeyCraftingRecipe;
import epicsquid.roots.util.IngredientWithStack;
import epicsquid.roots.util.ItemHandlerUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
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
  public List<ItemStack> craft(EntityPlayer player) {
    FeyCraftingRecipe recipe = getRecipe();
    ItemStack result = ItemStack.EMPTY;
    List<ItemStack> inputItems = new ArrayList<>();
    if (recipe != null) {
      for (int i = 0; i < 5; i++) {
        inputItems.add(inventory.extractItem(i, 1, false));
      }

      for (ItemStack stack : recipe.transformIngredients(inputItems, this)) {
        ItemUtil.spawnItem(world, pos.add(Util.rand.nextBoolean() ? -1 : 1, 1, Util.rand.nextBoolean() ? -1 : 1), stack);
      }

      result = recipe.getResult().copy();
      recipe.postCraft(result, inputItems, player);
      lastRecipe = recipe;
    }

    return Lists.newArrayList(result);
  }

  @Override
  public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
    if (player.world.isRemote) {
      return true;
    }

    if (!hasValidGroveStone()) {
      player.sendMessage(new TextComponentTranslation("roots.message.runic_crafter.no_grove").setStyle(new Style().setColor(TextFormatting.YELLOW)));
      return true;
    }

    ItemStack heldItem = player.getHeldItem(hand);
    ItemStack pedestalItem = pedestal.getStackInSlot(0);
    if (!ModItems.knives.contains(heldItem.getItem())) {
      boolean update = false;
      if (!pedestalItem.isEmpty() && heldItem.getCount() < heldItem.getMaxStackSize() && ItemUtil.equalWithoutSize(heldItem, pedestalItem) || heldItem.isEmpty()) {
        if (heldItem.isEmpty()) {
          player.setHeldItem(hand, pedestalItem);
        } else {
          heldItem.grow(1);
        }
        pedestal.setStackInSlot(0, ItemStack.EMPTY);
        update = true;
        // pop it off
      } else if (pedestalItem.isEmpty()) {
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
    ItemStack pedestalStack = pedestal.getStackInSlot(0);
    if (pedestalStack.isEmpty()) {
      return null;
    }
    if (currentRecipe != null && ItemUtil.equalWithoutSize(currentRecipe.getResult(), pedestalStack)) {
      return currentRecipe;
    } else {
      currentRecipe = ModRecipes.getFeyCraftingRecipe(pedestalStack);
      return currentRecipe;
    }
  }

  @Override
  public void update() {
    if (!world.isRemote) {
      if (!hasValidGroveStone()) {
        if (world.getTotalWorldTime() % 3 != 0) {
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
              219 / 255.0,
              15 / 255.0,
              15 / 255.0,
              1, //0.785,
              1,
              1
          );
        } else {
          if (world.getTotalWorldTime() % 3 != 0 && getRecipe() != null) {
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
                (140 / 255.0) + (Util.rand.nextDouble() - 0.5) * 0.1,
                52 / 255.0,
                245 / 255.0,
                1, //0.785,
                1,
                1
            );
          }
        }
      }
      if (countdown > 0) {
        countdown--;
      } else {
        countdown = -1;
        for (EnumFacing facing : EnumFacing.values()) {
          if (facing == EnumFacing.DOWN) {
            continue;
          }
          IBlockState state = world.getBlockState(getPos().offset(facing));
          if (GeneralConfig.getCrafterOutputIgnore().contains(state.getBlock())) {
            continue;
          }
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
      if (countdown == -1) {
        List<ItemStack> items = getContents();
        FeyCraftingRecipe recipe = getRecipe();
        if (recipe == null || (recipe != null && !items.isEmpty() && (items.size() != 5 || !recipe.matches(items)))) {
          // Eject these
          TileEntity te = world.getTileEntity(getPos().down());
          if (te != null) {
            IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if (cap != null) {
              for (int i = 0; i < inventory.getSlots(); i++) {
                ItemStack toPut = inventory.getStackInSlot(i);
                inventory.setStackInSlot(i, ItemHandlerHelper.insertItemStacked(cap, toPut, false));
              }
            }
          }
        }
        if (recipe != null && !items.isEmpty() && (items.size() != 5 || recipe.matches(items))) {
          return;
        }
        if (recipe != null) {
          refillInventory();
        }
      }
    }
  }

  public void refillInventory() {
    FeyCraftingRecipe recipe = getRecipe();
    if (!world.isRemote && recipe != null) {
      List<Ingredient> requirements = recipe.getIngredients();
      if (ItemHandlerUtil.isEmpty(inventory)) {
        TileEntity te = world.getTileEntity(getPos().down());
        if (te != null) {
          IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
          if (cap != null) {
            Int2ObjectOpenHashMap<IngredientWithStack> slotsToIngredient = new Int2ObjectOpenHashMap<>();
            int amount = 0;
            for (Ingredient ingredient : requirements) {
              for (int i = 0; i < cap.getSlots(); i++) {
                ItemStack inSlot = cap.getStackInSlot(i);
                if (ingredient.apply(inSlot)) {
                  if (slotsToIngredient.containsKey(i)) {
                    if (inSlot.getCount() > slotsToIngredient.get(i).getCount()) {
                      amount++;
                      slotsToIngredient.get(i).increment();
                      break;
                    }
                  } else {
                    amount++;
                    slotsToIngredient.put(i, new IngredientWithStack(ingredient, 1));
                    break;
                  }
                }
              }
            }
            if (amount == 5) {
              List<ItemStack> temp = ItemHandlerUtil.getItemsInSlots(cap, slotsToIngredient, true);
              if (temp.size() == 5) {
                temp = ItemHandlerUtil.getItemsInSlots(cap, slotsToIngredient, false);
                for (int i = 0; i < temp.size(); i++) {
                  ItemStack stack = temp.get(i);
                  inventory.setStackInSlot(i, stack);
                }
                markDirty();
                updatePacketViaState();
              }
            }
          }
        }
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

  @Override
  public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, EntityPlayer player) {
    super.breakBlock(world, pos, state, player);
    if (!world.isRemote) {
      Util.spawnInventoryInWorld(world, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, pedestal);
    }
  }
}

