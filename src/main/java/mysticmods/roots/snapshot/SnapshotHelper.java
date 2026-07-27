package mysticmods.roots.snapshot;

import mysticmods.roots.api.attachment.SnapshotStorage;
import mysticmods.roots.api.snapshot.Snapshot;
import mysticmods.roots.api.snapshot.SnapshotType;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class SnapshotHelper {
  public static <T extends Snapshot> void applyLivingWithVehicle(Entity living, SnapshotType<T> serializer, TriConsumer<Entity, Entity, T> consumer) {
    Entity rootVehicle = living.getRootVehicle();

    SnapshotStorage storage = living.getData(ModAttachments.SNAPSHOT_STORAGE);
    storage.ifPresent(living, serializer, snap -> consumer.accept(rootVehicle, living, snap));
    if (storage.isDirty()) {
      living.setData(ModAttachments.SNAPSHOT_STORAGE, storage);
    }
  }

  @Nullable
  public static <T extends Snapshot> T getSnapshot(Entity entity, SnapshotType<T> serializer) {
    SnapshotStorage storage = entity.getData(ModAttachments.SNAPSHOT_STORAGE);
    return storage.getSnapshot(entity, serializer);
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
