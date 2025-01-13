package mysticmods.roots.snapshot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.snapshot.Snapshot;
import mysticmods.roots.api.snapshot.SnapshotType;
import mysticmods.roots.init.ModSerializers;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

public class PetalShellSnapshot extends Snapshot {
  public static final MapCodec<PetalShellSnapshot> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          Codec.INT.fieldOf("timestamp").forGetter(Snapshot::getStartTime),
          Codec.INT.fieldOf("decay").forGetter(Snapshot::getDecay),
          Codec.INT.fieldOf("count").forGetter(PetalShellSnapshot::getCount)
      ).apply(instance, PetalShellSnapshot::new));
  public static final Codec<PetalShellSnapshot> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<ByteBuf, PetalShellSnapshot> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT, o -> o.startTime,
      ByteBufCodecs.VAR_INT, o -> o.decay,
      ByteBufCodecs.VAR_INT, o -> o.count,
      PetalShellSnapshot::new);

  private final int count;

  public PetalShellSnapshot(Player player, int decay, int count) {
    super(player, decay);
    this.count = count;
  }

  public PetalShellSnapshot(int timestamp, int decay, int count) {
    super(timestamp, decay);
    this.count = count;
  }

  public int getCount() {
    return count;
  }

  @Override
  public SnapshotType<?> getType() {
    return ModSerializers.PETAL_SHELL.get();
  }

  public static class Type implements SnapshotType<PetalShellSnapshot> {

    @Override
    public Codec<PetalShellSnapshot> codec() {
      return CODEC;
    }

    @Override
    public MapCodec<PetalShellSnapshot> mapCodec() {
      return MAP_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, PetalShellSnapshot> streamCodec() {
      return STREAM_CODEC.cast();
    }
  }
}
