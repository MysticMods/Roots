package mysticmods.roots.api.snapshot;

import mysticmods.roots.api.registry.IKeyedRegistryEntry;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public abstract class SnapshotSerializer<T extends Snapshot> implements IKeyedRegistryEntry {
  protected Builder<T> builder;

  public SnapshotSerializer(Builder<T> builder) {
    this.builder = builder;
  }

  public T fromTag(CompoundTag tag) {
    T snapshot = builder.build(tag.getInt("timestamp"));
    updateFromTag(snapshot, tag);
    return snapshot;
  }

  protected abstract void updateFromTag (T snapshot, CompoundTag tag);

  public CompoundTag toTag(T snapshot) {
    CompoundTag result = new CompoundTag();
    result.putInt("timestamp", snapshot.getTimestamp());
    updateToTag(snapshot, result);
    return result;
  }

  protected abstract void updateToTag (T snapshot, CompoundTag tag);

  public abstract int getDecay ();

  @Override
  public ResourceLocation getKey() {
    return Registries.SNAPSHOT_SERIALIZER_REGISTRY.get().getKey(this);
  }

  public T cast (Snapshot snapshot) {
    if (snapshot.getSerializer() == this) {
      // TODO: This should be safe?
      //noinspection unchecked
      return (T) snapshot;
    } else {
      throw new IllegalStateException("Snapshot serializer mismatch, snapshot " + snapshot.getSerializer() + " is not " + this);
    }
  }

  public interface Builder<T extends Snapshot> {
    T build (int timestamp);
  }
}
