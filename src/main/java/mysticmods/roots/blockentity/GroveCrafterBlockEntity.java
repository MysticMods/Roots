package mysticmods.roots.blockentity;

import com.mojang.datafixers.util.Pair;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.blockentity.ServerTickBlockEntity;
import mysticmods.roots.api.recipe.ConditionResult;
import mysticmods.roots.api.recipe.UnlockResult;
import mysticmods.roots.block.GroveCrafterBlock;
import mysticmods.roots.blockentity.template.UseDelegatedBlockEntity;
import mysticmods.roots.init.ModBlockEntities;
import mysticmods.roots.init.ModConditions;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.recipe.grove.GroveCrafting;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.util.ItemUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GroveCrafterBlockEntity extends UseDelegatedBlockEntity implements ServerTickBlockEntity {
  private RecipeHolder<GroveRecipe> lastRecipe = null;
  private RecipeHolder<GroveRecipe> cachedRecipe = null;

  public GroveCrafterBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
  }

  public GroveCrafterBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
    super(ModBlockEntities.GROVE_CRAFTER.get(), pWorldPosition, pBlockState);
  }

  @Override
  protected boolean canOutputTo(BlockState state, BlockPos pos) {
    return !state.is(RootsTags.Blocks.PEDESTALS);
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult ray, InteractionHand hand, ItemStack inHand) {
    if (level.isClientSide()) {
      return InteractionResult.CONSUME;
    }

    if (!state.getValue(GroveCrafterBlock.ACTIVE)) {
      return InteractionResult.FAIL;
    }

    if (inHand.isEmpty() || inHand.is(RootsTags.Items.GROVE_CRAFTER_ACTIVATION)) {
      GroveCrafting playerCrafting = new GroveCrafting(this, player);
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
        RootsAPI.LOG.info("Conditions failed.");
        conditionResult.failedLevelConditions().forEach(o -> RootsAPI.LOG.info("Failed: " + o.getDescriptionId()));
        conditionResult.failedPlayerConditions().forEach(o -> RootsAPI.LOG.info("Failed: " + o.getDescriptionId()));
        conditionResult.report(player);
        return InteractionResult.FAIL;
      }
      UnlockResult failedGrants = cachedRecipe.value().checkUnlocks(level, (ServerPlayer) player);
      if (failedGrants.anyFailed() && !cachedRecipe.value().hasOutput(level.registryAccess())) {
        failedGrants.report();
        return InteractionResult.FAIL;
      }
      lastRecipe = cachedRecipe;
      List<ItemStack> results = cachedRecipe.value()
          .assembleOutputs(playerCrafting, level.getRandom(), level.registryAccess(), playerCrafting::popItems);
      for (ItemStack stack : this.outputAdjacent(results)) {
        ItemUtil.Spawn.spawnItem(level, player.blockPosition(), stack);
      }
      cachedRecipe = null;
      setChanged();
      updateViaState();

      return InteractionResult.SUCCESS;
    }

    return InteractionResult.FAIL;
  }

  protected void revalidateRecipe() {
    if (getLevel() == null) {
      return;
    }

    if (cachedRecipeId != null) {
      cachedRecipe = ResolvedRecipes.GROVE.getRecipe(getLevel(), cachedRecipeId);
      cachedRecipeId = null;
    }
    if (lastRecipeId != null) {
      lastRecipe = ResolvedRecipes.GROVE.getRecipe(getLevel(), lastRecipeId);
      lastRecipeId = null;
    }

    boolean active = getBlockState().getValue(GroveCrafterBlock.ACTIVE);
    if (getBoundingBox() != null) {
      Set<BlockPos> groveStones = ModConditions.GROVE_STONE_ACTIVE.get()
          .test(getLevel(), null, PyreBlockEntity.getPyreBoundingBox(), getBlockPos(), Collections.emptySet());
      if (groveStones.isEmpty() && active) {
        getLevel().setBlock(getBlockPos(), getBlockState().setValue(GroveCrafterBlock.ACTIVE, false), 3);
      } else if (!groveStones.isEmpty() && !active) {
        getLevel().setBlock(getBlockPos(), getBlockState().setValue(GroveCrafterBlock.ACTIVE, true), 3);
      }
    }

    List<Pair<BlockPos, PedestalBlockEntity>> pedestals = pedestals(RootsTags.Blocks.GROVE_PEDESTALS, RootsTags.Blocks.DISPLAY_PEDESTALS);
    if (pedestals.isEmpty()) {
      cachedRecipe = null;
      setChanged();
      updateViaState();
      return;
    }
    GroveCrafting playerlessCrafting = new GroveCrafting(this, null);
    boolean changed = false;
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

    if (changed) {
      setChanged();
      updateViaState();
    }
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

/*  @Override
  public void notify(ServerLevel pLevel, BlockPos pPos) {
    if (pedestalPositions == null) {
      pedestalPositions = new ArrayList<>();
    } else {
      pedestalPositions.clear();
    }
    BlockPos.betweenClosedStream(getBoundingBox()).forEach(pos -> {
      BlockState state = pLevel.getBlockState(pos);
      if (state.is(RootsTags.Blocks.GROVE_PEDESTALS)) {
        if (!state.getValue(PedestalBlock.VALID)) {
*//*          if (pLevel.getBlockEntity(pos) instanceof PedestalBlockEntity pedestal) {*//*
   *//*            if (!pedestal.getHeldItem().isEmpty()) {*//*
              pLevel.setBlock(pos, state.setValue(PedestalBlock.VALID, true), 1 | 2 | 8);
              pedestalPositions.add(pos.immutable());
*//*            }*//*
   *//*          }*//*
        } else {
          pedestalPositions.add(pos.immutable());
        }
      }
    });
    revalidateRecipe();
  }*/

  @Override
  protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider lookup) {
    super.saveAdditional(pTag, lookup);
    if (cachedRecipe != null) {
      pTag.putString("cached_recipe", cachedRecipe.id().toString());
    }
    if (lastRecipe != null) {
      pTag.putString("last_recipe", lastRecipe.id().toString());
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
  }

  @Nullable
  public RecipeHolder<GroveRecipe> getRecipe() {
    return cachedRecipe;
  }

  @Override
  public void serverTick(ServerLevel pLevel, BlockPos pPos, BlockState pState) {
    revalidateRecipe();
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookup) {
    super.onDataPacket(net, pkt, lookup);
    CompoundTag tag = pkt.getTag();
    if (tag != null) {
      loadAdditional(tag, lookup);
    } else {
      lastRecipe = null;
      cachedRecipe = null;
    }
  }
}
