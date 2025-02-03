package mysticmods.roots.api.attachment;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.snapshot.Snapshot;
import mysticmods.roots.api.snapshot.SnapshotType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

public class SnapshotStorage implements ICleanable {
  public static final Codec<SnapshotStorage> CODEC = Codec.unboundedMap(RootsRegistries.SNAPSHOT_TYPES.byNameCodec(), Snapshot.CODEC).xmap(SnapshotStorage::new, SnapshotStorage::getSnapshots);
  public static final StreamCodec<RegistryFriendlyByteBuf, SnapshotStorage> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.map(HashMap::new, ByteBufCodecs.registry(RootsRegistries.Keys.SNAPSHOT_TYPES), Snapshot.STREAM_CODEC), o -> o.snapshots, SnapshotStorage::new);

  private final Map<SnapshotType<?>, Snapshot> snapshots;
  private boolean dirty = true;

  public SnapshotStorage() {
    this.snapshots = new HashMap<>();
  }

  public SnapshotStorage(Map<SnapshotType<?>, Snapshot> snapshots) {
    this.snapshots = new HashMap<>(snapshots);
  }

  public Map<SnapshotType<?>, Snapshot> getSnapshots() {
    return snapshots;
  }

  public void tick(LivingEntity living) {
    Iterator<Map.Entry<SnapshotType<?>, Snapshot>> iterator = snapshots.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<SnapshotType<?>, Snapshot> entry = iterator.next();
      if (entry == null || entry.getValue() == null || entry.getValue().isExpired(living)) {
        iterator.remove();
        dirty = true;
      }
    }
  }

  public boolean isDirty() {
    return dirty;
  }

  @Override
  public boolean isEmpty() {
    return snapshots.isEmpty();
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  @Nullable
  public <T extends Snapshot> T getSnapshot(LivingEntity living, SnapshotType<T> type) {
    Snapshot result = snapshots.get(type);
    if (result == null) {
      return null;
    }

    if (result.isExpired(living)) {
      snapshots.remove(type);
      this.dirty = true;
      return null;
    }

    if (result.getType() != type) {
      snapshots.remove(type);
      this.dirty = true;
      return null;
    }

    return type.cast(result);
  }

  public <T extends Snapshot> void ifPresent(LivingEntity living, SnapshotType<T> serializer, Consumer<T> consumer) {
    T result = getSnapshot(living, serializer);
    if (result != null) {
      consumer.accept(result);
    }
  }

  public <T extends Snapshot> void addSnapshot(LivingEntity living, SnapshotType<T> type, T snapshot) {
    snapshots.put(type, snapshot);
    this.dirty = true;
  }
}
