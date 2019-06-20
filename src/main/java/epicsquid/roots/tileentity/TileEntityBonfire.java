package epicsquid.roots.tileentity;

import epicsquid.mysticallib.tile.TileBase;
import epicsquid.mysticallib.util.ListUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.block.BlockBonfire;
import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.recipe.PyreCraftingRecipe;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.util.ItemSpawnUtil;
import epicsquid.roots.util.XPUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileEntityBonfire extends TileBase implements ITickable {
  private float ticker = 0;
  private float pickupDelay = 0;
  private int burnTime = 0;
  private boolean doBigFlame = false;
  private ItemStack craftingResult = ItemStack.EMPTY;
  private int craftingXP = 0;
  private RitualBase lastRitualUsed = null;
  private PyreCraftingRecipe lastRecipeUsed = null;
  private List<Ingredient> lastUsedIngredients = null;
  private EntityRitualBase ritualEntity = null;

  private boolean isBurning = false;

  private Random random = new Random();

  public ItemStackHandler inventory = new ItemStackHandler(5) {
    @Override
    protected void onContentsChanged(int slot) {
      if (!world.isRemote) {
        TileEntityBonfire.this.markDirty();
        TileEntityBonfire.this.updatePacketViaState();
      }
    }
  };

  public ItemStackHandler inventory_storage = new ItemStackHandler(5);

  public void clearStorage() {
    for (int i = 0; i < 5; i++) {
      inventory_storage.setStackInSlot(i, ItemStack.EMPTY);
    }
  }

  public TileEntityBonfire() {
    super();
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    tag.setTag("handler", inventory.serializeNBT());
    tag.setInteger("burnTime", burnTime);
    tag.setBoolean("doBigFlame", doBigFlame);
    tag.setTag("craftingResult", this.craftingResult.serializeNBT());
    tag.setInteger("craftingXP", craftingXP);
    tag.setString("lastRitualUsed", (this.lastRitualUsed != null) ? this.lastRitualUsed.getName() : "");
    tag.setString("lastRecipeUsed", (this.lastRecipeUsed != null) ? this.lastRecipeUsed.getName() : "");
    tag.setInteger("entity", (ritualEntity == null) ? -1 : ritualEntity.getEntityId());
    return tag;
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    inventory.deserializeNBT(tag.getCompoundTag("handler"));
    burnTime = tag.getInteger("burnTime");
    doBigFlame = tag.getBoolean("doBigFlame");
    craftingResult = new ItemStack(tag.getCompoundTag("craftingResult"));
    craftingXP = tag.getInteger("craftingXP");
    lastRitualUsed = RitualRegistry.getRitual(tag.getString("lastRitualUsed"));
    lastRecipeUsed = ModRecipes.getCraftingRecipe(tag.getString("lastRecipeUsed"));
    if (hasWorld()) {
      ritualEntity = tag.getInteger("entity") != -1 ? (EntityRitualBase) world.getEntityByID(tag.getInteger("entity")) : null;
    }
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

  public PyreCraftingRecipe getCurrentRecipe() {
    List<ItemStack> stacks = new ArrayList<>();
    for (int i = 0; i < inventory.getSlots(); i++) {
      ItemStack stack = inventory.extractItem(i, 1, true);
      stacks.add(stack);
    }

    return ModRecipes.getCraftingRecipe(stacks);
  }

  @Override
  public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand,
                          @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
    if (world.isRemote) return true;
    ItemStack heldItem = player.getHeldItem(hand);
    if (!heldItem.isEmpty()) {
      if (heldItem.getItem() instanceof ItemFlintAndSteel) {
        RitualBase ritual = RitualRegistry.getRitual(this, player);
        if (ritual != null) {
          if ((ritualEntity == null || ritualEntity.isDead) && ritual.canFire(this, player)) {
            ritualEntity = ritual.doEffect(world, pos);
            this.burnTime = ritual.getDuration();
            this.lastRitualUsed = ritual;
            this.lastRecipeUsed = null;
            this.lastUsedIngredients = ritual.getIngredients();
            this.doBigFlame = true;
            for (int i = 0; i < inventory.getSlots(); i++) {
              inventory.extractItem(i, 1, false);
            }
            markDirty();
            updatePacketViaState();
            return true;
          }
        }

        PyreCraftingRecipe recipe = getCurrentRecipe();
        if (recipe != null) {
          this.lastRecipeUsed = recipe;
          this.lastRitualUsed = null;
          this.lastUsedIngredients = recipe.getIngredients();
          this.craftingResult = recipe.getResult();
          this.craftingXP = recipe.getXP();
          this.burnTime = recipe.getBurnTime();
          this.doBigFlame = true;
          for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack item = inventory.extractItem(i, 1, false);
            inventory_storage.insertItem(i, item, false);
          }
          markDirty();
          updatePacketViaState();
          return true;
        }
      } else if (heldItem.getItem() == Items.WATER_BUCKET && burnTime > 0) {
        burnTime = 0;
        if (ritualEntity != null) {
          ritualEntity.setDead();
        }
        lastRecipeUsed = null;
        lastRitualUsed = null;
        BlockBonfire.setState(false, world, pos);
        world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1F, 1.25F);
        world.playSound(player, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.25F, 1F);
        if (!player.isCreative()) {
          IFluidHandlerItem handler = heldItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
          if (handler != null && handler.getTankProperties().length == 1) {
            handler.drain(handler.getTankProperties()[0].getContents(), true);
          }
        }
        return true;
      } else {
        for (int i = 0; i < 5; i++) {
          if (inventory.getStackInSlot(i).isEmpty()) {
            ItemStack toInsert = heldItem.copy();
            if (!player.isSneaking()) {
              toInsert.setCount(1);
            }

            ItemStack attemptedInsert = inventory.insertItem(i, toInsert, true);
            if (attemptedInsert.isEmpty()) {
              inventory.insertItem(i, toInsert, false);
              if (!player.isSneaking()) {
                heldItem.setCount(heldItem.getCount() - 1);
              } else {
                player.setHeldItem(hand, ItemStack.EMPTY);
              }
              if (heldItem.getCount() <= 0) {
                player.setHeldItem(hand, ItemStack.EMPTY);
              } else {
                player.setHeldItem(hand, heldItem);
              }
              markDirty();
              updatePacketViaState();
              return true;
            }
          }
        }
      }
    }
    if (player.isSneaking() && heldItem.isEmpty() && hand == EnumHand.MAIN_HAND) {
      if (this.lastUsedIngredients == null) {
        if (this.lastRitualUsed != null) {
          this.lastUsedIngredients = this.lastRitualUsed.getIngredients();
        } else if (this.lastRecipeUsed != null) {
          this.lastUsedIngredients = this.lastRecipeUsed.getIngredients();
        }
      }
      if (this.lastUsedIngredients != null) {
        if (player.capabilities.isCreativeMode) {
          int i = 0;
          for (Ingredient ingredient : this.lastUsedIngredients) {
            inventory.setStackInSlot(i++, ingredient.getMatchingStacks()[0]);
          }
        } else {
          IItemHandler inventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
          assert inventory != null;
          for (Ingredient ingredient : lastUsedIngredients) {
            for (int i = 0; i < inventory.getSlots(); i++) {
              ItemStack stack = inventory.getStackInSlot(i);
              if (ingredient.apply(stack)) {
                insertItemFromPlayerInventory(stack, player, i);
                break;
              }
            }
          }
        }
      }
      return true;
    }

    if (heldItem.isEmpty() && hand == EnumHand.MAIN_HAND) {
      for (int i = 4; i >= 0; i--) {
        if (!inventory.getStackInSlot(i).isEmpty()) {
          ItemStack extracted = inventory.extractItem(i, inventory.getStackInSlot(i).getCount(), false);
          ItemSpawnUtil.spawnItem(world, player.posX, player.posY + 1, player.posZ, false, extracted, 0, -1);
          markDirty();
          updatePacketViaState();
          pickupDelay = 40;
          return true;
        }
      }
    }
    return true;
  }

  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
    if (oldState.getBlock() == newState.getBlock() && newState.getBlock() == ModBlocks.bonfire) return false;

    return super.shouldRefresh(world, pos, oldState, newState);
  }

  @Override
  public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, EntityPlayer player) {
    if (!world.isRemote) {
      Util.spawnInventoryInWorld(world, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, inventory);
    }
  }

  public int getBurnTime() {
    return burnTime;
  }

  @Override
  public void update() {
    this.ticker += 1.0f;
    if (pickupDelay > 0) {
      pickupDelay--;
    }
    //Spawn the Ignite flame particle
    if (world.isRemote && this.doBigFlame) {
      for (int i = 0; i < 40; i++) {
        ParticleUtil.spawnParticleFiery(world, getPos().getX() + 0.125f + 0.75f * random.nextFloat(), getPos().getY() + 0.75f + 0.5f * random.nextFloat(),
            getPos().getZ() + 0.125f + 0.75f * random.nextFloat(), 0.03125f * (random.nextFloat() - 0.5f), 0.125f * random.nextFloat(),
            0.03125f * (random.nextFloat() - 0.5f), 255.0f, 224.0f, 32.0f, 0.75f, 9.0f + 9.0f * random.nextFloat(), 40);
      }
    }
    if (doBigFlame) {
      if (burnTime != 0) {
        BlockBonfire.setState(true, world, pos);
      }
      doBigFlame = false;
      markDirty();
      if (!world.isRemote)
        updatePacketViaState();
      //PacketHandler.sendToAllTracking(new MessageTEUpdate(this.getUpdateTag()), this);
    }

    if ((ritualEntity != null && ritualEntity.isDead) && craftingResult.isEmpty()) {
      burnTime = 1;
    }

    if (burnTime > 0) {

      burnTime--;

      if (getTicker() % 20.0f == 0 && random.nextDouble() < 0.05) {
        meltNearbySnow();
      }

      if (burnTime == 0) {
        BlockBonfire.setState(false, world, pos);
        //Check if it is a ritual, if so try and see if it has new ritual fuel.
        if (this.craftingResult.isEmpty() && this.lastRitualUsed != null) {
          List<ItemStack> stacks = new ArrayList<>();
          for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i).copy();
            stack.setCount(1);
            stacks.add(stack);
          }
          if (ListUtil.stackListsMatch(stacks, this.lastRitualUsed.getRecipe())) {
            lastRitualUsed.doEffect(world, getPos());
            this.burnTime = this.lastRitualUsed.getDuration();
            this.doBigFlame = true;
            for (int i = 0; i < inventory.getSlots(); i++) {
              inventory.extractItem(i, 1, false);
            }
            markDirty();
            if (!world.isRemote)
              updatePacketViaState();
            //PacketHandler.sendToAllTracking(new MessageTEUpdate(this.getUpdateTag()), this);
          }
        }

        //Spawn item if crafting recipe
        if (!world.isRemote && !this.craftingResult.isEmpty()) {
          ItemStack result = this.craftingResult.copy();
          if (this.lastRecipeUsed != null) {
            this.lastRecipeUsed.postCraft(result, inventory_storage);
          }

          EntityItem item = new EntityItem(world, getPos().getX() + 0.5, getPos().getY() + 1, getPos().getZ() + 0.5, result);
          item.setCustomNameTag("bonfire");
          ItemSpawnUtil.spawnItem(world, item);
          XPUtil.spawnXP(world, getPos(), this.craftingXP);
          this.craftingResult = ItemStack.EMPTY;
          clearStorage();
        }
      }
      //Spawn Fire particles
      if (world.isRemote) {
        for (int i = 0; i < 2; i++) {
          ParticleUtil
              .spawnParticleFiery(world, getPos().getX() + 0.3125f + 0.375f * Util.rand.nextFloat(), getPos().getY() + 0.625f + 0.375f * Util.rand.nextFloat(),
                  getPos().getZ() + 0.3125f + 0.375f * Util.rand.nextFloat(), 0.03125f * (Util.rand.nextFloat() - 0.5f), 0.125f * Util.rand.nextFloat(),
                  0.03125f * (Util.rand.nextFloat() - 0.5f), 255.0f, 96.0f, 32.0f, 0.75f, 7.0f + 7.0f * Util.rand.nextFloat(), 40);
        }
      }
    }

    pickupItem();
  }

  private void meltNearbySnow() {
    List<BlockPos> nearbySnowOrIce = new ArrayList<>();
    for (int x = -4; x < 5; x++) {
      for (int z = -4; z < 5; z++) {
        BlockPos topBlockPos = world.getTopSolidOrLiquidBlock(new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z));
        topBlockPos = topBlockPos.subtract(new Vec3i(0, 1, 0));
        IBlockState topBlockState = world.getBlockState(topBlockPos);
        if (topBlockState.getMaterial() == Material.SNOW || topBlockState.getMaterial() == Material.ICE) {
          nearbySnowOrIce.add(topBlockPos);
        }
      }
    }

    if (nearbySnowOrIce.isEmpty()) {
      return;
    }
    BlockPos posToMelt = nearbySnowOrIce.get(nearbySnowOrIce.size() > 1 ? random.nextInt(nearbySnowOrIce.size() - 1) : 0);
    if (world.getBlockState(posToMelt).getMaterial() == Material.SNOW) {
      world.setBlockState(posToMelt, Blocks.AIR.getDefaultState());
    } else if (world.getBlockState(posToMelt).getMaterial() == Material.ICE) {
      world.setBlockState(posToMelt, Blocks.WATER.getDefaultState());
    }
  }

  private void pickupItem() {
    if ((int) this.ticker % 20 == 0 && pickupDelay == 0) {
      List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class,
          new AxisAlignedBB(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX() + 1, getPos().getY() + 1, getPos().getZ() + 1));
      for (EntityItem item : items) {
        ItemStack stack = item.getItem();
        if (item.getCustomNameTag().equalsIgnoreCase("bonfire")) {
          continue;
        }
        for (int i = 0; i < this.inventory.getSlots(); i++) {
          if (this.inventory.getStackInSlot(i).isEmpty()) {
            ItemStack inputStack = stack.copy();
            inputStack.setCount(1);
            this.inventory.insertItem(i, inputStack, false);
            stack.shrink(1);
          }
        }

        markDirty();
        if (!world.isRemote)
          updatePacketViaState();
        //PacketHandler.sendToAllTracking(new MessageTEUpdate(this.getUpdateTag()), this);
      }
    }
  }

  private void insertItemFromPlayerInventory(ItemStack stack, EntityPlayer player, int slot) {
    for (int i = 0; i < 5; i++) {
      if (inventory.getStackInSlot(i).isEmpty()) {
        ItemStack toInsert = stack.copy();
        toInsert.setCount(1);
        inventory.insertItem(i, toInsert, false);
        stack.setCount(stack.getCount() - 1);
        //todo: check if the item goes away
        markDirty();
        if (!world.isRemote)
          updatePacketViaState();
        //PacketHandler.sendToAllTracking(new MessageTEUpdate(this.getUpdateTag()), this);
        break;
      }
    }
  }

  public RitualBase getLastRitualUsed() {
    return lastRitualUsed;
  }

  public PyreCraftingRecipe getLastRecipeUsed() {
    return lastRecipeUsed;
  }


  public void setLastRitualUsed(RitualBase lastRitualUsed) {
    this.lastRitualUsed = lastRitualUsed;
  }

  public void setLastRecipeUsed(PyreCraftingRecipe lastRecipeUsed) {
    this.lastRecipeUsed = lastRecipeUsed;
  }

  public void setBurnTime(int burnTime) {
    this.burnTime = burnTime;
  }

  public boolean getState() {
    return isBurning;
  }

  public float getTicker() {
    return ticker;
  }
}