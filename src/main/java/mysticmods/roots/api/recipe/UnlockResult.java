package mysticmods.roots.api.recipe;

import mysticmods.roots.api.attachment.Unlock;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.List;

// TODO: Handle when there are repeatables and non-repeatable grants
public record UnlockResult(List<Unlock<?>> failedUnlocks, Player player) {
  public boolean anyFailed() {
    return !failedUnlocks.isEmpty();
  }

  public void report() {
    if (player.level().isClientSide() || !anyFailed()) {
      return;
    }

    player.displayClientMessage(Component.translatable("roots.message.grants_failed"), false);
    failedUnlocks.forEach(grant -> player.displayClientMessage(grant.getFailed(), false));
  }
}
