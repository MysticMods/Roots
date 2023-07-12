package mysticmods.roots.api.recipe;

import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;

import java.util.List;

public record ConditionResult(List<LevelCondition> failedLevelConditions,
                              List<PlayerCondition> failedPlayerConditions) {
  public boolean anyFailed() {
    return !failedLevelConditions.isEmpty() || !failedPlayerConditions.isEmpty();
  }
}
