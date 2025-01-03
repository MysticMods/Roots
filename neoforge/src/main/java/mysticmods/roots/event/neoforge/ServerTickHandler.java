package mysticmods.roots.event.neoforge;

import mysticmods.roots.api.RootsAPI;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@EventBusSubscriber(modid = RootsAPI.MODID)
public class ServerTickHandler {
  @SubscribeEvent
  public static void onServerTickEnd(ServerTickEvent.Post event) {
/*      for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
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
        player.getCapability(Capabilities.REPUTATION_CAPABILITY).ifPresent(reputation -> {
          if (reputation.isDirty()) {
            Networking.sendTo(new ClientBoundReputationSyncPacket(reputation.toRecord()), player);
            reputation.setDirty(false);
          }
        });
      }*/
  }


  private static final List<Runnable> runnableList = new LinkedList<>();
  private static final List<Runnable> pendingRunnables = new LinkedList<>();

  private final static Object listLock = new Object();

  private final static Object worldLock = new Object();

  private static boolean tickingList = false;

  @SubscribeEvent
  public static void onServerTickStart(ServerTickEvent.Pre event) {
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
