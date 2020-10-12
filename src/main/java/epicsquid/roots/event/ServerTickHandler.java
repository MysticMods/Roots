package epicsquid.roots.event;

import epicsquid.roots.Roots;
import epicsquid.roots.entity.spell.EntityBoost;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Roots.MODID)
public class ServerTickHandler {

  @SubscribeEvent
  public static void clientTick(TickEvent.ServerTickEvent event) {
    if (event.phase != TickEvent.Phase.END) {
      return;
    }

    Map<UUID, EntityBoost.PlayerTracker> players = EntityBoost.getPlayers();
    if (!players.isEmpty()) {
      PlayerList list = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
      Set<UUID> toCull = new HashSet<>();
      for (Map.Entry<UUID, EntityBoost.PlayerTracker> entry : players.entrySet()) {
        EntityPlayerMP player = list.getPlayerByUUID(entry.getKey());
        //noinspection ConstantConditions
        if (player == null) {
          toCull.add(entry.getKey());
        } else {
          if (!entry.getValue().safe(player)) {
            toCull.add(entry.getKey());
          }
        }
      }
      toCull.forEach(players::remove);
    }
  }
}
