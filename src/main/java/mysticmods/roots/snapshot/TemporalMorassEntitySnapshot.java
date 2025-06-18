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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

public class TemporalMorassEntitySnapshot extends Snapshot {
  public static final MapCodec<TemporalMorassEntitySnapshot> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          Codec.LONG.fieldOf("timestamp").forGetter(Snapshot::getStartTime),
          Codec.INT.fieldOf("decay").forGetter(Snapshot::getDecay),
          Codec.INT.fieldOf("radiusZX").forGetter(TemporalMorassEntitySnapshot::getRadiusZX),
          Codec.INT.fieldOf("radiusY").forGetter(TemporalMorassEntitySnapshot::getRadiusY),
          Codec.INT.fieldOf("duration").forGetter(TemporalMorassEntitySnapshot::getDuration),
          Codec.INT.fieldOf("amplifier").forGetter(TemporalMorassEntitySnapshot::getAmplifier)
      ).apply(instance, TemporalMorassEntitySnapshot::new));
  public static final Codec<TemporalMorassEntitySnapshot> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<ByteBuf, TemporalMorassEntitySnapshot> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_LONG, o -> o.startTime,
      ByteBufCodecs.VAR_INT, o -> o.decay,
      ByteBufCodecs.VAR_INT, o -> o.radiusZX,
      ByteBufCodecs.VAR_INT, o -> o.radiusY,
      ByteBufCodecs.VAR_INT, o -> o.duration,
      ByteBufCodecs.VAR_INT, o -> o.amplifier,
      TemporalMorassEntitySnapshot::new);

  private final int radiusZX, radiusY, duration, amplifier;
  private AABB aabb;

  public TemporalMorassEntitySnapshot(LivingEntity player, int decay, int radiusZX, int radiusY, int duration, int amplifier) {
    super(player, decay);
    this.radiusZX = radiusZX;
    this.radiusY = radiusY;
    this.duration = duration;
    this.amplifier = amplifier;
  }

  public TemporalMorassEntitySnapshot(long timestamp, int decay, int radiusZX, int radiusY, int duration, int amplifier) {
    super(timestamp, decay);
    this.radiusY = radiusY;
    this.radiusZX = radiusZX;
    this.duration = duration;
    this.amplifier = amplifier;
  }

  @Override
  public SnapshotType<?> getType() {
    return ModSerializers.TEMPORAL_MORASS.get();
  }

  public int getRadiusZX() {
    return radiusZX;
  }

  public int getRadiusY() {
    return radiusY;
  }

  public int getDuration() {
    return duration;
  }

  public int getAmplifier() {
    return amplifier;
  }

  public AABB getAABB() {
    if (aabb == null) {
      aabb = new AABB(-radiusZX, -radiusY, -radiusZX, radiusZX, radiusY, radiusZX);
    }
    return aabb;
  }

  @Override
  public boolean isExpired(Entity entity) {
    if (this.decay == -1) {
      return false;
    }
    return super.isExpired(entity);
  }

  public static class Type implements SnapshotType<TemporalMorassEntitySnapshot> {
    @Override
    public Codec<TemporalMorassEntitySnapshot> codec() {
      return CODEC;
    }

    @Override
    public MapCodec<TemporalMorassEntitySnapshot> mapCodec() {
      return MAP_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, TemporalMorassEntitySnapshot> streamCodec() {
      return STREAM_CODEC.cast();
    }
  }
}
