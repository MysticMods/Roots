package mysticmods.roots.ritual;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.recipe.UnlockResult;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.recipe.pyre.PyrePedestalCrafting;
import mysticmods.roots.recipe.pyre.SummonCreaturesRecipe;
import mysticmods.roots.util.ItemUtil;
import mysticmods.roots.util.RitualPositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

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
  protected void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, RitualPositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    if (duration % getInterval() == 0) {
      PyrePedestalCrafting crafting = blockEntity.getPedestalCrafting();
      RecipeHolder<SummonCreaturesRecipe> recipe = ResolvedRecipes.SUMMON_CREATURES.findRecipe(crafting, pLevel);
      if (recipe != null && recipe.value().getEntity() != null) {
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
      }
    }
  }

  @Override
  protected void animationTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {

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
