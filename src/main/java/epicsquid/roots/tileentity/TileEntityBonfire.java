package epicsquid.roots.tileentity;

import epicsquid.mysticallib.tile.TileBase;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.ListUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.block.BonfireBlock;
import epicsquid.roots.entity.ritual.BaseRitualEntity;
import epicsquid.roots.entity.ritual.FrostLandsRitualEntity;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.recipe.PyreCraftingRecipe;
import epicsquid.roots.ritual.IColdRitual;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.util.ItemHandlerUtil;
import epicsquid.roots.util.XPUtil;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileEntityBonfire extends TileBase implements ITickableTileEntity {
  public static AxisAlignedBB bounding = new AxisAlignedBB(-1, -1, -1, 1, 1, 1);

  private float ticker = 0;
  private float pickupDelay = 0;
  private int burnTime = 0;
  private boolean doBigFlame = false;
  private ItemStack craftingResult = ItemStack.EMPTY;
  private int craftingXP = 0;
  private RitualBase lastRitualUsed = null;
  private PyreCraftingRecipe lastRecipeUsed = null;
  private List<Ingredient> lastUsedIngredients = null;
  private BaseRitualEntity ritualEntity = null;

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

  public TileEntityBonfire(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  public void clearStorage() {
    for (int i = 0; i < 5; i++) {
      inventory_storage.setStackInSlot(i, ItemStack.EMPTY);
    }
  }


  @Nonnull
  @Override
  public CompoundNBT write(CompoundNBT tag) {
    super.write(tag);
    tag.put("handler", inventory.serializeNBT());
    tag.putInt("burnTime", burnTime);
    tag.putBoolean("doBigFlame", doBigFlame);
    tag.put("craftingResult", this.craftingResult.serializeNBT());
    tag.putInt("craftingXP", craftingXP);
    tag.putString("lastRitualUsed", (this.lastRitualUsed != null) ? this.lastRitualUsed.getName() : "");
    tag.putString("lastRecipeUsed", (this.lastRecipeUsed != null) ? this.lastRecipeUsed.getName() : "");
    tag.putInt("entity", (ritualEntity == null) ? -1 : ritualEntity.getEntityId());
    return tag;
  }

  @Override
  public void read(CompoundNBT tag) {
    super.read(tag);
    inventory.deserializeNBT(tag.getCompound("handler"));
    burnTime = tag.getInt("burnTime");
    doBigFlame = tag.getBoolean("doBigFlame");
    ItemStack result = ItemStack.read(tag.getCompound("craftingResult"));
    craftingResult = result;
    craftingXP = tag.getInt("craftingXP");
    lastRitualUsed = RitualRegistry.getRitual(tag.getString("lastRitualUsed"));
    lastRecipeUsed = ModRecipes.getCraftingRecipe(tag.getString("lastRecipeUsed"));
    if (hasWorld()) {
      ritualEntity = tag.getInt("entity") != -1 ? (BaseRitualEntity) world.getEntityByID(tag.getInt("entity")) : null;
    }
  }

  @Nonnull
  @Override
  public CompoundNBT getUpdateTag() {
    return write(new CompoundNBT());
  }

  @Override
  public SUpdateTileEntityPacket getUpdatePacket() {
    return new SUpdateTileEntityPacket(getPos(), 0, getUpdateTag());
  }

  @Override
  public void onDataPacket(@Nonnull NetworkManager net, SUpdateTileEntityPacket pkt) {
    read(pkt.getNbtCompound());
  }

  public PyreCraftingRecipe getCurrentRecipe() {
    List<ItemStack> stacks = new ArrayList<>();
    for (int i = 0; i < inventory.getSlots(); i++) {
      ItemStack stack = inventory.extractItem(i, 1, true);
      stacks.add(stack);
    }

    return ModRecipes.getCraftingRecipe(stacks);
  }

  private void validateEntity() {
    if (ritualEntity == null) return;

    if (!ritualEntity.isAlive()) return;

    int lifetime = ritualEntity.getDataManager().get(BaseRitualEntity.lifetime);

    if (lifetime <= 0) {
      ritualEntity.remove();
    }

    if (burnTime <= 0) {
      ritualEntity.remove();
    }
  }

  private boolean startRitual(@Nullable PlayerEntity player) {
    RitualBase ritual = RitualRegistry.getRitual(this, player);
    validateEntity();

    if (ritual != null && !ritual.isDisabled()) {
      if ((ritualEntity == null || !ritualEntity.isAlive()) && ritual.canFire(this, player)) {
        ritualEntity = ritual.doEffect(world, pos);
        this.burnTime = ritual.getDuration();
        this.lastRitualUsed = ritual;
        this.lastRecipeUsed = null;
        this.lastUsedIngredients = ritual.getIngredients();
        this.doBigFlame = true;
        for (int i = 0; i < inventory.getSlots(); i++) {
          ItemStack item = inventory.extractItem(i, 1, false);
          if (!world.isRemote) {
            if (item.getItem().hasContainerItem(item)) {
              ItemStack container = ForgeHooks.getContainerItem(item);
              InventoryHelper.spawnItemStack(world, pos.getX() + 1.5, pos.getY() + 0.5, pos.getZ() - 0.5, container);
            }
          }
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
        if (!world.isRemote) {
          if (item.getItem().hasContainerItem(item)) {
            ItemStack container = ForgeHooks.getContainerItem(item);
            InventoryHelper.spawnItemStack(world, pos.getX() + 1.5, pos.getY() + 0.5, pos.getZ() - 0.5, container);
          }
        }
      }
      markDirty();
      updatePacketViaState();
      return true;
    }

    return false;
  }

  private void resolveLastIngredients() {
    if (this.lastUsedIngredients == null || this.lastUsedIngredients.isEmpty()) {
      if (this.lastRitualUsed != null) {
        this.lastUsedIngredients = this.lastRitualUsed.getIngredients();
      } else if (this.lastRecipeUsed != null) {
        this.lastUsedIngredients = this.lastRecipeUsed.getIngredients();
      }
    }
  }

  @Override
  public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull Direction side, float hitX, float hitY, float hitZ) {
    if (world.isRemote) return true;
    ItemStack heldItem = player.getHeldItem(hand);
    if (!heldItem.isEmpty()) {
      boolean extinguish = false;
      // TODO: Work out how fluid capabilities now work
      /*heldItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent((cap) -> {
        IFluidTankProperties[] props = cap.getTankProperties();
        if (props != null && props.length >= 1) {
          for (IFluidTankProperties prop : props) {
            FluidStack stack = prop.getContents();
            if (stack != null) {
              if (stack.getFluid().getTemperature() <= 300) {
                extinguish = true;
              } else if (RitualConfig.getExtinguishFluids().contains(stack.getFluid().getName())) {
                extinguish = true;
              }
            }
            if (extinguish) {
              if (stack.amount > 1000) {
                stack.amount = 1000;
              }
              if (!player.capabilities.isCreativeMode) {
                cap.drain(stack, true);
                if (heldItem.getItem() instanceof BucketItem) {
                  player.setHeldItem(hand, new ItemStack(Items.BUCKET));
                  ((ServerPlayerEntity) player).sendAllContents(player.openContainer, player.openContainer.getInventory());
                }
              }
              break;
            }
          }
        }
      });*/
      // TODO: Make this a configurable array of items or extensible classes
      if (heldItem.getItem() instanceof FlintAndSteelItem) {
        heldItem.damageItem(1, player, (p_220282_1_) -> {
          p_220282_1_.sendBreakAnimation(hand);
        });
        return startRitual(player);
      } else if (extinguish && burnTime > 0) {
        burnTime = 0;
        if (ritualEntity != null) {
          ritualEntity.remove();
        }
        lastRecipeUsed = null;
        lastRitualUsed = null;
        BonfireBlock.setState(false, world, pos);
        world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1F, 1F);
        world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1F, 1F);
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
    if (player.isSneaking() && heldItem.isEmpty() && hand == Hand.MAIN_HAND) {
      resolveLastIngredients();
      if (this.lastUsedIngredients != null) {
        if (player.isCreative()) {
          int i = 0;
          for (Ingredient ingredient : this.lastUsedIngredients) {
            inventory.setStackInSlot(i++, ingredient.getMatchingStacks()[0]);
          }
        } else {
          LazyOptional<IItemHandler> handler = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP);
          if (handler.isPresent()) {
            IItemHandler inventory = handler.orElseThrow(IllegalStateException::new);
            for (Ingredient ingredient : lastUsedIngredients) {
              for (int i = 0; i < inventory.getSlots(); i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (ingredient.test(stack)) {
                  insertItemFromPlayerInventory(stack, player, i);
                  break;
                }
              }
            }
          }
        }
      }
      return true;
    }

    if (heldItem.isEmpty() && hand == Hand.MAIN_HAND) {
      for (int i = 4; i >= 0; i--) {
        if (!inventory.getStackInSlot(i).isEmpty()) {
          ItemStack extracted = inventory.extractItem(i, inventory.getStackInSlot(i).getCount(), false);
          // TODO: What did these last parameters do?
          InventoryHelper.spawnItemStack(world, player.posX, player.posY + 1, player.posZ, extracted); // false, extracted, 0, -1);
          markDirty();
          updatePacketViaState();
          pickupDelay = 40;
          return true;
        }
      }
    }
    return true;
  }

  // TODO: This should be in the block

/*  @Override
  public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newState) {
    if (oldState.getBlock() == newState.getBlock() && newState.getBlock() instanceof BonfireBlock) return false;

    return super.shouldRefresh(world, pos, oldState, newState);
  }*/

  // TODO: Should be handled by the block

/*  @Override
  public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull BlockState state, PlayerEntity player) {
    if (!world.isRemote) {
      Util.spawnInventoryInWorld(world, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, inventory);
    }
  }*/

  public int getBurnTime() {
    return burnTime;
  }

  @Override
  public void tick() {
    // Potentially update from stuff below
    resolveLastIngredients();
    if (lastUsedIngredients != null && !lastUsedIngredients.isEmpty() && ItemHandlerUtil.isEmpty(inventory_storage) && ItemHandlerUtil.isEmpty(inventory)) {
      TileEntity te = world.getTileEntity(getPos().down());
      if (te != null) {
        te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent((cap) -> {
          IntArrayList slots = new IntArrayList();
          for (Ingredient ingredient : lastUsedIngredients) {
            for (int i = 0; i < cap.getSlots(); i++) {
              if (ingredient.test(cap.getStackInSlot(i))) {
                slots.add(i);
                break;
              }
            }
          }
          List<ItemStack> temp = ItemHandlerUtil.getItemsInSlots(cap, slots, true);
          if (temp.size() == 5) {
            temp = ItemHandlerUtil.getItemsInSlots(cap, slots, false);
            for (int i = 0; i < 5; i++) {
              ItemStack stack = temp.get(i);
              inventory.setStackInSlot(i, stack);
            }
          }
        });
      }
    }

    if (ticker % 10 == 0) {
      AxisAlignedBB bounds = bounding.offset(getPos());
      BlockPos start = new BlockPos(bounds.minX, bounds.minY, bounds.minZ);
      BlockPos stop = new BlockPos(bounds.maxX, bounds.maxY, bounds.maxZ);
      boolean fire = false;
      for (BlockPos pos : BlockPos.getAllInBoxMutable(start, stop)) {
        if (world.getBlockState(pos).getBlock() == Blocks.FIRE) {
          fire = true;
          if (!world.getBlockState(pos.down()).getBlock().isFireSource(world.getBlockState(pos.down()), world, pos.down(), Direction.UP)) {
            for (int i = 0; i < 1 + Util.rand.nextInt(3); i++) {
              world.getBlockState(pos).randomTick(world, pos, Util.rand);
            }
          }
        }
      }
      if (fire) {
        startRitual(null);
      }
    }

    this.ticker += 1.0f;
    if (pickupDelay > 0) {
      pickupDelay--;
    }
    //Spawn the Ignite flame particle
    if (world.isRemote && this.doBigFlame) {
      if (ritualEntity instanceof IColdRitual) {
        for (int i = 0; i < 40; i++) {
          ParticleUtil.spawnParticleFiery(world, getPos().getX() + 0.125f + 0.75f * random.nextFloat(), getPos().getY() + 0.75f + 0.5f * random.nextFloat(),
              getPos().getZ() + 0.125f + 0.75f * random.nextFloat(), 0.03125f * (random.nextFloat() - 0.5f), 0.125f * random.nextFloat(),
              0.03125f * (random.nextFloat() - 0.5f), 63.0f, 119.0f, 209.0f, 0.75f, 9.0f + 9.0f * random.nextFloat(), 40);
        }
      } else {
        for (int i = 0; i < 40; i++) {
          ParticleUtil.spawnParticleFiery(world, getPos().getX() + 0.125f + 0.75f * random.nextFloat(), getPos().getY() + 0.75f + 0.5f * random.nextFloat(),
              getPos().getZ() + 0.125f + 0.75f * random.nextFloat(), 0.03125f * (random.nextFloat() - 0.5f), 0.125f * random.nextFloat(),
              0.03125f * (random.nextFloat() - 0.5f), 255.0f, 224.0f, 32.0f, 0.75f, 9.0f + 9.0f * random.nextFloat(), 40);
        }
      }
    }
    if (doBigFlame) {
      if (burnTime != 0) {
        BonfireBlock.setState(true, world, pos);
      }
      doBigFlame = false;
      markDirty();
      if (!world.isRemote) {
        updatePacketViaState();
      }
    }

    if (burnTime > 0) {
      burnTime--;

      boolean burning = burnTime > 0;

      if ((ritualEntity != null && !ritualEntity.isAlive()) && craftingResult.isEmpty()) {
        burning = false;
      }

      if (burning && getTicker() % 20.0f == 0 && random.nextDouble() < 0.05 && !(ritualEntity instanceof IColdRitual)) {
        meltNearbySnow();
      }

      if (!burning || burnTime == 0) {
        burnTime = 0;
        BonfireBlock.setState(false, world, pos);
        //Check if it is a ritual, if so try and see if it has new ritual fuel.
        if (this.craftingResult.isEmpty() && this.lastRitualUsed != null) {
          List<ItemStack> stacks = new ArrayList<>();
          for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i).copy();
            stack.setCount(1);
            stacks.add(stack);
          }
          if (ListUtil.matchesIngredients(stacks, this.lastRitualUsed.getIngredients())) {
            lastRitualUsed.doEffect(world, getPos());
            burning = true;
            this.burnTime = this.lastRitualUsed.getDuration();
            this.doBigFlame = true;
            for (int i = 0; i < inventory.getSlots(); i++) {
              inventory.extractItem(i, 1, false);
            }
            markDirty();
            if (!world.isRemote) {
              updatePacketViaState();
            }
          }
        }

        //Spawn item if crafting recipe
        if (!world.isRemote && !this.craftingResult.isEmpty()) {
          ItemStack result = this.craftingResult.copy();
          if (this.lastRecipeUsed != null) {
            this.lastRecipeUsed.postCraft(result, inventory_storage, this);
          }

          ItemEntity item = new ItemEntity(world, getPos().getX() + 0.5, getPos().getY() + 1, getPos().getZ() + 0.5, result);
          item.setCustomName(new StringTextComponent("bonfire"));
          ItemUtil.spawnItem(world, item);
          XPUtil.spawnXP(world, getPos(), this.craftingXP);
          this.craftingResult = ItemStack.EMPTY;
          clearStorage();
        }
      }
      if (burning) {
        //Spawn Fire particles
        if (world.isRemote) {
          for (int i = 0; i < 2; i++) {
            if (ritualEntity instanceof FrostLandsRitualEntity) {
              ParticleUtil
                  .spawnParticleFiery(world, getPos().getX() + 0.3125f + 0.375f * Util.rand.nextFloat(), getPos().getY() + 0.625f + 0.375f * Util.rand.nextFloat(),
                      getPos().getZ() + 0.3125f + 0.375f * Util.rand.nextFloat(), 0.03125f * (Util.rand.nextFloat() - 0.5f), 0.125f * Util.rand.nextFloat(),
                      0.03125f * (Util.rand.nextFloat() - 0.5f), 90.0f, 134.0f, 204.0f, 0.75f, 7.0f + 7.0f * Util.rand.nextFloat(), 40);
            } else {
              ParticleUtil
                  .spawnParticleFiery(world, getPos().getX() + 0.3125f + 0.375f * Util.rand.nextFloat(), getPos().getY() + 0.625f + 0.375f * Util.rand.nextFloat(),
                      getPos().getZ() + 0.3125f + 0.375f * Util.rand.nextFloat(), 0.03125f * (Util.rand.nextFloat() - 0.5f), 0.125f * Util.rand.nextFloat(),
                      0.03125f * (Util.rand.nextFloat() - 0.5f), 255.0f, 96.0f, 32.0f, 0.75f, 7.0f + 7.0f * Util.rand.nextFloat(), 40);
            }
          }
        }
      }
      if (!burning || burnTime == 0) {
        burnTime = 0;
        markDirty();
        if (!world.isRemote) {
          updatePacketViaState();
        }
      }
    }

    pickupItem();
  }

  private void meltNearbySnow() {
    List<BlockPos> nearbySnowOrIce = new ArrayList<>();
    for (int x = -4; x < 5; x++) {
      for (int z = -4; z < 5; z++) {
        BlockPos topBlockPos = world.getHeight(Heightmap.Type.WORLD_SURFACE, new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z));
        topBlockPos = topBlockPos.subtract(new Vec3i(0, 1, 0));
        BlockState topBlockState = world.getBlockState(topBlockPos);
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
      List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class,
          new AxisAlignedBB(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX() + 1, getPos().getY() + 1, getPos().getZ() + 1));
      for (ItemEntity item : items) {
        ItemStack stack = item.getItem();
        // TODO: Change this to entity data
        if (item.getCustomName().getString().equalsIgnoreCase("bonfire")) {
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
        if (!world.isRemote) {
          updatePacketViaState();
        }
      }
    }
  }

  private void insertItemFromPlayerInventory(ItemStack stack, PlayerEntity player, int slot) {
    for (int i = 0; i < 5; i++) {
      if (inventory.getStackInSlot(i).isEmpty()) {
        ItemStack toInsert = stack.copy();
        toInsert.setCount(1);
        inventory.insertItem(i, toInsert, false);
        stack.setCount(stack.getCount() - 1);
        //todo: check if the item goes away
        markDirty();
        if (!world.isRemote) {
          updatePacketViaState();
        }
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