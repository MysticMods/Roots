package mysticmods.roots.snapshot;

import mysticmods.roots.api.attachment.SnapshotStorage;
import mysticmods.roots.api.snapshot.Snapshot;
import mysticmods.roots.api.snapshot.SnapshotType;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.world.entity.Entity;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.BiConsumer;

public class SnapshotHelper {
  public static <T extends Snapshot> void applyLivingOrVehicle(Entity living, SnapshotType<T> serializer, TriConsumer<Entity, Entity, T> consumer) {
    SnapshotStorage storage = living.getRootVehicle().getData(ModAttachments.SNAPSHOT_STORAGE);
    storage.ifPresent(living, serializer, snap -> consumer.accept(living.getRootVehicle(), living, snap));
    if (storage.isDirty()) {
      living.getRootVehicle().setData(ModAttachments.SNAPSHOT_STORAGE, storage);
    }
  }

  public static <T extends Snapshot> void applyLiving(Entity living, SnapshotType<T> serializer, BiConsumer<Entity, T> consumer) {
    SnapshotStorage storage = living.getData(ModAttachments.SNAPSHOT_STORAGE);
    storage.ifPresent(living, serializer, snap -> consumer.accept(living, snap));
    if (storage.isDirty()) {
      living.setData(ModAttachments.SNAPSHOT_STORAGE, storage);
    }
  }

  public static <T extends Snapshot> void addLiving(Entity living, SnapshotType<T> serializer, T snapshot) {
    SnapshotStorage storage = living.getData(ModAttachments.SNAPSHOT_STORAGE);
    storage.addSnapshot(living, serializer, snapshot);
    living.setData(ModAttachments.SNAPSHOT_STORAGE, storage);
  }

  public static <T extends Snapshot> void addLivingOrVehicle(Entity living, SnapshotType<T> serializer, T snapshot) {
    SnapshotStorage storage = living.getRootVehicle().getData(ModAttachments.SNAPSHOT_STORAGE);
    storage.addSnapshot(living, serializer, snapshot);
    living.getRootVehicle().setData(ModAttachments.SNAPSHOT_STORAGE, storage);
  }
}
