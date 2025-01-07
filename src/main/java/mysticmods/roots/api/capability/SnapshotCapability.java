package mysticmods.roots.api.capability;

import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.snapshot.Snapshot;
import mysticmods.roots.api.snapshot.SnapshotSerializer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;




import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SnapshotCapability {
  private final Map<SnapshotSerializer<?>, Snapshot> snapshots = new HashMap<>();

  public void tick(Player player) {
    snapshots.entrySet().removeIf(snapshotSerializerSnapshotEntry -> snapshotSerializerSnapshotEntry.getValue().isExpired(player));
  }

  @Nullable
  public <T extends Snapshot> T getSnapshot(Player player, SnapshotSerializer<T> serializer) {
    Snapshot result = snapshots.get(serializer);
    if (result == null) {
      return null;
    }

    if (result.isExpired(player)) {
      snapshots.remove(serializer);
      return null;
    }

    return serializer.cast(result);
  }

  public <T extends Snapshot> void ifPresent(Player player, SnapshotSerializer<T> serializer, Consumer<T> consumer) {
    T result = getSnapshot(player, serializer);
    if (result != null) {
      consumer.accept(result);
    }
  }

  public <T extends Snapshot> void addSnapshot (Player player, SnapshotSerializer<T> serializer, T snapshot) {
    snapshots.put(serializer, snapshot);
  }

  public ListTag serializeNBT() {
    ListTag result = new ListTag();
    for (Map.Entry<SnapshotSerializer<?>, Snapshot> entry : snapshots.entrySet()) {
/*      result.add(serializeSnapshot(entry.getKey().getKey(), entry.getValue()));*/
    }
    return result;
  }

  public void deserializeNBT(ListTag nbt) {
    for (int i = 0; i < nbt.size(); i++) {
      CompoundTag tag = nbt.getCompound(i);
      CompoundTag snapshotTag = tag.getCompound("snapshot");
      ResourceLocation key = ResourceLocation.tryParse(tag.getString("key"));
      if (key == null) {
        continue;
      }

      SnapshotSerializer<?> serializer = RootsRegistries.SNAPSHOT_SERIALIZERS.get(key);
      if (serializer == null) {
        continue;
      }

      snapshots.put(serializer, serializer.fromTag(snapshotTag));
    }
  }

  protected static <T extends Snapshot> CompoundTag serializeSnapshot(ResourceLocation key, T snapshot) {
    CompoundTag result = new CompoundTag();
    result.putString("type", key.toString());
    //noinspection unchecked
    SnapshotSerializer<T> serializer = ((SnapshotSerializer<T>) RootsRegistries.SNAPSHOT_SERIALIZERS.get(key));
    if (serializer == null) {
      throw new NullPointerException("Serializer '" + key + "' not found!");
    }
    result.put("snapshot", serializer.toTag(snapshot));
    return result;
  }
}
