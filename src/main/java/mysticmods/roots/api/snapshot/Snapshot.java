package mysticmods.roots.api.snapshot;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

public abstract class Snapshot {
  public static Codec<Snapshot> CODEC = RootsRegistries.SNAPSHOT_TYPES.byNameCodec().dispatch(Snapshot::getType, SnapshotType::mapCodec);
  public static StreamCodec<RegistryFriendlyByteBuf, Snapshot> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.SNAPSHOT_TYPES).dispatch(Snapshot::getType, SnapshotType::streamCodec);

  protected int startTime;
  protected int decay;

  public Snapshot(Player player, int decay) {
    this.startTime = player.tickCount;
    this.decay = decay;

  }

  public Snapshot(int startTime, int decay) {
    this.startTime = startTime;
    this.decay = decay;
  }

  public int getStartTime() {
    return startTime;
  }

  public int getDecay() {
    return decay;
  }

  public boolean isExpired(Player player) {
    return player.tickCount >= (startTime + decay) || player.tickCount < startTime;
  }

  public abstract SnapshotType<?> getType();
}
