package mysticmods.roots.blockentity;

import com.google.common.collect.ImmutableList;
import mysticmods.roots.action.CraftItemAction;
import mysticmods.roots.action.CraftRecipeAction;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.blockentity.*;
import mysticmods.roots.api.grove.GrovePowerGenerator;
import mysticmods.roots.api.grove.IGroveConsumer;
import mysticmods.roots.api.grove.PowerTicket;
import mysticmods.roots.api.recipe.ConditionResult;
import mysticmods.roots.api.recipe.RecipeUtil;
import mysticmods.roots.api.recipe.UnlockResult;
import mysticmods.roots.api.recipe.inventory.RecipeInventory;
import mysticmods.roots.api.reference.Constants;
import mysticmods.roots.block.FungalTransmuterBlock;
import mysticmods.roots.blockentity.template.UseDelegatedBlockEntity;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModActions;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModBlockEntities;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.inventory.fake.TransmuterContainer;
import mysticmods.roots.recipe.transmutation.TransmutationCrafting;
import mysticmods.roots.recipe.transmutation.TransmutationInventory;
import mysticmods.roots.recipe.transmutation.TransmutationRecipe;
import mysticmods.roots.util.ItemUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
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

// TODO: Change this to function more as a "valid recipe" -> "start" -> "consume power" -> "craft" -> "output" system.
public class FungalTransmuterBlockEntity extends UseDelegatedBlockEntity implements ServerTickBlockEntity, ClientTickBlockEntity, IGroveConsumer, InventoryBlockEntity, RefillProvider, ClearableBlockEntity, FakeMenuBlockEntity {
  private static PowerTicket.TicketDefinition TICKET_DEFINITION = null;

  private static PowerTicket.TicketDefinition getTicketDefinition() {
    if (TICKET_DEFINITION == null) {
      TICKET_DEFINITION = new PowerTicket.TicketDefinition(ImmutableList.of(new GrovePowerGenerator.Consumer(RootsTags.Groves.FUNGAL, ConfigManager.FUNGAL_TRANSMUTER_POWER_PER_TICK.getAsInt())));
    }
    return TICKET_DEFINITION;
  }

  private final TransmutationInventory inventory = new TransmutationInventory() {
    @Override
    protected void onContentsChanged(int slot) {
      if (FungalTransmuterBlockEntity.this.hasLevel() && !FungalTransmuterBlockEntity.this.getLevel().isClientSide()) {
        FungalTransmuterBlockEntity.this.setChanged();
        FungalTransmuterBlockEntity.this.revalidateRecipe();
        FungalTransmuterBlockEntity.this.updateViaState();
      }
    }
  };

  private final TransmutationCrafting playerlessCrafting = new TransmutationCrafting(inventory, this, null);
  private final List<ItemStack> storedItems = new ArrayList<>();
  private final List<ItemStack> animatedItems = new ArrayList<>();
  private PowerTicket ticket = null;
  private RecipeHolder<TransmutationRecipe> lastRecipe = null;
  private RecipeHolder<TransmutationRecipe> cachedRecipe = null;

  private ResourceLocation cachedRecipeId = null;
  private ResourceLocation lastRecipeId = null;

  private boolean poweredLastTick = false;
  private boolean revalidatedRecipes = false;
  private Player lastPlayer;
  private UUID lastUuid;
  private int storedPower = -1;
  private int craftingTicks = 0;

  public float dissolveProgress = 0f;
  public float oDissolveProgress = 0f;

  private BlockCapabilityCache<IItemHandler, Direction> capabilityCache;

