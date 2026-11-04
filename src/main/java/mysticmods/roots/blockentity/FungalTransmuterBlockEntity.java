package mysticmods.roots.blockentity;

import mysticmods.roots.action.CraftItemAction;
import mysticmods.roots.action.CraftRecipeAction;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.blockentity.*;
import mysticmods.roots.api.grove.power.consumer.IGrovePowerConsumer;
import mysticmods.roots.api.grove.power.consumer.PowerCollection;
import mysticmods.roots.api.grove.power.consumer.PowerRecord;
import mysticmods.roots.api.grove.power.consumer.PowerTicket;
import mysticmods.roots.api.recipe.ConditionResult;
import mysticmods.roots.api.recipe.RecipeUtil;
import mysticmods.roots.api.recipe.UnlockResult;
import mysticmods.roots.api.recipe.inventory.RecipeInventory;
import mysticmods.roots.api.state.*;
import mysticmods.roots.blockentity.template.StatefulBlockEntity;
import mysticmods.roots.blockentity.template.UseDelegatedBlockEntity;
import mysticmods.roots.init.ModActions;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModBlockEntities;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.inventory.fake.TransmuterContainer;
import mysticmods.roots.recipe.transmutation.TransmutationCrafting;
import mysticmods.roots.recipe.transmutation.TransmutationInventory;
import mysticmods.roots.recipe.transmutation.TransmutationRecipe;
import mysticmods.roots.util.ItemUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.Connection;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FungalTransmuterBlockEntity extends UseDelegatedBlockEntity implements ServerTickBlockEntity, ClientTickBlockEntity, IGrovePowerConsumer, InventoryBlockEntity, RefillProvider, ClearableBlockEntity, FakeMenuBlockEntity, StatefulBlockEntity<FungalTransmuterBlockEntity> {
  private static final StateSchema<FungalTransmuterBlockEntity> STATE_SCHEMA = StateSchema.<FungalTransmuterBlockEntity>builder()
      .add(StateCategories.INVENTORY, be -> InventoryState.of(be.inventory))
      .add(StateCategories.RECIPE, be -> RecipeState.of(be.currentRecipe != null || be.cachedRecipe != null))
      .add(StateCategories.LOCK, be -> LockState.of(be.isCrafting() || be.currentRecipe != null))
      .add(StateCategories.PROGRESS, be -> {
        if (be.isCrafting()) {
          return ProgressState.CRAFTING;
        }
        if (be.currentRecipe != null) {
          return ProgressState.CONSUMING_POWER;
        }
        return ProgressState.NONE;
      })
      .add(StateCategories.ANIMATION, be -> {
        if (be.isCrafting()) {
          return AnimationState.CRAFTING;
        }
        if (be.currentRecipe != null) {
          return AnimationState.CONSUMING_POWER;
        }
        return AnimationState.NONE;
      })
      .build();
  private final StateHolder<FungalTransmuterBlockEntity> stateHolder = STATE_SCHEMA.createHolder();

  private final TransmutationInventory inventory = new TransmutationInventory() {
    @Override
    protected void onContentsChanged(int slot) {
      if (FungalTransmuterBlockEntity.this.hasLevel() && !FungalTransmuterBlockEntity.this.getLevel().isClientSide()) {
        FungalTransmuterBlockEntity.this.lastRevalidationTick = -1;
        FungalTransmuterBlockEntity.this.setChanged();
        FungalTransmuterBlockEntity.this.revalidateRecipe();
        FungalTransmuterBlockEntity.this.updateViaState();
      }
    }
  };

  private final TransmutationCrafting playerlessCrafting = new TransmutationCrafting(inventory, this, null);
  private final List<ItemStack> storedOutputs = new ArrayList<>();
  private final List<ItemStack> storedInputs = new ArrayList<>();

  private RecipeHolder<TransmutationRecipe> lastRecipe = null;
  private RecipeHolder<TransmutationRecipe> cachedRecipe = null;
  private RecipeHolder<TransmutationRecipe> currentRecipe = null;
  private int ticksConsumingPower = -1;
  private int lifetime = -1;

  private PowerTicket ticket = null;
  private PowerRecord record = null;

  private ResourceLocation cachedRecipeId = null;
  private ResourceLocation lastRecipeId = null;
  private ResourceLocation currentRecipeId = null;

  private long lastRevalidationTick = -1;
  private boolean revalidatedRecipes = false;
  private Player lastPlayer;
  private UUID lastUuid;

  public float dissolveProgress = 0f;
  public float oDissolveProgress = 0f;

  private BlockCapabilityCache<IItemHandler, Direction> capabilityCache;

  private static final int CRAFTING_TIME = 100;

  public FungalTransmuterBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
    super(ModBlockEntities.FUNGAL_TRANSMUTER.get(), pWorldPosition, pBlockState);
  }

  @Override
  public StateHolder<FungalTransmuterBlockEntity> getStateHolder() {
    return stateHolder;
  }

  @Override
  protected boolean recomputeState() {
    return stateHolder.recompute(this);
  }

  private void emergencyEject() {

  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult ray, InteractionHand hand, ItemStack stack) {
    if (level.isClientSide()) {
      return InteractionResult.CONSUME;
    }

    updateState();

    if (stateHolder.is(LockState.LOCKED)) {
      if (!stateHolder.is(ProgressState.CONSUMING_POWER) && player.isShiftKeyDown()) {
        // try to unlock
        emergencyEject();
      }
      return InteractionResult.SUCCESS_NO_ITEM_USED;
    }

    if (stack.isEmpty() && !player.isCrouching()) {
      // Extract
      if (!stateHolder.is(InventoryState.EMPTY)) {
        ItemStack popped = inventory.pop();
        if (!popped.isEmpty()) {
          ItemUtil.Spawn.spawnItem(level, getBlockPos(), popped);
        }
      }
    } else if (stack.isEmpty() && player.isCrouching()) {
      if (lastRecipe != null && stateHolder.is(InventoryState.EMPTY)) {
        RecipeUtil.refillRecipeFromPlayer((ServerPlayer) player, lastRecipe.value(), inventory);
      }
    } else if (stack.is(RootsTags.Items.FUNGAL_TRANSMUTER_ACTIVATION)) {
      InteractionResult result = startConsuming(player);
      if (result.indicateItemUse() && stack.isDamageableItem() && !player.isCreative()) {
        stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
      }
      updateState();
      return result;
    } else {
      if (stateHolder.is(InventoryState.FULL)) {
        return InteractionResult.PASS;
      }
      ItemStack result = inventory.insert(stack);
      if (stack.equals(result)) {
        return InteractionResult.PASS;
      }
      if (!player.isCreative()) {
        player.setItemInHand(hand, result);
      }
    }

    updateState();
    return InteractionResult.SUCCESS;
  }

  public InteractionResult startConsuming(Player player) {
    if (stateHolder.anyOf(ProgressState.CONSUMING_POWER, ProgressState.CRAFTING)) {
      return InteractionResult.FAIL;
    }
    if (!stateHolder.is(RecipeState.MATCHING)) {
      return InteractionResult.SUCCESS_NO_ITEM_USED;
    }

    RecipeHolder<TransmutationRecipe> recipe = cachedRecipe;
    ConditionResult result = recipe.value()
        .checkConditions(level, player, PyreBlockEntity.getPyreBoundingBox(), getBlockPos());
    if (result.anyFailed()) {
      result.report(player);
      return InteractionResult.FAIL;
    }
    UnlockResult failedGrants = recipe.value().checkUnlocks(level, (ServerPlayer) player);
    if (failedGrants.anyFailed() && !recipe.value().hasOutput(level.registryAccess())) {
      RootsAPI.LOG.info("Grants failed and recipe has no output");
      failedGrants.failedUnlocks().forEach(o -> RootsAPI.LOG.info("Failed unlock {}", o));
      failedGrants.report();
      return InteractionResult.FAIL;
    }

    lastPlayer = player;
    lastUuid = null;
    currentRecipe = recipe;
    lifetime = -1;
    ticksConsumingPower = 0;
    setChanged();
    updateViaState();

    return InteractionResult.SUCCESS;
  }

  public InteractionResult startCrafting(@Nullable Player player) {
    if (getCurrentRecipe() == null) {
      throw new IllegalStateException("Tried to start crafting when the recipe was invalid!");
    }

    if (!powerRequirementsMet()) {
      throw new IllegalStateException("Tried to start crafting when the power requirements weren't met!");
    }

    RecipeHolder<TransmutationRecipe> recipe = getCurrentRecipe();
    TransmutationCrafting crafting = new TransmutationCrafting(inventory, this, player);
    storedOutputs.clear();
    storedOutputs.addAll(recipe.value()
        .assembleOutputs(crafting, level.getRandom(), level.registryAccess(), null));
    storedOutputs.removeIf(ItemStack::isEmpty);

    List<ItemStack> items = inventory.getItemsAndClear();
    storedInputs.clear();
    items.forEach(o -> storedInputs.add(o.copy()));

    List<ItemStack> processed = recipe.value().process(items);
    processed = outputAdjacent(processed);
    BlockPos spawnPos = player != null ? player.blockPosition() : getBlockPos().above();
    for (ItemStack stack : processed) {
      ItemUtil.Spawn.spawnItem(level, spawnPos, stack);
    }
    lastRecipe = recipe;
    currentRecipe = null;
    cachedRecipe = null;
    lifetime = 100;
    record = null;
    ticksConsumingPower = -1;

    setChanged();
    updateViaState();
    return InteractionResult.SUCCESS;
  }

  // The recipe currently being crafted
  public RecipeHolder<TransmutationRecipe> getCurrentRecipe() {
    return currentRecipe;
  }

  // The recipe currently in the block
  public RecipeHolder<TransmutationRecipe> getCachedRecipe() {
    return cachedRecipe;
  }

  // The last recipe that was successfully crafted
  public RecipeHolder<TransmutationRecipe> getLastRecipe() {
    return lastRecipe;
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
    super.onDataPacket(net, pkt, lookupProvider);
    CompoundTag tag = pkt.getTag();
    if (tag != null) {
      loadAdditional(tag, lookupProvider);
      revalidateRecipe();
    }
  }

  protected boolean revalidateRecipe() {
    if (getLevel() == null) {
      return false;
    }

    long tick = getLevel().getGameTime();
    if (tick == lastRevalidationTick) {
      return false;
    }

    lastRevalidationTick = tick;

    boolean changed = false;
    if (cachedRecipeId != null) {
      cachedRecipe = ResolvedRecipes.TRANSMUTATION.getRecipe(getLevel(), cachedRecipeId);
      if (cachedRecipe != null) {
        cachedRecipeId = null;
        changed = true;
      }
    }

    if (lastRecipeId != null) {
      lastRecipe = ResolvedRecipes.TRANSMUTATION.getRecipe(getLevel(), lastRecipeId);
      if (lastRecipe != null) {
        lastRecipeId = null;
        changed = true;
      }
    }

    if (currentRecipeId != null) {
      currentRecipe = ResolvedRecipes.TRANSMUTATION.getRecipe(getLevel(), currentRecipeId);
      if (currentRecipe != null) {
        currentRecipeId = null;
        changed = true;
      }
    }

    boolean matched = false;

    if (currentRecipe != null) {
      if (!currentRecipe.value().matches(playerlessCrafting, getLevel())) {
        currentRecipe = null;
        changed = true;
      }
    }

    if (cachedRecipe == null) {
      if (lastRecipe != null && lastRecipe.value().matches(playerlessCrafting, getLevel())) {
        cachedRecipe = lastRecipe;
        matched = true;
        changed = true;
      } else {
        cachedRecipe = ResolvedRecipes.TRANSMUTATION.findRecipe(playerlessCrafting, getLevel());
        changed = true;
      }
    }

    if (cachedRecipe != null) {
      if (!matched && !cachedRecipe.value().matches(playerlessCrafting, getLevel())) {
        cachedRecipe = null;
      }
    }

    recomputeState();

    if (changed && !getLevel().isClientSide()) {
      setChanged();
      updateViaState();
    }

    return changed;
  }

  @Override
  public List<ItemStack> getNonEmptyItems() {
    if (!storedInputs.isEmpty()) {
      return storedInputs;
    }
    return InventoryBlockEntity.super.getNonEmptyItems();
  }

  @Override
  protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider lookup) {
    super.saveAdditional(pTag, lookup);
    if (cachedRecipe != null) {
      pTag.putString("cached_recipe", cachedRecipe.id().toString());
    }
    if (lastRecipe != null) {
      pTag.putString("last_recipe", lastRecipe.id().toString());
    }
    if (currentRecipe != null) {
      pTag.putString("current_recipe", currentRecipe.id().toString());
    }
    ListTag storedItems = new ListTag();
    for (ItemStack stack : this.storedOutputs) {
      if (!stack.isEmpty()) {
        storedItems.add(stack.save(lookup, new CompoundTag()));
      }
    }
    ListTag animatedItems = new ListTag();
    for (ItemStack stack : this.storedInputs) {
      if (!stack.isEmpty()) {
        animatedItems.add(stack.save(lookup, new CompoundTag()));
      }
    }
    if (!storedItems.isEmpty()) {
      pTag.put("stored_outputs", storedItems);
    }
    if (!animatedItems.isEmpty()) {
      pTag.put("animated_items", animatedItems);
    }
    pTag.put("inventory", inventory.serializeNBT(lookup));

    if (lastPlayer != null) {
      pTag.putUUID("last_player", lastPlayer.getUUID());
    } else if (lastUuid != null) {
      pTag.putUUID("last_player", lastUuid);
    }

    if (record != null) {
      pTag.put("power_record", PowerRecord.CODEC.encodeStart(lookup.createSerializationContext(NbtOps.INSTANCE), record)
          .getOrThrow());
    }

    pTag.putInt("ticks_consuming_power", ticksConsumingPower);
    pTag.putInt("lifetime", lifetime);
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    cachedRecipeId = null;
    if (tag.contains("cached_recipe", CompoundTag.TAG_STRING)) {
      cachedRecipeId = ResourceLocation.parse(tag.getString("cached_recipe"));
    }
    lastRecipeId = null;
    if (tag.contains("last_recipe", CompoundTag.TAG_STRING)) {
      lastRecipeId = ResourceLocation.parse(tag.getString("last_recipe"));
    }
    currentRecipeId = null;
    if (tag.contains("current_recipe", CompoundTag.TAG_STRING)) {
      currentRecipeId = ResourceLocation.parse(tag.getString("current_recipe"));
    }
    if (tag.contains("inventory", CompoundTag.TAG_COMPOUND)) {
      inventory.deserializeNBT(registries, tag.getCompound("inventory"));
    }
    if (!storedOutputs.isEmpty()) {
      outputStoredItems(getLastPlayer());
    }
    storedOutputs.clear();
    String storedKey = tag.contains("stored_outputs", CompoundTag.TAG_LIST) ? "stored_outputs" : "stored_items";
    if (tag.contains(storedKey, CompoundTag.TAG_LIST)) {
      ListTag storedItemsList = tag.getList(storedKey, CompoundTag.TAG_COMPOUND);
      for (int i = 0; i < storedItemsList.size(); i++) {
        ItemStack.parse(registries, storedItemsList.getCompound(i))
            .ifPresent(storedOutputs::add);
      }
    }
    storedInputs.clear();
    if (tag.contains("animated_items", CompoundTag.TAG_LIST)) {
      ListTag animatedItemsList = tag.getList("animated_items", CompoundTag.TAG_COMPOUND);
      for (int i = 0; i < animatedItemsList.size(); i++) {
        ItemStack.parse(registries, animatedItemsList.getCompound(i))
            .ifPresent(storedInputs::add);
      }
    }
    lastUuid = null;
    lastPlayer = null;
    if (tag.hasUUID("last_player")) {
      lastUuid = tag.getUUID("last_player");
      if (getLevel() != null) {
        lastPlayer = getLevel().getPlayerByUUID(lastUuid);
      }
    }
    this.lifetime = tag.contains("crafting_ticks", CompoundTag.TAG_INT) ? tag.getInt("crafting_ticks") : tag.contains("lifetime", CompoundTag.TAG_INT) ? tag.getInt("lifetime") : -1;
    this.revalidatedRecipes = false;
    this.lastRevalidationTick = -1;
    this.ticksConsumingPower = tag.getInt("ticks_consuming_power");

    if (tag.contains("power_record")) {
      PowerRecord.CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), tag.get("power_record"))
          .resultOrPartial(Util.prefix("Failed to load power record: ", RootsAPI.LOG::warn))
          .ifPresent(o -> this.record = o);
    }
  }

  @Override
  public void clientTick(Level pLevel, BlockPos pPos, BlockState pState) {
    if (!revalidatedRecipes) {
      revalidateRecipe();
      recomputeState();
      revalidatedRecipes = true;
    }
    if (stateHolder.is(AnimationState.CRAFTING)) {
      oDissolveProgress = dissolveProgress;
      dissolveProgress += 1.0f / CRAFTING_TIME;
    } else {
      dissolveProgress = 0f;
      oDissolveProgress = 0f;
    }
  }

  public boolean isCrafting() {
    return lifetime != -1;
  }

  public boolean isConsuming() {
    return lifetime == -1 && canConsumePower();
  }

  public boolean canConsumePower() {
    if (getCurrentRecipe() == null) {
      return false;
    }

    if (powerRequirementsMet()) {
      return false;
    }

    return true;
  }

  protected boolean powerRequirementsMet() {
    PowerCollection collection = getPowerCollection();
    if (collection == null) {
      return false;
    }

    PowerRecord record = getOrCreateRecord();
    if (record == null) {
      throw new IllegalStateException("Can't calculate the record on the client as we got a power collection but no record");
    }

    if (record.isFulfilled(collection)) {
      return true;
    }

    return false;
  }

  @Nullable
  protected PowerCollection getPowerCollection() {
    RecipeHolder<TransmutationRecipe> currentRecipe = getCurrentRecipe();
    if (currentRecipe == null) {
      return null;
    }

    return currentRecipe.value().getPowerRequirements();
  }

  @Nullable
  protected PowerRecord getOrCreateRecord() {
    PowerCollection power = getPowerCollection();
    if (power == null) {
      return null;
    }

    if (this.record != null && this.record.isFor(power)) {
      return this.record;
    }

    this.record = new PowerRecord(power);

    return this.record;
  }

  public boolean isActive() {
    return isCrafting() || isConsuming();
  }

  @Override
  public void serverTick(ServerLevel pLevel, BlockPos pPos, BlockState pState) {
    revalidateRecipe();
    boolean changed = recomputeState();

    if (stateHolder.is(ProgressState.CONSUMING_POWER)) {
      ticksConsumingPower++;

      if (powerRequirementsMet()) {
        startCrafting(getLastPlayer());
      }

      changed = true;
    }

    if (lifetime > 0) {
      lifetime--;
      if (lifetime == 0) {
        outputStoredItems(getLastPlayer());
      }
      changed = true;
    }

    if (!stateHolder.is(LockState.LOCKED) && tryRefill(pLevel, pPos.below())) {
      changed = true;
    }

    if (recomputeState() || changed) {
      updateViaState();
    }
  }

  protected void outputStoredItems(@Nullable Player player) {
    if (getLevel() == null || getLevel().isClientSide()) {
      return;
    }
    if (!storedOutputs.isEmpty()) {
      if (player != null && ModActions.CRAFT_ITEM.get().shouldTest()) {
        for (ItemStack item : storedOutputs) {
          CraftItemAction.Context context = new CraftItemAction.Context(
              (ServerLevel) level,
              (ServerPlayer) player,
              item
          );
          ModActions.CRAFT_ITEM.get().accept(context);
        }
      }
      for (ItemStack stack : this.outputAdjacent(storedOutputs)) {
        ItemUtil.Spawn.spawnItem(level, /*player == null ? */this.getBlockPos()
            .above()/* : player.blockPosition()*/, stack);
      }
    }
    storedOutputs.clear();
    if (lastRecipe != null && player != null && ModActions.CRAFT_RECIPE.get().shouldTest()) {
      CraftRecipeAction.Context context = new CraftRecipeAction.Context(
          (ServerLevel) level,
          (ServerPlayer) player,
          lastRecipe.id(),
          lastRecipe.value(),
          this
      );
      ModActions.CRAFT_RECIPE.get().accept(context);
    }
    storedInputs.clear();
    ticksConsumingPower = -1;
    record = null;
    currentRecipe = null;
    lastPlayer = null;
    lastUuid = null;
    lifetime = -1;
    setChanged();
    updateViaState();
  }

  @Override
  public void onLoad() {
    super.onLoad();
    this.revalidateRecipe();
    if (getLevel() == null) {
      RootsAPI.LOG.error("I feel like this is a broken contract: onLoad called without a level for {}", this);
      return;
    }

    getLevel().getData(ModAttachments.GROVE_CONSUMERS).add(getBlockPos());
  }

  @javax.annotation.Nullable
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
  public PowerTicket getTicket() {
    return ticket;
  }

  @Override
  public PowerTicket createTicket(long tick) {
    if (getCurrentRecipe() == null) {
      return null;
    }

    return getCurrentRecipe().value().getPowerRequirements().create(tick);
  }

  @Override
  public void setPowerTicket(PowerTicket ticket) {
    this.ticket = ticket;
  }

  @Override
  public boolean wasPoweredLastTick() {
    return false; // TODO:
  }

  @Override
  public void finalizeTicket(PowerTicket ticket) {
    PowerCollection collection = getPowerCollection();
    if (collection == null) {
      RootsAPI.LOG.error("Unable to find a power record for the current ticket, discarding ticket");
      return;
    }

    PowerRecord record = getOrCreateRecord();
    if (record == null) {
      throw new IllegalStateException("WE had a valid pwoer collection but getOrCreateRecord failed");
    }
    ticket.updateRecord(record);
  }

  @Override
  public TransmutationInventory getInventory() {
    return inventory;
  }

  @Override
  public RecipeInventory getRefillInventory() {
    return getInventory();
  }

  @Override
  public @Nullable Recipe<?> getRefillRecipe() {
    return lastRecipe != null ? lastRecipe.value() : null;
  }

  @Override
  public @Nullable BlockCapabilityCache<IItemHandler, Direction> getBlockCapabilityCache() {
    return capabilityCache;
  }

  @Override
  public void setBlockCapabilityCache(BlockCapabilityCache<IItemHandler, Direction> blockCapabilityCache) {
    this.capabilityCache = blockCapabilityCache;
  }

  @Override
  public void clearContents() {
    for (ItemStack item : inventory.getItemsAndClear()) {
      ItemUtil.Spawn.spawnItem(level, getBlockPos(), item);
    }
  }

  @Override
  public boolean canClear() {
    return !stateHolder.is(InventoryState.EMPTY) && !stateHolder.is(LockState.LOCKED);
  }

  @Override
  public boolean shouldShowInsert() {
    return stateHolder.is(RecipeState.NONE) && !stateHolder.is(LockState.LOCKED);
  }

  @Override
  public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
    return new TransmuterContainer(containerId, playerInventory, inventory, ContainerLevelAccess.create(getLevel(), getBlockPos()));
  }

  @Override
  public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buffer) {
    buffer.writeBlockPos(getBlockPos());
  }
}
