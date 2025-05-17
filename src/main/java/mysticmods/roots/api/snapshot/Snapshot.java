package mysticmods.roots.api.snapshot;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;

public abstract class Snapshot {
  public static Codec<Snapshot> CODEC = RootsRegistries.SNAPSHOT_TYPES.byNameCodec()
      .dispatch(Snapshot::getType, SnapshotType::mapCodec);
  public static StreamCodec<RegistryFriendlyByteBuf, Snapshot> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.SNAPSHOT_TYPES)
      .dispatch(Snapshot::getType, SnapshotType::streamCodec);

  protected long startTime;
  protected int decay;

  public Snapshot(Entity entity, int decay) {
    if (entity.level().isClientSide()) {
      throw new IllegalArgumentException("Cannot create a snapshot from an entity on the client side");
    }
    this.startTime = entity.level().getGameTime();
    this.decay = decay;
  }

  public Snapshot(long startTime, int decay) {
    this.startTime = startTime;
    this.decay = decay;
  }

  public long getStartTime() {
    return startTime;
  }

  public int getDecay() {
    return decay;
  }

  public boolean isExpired(Entity entity) {
    if (entity.level().isClientSide()) {
      return false;
    }
    return entity.level().getGameTime() >= (startTime + decay) || entity.level().getGameTime() < startTime;
  }

  public abstract SnapshotType<?> getType();
}
