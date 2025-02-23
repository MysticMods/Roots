package mysticmods.roots.api.recipe;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.Unlock;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.crafting.IRootsCrafting;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.api.util.SetUtils;
import net.minecraft.core.BlockPos;
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

// TODO: List of ItemStack results
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

  default List<LevelCondition> getLevelConditions() {
    return Collections.emptyList();
  }

  default List<PlayerCondition> getPlayerConditions() {
    return Collections.emptyList();
  }

  default List<ChanceOutput> getChanceOutputs() {
    return Collections.emptyList();
  }

  default List<Unlock<?>> getUnlocks() {
    return Collections.emptyList();
  }

  default ConditionResult checkConditions(Level level, Player player, BoundingBox bounds, BlockPos center) {
    List<PlayerCondition> failedPlayer = new ArrayList<>();
    for (PlayerCondition condition : this.getPlayerConditions()) {
      if (!condition.test(level, player)) {
        failedPlayer.add(condition);
      }
    }
    List<LevelCondition> failedLevel = new ArrayList<>();
    Set<BlockPos> testedPositions = new HashSet<>();
    for (LevelCondition condition : this.getLevelConditions()) {
      Set<BlockPos> newPositions = condition.test(level, player, bounds, center, testedPositions);
      if (newPositions.isEmpty() || SetUtils.containsAny(testedPositions, newPositions)) {
        failedLevel.add(condition);
      } else {
        testedPositions.addAll(newPositions);
      }
    }

    return new ConditionResult(failedLevel, failedPlayer);
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

  default int getPriority() {
    return 0;
  }

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
    return false;
  }

  default boolean hasOutput(HolderLookup.Provider provider) {
    return hasItemOutput(provider) || hasChanceOutputs(provider) || hasOtherOutput(provider);
  }

  default List<ItemStack> assembleChanceOutputs(W inventory, RandomSource source, HolderLookup.Provider provider) {
    return ChanceOutput.getOutputs(getChanceOutputs(), source);
  }

  default List<ItemStack> assembleOutputs(W inventory, RandomSource random, HolderLookup.Provider provider, @Nullable Supplier<List<ItemStack>> inputProvider) {
    List<ItemStack> results = new ArrayList<>();
    // TODO: There may be doubles-up, check
    if (!hasItemOutput(provider)) {
      if (inventory instanceof IRootsCrafting<?> crafting) {
        Player player = crafting.getPlayer();
        if (player instanceof ServerPlayer sPlayer) {
          for (Unlock<?> unlock : getUnlocks()) {
            if (RootsAPI.getInstance().canUnlock(sPlayer, unlock)) {
              // TODO: Message that it's unlocked
              RootsAPI.getInstance().unlock(sPlayer, unlock);
            }
          }
        }
      }
    }
    if (hasItemOutput(provider)) {
      results.add(assemble(inventory, provider));
    }
    if (hasChanceOutputs(provider)) {
      results.addAll(assembleChanceOutputs(inventory, random, provider));
    }
    if (inputProvider != null) {
      results.addAll(process(inputProvider.get()));
    }
    results.removeIf(ItemStack::isEmpty);
    return results;
  }
}
