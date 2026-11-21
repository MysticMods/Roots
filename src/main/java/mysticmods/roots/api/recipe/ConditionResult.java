package mysticmods.roots.api.recipe;

import mysticmods.roots.api.condition.ICondition;
import mysticmods.roots.api.condition.ILevelCondition;
import mysticmods.roots.api.condition.IPlayerCondition;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public record ConditionResult(List<Holder<ICondition>> failed) {
  public boolean anyFailed() {
    return !failed.isEmpty();
  }

  public void report(Player player) {
    if (player.level().isClientSide() || !anyFailed()) {
      return;
    }

    // Improve this

    player.displayClientMessage(Component.translatable("roots.message.recipe.failures"), false);
    failed.forEach(condition -> player.displayClientMessage(Component.translatable("roots.message.recipe.requires", ICondition.getName(condition)), false));
  }
}
