package mysticmods.roots.util;

import mysticmods.roots.api.attachment.ReputationStorage;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.world.entity.player.Player;

public class ReputationHelper {
  public static ReputationStorage getReputationStorage (Player player) {
    return player.getData(ModAttachments.REPUTATION_STORAGE);
  }

  public static int getRank (Player player, Grove grove) {
    ReputationStorage storage = getReputationStorage(player);
    return storage.getRank(grove);
  }
}
