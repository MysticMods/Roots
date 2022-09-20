package mysticmods.roots.api.snapshot;

import mysticmods.roots.api.registry.IKeyedRegistryEntry;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public interface SnapshotSerializer<T extends Snapshot> extends IKeyedRegistryEntry {
  T fromTag(CompoundTag tag);

  CompoundTag toTag(T snapshot);

  @Override
  default ResourceLocation getKey() {
    return Registries.SNAPSHOT_SERIALIZER_REGISTRY.get().getKey(this);
  }
}
