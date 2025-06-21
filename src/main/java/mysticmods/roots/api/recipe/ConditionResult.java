package mysticmods.roots.api.recipe;

import mysticmods.roots.api.condition.ILevelCondition;
import mysticmods.roots.api.condition.IPlayerCondition;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public record ConditionResult(List<ILevelCondition> failedLevelConditions,
                              List<IPlayerCondition> failedPlayerConditions) {
  public boolean anyFailed() {
    return !failedLevelConditions.isEmpty() || !failedPlayerConditions.isEmpty();
  }

  public void report(Player player) {
    if (player.level().isClientSide() || !anyFailed()) {
      return;
    }

    // Improve this

    player.displayClientMessage(Component.translatable("roots.message.recipe.failures"), false);
    failedLevelConditions.forEach(condition -> player.displayClientMessage(Component.translatable("roots.message.recipe.requires", condition.getNameComponent()), false));
    failedPlayerConditions.forEach(condition -> player.displayClientMessage(Component.translatable("roots.message.recipe.requires", condition.getNameComponent()), false));
  }
}
