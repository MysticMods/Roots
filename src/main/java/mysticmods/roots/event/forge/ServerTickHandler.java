package mysticmods.roots.event.forge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.capability.Capabilities;
import mysticmods.roots.network.Networking;
import mysticmods.roots.network.client.ClientBoundGrantSyncPacket;
import mysticmods.roots.network.client.ClientBoundHerbSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Mod.EventBusSubscriber(modid = RootsAPI.MODID)
public class ServerTickHandler {
  @SubscribeEvent
  public static void onServerTickEnd(TickEvent.ServerTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
        player.getCapability(Capabilities.GRANT_CAPABILITY).ifPresent(grant -> {
          if (grant.isDirty()) {
            Networking.sendTo(new ClientBoundGrantSyncPacket(grant.toRecord()), player);
            grant.setDirty(false);
          }
        });
        player.getCapability(Capabilities.HERB_CAPABILITY).ifPresent(herb -> {
          if (herb.isDirty()) {
            Networking.sendTo(new ClientBoundHerbSyncPacket(herb.toRecord()), player);
            herb.setDirty(false);
          }
        });
        player.getCapability(Capabilities.SNAPSHOT_CAPABILITY).ifPresent(snapshot -> snapshot.tick(player));
      }
    }
  }


  private static final List<Runnable> runnableList = new LinkedList<>();
  private static final List<Runnable> pendingRunnables = new LinkedList<>();

  private final static Object listLock = new Object();

  private final static Object worldLock = new Object();

  private static boolean tickingList = false;

  @SubscribeEvent
  public static void onServerTickStart(TickEvent.ServerTickEvent event) {
    if (event.phase == TickEvent.Phase.START) {
      List<Runnable> copy;
      synchronized (listLock) {
        tickingList = true;
        copy = new ArrayList<>(runnableList);
        tickingList = false;
      }
      synchronized (worldLock) {
        for (Runnable runnable : copy) {
          runnable.run();
        }
      }
      synchronized (listLock) {
        tickingList = true;
        runnableList.clear();
        runnableList.addAll(pendingRunnables);
        tickingList = false;
        pendingRunnables.clear();
      }
    }
  }

  public static void nextTick(Runnable runnable) {
    synchronized (listLock) {
      if (tickingList) {
        pendingRunnables.add(runnable);
      } else {
        runnableList.add(runnable);
      }
    }
  }
}
