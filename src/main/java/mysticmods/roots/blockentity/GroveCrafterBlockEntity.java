package mysticmods.roots.blockentity;

import com.mojang.datafixers.util.Pair;
import mysticmods.roots.action.CraftItemAction;
import mysticmods.roots.action.CraftRecipeAction;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.blockentity.ClearableBlockEntity;
import mysticmods.roots.api.blockentity.ClientTickBlockEntity;
import mysticmods.roots.api.blockentity.FakeMenuBlockEntity;
import mysticmods.roots.api.blockentity.ServerTickBlockEntity;
import mysticmods.roots.api.condition.GroveType;
import mysticmods.roots.api.recipe.ConditionResult;
import mysticmods.roots.api.recipe.UnlockResult;
import mysticmods.roots.api.reference.Constants;
import mysticmods.roots.block.GroveCrafterBlock;
import mysticmods.roots.blockentity.template.UseDelegatedBlockEntity;
import mysticmods.roots.condition.GroveStoneCondition;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModActions;
import mysticmods.roots.init.ModBlockEntities;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.inventory.fake.GroveContainer;
import mysticmods.roots.network.client.fx.StartGroveCraftingFX;
import mysticmods.roots.particle.RootsParticleOptions;
import mysticmods.roots.recipe.TaggedPedestalCrafting;
import mysticmods.roots.recipe.grove.GroveCrafting;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.util.ItemUtil;
import net.minecraft.core.BlockPos;
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
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.*;

public class GroveCrafterBlockEntity extends UseDelegatedBlockEntity implements ServerTickBlockEntity, ClientTickBlockEntity, FakeMenuBlockEntity, ClearableBlockEntity {
  private RecipeHolder<GroveRecipe> lastRecipe = null;
  private RecipeHolder<GroveRecipe> cachedRecipe = null;

  private final List<ItemStack> storedItems = new ArrayList<>();
  private int craftingTicks = 0;

  private Player lastPlayer;
  private UUID lastUuid;

  private boolean revalidatedRecipes = false;

