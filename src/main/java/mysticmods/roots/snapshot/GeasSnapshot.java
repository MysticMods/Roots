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
import net.minecraft.world.entity.LivingEntity;

public class GeasSnapshot extends Snapshot {
  public static final MapCodec<GeasSnapshot> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          Codec.INT.fieldOf("timestamp").forGetter(Snapshot::getStartTime),
          Codec.INT.fieldOf("decay").forGetter(Snapshot::getDecay)
      ).apply(instance, GeasSnapshot::new));
  public static final Codec<GeasSnapshot> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<ByteBuf, GeasSnapshot> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT, o -> o.startTime,
      ByteBufCodecs.VAR_INT, o -> o.decay,
      GeasSnapshot::new);

  public GeasSnapshot(LivingEntity player, int decay) {
    super(player, decay);
  }

  public GeasSnapshot(int timestamp, int decay) {
    super(timestamp, decay);
  }

  @Override
  public SnapshotType<?> getType() {
    return ModSerializers.EXTENSION.get();
  }

  public static class Type implements SnapshotType<GeasSnapshot> {
    @Override
    public Codec<GeasSnapshot> codec() {
      return CODEC;
    }

    @Override
    public MapCodec<GeasSnapshot> mapCodec() {
      return MAP_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, GeasSnapshot> streamCodec() {
      return STREAM_CODEC.cast();
    }
  }
}
