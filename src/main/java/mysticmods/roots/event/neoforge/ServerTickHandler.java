package mysticmods.roots.event.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.attachment.AttachmentUtil;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.network.client.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@EventBusSubscriber(modid = RootsAPI.MODID)
public class ServerTickHandler {
  @SubscribeEvent
  public static void onTickEntity(EntityTickEvent.Post event) {
    if (!event.getEntity().level().isClientSide() && !event.getEntity().getType().is(RootsTags.Entities.PLAYERS)) {
      AttachmentUtil.monitorAndSyncEntity(
          event.getEntity(),
          ModAttachments.SNAPSHOT_STORAGE,
          (entity, storage) -> storage.tick(entity),
          ClientboundEntitySnapshotSyncPacket::new
      );
    }
  }

  @SubscribeEvent
  public static void onServerTickEnd(ServerTickEvent.Post event) {
    for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
      AttachmentUtil.monitorAndSync(
          player,
          ModAttachments.GRANT_STORAGE,
          ClientboundGrantSyncPacket::new
      );
      AttachmentUtil.monitorAndSync(
          player,
          ModAttachments.HERB_STORAGE,
          ClientboundHerbSyncPacket::new
      );
      AttachmentUtil.monitorAndSync(
          player,
          ModAttachments.SNAPSHOT_STORAGE,
          (player1, data) -> data.tick(player1),
          ClientboundPlayerSnapshotSyncPacket::new
      );
      AttachmentUtil.monitorAndSync(
          player,
          ModAttachments.REPUTATION_STORAGE,
          ClientboundReputationSyncPacket::new
      );
    }
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
