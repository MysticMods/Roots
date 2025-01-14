package mysticmods.roots.snapshot;

import mysticmods.roots.api.attachment.SnapshotStorage;
import mysticmods.roots.api.snapshot.Snapshot;
import mysticmods.roots.api.snapshot.SnapshotType;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.BiConsumer;

public class SnapshotHelper {
  public static <T extends Snapshot> void applyPlayerOrVehicle(LivingEntity entity, SnapshotType<T> serializer, TriConsumer<Entity, Player, T> consumer) {
    if (entity instanceof Player player) {
      SnapshotStorage storage = player.getRootVehicle().getData(ModAttachments.SNAPSHOT_STORAGE);
      storage.ifPresent(player, serializer, snap -> consumer.accept(player.getRootVehicle(), player, snap));
      if (storage.isDirty()) {
        player.getRootVehicle().setData(ModAttachments.SNAPSHOT_STORAGE, storage);
      }
    }
  }

  public static <T extends Snapshot> void applyPlayer(LivingEntity entity, SnapshotType<T> serializer, BiConsumer<Player, T> consumer) {
    if (entity instanceof Player player) {
      SnapshotStorage storage = player.getData(ModAttachments.SNAPSHOT_STORAGE);
      storage.ifPresent(player, serializer, snap -> consumer.accept(player, snap));
      if (storage.isDirty()) {
        player.setData(ModAttachments.SNAPSHOT_STORAGE, storage);
      }
    }
  }

  public static <T extends Snapshot> void addPlayer(LivingEntity entity, SnapshotType<T> serializer, T snapshot) {
    if (entity instanceof Player player) {
      SnapshotStorage storage = player.getData(ModAttachments.SNAPSHOT_STORAGE);
      storage.addSnapshot(player, serializer, snapshot);
      player.setData(ModAttachments.SNAPSHOT_STORAGE, storage);
    }
  }

  public static <T extends Snapshot> void addPlayerOrVehicle(LivingEntity entity, SnapshotType<T> serializer, T snapshot) {
    if (entity instanceof Player player) {
      SnapshotStorage storage = player.getRootVehicle().getData(ModAttachments.SNAPSHOT_STORAGE);
      storage.addSnapshot(player, serializer, snapshot);
      player.getRootVehicle().setData(ModAttachments.SNAPSHOT_STORAGE, storage);
    }
  }
}
