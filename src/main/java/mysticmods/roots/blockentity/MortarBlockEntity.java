package mysticmods.roots.blockentity;

import mysticmods.roots.action.CraftItemAction;
import mysticmods.roots.action.CraftRecipeAction;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.blockentity.*;
import mysticmods.roots.api.recipe.ConditionResult;
import mysticmods.roots.api.recipe.RecipeUtil;
import mysticmods.roots.api.recipe.UnlockResult;
import mysticmods.roots.api.recipe.inventory.RecipeInventory;
import mysticmods.roots.blockentity.template.UseDelegatedBlockEntity;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModActions;
import mysticmods.roots.init.ModBlockEntities;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.inventory.fake.MortarContainer;
import mysticmods.roots.recipe.mortar.MortarCrafting;
import mysticmods.roots.recipe.mortar.MortarInventory;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.util.ItemUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MortarBlockEntity extends UseDelegatedBlockEntity implements ServerTickBlockEntity, InventoryBlockEntity, RefillProvider, FakeMenuBlockEntity, ClearableBlockEntity {
  private final MortarInventory inventory = new MortarInventory() {
    @Override
    protected void onContentsChanged(int slot) {
      if (MortarBlockEntity.this.hasLevel() && !MortarBlockEntity.this.getLevel().isClientSide()) {
        MortarBlockEntity.this.getLevel().invalidateCapabilities(MortarBlockEntity.this.getBlockPos());
        MortarBlockEntity.this.revalidateRecipe();
        MortarBlockEntity.this.updateViaState();
      }
    }
  };
  private final MortarCrafting playerlessCrafting = new MortarCrafting(inventory, this, null);
  private final List<ItemStack> previousRecipeItems = new ArrayList<>();
  private RecipeHolder<MortarRecipe> lastRecipe = null;
  private RecipeHolder<MortarRecipe> cachedRecipe = null;
  private int uses = -1;
  private BlockCapabilityCache<IItemHandler, Direction> capabilityCache;

  public MortarBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
  }

  public MortarBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
    super(ModBlockEntities.MORTAR.get(), pWorldPosition, pBlockState);
  }

  private boolean onlyMortar() {
    if (this.inventory.getStackInSlot(0).is(RootsTags.Items.MORTAR_ACTIVATION)) {
      for (int i = 1; i < this.inventory.getSlots(); i++) {
        if (!this.inventory.getStackInSlot(i).isEmpty()) {
          return false;
        }
      }

      return true;
    }

    return false;
  }

  public RecipeHolder<MortarRecipe> getCachedRecipe() {
    return cachedRecipe;
  }

  public RecipeHolder<MortarRecipe> getLastRecipe() {
    return lastRecipe;
  }

  protected void revalidateRecipe() {
    if (getLevel() == null) {
      return;
    }

    boolean changed = false;

    if (cachedRecipeId != null) {
      cachedRecipe = ResolvedRecipes.MORTAR.getRecipe(getLevel(), cachedRecipeId);
      if (cachedRecipe != null) {
        cachedRecipeId = null;
        changed = true;
      }
    }
    if (lastRecipeId != null) {
      lastRecipe = ResolvedRecipes.MORTAR.getRecipe(getLevel(), lastRecipeId);
      if (lastRecipe != null) {
        lastRecipeId = null;
        changed = true;
      }
    }

    boolean matched = false;
    if (cachedRecipe == null) {
      uses = -1;
      if (lastRecipe != null && lastRecipe.value().matches(playerlessCrafting, getLevel())) {
        cachedRecipe = lastRecipe;
        changed = true;
        matched = true;
      } else {
        cachedRecipe = ResolvedRecipes.MORTAR.findRecipe(playerlessCrafting, getLevel());
        changed = true;
      }
    }

    if (cachedRecipe != null) {
      // only test once
      if (matched || cachedRecipe.value().matches(playerlessCrafting, getLevel())) {
        if (uses == -1) {
          uses = 0;
        }
      } else {
        cachedRecipe = null;
        uses = -1;
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
    // TODO: reference this
    pTag.putInt("MortarUses", uses);
    boolean previous = false;
    ListTag previousItems = new ListTag();
    for (ItemStack stack : previousRecipeItems) {
      if (!stack.isEmpty()) {
        previous = true;
        // Does it need the new compound tag?
        previousItems.add(stack.save(provider, new CompoundTag()));
      }
    }

    if (previous) {
      pTag.put("MortarPreviousItems", previousItems);
    }

    if (cachedRecipe != null) {
      pTag.putString("MortarCachedRecipe", cachedRecipe.id().toString());
    }
    if (lastRecipe != null) {
      pTag.putString("MortarLastRecipe", lastRecipe.id().toString());
    }
    pTag.put("MortarInventory", inventory.serializeNBT(provider));
  }

  private ResourceLocation cachedRecipeId = null;
  private ResourceLocation lastRecipeId = null;

  @Override
  public void loadAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
    super.loadAdditional(pTag, provider);
    // TODO: reference this
    if (pTag.contains("MortarUses", Tag.TAG_INT)) {
      this.uses = pTag.getInt("MortarUses");
    }
    if (pTag.contains("MortarPreviousItems", Tag.TAG_LIST)) {
      previousRecipeItems.clear();
      ListTag previousItems = pTag.getList("MortarPreviousItems", Tag.TAG_COMPOUND);
      for (int i = 0; i < previousItems.size(); i++) {
        ItemStack.parse(provider, previousItems.getCompound(i)).ifPresent(previousRecipeItems::add);
      }
    }
    cachedRecipeId = null;
    if (pTag.contains("MortarCachedRecipe", Tag.TAG_STRING)) {
      cachedRecipeId = ResourceLocation.parse(pTag.getString("MortarCachedRecipe"));
    }
    lastRecipeId = null;
    if (pTag.contains("MortarLastRecipe", Tag.TAG_STRING)) {
      lastRecipeId = ResourceLocation.parse(pTag.getString("MortarLastRecipe"));
    }
    if (pTag.contains("MortarInventory", Tag.TAG_COMPOUND)) {
      inventory.deserializeNBT(provider, pTag.getCompound("MortarInventory"));
    }
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider provider) {
    super.onDataPacket(net, pkt, provider);
    CompoundTag tag = pkt.getTag();
    if (tag != null) {
      loadAdditional(tag, provider);
      revalidateRecipe();
    }
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult ray, InteractionHand hand, ItemStack inHand) {
    if (level.isClientSide()) {
      return InteractionResult.CONSUME;
    }
    ItemCooldowns cooldowns = player.getCooldowns();
    if (inHand.getCount() == 1 && onlyMortar()) {
      ItemStack stack = inventory.pop();
      inventory.insert(inHand);
      player.setItemInHand(hand, stack);
    } else if (inHand.isEmpty() && !player.isCrouching()) {
      // extract
      ItemStack popped = inventory.pop();
      if (!popped.isEmpty()) {
        ItemUtil.Spawn.spawnItem(level, getBlockPos(), popped);
      }
      // TODO: This only ever fires IF the hand is empty as otherwise the item consumes the event
    } else if (/*inHand.isEmpty() && */player.isCrouching()) {
      if (lastRecipe != null && inventory.isEmpty()) {
        if (RecipeUtil.refillRecipeFromPlayer((ServerPlayer) player, lastRecipe.value(), inventory)) {
          revalidateRecipe();
        }
      }
    } else if (inHand.is(RootsTags.Items.MORTAR_ACTIVATION) && !inventory.isEmpty()) {
      if (player.isFakePlayer() || !cooldowns.isOnCooldown(inHand.getItem())) {
        if (cachedRecipe == null) {
          revalidateRecipe();
        }
        // TODO: Provider better feedback to the player
        if (cachedRecipe != null && cachedRecipe.value().matches(playerlessCrafting, level)) {
          ConditionResult conditionResult = cachedRecipe.value()
              .checkConditions(level, player, PyreBlockEntity.getPyreBoundingBox(), pos);
          if (conditionResult.anyFailed()) {
            conditionResult.report(player);
            return InteractionResult.FAIL;
          }
          UnlockResult failedUnlocks = cachedRecipe.value().checkUnlocks(level, (ServerPlayer) player);
          if (failedUnlocks.anyFailed() && !cachedRecipe.value().hasOutput(level.registryAccess())) {
            RootsAPI.LOG.info("Grants failed and recipe has no output");
            failedUnlocks.failedUnlocks().forEach(o -> RootsAPI.LOG.info("Failed grant {}", o));
            failedUnlocks.report();
            return InteractionResult.FAIL;
          }

          uses++;
          getLevel().playSound(null, getBlockPos(), SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 1.0f, 0.6f);
          setChanged();
          if (!player.isFakePlayer() && ConfigManager.PESTLE_COOLDOWN.getAsInt() != -1) {
            cooldowns.addCooldown(inHand.getItem(), ConfigManager.PESTLE_COOLDOWN.getAsInt());
          }

          if (uses >= cachedRecipe.value().getTimes()) {
            MortarCrafting playerCrafting = new MortarCrafting(inventory, this, player);
            lastRecipe = cachedRecipe;
            previousRecipeItems.clear();
            previousRecipeItems.addAll(inventory.getItemsCopy());
            List<ItemStack> results = cachedRecipe.value()
                .assembleOutputs(playerCrafting, level.getRandom(), level.registryAccess(), inventory::getItemsAndClear);
            for (ItemStack stack : results) {
              CraftItemAction.Context context = new CraftItemAction.Context(
                  (ServerLevel) this.getLevel(),
                  (ServerPlayer) player,
                  stack
              );
              ModActions.CRAFT_ITEM.get().accept(context);
            }
            for (ItemStack stack : results) {
              ItemUtil.Spawn.spawnItem(level, player.blockPosition(), stack);
            }
            CraftRecipeAction.Context context = new CraftRecipeAction.Context(
                (ServerLevel) level,
                (ServerPlayer) player,
                lastRecipe.id(),
                lastRecipe.value(),
                this
            );
            ModActions.CRAFT_RECIPE.get().accept(context);
            uses = -1;
            cachedRecipe = null;
          }

          updateViaState();
        }
      } else {
        return InteractionResult.FAIL;
      }
    } else if ((!onlyMortar() && !cooldowns.isOnCooldown(inHand.getItem())) || (!onlyMortar() && inventory.isEmpty() && inHand.is(RootsTags.Items.MORTAR_ACTIVATION))) {
      // insert
      player.setItemInHand(hand, inventory.insert(inHand));
    } else {
      return InteractionResult.FAIL;
    }

    return InteractionResult.SUCCESS;
  }

  @Override
  public void onLoad() {
    super.onLoad();
    revalidateRecipe();
  }

  public int getUses() {
    return uses;
  }

  @Override
  public MortarInventory getInventory() {
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
  public void serverTick(ServerLevel pLevel, BlockPos pPos, BlockState pState) {
    boolean changed = tryRefill(pLevel, pPos.below());
    if (changed) {
      updateViaState();
    }
  }

  @Override
  public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
    return new MortarContainer(containerId, playerInventory, inventory, ContainerLevelAccess.create(getLevel(), getBlockPos()));
  }

  @Override
  public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buffer) {
    FakeMenuBlockEntity.super.writeClientSideData(menu, buffer);
    buffer.writeBlockPos(getBlockPos());
  }

  @Override
  public void clearContents() {
    ItemUtil.Spawn.spawnItems(level, getBlockPos(), inventory.getItemsAndClear());
  }

  @Override
  public boolean canClear() {
    return !inventory.isEmpty();
  }

  @Override
  public boolean shouldShowInsert() {
    if (inventory.isEmpty()) {
      return true;
    }

    if (getCachedRecipe() != null) {
      return true;
    }

    return false;
  }

  @Override
  public double getInsertOffset() {
    return 1.0;
  }

  @Override
  public double getDeleteOffset() {
    return 1.4;
  }
}
