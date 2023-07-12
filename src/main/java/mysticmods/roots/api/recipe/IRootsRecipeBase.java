package mysticmods.roots.api.recipe;

import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.crafting.IRootsCrafting;
import mysticmods.roots.api.recipe.output.ConditionalOutput;
import mysticmods.roots.util.SetUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.items.IItemHandler;

import java.util.*;

public interface IRootsRecipeBase {
  ItemStack getBaseResultItem ();

  NonNullList<Ingredient> getBaseIngredients ();

  void setIngredients(NonNullList<Ingredient> ingredients);

  void setLevelConditions(List<LevelCondition> levelConditions);

  void setPlayerConditions(List<PlayerCondition> playerConditions);

  List<LevelCondition> getLevelConditions();

  List<PlayerCondition> getPlayerConditions();

  void setResultItem(ItemStack result);

  void addConditionalOutput(ConditionalOutput output);

  void addConditionalOutputs(Collection<ConditionalOutput> outputs);

  void addGrant(Grant grant);

  void addGrants(Collection<Grant> grants);

  List<ConditionalOutput> getConditionalOutputs();

  List<Grant> getGrants();

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
}
