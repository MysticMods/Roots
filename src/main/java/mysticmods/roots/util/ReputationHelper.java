package mysticmods.roots.util;

import mysticmods.roots.api.attachment.ReputationStorage;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class ReputationHelper {
  public static ReputationStorage getReputationStorage(Player player) {
    return player.getData(ModAttachments.REPUTATION_STORAGE);
  }

  public static int getRank(@NotNull Player player, Grove grove) {
    ReputationStorage storage = getReputationStorage(player);
    return storage.getRank(grove);
  }
}
