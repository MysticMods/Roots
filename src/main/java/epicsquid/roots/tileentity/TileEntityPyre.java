package epicsquid.roots.tileentity;

import com.google.common.collect.Sets;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.particle.RangeParticle;
import epicsquid.mysticallib.particle.RenderUtil;
import epicsquid.mysticallib.tile.TileBase;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.block.BlockPyre;
import epicsquid.roots.config.RitualConfig;
import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualFrostLands;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.network.fx.MessagePyreBigFlameFX;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.recipe.PyreCraftingRecipe;
import epicsquid.roots.ritual.*;
import epicsquid.roots.util.IngredientWithStack;
import epicsquid.roots.util.ItemHandlerUtil;
import epicsquid.roots.util.XPUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class TileEntityPyre extends TileBase implements ITickable, RenderUtil.IRanged {
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
	private EntityRitualBase ritualEntity = null;
	private Block block = null;
	private UUID lastPlayerId = null;
	
	private boolean isBurning = false;
	
	private PyreCraftingRecipe currentRecipe = null;
	private RitualBase currentRitual = null;
	
	public ItemStackHandler inventory = new ItemStackHandler(5) {
		@Override
		protected void onContentsChanged(int slot) {
			currentRecipe = null;
			currentRitual = null;
			TileEntityPyre.this.markDirty();
			if (!world.isRemote) {
				TileEntityPyre.this.world.updateComparatorOutputLevel(pos, getBlock());
				TileEntityPyre.this.updatePacketViaState();
			}
		}
	};
	
	public ItemStackHandler inventory_storage = new ItemStackHandler(5);
	
	public void clearStorage() {
		for (int i = 0; i < 5; i++) {
			inventory_storage.setStackInSlot(i, ItemStack.EMPTY);
		}
	}
	
	protected Block getBlock() {
		if (block == null) {
			block = world.getBlockState(pos).getBlock();
		}
		return block;
	}
	
	public TileEntityPyre() {
		super();
		GameRegistry.registerTileEntity(TileEntityPyre.class, new ResourceLocation(Roots.MODID, "tile_entity_bonfire"));
	}
	
	protected void readFromNBTLegacy(NBTTagCompound tag) {
	}
	
	@Nonnull
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setTag("handler", inventory.serializeNBT());
		tag.setInteger("burnTime", burnTime);
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
		if (tag.hasKey("craftingResult")) {
			craftingResult = new ItemStack(tag.getCompoundTag("craftingResult"));
		} else {
			craftingResult = ItemStack.EMPTY;
		}
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
	
	private void validateEntity() {
		if (ritualEntity == null) return;
		
		if (ritualEntity.isDead) return;
		
		int lifetime = ritualEntity.getDataManager().get(EntityRitualBase.lifetime);
		
		if (lifetime <= 0) {
			ritualEntity.setDead();
		}
		
		if (burnTime <= 0) {
			ritualEntity.setDead();
		}
	}
	
	public boolean startRitual(@Nullable UUID playerId, boolean refire) {
		if (playerId == null || lastPlayerId == null) {
			return startRitual((EntityPlayer) null, refire);
		} else {
			return startRitual(Objects.requireNonNull(world.getMinecraftServer()).getPlayerList().getPlayerByUUID(lastPlayerId), refire);
		}
	}
	
	public boolean startRitual(@Nullable EntityPlayer player, boolean refire) {
		RitualBase ritual = RitualRegistry.getRitual(this, player);
		
		if (ritual != null && !ritual.isDisabled()) {
			if (ritual.canFire(this, player, refire)) {
				this.burnTime = ritual.getDuration();
/*        if (ritualEntity != null && !ritualEntity.isDead) {
          ritualEntity.setDead();
        }*/
				ritualEntity = ritual.doEffect(world, pos, player);
				this.lastRitualUsed = ritual;
				this.lastRecipeUsed = null;
				this.lastUsedIngredients = ritual.getIngredients();
				this.doBigFlame = true;
				this.isBurning = true;
				if (!world.isRemote) {
					List<ItemStack> transformers = new ArrayList<>();
					for (int i = 0; i < inventory.getSlots(); i++) {
						transformers.add(inventory.extractItem(i, 1, false));
					}
					for (ItemStack stack : ritual.recipe.transformIngredients(transformers, this)) {
						ItemUtil.spawnItem(world, getPos().add(1, 0, -1), stack);
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
			this.isBurning = true;
			List<ItemStack> transformers = new ArrayList<>();
			for (int i = 0; i < inventory.getSlots(); i++) {
				ItemStack item = inventory.extractItem(i, 1, false);
				transformers.add(item);
				inventory_storage.insertItem(i, item, false);
			}
			if (!world.isRemote) {
				for (ItemStack stack : recipe.transformIngredients(transformers, this)) {
					ItemUtil.spawnItem(world, getPos().add(1, 0, -1), stack);
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
	
	public static Ingredient fireStarters = null;
	
	public static Ingredient getFireStarters() {
		if (fireStarters == null) {
			fireStarters = new OreIngredient("pyreFireStarters");
		}
		return fireStarters;
	}
	
	@Override
	public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem(hand);
		
		if (heldItem.getItem() == ModItems.firestarter) {
			return false;
		}
		
		if (ModItems.knives.contains(heldItem.getItem()) && player.isSneaking()) {
			if (world.isRemote) {
				toggleShowRange();
			} else {
				world.playSound(null, getPos(), ModSounds.Events.PYRE_ADD_ITEM, SoundCategory.PLAYERS, 1f, 1f);
			}
			return true;
		}
		
		if (world.isRemote) {
			return true;
		}
		
		if (!heldItem.isEmpty()) {
			boolean extinguish = false;
			IFluidHandlerItem cap = heldItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			if (cap != null) {
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
								if (heldItem.getItem() instanceof ItemBucket) {
									player.setHeldItem(hand, heldItem.getItem().getContainerItem(heldItem));
									((EntityPlayerMP) player).sendAllContents(player.openContainer, player.openContainer.getInventory());
								}
							}
							break;
						}
					}
				}
			}
			// TODO: Make this a configurable array of items or extensible classes
			if (getFireStarters().test(heldItem)) {
				boolean result = startRitual(player, false);
				this.lastPlayerId = player.getUniqueID();
				if (result && heldItem.isItemStackDamageable() /*&& !player.isCreative()*/) {
					heldItem.damageItem(1, player);
				}
				return result;
			} else if (extinguish && burnTime > 0) {
				burnTime = 0;
				if (ritualEntity != null) {
					ritualEntity.setDead();
				}
				isBurning = false;
				BlockPyre.setState(false, world, pos);
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
							world.playSound(null, getPos(), ModSounds.Events.PYRE_ADD_ITEM, SoundCategory.PLAYERS, 1f, 1f);
							return true;
						}
					}
				}
			}
		}
		if (player.isSneaking() && heldItem.isEmpty() && hand == EnumHand.MAIN_HAND) {
			resolveLastIngredients();
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
					ItemUtil.spawnItem(world, player.posX, player.posY + 1, player.posZ, false, extracted, 0, -1);
					pickupDelay = 40;
					markDirty();
					updatePacketViaState();
					world.playSound(null, getPos(), ModSounds.Events.PYRE_REMOVE_ITEM, SoundCategory.PLAYERS, 1f, 1f);
					return true;
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		if (oldState.getBlock() instanceof BlockPyre && newState.getBlock() instanceof BlockPyre) {
			return false;
		}
		
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
	
	public void ongoingFlame() {
		if (world.isRemote) {
			IBlockState state = world.getBlockState(getPos());
			if (state.getBlock() instanceof BlockPyre) {
				if (state.getValue(BlockPyre.BURNING)) {
					for (int i = 0; i < 2; i++) {
						if (ritualEntity instanceof EntityRitualFrostLands) {
							ParticleUtil.spawnParticleFiery(world, getPos().getX() + 0.3125f + 0.375f * Util.rand.nextFloat(), getPos().getY() + 0.625f + 0.375f * Util.rand.nextFloat(), getPos().getZ() + 0.3125f + 0.375f * Util.rand.nextFloat(), 0.03125f * (Util.rand.nextFloat() - 0.5f), 0.125f * Util.rand.nextFloat(), 0.03125f * (Util.rand.nextFloat() - 0.5f), 90.0f, 134.0f, 204.0f, 0.75f, 7.0f + 7.0f * Util.rand.nextFloat(), 40);
						} else {
							ParticleUtil.spawnParticleFiery(world, getPos().getX() + 0.3125f + 0.375f * Util.rand.nextFloat(), getPos().getY() + 0.625f + 0.375f * Util.rand.nextFloat(), getPos().getZ() + 0.3125f + 0.375f * Util.rand.nextFloat(), 0.03125f * (Util.rand.nextFloat() - 0.5f), 0.125f * Util.rand.nextFloat(), 0.03125f * (Util.rand.nextFloat() - 0.5f), 255.0f, 96.0f, 32.0f, 0.75f, 7.0f + 7.0f * Util.rand.nextFloat(), 40);
						}
					}
				}
			}
		}
	}
	
	public void bigFlame() {
		if (this.doBigFlame) {
			PacketHandler.sendToAllTracking(new MessagePyreBigFlameFX(getPos(), ritualEntity instanceof IColdRitual), this);
			doBigFlame = false;
		}
	}
	
	public void refillInventory() {
		if (!world.isRemote) {
			resolveLastIngredients();
			if (lastUsedIngredients != null && !lastUsedIngredients.isEmpty() && ItemHandlerUtil.isEmpty(inventory)) {
				TileEntity te = world.getTileEntity(getPos().down());
				if (te != null) {
					IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
					if (cap != null) {
						Int2ObjectOpenHashMap<IngredientWithStack> slotsToIngredient = new Int2ObjectOpenHashMap<>();
						int amount = 0;
						for (Ingredient ingredient : lastUsedIngredients) {
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
	
	public boolean tickFire() {
		boolean found = false;
		if (!world.isRemote) {
			if (ticker % 10 == 0 && !isBurning) {
				AxisAlignedBB bounds = bounding.offset(getPos());
				BlockPos start = new BlockPos(bounds.minX, bounds.minY, bounds.minZ);
				BlockPos stop = new BlockPos(bounds.maxX, bounds.maxY, bounds.maxZ);
				for (BlockPos.MutableBlockPos pos : BlockPos.getAllInBoxMutable(start, stop)) {
					if (world.getBlockState(pos).getBlock() == Blocks.FIRE) {
						found = true;
						if (!world.getBlockState(pos.down()).getBlock().isFireSource(world, pos.down(), EnumFacing.UP)) {
							world.setBlockState(pos, Blocks.AIR.getDefaultState());
						}
						break;
					}
				}
			}
		}
		return found;
	}
	
	@Override
	public void update() {
		this.ticker += 1.0f;
		
		// Potentially update from stuff below
		if (!world.isRemote) {
			refillInventory();
			boolean restart = tickFire();
			boolean burning = false;
			
			if (pickupDelay > 0) {
				pickupDelay--;
			}
			//Spawn the Ignite flame particle
			
			if (doBigFlame) {
				bigFlame();
				markDirty();
				if (burnTime != 0) {
					BlockPyre.setState(true, world, pos);
				}
				updatePacketViaState();
			}
			
			validateEntity();
			
			burning = burnTime > 0;
			
			boolean shouldUpdate = false;
			
			if (burnTime > 0) {
				burnTime--;
				markDirty();
				shouldUpdate = true;
				
				if ((ritualEntity != null && ritualEntity.isDead) && craftingResult.isEmpty()) {
					burning = false;
				}
				
				if (burning && getTicker() % 20.0f == 0 && Util.rand.nextDouble() < 0.05 && !(ritualEntity instanceof IColdRitual)) {
					meltNearbySnow();
				}
				
				if (!burning || burnTime == 0) {
					burnTime = 0;
					isBurning = false;
					spawnCraftResult();
					markDirty();
					if (!startRitual(restart ? null : lastPlayerId, true)) {
						BlockPyre.setState(false, world, pos);
					}
					restart = false;
					updatePacketViaState();
					shouldUpdate = false;
				}
			}
			
			if (shouldUpdate) {
				updatePacketViaState();
			}
			
			pickupItem();
			if (restart && !burning) {
				startRitual((UUID) null, true);
			}
		} else {
			ongoingFlame();
		}
	}
	
	public void spawnCraftResult() {
		//Spawn item if crafting recipe
		if (!world.isRemote && !this.craftingResult.isEmpty()) {
			ItemStack result = this.craftingResult.copy();
			if (this.lastRecipeUsed != null) {
				this.lastRecipeUsed.postCraft(result, inventory_storage, this);
			}
			
			EntityItem item = new EntityItem(world, getPos().getX() + 0.5, getPos().getY() + 1, getPos().getZ() + 0.5, result);
			item.setCustomNameTag("pyre");
			ItemUtil.spawnItem(world, item);
			XPUtil.spawnXP(world, getPos(), this.craftingXP);
			this.craftingResult = ItemStack.EMPTY;
			clearStorage();
		}
	}
	
	public List<ItemStack> getInventory() {
		List<ItemStack> stacks = new ArrayList<>();
		for (int i = 0; i < inventory.getSlots(); i++) {
			ItemStack stack = inventory.getStackInSlot(i).copy();
			stack.setCount(1);
			stacks.add(stack);
		}
		return stacks;
	}
	
	private void meltNearbySnow() {
		if (!world.isRemote) {
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
			BlockPos posToMelt = nearbySnowOrIce.get(nearbySnowOrIce.size() > 1 ? Util.rand.nextInt(nearbySnowOrIce.size() - 1) : 0);
			if (world.getBlockState(posToMelt).getMaterial() == Material.SNOW) {
				world.setBlockState(posToMelt, Blocks.AIR.getDefaultState());
			} else if (world.getBlockState(posToMelt).getMaterial() == Material.ICE) {
				world.setBlockState(posToMelt, Blocks.WATER.getDefaultState());
			}
		}
	}
	
	private void pickupItem() {
		if ((int) this.ticker % 20 == 0 && pickupDelay == 0) {
			List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class,
					new AxisAlignedBB(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX() + 1, getPos().getY() + 1, getPos().getZ() + 1));
			for (EntityItem item : items) {
				ItemStack stack = item.getItem();
				if (item.getCustomNameTag().equalsIgnoreCase("pyre")) {
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
				break;
			}
		}
	}
	
	@Nullable
	public RitualBase getLastRitualUsed() {
		return lastRitualUsed;
	}
	
	@Nullable
	public PyreCraftingRecipe getLastRecipeUsed() {
		return lastRecipeUsed;
	}
	
	@Nullable
	public RitualBase getCurrentRitual() {
		return RitualRegistry.getRitual(this, null);
	}
	
	@Nullable
	public PyreCraftingRecipe getCurrentRecipe() {
		List<ItemStack> stacks = new ArrayList<>();
		for (int i = 0; i < inventory.getSlots(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			stacks.add(stack);
		}
		if (lastRecipeUsed != null && lastRecipeUsed.matches(stacks)) {
			if (currentRecipe != lastRecipeUsed) {
				currentRecipe = lastRecipeUsed;
			}
		} else if (lastRecipeUsed != null && lastRecipeUsed == currentRecipe) {
			currentRecipe = null;
		}
		if (currentRecipe == null) {
			currentRecipe = ModRecipes.getCraftingRecipe(stacks);
		}
		return currentRecipe;
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
	
	private boolean showingRange = false;
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isShowingRange() {
		return showingRange;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void toggleShowRange() {
		setShowingRange(!isShowingRange());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void setShowingRange(boolean showingRange) {
		this.showingRange = showingRange;
		if (showingRange) {
			Minecraft.getMinecraft().effectRenderer.addEffect(new RangeParticle<>(this));
		}
	}
	
	private static AxisAlignedBB nullBox = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
	
	private static Set<Class<?>> excluded = Sets.newHashSet(RitualGroveSupplication.class, RitualFireStorm.class);
	// Summon Creatures
	
	@Nonnull
	@Override
	public AxisAlignedBB getBounds() {
		RitualBase ritual = RitualRegistry.getRitual(this, null);
		if (ritual == null) {
			if (getLastRitualUsed() != null) {
				ritual = getLastRitualUsed();
			}
		}
		if (ritual == null) { // Shouldn't happen*/
			setShowingRange(false);
			return nullBox;
		}
		
		if (excluded.contains(ritual.getClass())) {
			setShowingRange(false);
			return nullBox;
		}
		
		AxisAlignedBB bb = ritual.getBoundingBox();
		if (bb == null) {
			setShowingRange(false);
			return nullBox;
		}
		
		return bb.offset(getPos());
	}
	
	public void setLastPlayerId(UUID lastPlayerId) {
		this.lastPlayerId = lastPlayerId;
	}
}