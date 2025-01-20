package mysticmods.roots.blockentity;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.blockentity.InventoryBlockEntity;
import mysticmods.roots.api.recipe.ConditionResult;
import mysticmods.roots.api.recipe.UnlockResult;
import mysticmods.roots.blockentity.template.UseDelegatedBlockEntity;
import mysticmods.roots.init.ModBlockEntities;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.recipe.mortar.MortarCrafting;
import mysticmods.roots.recipe.mortar.MortarInventory;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.util.ItemUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public class MortarBlockEntity extends UseDelegatedBlockEntity implements InventoryBlockEntity {
  private final MortarInventory inventory = new MortarInventory() {
    @Override
    protected void onContentsChanged(int slot) {
      if (MortarBlockEntity.this.hasLevel() && !MortarBlockEntity.this.getLevel().isClientSide()) {
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

  public MortarBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
  }

  public MortarBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
    super(ModBlockEntities.MORTAR.get(), pWorldPosition, pBlockState);
  }

  protected void revalidateRecipe() {
    boolean matched = false;
    if (cachedRecipe == null) {
      uses = -1;
      if (lastRecipe != null && lastRecipe.value().matches(playerlessCrafting, getLevel())) {
        cachedRecipe = lastRecipe;
        matched = true;
      } else {
        cachedRecipe = ResolvedRecipes.MORTAR.findRecipe(playerlessCrafting, getLevel());
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
  }

  @Override
  protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
    super.saveAdditional(pTag, provider);
    // TODO: reference this
    pTag.putInt("uses", uses);
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
      pTag.put("previous_items", previousItems);
    }

    if (cachedRecipe != null) {
      pTag.putString("cached_recipe", cachedRecipe.id().toString());
    }
    if (lastRecipe != null) {
      pTag.putString("last_recipe", lastRecipe.id().toString());
    }
    pTag.put("inventory", inventory.serializeNBT(provider));
  }

  @Override
  public void loadAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
    super.loadAdditional(pTag, provider);
    // TODO: reference this
    if (pTag.contains("uses", Tag.TAG_INT)) {
      this.uses = pTag.getInt("uses");
    }
    if (pTag.contains("previous_items", Tag.TAG_LIST)) {
      previousRecipeItems.clear();
      ListTag previousItems = pTag.getList("previous_items", Tag.TAG_COMPOUND);
      for (int i = 0; i < previousItems.size(); i++) {
        ItemStack.parse(provider, previousItems.getCompound(i)).ifPresent(previousRecipeItems::add);
      }
    }
    if (pTag.contains("cached_recipe", Tag.TAG_STRING)) {
      ResourceLocation cachedId = ResourceLocation.parse(pTag.getString("cached_recipe"));
      cachedRecipe = ResolvedRecipes.MORTAR.getRecipe(cachedId);
    } else {
      cachedRecipe = null;
    }
    if (pTag.contains("last_recipe", Tag.TAG_STRING)) {
      ResourceLocation lastId = ResourceLocation.parse(pTag.getString("last_recipe"));
      lastRecipe = ResolvedRecipes.MORTAR.getRecipe(lastId);
    } else {
      cachedRecipe = null;
    }
    if (pTag.contains("inventory", Tag.TAG_COMPOUND)) {
      inventory.deserializeNBT(provider, pTag.getCompound("inventory"));
    }
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider provider) {
    super.onDataPacket(net, pkt, provider);
    CompoundTag tag = pkt.getTag();
    if (tag != null) {
      loadAdditional(tag, provider);
    }
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult ray, InteractionHand hand, ItemStack inHand) {
    if (level.isClientSide()) {
      return InteractionResult.CONSUME;
    }
    if (inHand.isEmpty() && !player.isCrouching()) {
      // extract
      ItemStack popped = inventory.pop();
      if (!popped.isEmpty()) {
        ItemUtil.Spawn.spawnItem(level, getBlockPos(), popped);
      }
    } else if (inHand.isEmpty() && player.isCrouching()) {
      if (lastRecipe != null) {
        refillRecipe((ServerPlayer) player, lastRecipe, inventory);
      }
    } else if (inHand.is(RootsTags.Items.MORTAR_ACTIVATION)) {
      if (cachedRecipe == null) {
        revalidateRecipe();
      }
      // TODO: Provider better feedback to the player
      if (cachedRecipe != null && cachedRecipe.value().matches(playerlessCrafting, level)) {
        ConditionResult conditionResult = cachedRecipe.value().checkConditions(level, player, PyreBlockEntity.PYRE_BOUNDS, pos);
        if (conditionResult.anyFailed()) {
          RootsAPI.LOG.info("Conditions failed.");
          conditionResult.failedLevelConditions().forEach(o -> RootsAPI.LOG.info("Failed: " + o.getDescriptionId()));
          conditionResult.failedPlayerConditions().forEach(o -> RootsAPI.LOG.info("Failed: " + o.getDescriptionId()));
          conditionResult.report();
          return InteractionResult.FAIL;
        }
        UnlockResult failedUnlocks = cachedRecipe.value().checkUnlocks(level, (ServerPlayer) player);
        if (failedUnlocks.failed() && !cachedRecipe.value().hasOutput(level.registryAccess())) {
          RootsAPI.LOG.info("Grants failed and recipe has no output");
          failedUnlocks.failedUnlocks().forEach(o -> RootsAPI.LOG.info("Failed grant {}", o));
          failedUnlocks.report();
          return InteractionResult.FAIL;
        }

        uses++;
        setChanged();

        if (uses >= cachedRecipe.value().getTimes()) {
          MortarCrafting playerCrafting = new MortarCrafting(inventory, this, player);
          lastRecipe = cachedRecipe;
          previousRecipeItems.clear();
          previousRecipeItems.addAll(inventory.getItemsCopy());
          List<ItemStack> results = cachedRecipe.value().assembleOutputs(playerCrafting, level.getRandom(), level.registryAccess(), inventory::getItemsAndClear);
          for (ItemStack stack : results) {
            ItemUtil.Spawn.spawnItem(level, player.blockPosition(), stack);
          }
          uses = -1;
          cachedRecipe = null;
        }

        updateViaState();
      }
    } else {
      // insert
      player.setItemInHand(hand, inventory.insert(inHand));
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
  public ItemStackHandler getInventory() {
    return inventory;
  }
}
