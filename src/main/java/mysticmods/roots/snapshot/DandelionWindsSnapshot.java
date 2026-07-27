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

public class DandelionWindsSnapshot extends Snapshot {
  public static final MapCodec<DandelionWindsSnapshot> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          Codec.LONG.fieldOf("timestamp").forGetter(Snapshot::getStartTime),
          Codec.INT.fieldOf("decay").forGetter(Snapshot::getDecay),
          Codec.FLOAT.fieldOf("deflectionChance").forGetter(DandelionWindsSnapshot::getDeflectionChance)
      ).apply(instance, DandelionWindsSnapshot::new));
  public static final Codec<DandelionWindsSnapshot> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<ByteBuf, DandelionWindsSnapshot> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_LONG, o -> o.startTime,
      ByteBufCodecs.VAR_INT, o -> o.decay,
      ByteBufCodecs.FLOAT, o -> o.deflectionChance,
      DandelionWindsSnapshot::new);

  private final float deflectionChance;

  public DandelionWindsSnapshot(LivingEntity entity, int decay, float deflectionChance) {
    super(entity, decay);
    this.deflectionChance = deflectionChance;
  }

  public DandelionWindsSnapshot(long timestamp, int decay, float deflectionChance) {
    super(timestamp, decay);
    this.deflectionChance = deflectionChance;
  }

  public float getDeflectionChance() {
    return deflectionChance;
  }

  @Override
  public SnapshotType<?> getType() {
    return ModSerializers.DANDELION_WINDS.get();
  }

  public static class Type implements SnapshotType<DandelionWindsSnapshot> {

    @Override
    public Codec<DandelionWindsSnapshot> codec() {
      return CODEC;
    }

    @Override
    public MapCodec<DandelionWindsSnapshot> mapCodec() {
      return MAP_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, DandelionWindsSnapshot> streamCodec() {
      return STREAM_CODEC.cast();
    }
  }
}
