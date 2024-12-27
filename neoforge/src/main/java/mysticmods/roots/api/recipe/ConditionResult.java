package mysticmods.roots.api.recipe;

import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public record ConditionResult(List<LevelCondition> failedLevelConditions, List<PlayerCondition> failedPlayerConditions,
                              Player player) {
  public boolean anyFailed() {
    return !failedLevelConditions.isEmpty() || !failedPlayerConditions.isEmpty();
  }

  public void report () {
    if (player.level.isClientSide() || !anyFailed()) {
      return;
    }

    player.displayClientMessage(Component.translatable("roots.message.recipe.failures"), false);
    failedLevelConditions.forEach(condition -> player.displayClientMessage(Component.translatable("roots.message.recipe.requires", Component.translatable(condition.getDescriptionId())), false));
    failedPlayerConditions.forEach(condition -> player.displayClientMessage(Component.translatable("roots.message.recipe.requires", Component.translatable(condition.getDescriptionId())), false));
  }
}