  public FungalTransmuterBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
    super(ModBlockEntities.FUNGAL_TRANSMUTER.get(), pWorldPosition, pBlockState);
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult ray, InteractionHand hand, ItemStack stack) {
    if (level.isClientSide()) {
      return InteractionResult.CONSUME;
    }

    if (isCrafting()) {
      return InteractionResult.SUCCESS_NO_ITEM_USED;
    }

    if (stack.isEmpty() && !player.isCrouching()) {
      // Extract
      ItemStack popped = inventory.pop();
      if (!popped.isEmpty()) {
        ItemUtil.Spawn.spawnItem(level, getBlockPos(), popped);
      }
    } else if (stack.isEmpty() && player.isCrouching()) {
      if (lastRecipe != null && inventory.isEmpty()) {
        RecipeUtil.refillRecipeFromPlayer((ServerPlayer) player, lastRecipe.value(), inventory);
        revalidateRecipe();
      }
    } else if (stack.is(RootsTags.Items.FUNGAL_TRANSMUTER_ACTIVATION)) {
      if (!state.getValue(FungalTransmuterBlock.ACTIVE)) {
        InteractionResult result = startCrafting(player);
        if (result.indicateItemUse() && stack.isDamageableItem() && !player.isCreative()) {
          stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        }
        return result;
      }

      return InteractionResult.SUCCESS;
      // Start crafting
    } else {
      ItemStack result = inventory.insert(stack);
      if (stack.equals(result)) {
        return InteractionResult.PASS;
      }
      if (!player.isCreative()) {
        player.setItemInHand(hand, result);
      }
    }

    return InteractionResult.SUCCESS;
  }

  public InteractionResult startCrafting(Player player) {
    if (cachedRecipe == null || !cachedRecipe.value().matches(playerlessCrafting, level)) {
      revalidateRecipe();
    }
    if (cachedRecipe != null) {
      if (cachedRecipe.value().getPower() > storedPower) {
        RootsAPI.LOG.info("Not enough power to craft: {} < {}", storedPower, cachedRecipe.value().getPower());
        player.displayClientMessage(Component.translatable("roots.transmutation.not_enough_power", storedPower, cachedRecipe.value()
            .getPower()), true);
        return InteractionResult.FAIL;
      }
      storedPower -= cachedRecipe.value().getPower();
      ConditionResult result = cachedRecipe.value()
          .checkConditions(level, player, PyreBlockEntity.getPyreBoundingBox(), getBlockPos());
      if (result.anyFailed()) {
        result.report(player);
        return InteractionResult.FAIL;
      }
      UnlockResult failedGrants = cachedRecipe.value().checkUnlocks(level, (ServerPlayer) player);
      if (failedGrants.anyFailed() && !cachedRecipe.value().hasOutput(level.registryAccess())) {
        RootsAPI.LOG.info("Grants failed and recipe has no output");
        failedGrants.failedUnlocks().forEach(o -> RootsAPI.LOG.info("Failed unlock {}", o));
        failedGrants.report();
        return InteractionResult.FAIL;
      }

      TransmutationCrafting playerCrafting = new TransmutationCrafting(inventory, this, player);
      lastPlayer = player;
      lastUuid = null;
      lastRecipe = cachedRecipe;
      storedItems.clear();

      storedItems.addAll(lastRecipe.value()
          .assembleOutputs(playerCrafting, level.getRandom(), level.registryAccess(), null));
      storedItems.removeIf(ItemStack::isEmpty);

      animatedItems.clear();
      List<ItemStack> items = inventory.getItemsAndClear();
      items.forEach(o -> animatedItems.add(o.copy()));

      List<ItemStack> processed = lastRecipe.value().process(items);
      processed = outputAdjacent(processed);
      for (ItemStack stack : processed) {
        ItemUtil.Spawn.spawnItem(level, player.blockPosition(), stack);
      }
      cachedRecipe = null;
      craftingTicks = Constants.GROVE_CRAFTING_ANIMATION_TICKS;
      setChanged();
      updateViaState();
    }
    return InteractionResult.SUCCESS;
  }

  public RecipeHolder<TransmutationRecipe> getCachedRecipe() {
    return cachedRecipe;
  }

  public RecipeHolder<TransmutationRecipe> getLastRecipe() {
    return lastRecipe;
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
    super.onDataPacket(net, pkt, lookupProvider);
    CompoundTag tag = pkt.getTag();
    if (tag != null) {
      revalidateRecipe();
      loadAdditional(tag, lookupProvider);
    }
  }

  protected boolean revalidateRecipe() {
    if (getLevel() == null) {
      return false;
    }

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

    boolean matched = false;

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

    if (changed && !getLevel().isClientSide()) {
      setChanged();
      updateViaState();
    }

    return changed;
  }

  @Override
  public List<ItemStack> getNonEmptyItems() {
    if (!animatedItems.isEmpty()) {
      return animatedItems;
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
    ListTag storedItems = new ListTag();
    for (ItemStack stack : this.storedItems) {
      if (!stack.isEmpty()) {
        storedItems.add(stack.save(lookup, new CompoundTag()));
      }
    }
    ListTag animatedItems = new ListTag();
    for (ItemStack stack : this.animatedItems) {
      if (!stack.isEmpty()) {
        animatedItems.add(stack.save(lookup, new CompoundTag()));
      }
    }
    if (!storedItems.isEmpty()) {
      pTag.put("stored_items", storedItems);
    }
    if (!animatedItems.isEmpty()) {
      pTag.put("animated_items", animatedItems);
    }
    pTag.putInt("stored_power", storedPower);
    pTag.put("inventory", inventory.serializeNBT(lookup));

    if (lastPlayer != null) {
      pTag.putUUID("last_player", lastPlayer.getUUID());
    } else if (lastUuid != null) {
      pTag.putUUID("last_player", lastUuid);
    }

    pTag.putInt("crafting_ticks", craftingTicks);
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
    if (tag.contains("inventory", CompoundTag.TAG_COMPOUND)) {
      inventory.deserializeNBT(registries, tag.getCompound("inventory"));
    }
    storedPower = 0;
    if (tag.contains("stored_power", CompoundTag.TAG_INT)) {
      storedPower = tag.getInt("stored_power");
    }
    if (!storedItems.isEmpty()) {
      outputStoredItems(getLastPlayer());
    }
    storedItems.clear();
    if (tag.contains("stored_items", CompoundTag.TAG_LIST)) {
      ListTag storedItemsList = tag.getList("stored_items", CompoundTag.TAG_COMPOUND);
      for (int i = 0; i < storedItemsList.size(); i++) {
        ItemStack.parse(registries, storedItemsList.getCompound(i))
            .ifPresent(storedItems::add);
      }
    }
    animatedItems.clear();
    if (tag.contains("animated_items", CompoundTag.TAG_LIST)) {
      ListTag animatedItemsList = tag.getList("animated_items", CompoundTag.TAG_COMPOUND);
      for (int i = 0; i < animatedItemsList.size(); i++) {
        ItemStack.parse(registries, animatedItemsList.getCompound(i))
            .ifPresent(animatedItems::add);
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
    this.craftingTicks = tag.getInt("crafting_ticks");
    this.revalidatedRecipes = false;
  }


  @Override
  public void clientTick(Level pLevel, BlockPos pPos, BlockState pState) {
    if (!revalidatedRecipes) {
      revalidateRecipe();
      revalidatedRecipes = true;
    }
    if (dissolveProgress > 1) {
      dissolveProgress = 0f;
      oDissolveProgress = 0f;
    } else {
      oDissolveProgress = dissolveProgress;
      dissolveProgress += 0.02f;
    }
  }

  public boolean isCrafting() {
    return craftingTicks > 0;
  }

  @Override
  public void serverTick(ServerLevel pLevel, BlockPos pPos, BlockState pState) {
    // Power storage is handled separately from crafting and happens regardless
    boolean changed = false;
    revalidateRecipe();

    if (craftingTicks > 0) {
      craftingTicks--;
      if (craftingTicks == 0) {
        outputStoredItems(getLastPlayer());
      }
      changed = true;
    }

    if (pState.getValue(FungalTransmuterBlock.ACTIVE) != isCrafting()) {
      pLevel.setBlock(pPos, pState.setValue(FungalTransmuterBlock.ACTIVE, isCrafting()), 3);
      changed = false;
    }

    if (changed) {
      this.setChanged();
      this.updateViaState();
    }
  }

  protected void outputStoredItems(@Nullable Player player) {
    if (getLevel() == null || getLevel().isClientSide()) {
      return;
    }
    if (!storedItems.isEmpty()) {
      if (player != null) {
        for (ItemStack item : storedItems) {
          CraftItemAction.Context context = new CraftItemAction.Context(
              (ServerLevel) level,
              (ServerPlayer) player,
              item
          );
          ModActions.CRAFT_ITEM.get().accept(context);
        }
      }
      for (ItemStack stack : this.outputAdjacent(storedItems)) {
        ItemUtil.Spawn.spawnItem(level, /*player == null ? */this.getBlockPos()
            .above()/* : player.blockPosition()*/, stack);
      }
    }
    storedItems.clear();
    if (lastRecipe != null && player != null) {
      CraftRecipeAction.Context context = new CraftRecipeAction.Context(
          (ServerLevel) level,
          (ServerPlayer) player,
          lastRecipe.id(),
          lastRecipe.value(),
          this
      );
      ModActions.CRAFT_RECIPE.get().accept(context);
    }
    animatedItems.clear();
    lastPlayer = null;
    lastUuid = null;
    level.setBlock(getBlockPos(), getBlockState().setValue(FungalTransmuterBlock.ACTIVE, false), 3);
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
  public PowerTicket getTicketForTick(long tick) {
    if (ticket == null) {
      ticket = getTicketDefinition().create(tick);
      return ticket;
    }

    if (ticket.isValid(tick)) {
      return ticket;
    }

    this.poweredLastTick = ticket.wasFullfilled();
    int oldPower = this.storedPower;
    this.storedPower += ticket.getSupplied(RootsTags.Groves.FUNGAL);
    this.storedPower = Math.min(this.storedPower, getMaxPower());
    if (this.storedPower != oldPower) {
      this.setChanged();
      this.updateViaState();
    }

    ticket = TICKET_DEFINITION.create(tick);
    return ticket;
  }

  @Override
  public boolean wasPoweredLastTick() {
    return this.poweredLastTick;
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

  public int getPower() {
    return storedPower;
  }

  public int getMaxPower() {
    return ConfigManager.FUNGAL_TRANSMUTER_MAX_STORED_POWER.getAsInt();
  }

  @Override
  public void clearContents() {
    for (ItemStack item : inventory.getItemsAndClear()) {
      ItemUtil.Spawn.spawnItem(level, getBlockPos(), item);
    }
  }

  @Override
  public boolean canClear() {
    return !inventory.isEmpty();
  }

  @Override
  public boolean shouldShowInsert() {
    return getCachedRecipe() == null;
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
