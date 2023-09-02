package mysticmods.roots.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.capability.Capabilities;
import mysticmods.roots.api.capability.GrantCapability;
import mysticmods.roots.api.capability.HerbCapability;
import mysticmods.roots.api.capability.ReputationCapability;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value= Dist.CLIENT, modid= RootsAPI.MODID)
public class ClientTicker {
  private static HerbCapability.SerializedHerbRecord herbRecord = null;
  private static GrantCapability.SerializedGrantRecord grantRecord = null;
  private static ReputationCapability.SerializedReputationRecord reputationRecord = null;

  @SubscribeEvent
  public static void onClientTick (TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      if (herbRecord != null || grantRecord != null || reputationRecord != null) {
        Player player = RootsAPI.getInstance().getPlayer();
        if (grantRecord != null) {
          player.getCapability(Capabilities.GRANT_CAPABILITY).ifPresent(grant -> {
            grant.fromRecord(grantRecord);
            grantRecord = null;
          });
        }
        if (herbRecord != null) {
          player.getCapability(Capabilities.HERB_CAPABILITY).ifPresent(herb -> {
            herb.fromRecord(herbRecord);
            herbRecord = null;
          });
        }
        if (reputationRecord != null) {
          player.getCapability(Capabilities.REPUTATION_CAPABILITY).ifPresent(reputation -> {
            reputation.fromRecord(reputationRecord);
            reputationRecord = null;
          });
        }
      }
    }
  }

  public static void setHerbRecord(HerbCapability.SerializedHerbRecord herbRecord) {
    ClientTicker.herbRecord = herbRecord;
  }

  public static void setGrantRecord(GrantCapability.SerializedGrantRecord grantRecord) {
    ClientTicker.grantRecord = grantRecord;
  }

  public static void setReputationRecord(ReputationCapability.SerializedReputationRecord reputationRecord) {
    ClientTicker.reputationRecord = reputationRecord;
  }
}
