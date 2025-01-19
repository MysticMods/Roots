package mysticmods.roots.blockentity;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.blockentity.ClientTickBlockEntity;
import mysticmods.roots.api.blockentity.InventoryBlockEntity;
import mysticmods.roots.api.blockentity.ServerTickBlockEntity;
import mysticmods.roots.api.recipe.ConditionResult;
import mysticmods.roots.api.recipe.UnlockResult;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.block.PyreBlock;
import mysticmods.roots.blockentity.template.UseDelegatedBlockEntity;
import mysticmods.roots.init.ModBlockEntities;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.recipe.pyre.PyreCrafting;
import mysticmods.roots.recipe.pyre.PyreInventory;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import mysticmods.roots.util.ItemUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PyreBlockEntity extends UseDelegatedBlockEntity implements ClientTickBlockEntity, ServerTickBlockEntity, InventoryBlockEntity {
  private final PyreInventory inventory = new PyreInventory() {
    @Override
    protected void onContentsChanged(int slot) {
      if (PyreBlockEntity.this.hasLevel() && !PyreBlockEntity.this.getLevel().isClientSide()) {
        PyreBlockEntity.this.revalidateRecipe();
        PyreBlockEntity.this.updateViaState();
      }
    }
  };

  // TODO: UNHARDCODE THIS
  public final static BoundingBox PYRE_BOUNDS = new BoundingBox(-10, -10, -10, 11, 11, 11);

  private final PyreCrafting playerlessCrafting = new PyreCrafting(inventory, this, null);
  private final List<ItemStack> previousRecipeItems = new ArrayList<>();
  private RecipeHolder<PyreRecipe> lastRecipe = null;
  private RecipeHolder<PyreRecipe> cachedRecipe = null;
  private Ritual currentRitual = null;
  private final List<ItemStack> storedItems = new ArrayList<>();
  private int lifetime = -1;

  public PyreBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
  }

  public PyreBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
    super(ModBlockEntities.PYRE.get(), pWorldPosition, pBlockState);
  }

  @Override
  public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult ray) {
    // TODO:
    InteractionHand hand = InteractionHand.MAIN_HAND;
    ItemStack inHand = player.getItemInHand(hand);
    // This is a very specific hack.
    if (inHand.is(ModItems.FIRE_STARTER.get())) {
      return InteractionResult.PASS;
    }
    if (level.isClientSide()) {
      return InteractionResult.CONSUME;
    }
    if (inHand.isEmpty()) {
      // extract
      ItemStack popped = inventory.pop();
      if (!popped.isEmpty()) {
        ItemUtil.Spawn.spawnItem(level, getBlockPos(), popped);
      }
      // TODO: starting a ritual while one is already active
    } else if (inHand.is(RootsTags.Items.PYRE_ACTIVATION)) {
      return light(player, pos);
    } else {
      // insert
      ItemStack result = inventory.insert(inHand);
      // ??? TODO
      // it was result, false
      if (inHand.equals(result)) {
        return InteractionResult.PASS;
      }
      player.setItemInHand(hand, result);
    }

    return InteractionResult.SUCCESS;
  }

  public InteractionResult light(Player player, BlockPos pos) {
    if (cachedRecipe == null) {
      // should this revalidate?
      revalidateRecipe();
    }
    if (cachedRecipe != null && cachedRecipe.value().matches(playerlessCrafting, level)) {
      Ritual newRitual = cachedRecipe.value().getRitual();
      if (newRitual == null) {
        currentRitual = ModRituals.CRAFTING.get();
      } else {
        currentRitual = newRitual;
      }
      boundingBox = null;

      // TODO: Provider better feedback to the player
      ConditionResult result = cachedRecipe.value().checkConditions(level, player, PYRE_BOUNDS, pos);
      if (result.anyFailed()) {
        RootsAPI.LOG.info("Conditions failed.");
        result.failedLevelConditions().forEach(o -> RootsAPI.LOG.info("Failed: " + o.getDescriptionId()));
        result.failedPlayerConditions().forEach(o -> RootsAPI.LOG.info("Failed: " + o.getDescriptionId()));
        result.report();
        // Needs to be a success or it sets things on fire
        return InteractionResult.SUCCESS;
      }
      UnlockResult failedGrants = cachedRecipe.value().checkUnlocks(level, (ServerPlayer) player);
      if (failedGrants.failed() && !cachedRecipe.value().hasOutput(level.registryAccess())) {
        RootsAPI.LOG.info("Grants failed and recipe has no output");
        /*        failedUnlocks.failedUnlocks().forEach(o -> RootsAPI.LOG.info("Failed grant of type " + o.type().name() + " with id " + o.id()));*/
        failedGrants.report();
        return InteractionResult.SUCCESS;
      }

      PyreCrafting playerCrafting = new PyreCrafting(inventory, this, player);
      lastRecipe = cachedRecipe;
      previousRecipeItems.clear();
      previousRecipeItems.addAll(inventory.getItemsCopy());
      storedItems.clear();
      if (currentRitual == ModRituals.CRAFTING.get()) {
        // TODO: Item could be empty with only chance outputs
        storedItems.addAll(cachedRecipe.value().assembleOutputs(playerCrafting, level.getRandom(), level.registryAccess(), null));
      }
      storedItems.removeIf(ItemStack::isEmpty);
      // process
      NonNullList<ItemStack> processed = cachedRecipe.value().process(inventory.getItemsAndClear());
      for (ItemStack stack : processed) {
        ItemUtil.Spawn.spawnItem(level, player.blockPosition(), stack);
      }
      cachedRecipe = null;
      startRitual(player);
      setChanged();
      updateViaState();
    }

    return InteractionResult.SUCCESS;
  }

  public void startRitual(Player player) {
    if (currentRitual != null) {
      this.lifetime = currentRitual.getDuration();
    } else {
      RootsAPI.LOG.error("tried to start a ritual but the ritual is null");
    }
  }

  public RecipeHolder<PyreRecipe> getCachedRecipe () {
    return cachedRecipe;
  }

  public RecipeHolder<PyreRecipe> getLastRecipe () {
    return lastRecipe;
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider provider) {
    super.onDataPacket(net, pkt, provider);
    CompoundTag tag = pkt.getTag();
    if (tag != null) {
      loadAdditional(tag, provider);
    } else {
      lastRecipe = null;
      cachedRecipe = null;
      currentRitual = null;
      storedItems.clear();
    }
  }

  protected void revalidateRecipe() {
    boolean matched = false;
    if (cachedRecipe == null) {
      if (lastRecipe != null && lastRecipe.value().matches(playerlessCrafting, getLevel())) {
        cachedRecipe = lastRecipe;
        matched = true;
      } else {
        cachedRecipe = ResolvedRecipes.PYRE.findRecipe(playerlessCrafting, getLevel());
      }
    }

    if (cachedRecipe != null) {
      // only test once
      if (!matched && !cachedRecipe.value().matches(playerlessCrafting, getLevel())) {
        cachedRecipe = null;
      }
    }
  }

  @Override
  protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
    super.saveAdditional(pTag, provider);
    ListTag previousItems = new ListTag();
    for (ItemStack stack : previousRecipeItems) {
      if (!stack.isEmpty()) {
        previousItems.add(stack.save(provider, new CompoundTag()));
      }
    }

    if (!previousItems.isEmpty()) {
      pTag.put("previous_items", previousItems);
    }

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
  }

  @Override
  public void loadAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
    super.loadAdditional(pTag, provider);
    previousRecipeItems.clear();
    if (pTag.contains("previous_items", Tag.TAG_LIST)) {
      ListTag previousItems = pTag.getList("previous_items", Tag.TAG_COMPOUND);
      for (int i = 0; i < previousItems.size(); i++) {
        ItemStack.parse(provider, previousItems.getCompound(i)).ifPresent(previousRecipeItems::add);
      }
    }
    if (pTag.contains("cached_recipe", Tag.TAG_STRING)) {
      ResourceLocation cachedId = ResourceLocation.parse(pTag.getString("cached_recipe"));
      cachedRecipe = ResolvedRecipes.PYRE.getRecipe(cachedId);
    }
    if (pTag.contains("last_recipe", Tag.TAG_STRING)) {
      ResourceLocation lastId = ResourceLocation.parse(pTag.getString("last_recipe"));
      lastRecipe = ResolvedRecipes.PYRE.getRecipe(lastId);
    }
    if (pTag.contains("inventory", Tag.TAG_COMPOUND)) {
      inventory.deserializeNBT(provider, pTag.getCompound("inventory"));
    }
    if (pTag.contains("current_ritual", Tag.TAG_STRING)) {
      ResourceLocation ritualId = ResourceLocation.parse(pTag.getString("current_ritual"));
      currentRitual = RootsRegistries.RITUALS.get(ritualId);
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
  }

  @Override
  public ItemStackHandler getInventory() {
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

  @Override
  // TODO: handle client ticking
  public void clientTick(Level pLevel, BlockPos pPos, BlockState pState) {
    RandomSource pRandom = pLevel.getRandom();
    if (pState.is(RootsTags.Blocks.PYRES) && pState.getValue(PyreBlock.LIT) && pRandom.nextInt(10) == 0) {
/*      Particles.create(ModParticles.FIERY_PARTICLE.get())
        .addVelocity(0.00525f * (pRandom.nextFloat() - 0.5f), 0, 0.00525f * (pRandom.nextFloat() - 0.5f))
        .setAlpha(1f, 0.6f)
        .setScale(1f + 0.2f * pRandom.nextFloat())
        .setColor(230 / 255.0f, 55 / 255.0f, 16 / 255.0f, 230 / 255.0f, 83 / 255.0f, 16 / 255.0f)
        .setLifetime(50)
        .disableGravity()
        .setSpin(0)
        .spawn(pLevel, pPos.getX() + 0.5f + 0.3f * (pRandom.nextFloat() - 0.5f), pPos.getY() + 0.625f + 0.125f * pRandom.nextFloat(), pPos.getZ() + 0.5f + 0.3f * (pRandom.nextFloat() - 0.5f));*/
    }
    // ritual animation tick still happens ON THE SERVER
  }

  @Override
  public void serverTick(Level pLevel, BlockPos pPos, BlockState pState) {
    if (currentRitual != null && lifetime > 0) {
      lifetime--;
      setChanged();
      if (lifetime <= 0) {
        currentRitual = null;
        boundingBox = null;
        if (pState.is(RootsTags.Blocks.PYRES) && pState.hasProperty(PyreBlock.LIT)) {
          pLevel.setBlock(pPos, pState.setValue(PyreBlock.LIT, false), 3);
        } else {
          updateViaState();
        }
      } else {
        currentRitual.tick(pLevel, pPos, pState, this);
        if (pState.is(RootsTags.Blocks.PYRES) && pState.hasProperty(PyreBlock.LIT) && !pState.getValue(PyreBlock.LIT)) {
          pLevel.setBlock(pPos, pState.setValue(PyreBlock.LIT, true), 3);
        }
      }
    }
  }
}