  public GroveCrafterBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
  }

  public GroveCrafterBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
    super(ModBlockEntities.GROVE_CRAFTER.get(), pWorldPosition, pBlockState);
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
    lastPlayer = null;
    lastUuid = null;
    level.setBlock(getBlockPos(), getBlockState().setValue(GroveCrafterBlock.CRAFTING, false), 3);
  }

  public boolean isCrafting() {
    return craftingTicks > 0;
  }

  public float getCraftingProgress() {
    if (craftingTicks <= 0) {
      return 1f;
    }
    return 1f - Mth.clamp((float) craftingTicks / Constants.GROVE_CRAFTING_ANIMATION_TICKS, 0f, 1f);
  }

  protected List<Pair<BlockPos, PedestalBlockEntity>> getPedestals() {
    return pedestals(RootsTags.Blocks.GROVE_PEDESTALS, RootsTags.Blocks.DISPLAY_PEDESTALS, false);
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult ray, InteractionHand hand, ItemStack inHand) {
    if (level.isClientSide()) {
      return InteractionResult.CONSUME;
    }

    if (!state.getValue(GroveCrafterBlock.ACTIVE)) {
      return InteractionResult.PASS;
    }

    if (isCrafting()) {
      return InteractionResult.PASS;
    }

    if (inHand.isEmpty() || inHand.is(RootsTags.Items.GROVE_CRAFTER_ACTIVATION)) {
      GroveCrafting playerCrafting = getCrafting(player, false);
      if (cachedRecipe == null) {
        cachedRecipe = ResolvedRecipes.GROVE.findRecipe(playerCrafting, getLevel());
      }
      if (cachedRecipe == null) {
        return InteractionResult.FAIL;
      }
      // TODO: Provider better feedback to the player
      ConditionResult conditionResult = cachedRecipe.value()
          .checkConditions(level, player, PyreBlockEntity.getPyreBoundingBox(), pos);
      if (conditionResult.anyFailed()) {
        conditionResult.report(player);
        return InteractionResult.FAIL;
      }
      UnlockResult failedGrants = cachedRecipe.value().checkUnlocks(level, (ServerPlayer) player);
      if (failedGrants.anyFailed() && !cachedRecipe.value().hasOutput(level.registryAccess())) {
        failedGrants.report();
        return InteractionResult.FAIL;
      }
      lastPlayer = player;
      lastUuid = null;
      lastRecipe = cachedRecipe;
      // TODO: We never cleared storedItems here, unlike the pyre
      List<TaggedPedestalCrafting.ItemPosition> positions = playerCrafting.getItemsAndPositions();
      if (player instanceof FakePlayer) {
        PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, new ChunkPos(getBlockPos()), new StartGroveCraftingFX(getBlockPos(), positions));
      } else {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new StartGroveCraftingFX(getBlockPos(), positions));
      }
      List<ItemStack> results = cachedRecipe.value()
          .assembleOutputs(playerCrafting, level.getRandom(), level.registryAccess(), playerCrafting::popAndAnimateItems);
      storedItems.addAll(results);
      this.craftingTicks = Constants.GROVE_CRAFTING_ANIMATION_TICKS;
      cachedRecipe = null;
      if (!level.isClientSide()) {
        setChanged();
        level.setBlock(getBlockPos(), getBlockState().setValue(GroveCrafterBlock.CRAFTING, true), 3);
        // I think the setBlock is sufficient because it's changing states
        //updateViaState();
      }

      return InteractionResult.SUCCESS;
    }

    return InteractionResult.PASS;
  }

  public static final GroveStoneCondition ANY_VALID_GROVE_STONE = new GroveStoneCondition(GroveType.ANY, true);

  protected void revalidateRecipe() {
    if (getLevel() == null) {
      return;
    }

    boolean changed = false;

    if (cachedRecipeId != null) {
      cachedRecipe = ResolvedRecipes.GROVE.getRecipe(getLevel(), cachedRecipeId);
      if (cachedRecipe != null) {
        cachedRecipeId = null;
        changed = true;
      }
    }
    if (lastRecipeId != null) {
      lastRecipe = ResolvedRecipes.GROVE.getRecipe(getLevel(), lastRecipeId);
      if (lastRecipe != null) {
        lastRecipeId = null;
        changed = true;
      }
    }

    boolean active = getBlockState().getValue(GroveCrafterBlock.ACTIVE);
    Set<BlockPos> groveStones = ANY_VALID_GROVE_STONE
        .test(getLevel(), null, PyreBlockEntity.getPyreBoundingBox(), getBlockPos(), Collections.emptySet());
    if (groveStones.isEmpty() && active) {
      getLevel().setBlock(getBlockPos(), getBlockState().setValue(GroveCrafterBlock.ACTIVE, false), 3);
    } else if (!groveStones.isEmpty() && !active) {
      getLevel().setBlock(getBlockPos(), getBlockState().setValue(GroveCrafterBlock.ACTIVE, true), 3);
    }

    List<Pair<BlockPos, PedestalBlockEntity>> pedestals = getPedestals();
    if (pedestals.isEmpty()) {
      cachedRecipe = null;
      if (!getLevel().isClientSide()) {
        setChanged();
        updateViaState();
      }
      return;
    }
    GroveCrafting playerlessCrafting = getCrafting(null, false); //new GroveCrafting(this, null);

    if (cachedRecipe == null) {
      cachedRecipe = ResolvedRecipes.GROVE.findRecipe(playerlessCrafting, getLevel());
      if (cachedRecipe != null) {
        changed = true;
      }
    } else {
      if (!cachedRecipe.value().matches(playerlessCrafting, getLevel())) {
        cachedRecipe = null;
        changed = true;
      }
    }

    if (changed && !getLevel().isClientSide()) {
      setChanged();
      updateViaState();
    }
  }

  public GroveCrafting getCrafting(@Nullable Player player, boolean allowEmpty) {
    return new GroveCrafting(this, player, allowEmpty);
  }

  @Override
  public int getRadiusX() {
    return 3;
  }

  @Override
  public int getRadiusY() {
    return 2;
  }

  @Override
  public int getRadiusZ() {
    return 3;
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
    if (!storedItems.isEmpty()) {
      pTag.put("stored_items", storedItems);
    }
    pTag.putInt("crafting_ticks", craftingTicks);
    if (lastPlayer != null) {
      pTag.putUUID("last_player", lastPlayer.getUUID());
    } else if (lastUuid != null) {
      pTag.putUUID("last_player", lastUuid);
    }
  }

  private ResourceLocation cachedRecipeId = null;
  private ResourceLocation lastRecipeId = null;

  @Override
  public void loadAdditional(CompoundTag pTag, HolderLookup.Provider lookup) {
    super.loadAdditional(pTag, lookup);
    cachedRecipe = null;
    if (pTag.contains("cached_recipe", Tag.TAG_STRING)) {
      cachedRecipeId = RootsAPI.parse(pTag.getString("cached_recipe"));
    }
    lastRecipe = null;
    if (pTag.contains("last_recipe", Tag.TAG_STRING)) {
      lastRecipeId = RootsAPI.parse(pTag.getString("last_recipe"));
    }
    if (!storedItems.isEmpty()) {
      outputStoredItems(getLastPlayer());
    }
    storedItems.clear();
    if (pTag.contains("stored_items", Tag.TAG_LIST)) {
      ListTag incomingStoredItems = pTag.getList("stored_items", Tag.TAG_COMPOUND);
      for (int i = 0; i < incomingStoredItems.size(); i++) {
        ItemStack.parse(lookup, incomingStoredItems.getCompound(i)).ifPresent(storedItems::add);
      }
    }
    if (pTag.hasUUID("last_player")) {
      lastUuid = pTag.getUUID("last_player");
      if (getLevel() != null) {
        lastPlayer = getLevel().getPlayerByUUID(lastUuid);
      }
    }
    this.craftingTicks = pTag.getInt("crafting_ticks");
    this.revalidatedRecipes = false;
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

  @Nullable
  public RecipeHolder<GroveRecipe> getRecipe() {
    return cachedRecipe;
  }

  @Nullable
  public RecipeHolder<GroveRecipe> getCurrentRecipe() {
    if (craftingTicks == 0) {
      return getRecipe();
    } else {
      return lastRecipe;
    }
  }

  @Override
  public void serverTick(ServerLevel pLevel, BlockPos pPos, BlockState pState) {
    revalidateRecipe();
    if (craftingTicks > 0) {
      if (pState.getValue(GroveCrafterBlock.CRAFTING) != isCrafting()) {
        level.setBlock(pPos, pState.setValue(GroveCrafterBlock.CRAFTING, isCrafting()), 3);
      }
      craftingTicks--;
      if (craftingTicks == 0) {
        outputStoredItems(getLastPlayer());
      }
      setChanged();
      updateViaState();
    }
/*    if (pLevel.getServer().getTickCount() % 8 == 0) {
      PacketDistributor.sendToPlayersNear(pLevel, null, pPos.getX(), pPos.getY(), pPos.getZ(), 32, new SpiralFXPacket(pPos, 0.5, (Mth.TWO_PI / 256), 0xc1f3ee, 0xe5ff23));
    }*/
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookup) {
    super.onDataPacket(net, pkt, lookup);
    CompoundTag tag = pkt.getTag();
    if (tag != null) {
      loadAdditional(tag, lookup);
      revalidateRecipe();
    } else {
      lastRecipe = null;
      cachedRecipe = null;
    }
  }

  @Override
  public void clientTick(Level pLevel, BlockPos pPos, BlockState pState) {
    if (!revalidatedRecipes) {
      revalidateRecipe();
      revalidatedRecipes = true;
    }
    if (ConfigManager.DISABLE_PATICLES.get()) {
      return;
    }
    if (isCrafting() && craftingTicks % 4 == 0) {
      float progress = 1.0f - (craftingTicks / (float) Constants.GROVE_CRAFTING_ANIMATION_TICKS); // 0 to 1
      double spread = 0.1 + progress * 0.4; // dynamic radius

      int tickCount = (Constants.GROVE_CRAFTING_ANIMATION_TICKS - craftingTicks) / 4;
      int particleCount = 5 + pLevel.random.nextInt(10) + pLevel.random.nextInt(Math.max(tickCount, 1));

      if (craftingTicks <= 24 && craftingTicks > 16) {
        particleCount += 20 + pLevel.random.nextInt(20);
        spread += 0.3;
      }

      for (int i = 0; i < particleCount; i++) {
        double x = pPos.getX() + 0.5 + (pLevel.random.nextDouble() - 0.5) * spread;
        double y = pPos.getY() + 0.5;
        double z = pPos.getZ() + 0.5 + (pLevel.random.nextDouble() - 0.5) * spread;

        level.addParticle(RootsParticleOptions.builder(ModParticles.GROVE_CRAFTER).build(),
            x, y, z,
            0, (pLevel.random.nextDouble() * 0.05), 0
        );
      }
    }
  }

  @Override
  public void clearContents() {
    getPedestals().forEach(entry -> {
      entry.getSecond().dropContents();
    });
  }

  @Override
  public boolean canClear() {
    return !getPedestals().isEmpty();
  }

  @Override
  public boolean shouldShowInsert() {
    return getRecipe() == null && !isCrafting();
  }

  @Override
  public @org.jetbrains.annotations.Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
    return new GroveContainer(containerId, playerInventory, this.getCrafting(player, true), ContainerLevelAccess.create(getLevel(), getBlockPos()));
  }

  @Override
  public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buffer) {
    buffer.writeBlockPos(getBlockPos());
  }
}
