package mysticmods.roots.api.recipe;

import mysticmods.roots.api.attachment.Unlock;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.List;

// TODO: Handle when there are repeatables and non-repeatable grants
public record UnlockResult(List<Unlock<?>> failedGrants, Player player) {
  public boolean failed() {
    return !failedGrants.isEmpty();
  }

  public void report() {
    if (player.level().isClientSide() || !failed()) {
      return;
    }

    player.displayClientMessage(Component.translatable("roots.message.grants_failed"), false);
    // TODO::
    /*    failedGrants.forEach(grant -> player.displayClientMessage(grant.getFailed(), false));*/
  }
}
