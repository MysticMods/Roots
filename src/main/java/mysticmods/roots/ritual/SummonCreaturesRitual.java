package mysticmods.roots.ritual;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.recipe.ComplexEntityType;
import mysticmods.roots.api.recipe.UnlockResult;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.recipe.pyre.PyrePedestalCrafting;
import mysticmods.roots.recipe.pyre.PyrePedestalRecipe;
import mysticmods.roots.recipe.pyre.SummonCreaturesRecipe;
import mysticmods.roots.util.ItemUtil;
import mysticmods.roots.util.PositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class SummonCreaturesRitual extends Ritual {
  @Override
  public void starts(Level pLevel, BlockPos pPos, BlockState pState, PyreBlockEntity blockEntity, RandomSource random) {
    super.starts(pLevel, pPos, pState, blockEntity, random);
    blockEntity.clearPedestalCrafting();
  }

  @Override
  public void ends(Level pLevel, BlockPos pPos, BlockState pState, PyreBlockEntity blockEntity, RandomSource random) {
    super.ends(pLevel, pPos, pState, blockEntity, random);
    blockEntity.clearPedestalCrafting();
  }

  @Override
  protected void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, @Nullable PositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    PyrePedestalCrafting crafting = blockEntity.getPedestalCrafting();
    PyrePedestalRecipe.PyrePedestalRecipeHolder cachedRecipe = blockEntity.getData(ModAttachments.CACHED_PEDESTAL_RECIPE);
    RecipeHolder<SummonCreaturesRecipe> recipe = null;
    boolean matchedCache = false;
    if (!cachedRecipe.isEmpty() && cachedRecipe.value() instanceof SummonCreaturesRecipe summonRecipe) {
      if (summonRecipe.matches(crafting, pLevel)) {
        recipe = new RecipeHolder<>(cachedRecipe.id(), summonRecipe);
        matchedCache = true;
      }
    }
    if (recipe == null) {
      recipe = ResolvedRecipes.SUMMON_CREATURES.findRecipe(crafting, pLevel);
    }
    if (!matchedCache) {
      if (recipe != null) {
        blockEntity.setData(ModAttachments.CACHED_PEDESTAL_RECIPE, PyrePedestalRecipe.of(recipe));
        blockEntity.setData(ModAttachments.CACHED_PYRE_ENTITY, recipe.value().getEntity());
      } else {
        blockEntity.setData(ModAttachments.CACHED_PEDESTAL_RECIPE, PyrePedestalRecipe.NULL);
        blockEntity.setData(ModAttachments.CACHED_PYRE_ENTITY, ComplexEntityType.EMPTY);
      }
    }

    if (duration % getInterval() == 0) {
      if (recipe != null && !recipe.value().getEntity().isEmpty()) {
        if (blockEntity.getLastPlayer() != null) {
          UnlockResult failedGrants = recipe.value().checkUnlocks(pLevel, (ServerPlayer) blockEntity.getLastPlayer());
          if (failedGrants.anyFailed() && !recipe.value().hasOutput(pLevel.registryAccess())) {
            RootsAPI.LOG.info("Grants failed and recipe has no output");
            // TODO:
            /*        failedUnlocks.failedUnlocks().forEach(o -> RootsAPI.LOG.info("Failed grant of type " + o.type().name() + " with id " + o.id()));*/
            failedGrants.report();
          }
        }

        // Handle any actual outputs, process inputs
        List<ItemStack> results = recipe.value()
            .assembleOutputs(crafting, randomSource, pLevel.registryAccess(), crafting::popItems);
        results = blockEntity.outputAdjacent(results);
        if (!results.isEmpty()) {
          for (ItemStack stack : results) {
            ItemUtil.Spawn.spawnItem(pLevel, pPos, stack);
          }
        }

        BlockPos boundPos = blockEntity.getBoundPosition();
        if (boundPos.equals(BlockPos.ZERO)) {
          boundPos = pPos.above(1);
        }

        // TODO: Some sanity checking of the bound position
        Entity entity = recipe.value().getEntity()
            .create((ServerLevel) pLevel, null, boundPos, MobSpawnType.EVENT, true, true);
        if (entity != null) {
          pLevel.addFreshEntity(entity);
        }
      }
    }
  }

  @Override
  protected void initialize(Holder<Ritual> holder) {

  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getDurationProperty() {
    return ModRituals.SUMMON_CREATURES_DURATION;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusXZProperty() {
    return null;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return null;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getIntervalProperty() {
    return ModRituals.SUMMON_CREATURES_INTERVAL;
  }
}
