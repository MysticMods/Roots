package mysticmods.roots.api.recipe;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.Unlock;
import mysticmods.roots.api.condition.ICondition;
import mysticmods.roots.api.condition.ILevelCondition;
import mysticmods.roots.api.condition.IPlayerCondition;
import mysticmods.roots.api.grove.power.consumer.PowerCollection;
import mysticmods.roots.api.recipe.crafting.IRootsCrafting;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.api.util.SetUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public interface IRootsRecipe<W extends RecipeInput> extends Recipe<W> {
  default NonNullList<ItemStack> process(List<ItemStack> ingredients) {
    NonNullList<ItemStack> result = NonNullList.create();
    for (ItemStack stack : ingredients) {
      if (stack.hasCraftingRemainingItem()) {
        result.add(stack.getCraftingRemainingItem());
      }
    }
    return result;
  }

  default List<Holder<ICondition>> getConditions() {
    return Collections.emptyList();
  }

  default List<ChanceOutput> getChanceOutputs() {
    return Collections.emptyList();
  }

  default List<ItemStack> getOtherOutputs() {
    return Collections.emptyList();
  }

  default List<Unlock<?>> getUnlocks() {
    return Collections.emptyList();
  }

  default PowerCollection getPowerRequirements() {
    return PowerCollection.EMPTY;
  }

  default ConditionResult checkConditions(Level level, Player player, BoundingBox bounds, BlockPos center) {
    List<Holder<ICondition>> failed = new ArrayList<>();
    Set<BlockPos> testedPositions = new HashSet<>();
    for (Holder<ICondition> condition : this.getConditions()) {
      if (condition.value() instanceof IPlayerCondition playerCondition) {
        if (!playerCondition.test(level, player)) {
          failed.add(condition);
        }
      } else if (condition.value() instanceof ILevelCondition levelCondition) {
        Set<BlockPos> newPositions = levelCondition.test(level, player, bounds, center, testedPositions);
        if (newPositions.isEmpty() || SetUtils.containsAny(testedPositions, newPositions)) {
          failed.add(condition);
        } else {
          testedPositions.addAll(newPositions);
        }
      }
    }

    return new ConditionResult(failed);
  }

  default UnlockResult checkUnlocks(Level level, ServerPlayer player) {
    List<Unlock<?>> result = new ArrayList<>();
    for (Unlock<?> unlock : getUnlocks()) {
      if (!RootsAPI.getInstance().canUnlock(player, unlock)) {
        result.add(unlock);
      }
    }

    return new UnlockResult(result, player);
  }

  int getPriority();

  default boolean isDynamic() {
    return false;
  }

  default boolean hasItemOutput(HolderLookup.Provider provider) {
    ItemStack item = getResultItem(provider);
    //noinspection ConstantValue
    return item != null && !item.isEmpty();
  }

  default boolean hasChanceOutputs(HolderLookup.Provider provider) {
    return !getChanceOutputs().isEmpty();
  }

  default boolean hasOtherOutput(HolderLookup.Provider provider) {
    return !getOtherOutputs().isEmpty();
  }

  default boolean hasOutput(HolderLookup.Provider provider) {
    return hasItemOutput(provider) || hasChanceOutputs(provider) || hasOtherOutput(provider);
  }

  default List<ItemStack> assembleChanceOutputs(W inventory, RandomSource source, HolderLookup.Provider provider) {
    return ChanceOutput.getOutputs(getChanceOutputs(), source);
  }

  default List<ItemStack> assembleOutputs(W inventory, RandomSource random, HolderLookup.Provider provider, @Nullable Supplier<List<ItemStack>> inputProvider) {
    List<ItemStack> results = new ArrayList<>();
    // TODO: In theory, this being wrapped this way means that the unlocks
    // would only ever trigger if there were no outputs.
    //if (!hasItemOutput(provider)) {
    if (inventory instanceof IRootsCrafting<?> crafting) {
      Player player = crafting.getPlayer();
      if (player instanceof ServerPlayer sPlayer) {
        for (Unlock<?> unlock : getUnlocks()) {
          if (RootsAPI.getInstance().canUnlock(sPlayer, unlock)) {
            RootsAPI.getInstance().unlock(sPlayer, unlock);
          }
        }
      }
    }
    //}
    results.add(assemble(inventory, provider));
    if (hasChanceOutputs(provider)) {
      results.addAll(assembleChanceOutputs(inventory, random, provider));
    }
    if (inputProvider != null) {
      results.addAll(process(inputProvider.get()));
    }
    results.removeIf(ItemStack::isEmpty);
    return results;
  }

  default boolean partiallyMatches(W input, Level level) {
    return RecipeUtil.partiallyMatchesIngredients(this, input, level);
  }

  default int getDurabilityCost() {
    return 0;
  }

  default boolean hasDurabilityCost() {
    return getDurabilityCost() > 0;
  }

  default int getCooldown() {
    return -1;
  }

  default boolean hasCooldown() {
    return getCooldown() >= 0;
  }
}
