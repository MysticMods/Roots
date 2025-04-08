package mysticmods.roots.blockentity;

import mysticmods.roots.action.StartRitualAction;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.blockentity.ClientTickBlockEntity;
import mysticmods.roots.api.blockentity.InventoryBlockEntity;
import mysticmods.roots.api.blockentity.RefillProvider;
import mysticmods.roots.api.blockentity.ServerTickBlockEntity;
import mysticmods.roots.api.recipe.ConditionResult;
import mysticmods.roots.api.recipe.RecipeUtil;
import mysticmods.roots.api.recipe.UnlockResult;
import mysticmods.roots.api.recipe.inventory.RecipeInventory;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.IRitualInstance;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.block.PyreBlock;
import mysticmods.roots.blockentity.template.UseDelegatedBlockEntity;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.*;
import mysticmods.roots.recipe.pyre.PyreCrafting;
import mysticmods.roots.recipe.pyre.PyreInventory;
import mysticmods.roots.recipe.pyre.PyrePedestalCrafting;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import mysticmods.roots.util.ItemUtil;
import mysticmods.roots.util.RitualPositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PyreBlockEntity extends UseDelegatedBlockEntity implements ClientTickBlockEntity, ServerTickBlockEntity, InventoryBlockEntity, RefillProvider {
  private static BoundingBox PYRE_BOUNDS;

  public static BoundingBox getPyreBoundingBox() {
    if (PYRE_BOUNDS == null) {
      PYRE_BOUNDS = new BoundingBox(-ConfigManager.PYRE_BOUNDS_X.get(), -ConfigManager.PYRE_BOUNDS_Y.get(), -ConfigManager.PYRE_BOUNDS_Z.get(), ConfigManager.PYRE_BOUNDS_X.get() + 1, ConfigManager.PYRE_BOUNDS_Y.get() + 1, ConfigManager.PYRE_BOUNDS_Z.get() + 1);
    }

    return PYRE_BOUNDS;
  }

  private final PyreInventory inventory = new PyreInventory() {
    @Override
    protected void onContentsChanged(int slot) {
      if (PyreBlockEntity.this.hasLevel() && !PyreBlockEntity.this.getLevel().isClientSide()) {
        PyreBlockEntity.this.getLevel().invalidateCapabilities(PyreBlockEntity.this.getBlockPos());
        PyreBlockEntity.this.revalidateRecipe();
        PyreBlockEntity.this.updateViaState();
      }
    }
  };
  private final PyreCrafting playerlessCrafting = new PyreCrafting(inventory, this, null);
  private PyrePedestalCrafting playerlessPedestalCrafting;
  private final List<ItemStack> storedItems = new ArrayList<>();
  private RecipeHolder<PyreRecipe> lastRecipe = null;
  private RecipeHolder<PyreRecipe> cachedRecipe = null;
  private Ritual currentRitual = null;
  private int lifetime = -1;
  private Player lastPlayer;
  private UUID lastUuid;

  private BlockCapabilityCache<IItemHandler, @org.jetbrains.annotations.Nullable Direction> capabilityCache;
  private RitualPositionCache cache;

  public PyreBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
  }

  public PyreBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
    super(ModBlockEntities.PYRE.get(), pWorldPosition, pBlockState);
  }

  private void setCurrentRitual(Ritual ritual) {
    this.currentRitual = ritual;
    this.refreshRitualCache();
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult ray, InteractionHand hand, ItemStack inHand) {
    // This is a very specific hack.
    if (inHand.is(ModItems.FIRE_STARTER.get())) {
      return InteractionResult.PASS;
    }
    if (level.isClientSide()) {
      return InteractionResult.CONSUME;
    }

    if (currentRitual != ModRituals.CRAFTING.get() && (lifetime > 0 || getBlockState().getValue(PyreBlock.BURNING))) {
      Optional<IFluidHandlerItem> optFluid = FluidUtil.getFluidHandler(inHand);
      if (optFluid.isPresent()) {
        IFluidHandlerItem fluidHandler = optFluid.get();
        FluidStack toDrain = new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME);
        if (FluidStack.isSameFluidSameComponents(fluidHandler.drain(toDrain, IFluidHandler.FluidAction.SIMULATE), toDrain)) {
          stopRitual();
          if (!player.isCreative()) {
            fluidHandler.drain(toDrain, IFluidHandler.FluidAction.EXECUTE);
            player.setItemInHand(hand, fluidHandler.getContainer());
          }
          return InteractionResult.SUCCESS;
        }
      }
    }

    // Once the ritual has started it doesn't matter what's happening
    if (inHand.isEmpty() && !player.isCrouching()) {
      // extract
      ItemStack popped = inventory.pop();
      if (!popped.isEmpty()) {
        ItemUtil.Spawn.spawnItem(level, getBlockPos(), popped);
      }
    } else if (inHand.isEmpty() && player.isCrouching()) {
      // Try to refill
      if (lastRecipe != null) {
        RecipeUtil.refillRecipeFromPlayer((ServerPlayer) player, lastRecipe.value(), inventory);
      }
    } else if (inHand.is(RootsTags.Items.PYRE_ACTIVATION)) {
      InteractionResult result = light(player);
      if (result.indicateItemUse() && inHand.isDamageableItem() && !player.isCreative()) {
        inHand.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
      }
      return result;
    } else {
      // insert
      ItemStack result = inventory.insert(inHand);
      // ??? TODO
      // it was result, false
      if (inHand.equals(result)) {
        return InteractionResult.PASS;
      }
      if (!player.isCreative()) {
        player.setItemInHand(hand, result);
      }
    }

    return InteractionResult.SUCCESS;
  }

  public InteractionResult light(Player player) {
    if (cachedRecipe == null) {
      revalidateRecipe();
    }
    if (cachedRecipe != null && cachedRecipe.value().matches(playerlessCrafting, level)) {
      Ritual newRitual = cachedRecipe.value().getRitual();
      if (newRitual == null) {
        setCurrentRitual(ModRituals.CRAFTING.get());
      } else {
        setCurrentRitual(newRitual);
      }

      // TODO: Provider better feedback to the player
      ConditionResult result = cachedRecipe.value().checkConditions(level, player, getPyreBoundingBox(), getBlockPos());
      if (result.anyFailed()) {
        RootsAPI.LOG.info("Conditions failed.");
        result.failedLevelConditions().forEach(o -> RootsAPI.LOG.info("Failed: {}", o.getDescriptionId()));
        result.failedPlayerConditions().forEach(o -> RootsAPI.LOG.info("Failed: {}", o.getDescriptionId()));
        result.report(player);
        // Needs to be a success or it sets things on fire
        return InteractionResult.SUCCESS_NO_ITEM_USED;
      }
      UnlockResult failedGrants = cachedRecipe.value().checkUnlocks(level, (ServerPlayer) player);
      if (failedGrants.anyFailed() && !cachedRecipe.value().hasOutput(level.registryAccess())) {
        RootsAPI.LOG.info("Grants failed and recipe has no output");
        failedGrants.failedUnlocks().forEach(o -> RootsAPI.LOG.info("Failed unlock {}", o));
        failedGrants.report();
        return InteractionResult.SUCCESS_NO_ITEM_USED;
      }

      PyreCrafting playerCrafting = new PyreCrafting(inventory, this, player);
      lastRecipe = cachedRecipe;
      storedItems.clear();
      if (currentRitual == ModRituals.CRAFTING.get()) {
        storedItems.addAll(cachedRecipe.value()
            .assembleOutputs(playerCrafting, level.getRandom(), level.registryAccess(), null));
      }
      storedItems.removeIf(ItemStack::isEmpty);
      // process
      List<ItemStack> processed = cachedRecipe.value().process(inventory.getItemsAndClear());
      processed = outputAdjacent(processed);
      for (ItemStack stack : processed) {
        ItemUtil.Spawn.spawnItem(level, player.blockPosition(), stack);
      }
      cachedRecipe = null;
      startRitual(player);
      setChanged();
      updateViaState();

      return InteractionResult.SUCCESS;
    }

    return InteractionResult.SUCCESS_NO_ITEM_USED;
  }

  public void startRitual(Player player) {
    this.lastPlayer = player;
    this.lastUuid = null;
    if (currentRitual != null) {
      this.lifetime = currentRitual.getDuration();
      this.refreshRitualCache();
      this.currentRitual.starts(getLevel(), getBlockPos(), getBlockState(), this, getRandom());
      StartRitualAction.Context context = new StartRitualAction.Context((ServerLevel) getLevel(), (ServerPlayer) player, IRitualInstance.of(currentRitual), this);
      ModActions.START_RITUAL.get().accept(context);
    } else {
      RootsAPI.LOG.error("tried to start a ritual but the ritual is null");
    }
  }

  private void refreshRitualCache() {
    if (this.currentRitual == null) {
      this.cache = null;
      return;
    }

    BlockPos p = getBlockPos();

    if (cache == null || !this.cache.getPosition().equals(p)) {
      BoundingBox bb = currentRitual.getBoundingBox().moved(p.getX(), p.getY(), p.getZ());
      this.cache = new RitualPositionCache(p, bb, new ArrayList<>(BlockPos.betweenClosedStream(bb)
          .map(BlockPos::immutable).toList()));
    }
  }

  @Nullable
  public RitualPositionCache getCache() {
    return cache;
  }

  public RecipeHolder<PyreRecipe> getCachedRecipe() {
    return cachedRecipe;
  }

  public RecipeHolder<PyreRecipe> getLastRecipe() {
    return lastRecipe;
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider provider) {
    super.onDataPacket(net, pkt, provider);
    CompoundTag tag = pkt.getTag();
    if (tag != null) {
      revalidateRecipe();
      loadAdditional(tag, provider);
    }
  }

  protected void revalidateRecipe() {
    if (getLevel() == null) {
      return;
    }

    boolean changed = false;

    if (cachedRecipeId != null) {
      cachedRecipe = ResolvedRecipes.PYRE.getRecipe(getLevel(), cachedRecipeId);
      if (cachedRecipe != null) {
        changed = true;
        cachedRecipeId = null;
      }
    }
    if (lastRecipeId != null) {
      lastRecipe = ResolvedRecipes.PYRE.getRecipe(getLevel(), lastRecipeId);
      if (lastRecipe != null) {
        changed = true;
        lastRecipeId = null;
      }
    }

    boolean matched = false;
    if (cachedRecipe == null) {
      if (lastRecipe != null && lastRecipe.value().matches(playerlessCrafting, getLevel())) {
        cachedRecipe = lastRecipe;
        matched = true;
        changed = true;
      } else {
        cachedRecipe = ResolvedRecipes.PYRE.findRecipe(playerlessCrafting, getLevel());
        changed = true;
      }
    }

    if (cachedRecipe != null) {
      // only test once
      if (!matched && !cachedRecipe.value().matches(playerlessCrafting, getLevel())) {
        cachedRecipe = null;
      }
    }

    if (changed && !getLevel().isClientSide()) {
      setChanged();
      updateViaState();
    }
  }

  @Override
  protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
    super.saveAdditional(pTag, provider);
    if (cachedRecipe != null) {
      pTag.putString("cached_recipe", cachedRecipe.id().toString());
    }
    if (lastRecipe != null) {
      pTag.putString("last_recipe", lastRecipe.id().toString());
    }
    if (currentRitual != null) {
      pTag.putString("current_ritual", RootsRegistries.RITUALS.getKey(currentRitual).toString());
    }

    ListTag storedItems = new ListTag();
    for (ItemStack stack : this.storedItems) {
      if (!stack.isEmpty()) {
        storedItems.add(stack.save(provider, new CompoundTag()));
      }
    }
    if (!storedItems.isEmpty()) {
      pTag.put("stored_items", storedItems);
    }
    pTag.putInt("lifetime", lifetime);
    pTag.put("inventory", inventory.serializeNBT(provider));
    if (lastPlayer != null) {
      pTag.putUUID("last_player", lastPlayer.getUUID());
    } else if (lastUuid != null) {
      pTag.putUUID("last_player", lastUuid);
    }
  }

  private ResourceLocation cachedRecipeId = null;
  private ResourceLocation lastRecipeId = null;

  @Override
  public void loadAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
    super.loadAdditional(pTag, provider);
    cachedRecipeId = null;
    if (pTag.contains("cached_recipe", Tag.TAG_STRING)) {
      cachedRecipeId = ResourceLocation.parse(pTag.getString("cached_recipe"));
    }
    lastRecipeId = null;
    if (pTag.contains("last_recipe", Tag.TAG_STRING)) {
      lastRecipeId = ResourceLocation.parse(pTag.getString("last_recipe"));
    }
    if (pTag.contains("inventory", Tag.TAG_COMPOUND)) {
      inventory.deserializeNBT(provider, pTag.getCompound("inventory"));
    }
    if (pTag.contains("current_ritual", Tag.TAG_STRING)) {
      ResourceLocation ritualId = ResourceLocation.parse(pTag.getString("current_ritual"));
      setCurrentRitual(RootsRegistries.RITUALS.get(ritualId));
    } else {
      setCurrentRitual(null);
    }
    if (pTag.contains("lifetime", Tag.TAG_INT)) {
      lifetime = pTag.getInt("lifetime");
    }
    storedItems.clear();
    if (pTag.contains("stored_items", Tag.TAG_LIST)) {
      ListTag incomingStoredItems = pTag.getList("stored_items", Tag.TAG_COMPOUND);
      for (int i = 0; i < incomingStoredItems.size(); i++) {
        ItemStack.parse(provider, incomingStoredItems.getCompound(i)).ifPresent(storedItems::add);
      }
    }
    if (pTag.hasUUID("last_player")) {
      lastUuid = pTag.getUUID("last_player");
      if (getLevel() != null) {
        lastPlayer = getLevel().getPlayerByUUID(lastUuid);
      }
    }
  }

  @Override
  public PyreInventory getInventory() {
    return inventory;
  }

  public RandomSource getRandom() {
    return getLevel().getRandom();
  }

  public List<ItemStack> popStoredItems() {
    List<ItemStack> result = new ArrayList<>(storedItems);
    storedItems.clear();
    return result;
  }

  public int getLifetime() {
    return lifetime;
  }

  @Nullable
  public Ritual getCurrentRitual() {
    return currentRitual;
  }

  @Override
  public int getRadiusX() {
    Ritual ritual = getCurrentRitual();
    if (ritual == null) {
      return super.getRadiusX();
    }

    return ritual.getRadiusXZ();
  }

  @Override
  public int getRadiusY() {
    Ritual ritual = getCurrentRitual();
    if (ritual == null) {
      return super.getRadiusY();
    }

    return ritual.getRadiusY();
  }

  @Override
  public int getRadiusZ() {
    Ritual ritual = getCurrentRitual();
    if (ritual == null) {
      return super.getRadiusZ();
    }

    return ritual.getRadiusXZ();
  }

  public PyrePedestalCrafting getPedestalCrafting() {
    if (playerlessPedestalCrafting == null) {
      playerlessPedestalCrafting = new PyrePedestalCrafting(this, null);
    }
    return playerlessPedestalCrafting;
  }

  public void clearPedestalCrafting() {
    this.playerlessPedestalCrafting = null;
  }

  @Override
  public void clientTick(Level pLevel, BlockPos pPos, BlockState pState) {
  }

  @Override
  public void onLoad() {
    super.onLoad();
    this.revalidateRecipe();
  }

  public void stopRitual() {
    stopRitual(true);
  }

  public void stopRitual(boolean doLight) {
    if (currentRitual != null) {
      currentRitual.ends(getLevel(), getBlockPos(), getBlockState(), this, getRandom());
    }
    setCurrentRitual(null);
    this.lifetime = -1;
    setChanged();
    getLevel().setBlock(getBlockPos(), getBlockState().setValue(PyreBlock.BURNING, false)
        .setValue(PyreBlock.LIT, !doLight), 3);
  }

  @Nullable
  public Player getLastPlayer() {
    if (lastPlayer != null) {
      return lastPlayer;
    }

    if (getLevel() == null) {
      return null;
    }

    if (lastUuid != null) {
      lastPlayer = getLevel().getPlayerByUUID(lastUuid);
      return lastPlayer;
    }

    return null;
  }

  @Override
  public void serverTick(ServerLevel pLevel, BlockPos pPos, BlockState pState) {
    boolean changed = false;
    if (currentRitual != null && lifetime > 0) {
      lifetime--;
      changed = true;
      setChanged();
      if (lifetime <= 0) {
        if ((!inventory.isEmpty() && cachedRecipe != null && getLastPlayer() != null && lastRecipe != null && lifetime <= 0) && (cachedRecipe.equals(lastRecipe) && cachedRecipe.value()
            .matches(playerlessCrafting, pLevel))) {
          stopRitual(false);
          // Start
          light(getLastPlayer());
        } else {
          stopRitual();
        }
      } else {
        cache.initCache(pLevel, currentRitual.getPredicates());
        currentRitual.tick(pLevel, pPos, pState, this, cache, this.getRandom());
        BlockState newState = pState;
        if (pState.is(RootsTags.Blocks.PYRES)) {
          if (currentRitual.providesLight() && pState.hasProperty(PyreBlock.LIT) && !pState.getValue(PyreBlock.LIT)) {
            newState = newState.setValue(PyreBlock.LIT, true);
          }
          if (pState.hasProperty(PyreBlock.BURNING) && !pState.getValue(PyreBlock.BURNING)) {
            newState = newState.setValue(PyreBlock.BURNING, true);
          }

          if (newState != pState) {
            // This set block will force an update
            changed = false;
            pLevel.setBlock(pPos, newState, 3);
          }
        }
      }
    }

    if (tryRefill(pLevel, getBlockPos().below())) {
      revalidateRecipe();
      changed = true;
    }

    if (changed) {
      updateViaState();
    }
  }

  @Override
  public RecipeInventory getRefillInventory() {
    return getInventory();
  }

  @Override
  public Recipe<?> getRefillRecipe() {
    return lastRecipe != null ? lastRecipe.value() : null;
  }

  @Override
  public @org.jetbrains.annotations.Nullable BlockCapabilityCache<IItemHandler, Direction> getBlockCapabilityCache() {
    return capabilityCache;
  }

  @Override
  public void setBlockCapabilityCache(BlockCapabilityCache<IItemHandler, Direction> blockCapabilityCache) {
    this.capabilityCache = blockCapabilityCache;
  }
}
