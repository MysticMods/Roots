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

public class TimeStopEntitySnapshot extends Snapshot {
  public static final MapCodec<TimeStopEntitySnapshot> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          Codec.INT.fieldOf("timestamp").forGetter(Snapshot::getStartTime),
          Codec.INT.fieldOf("decay").forGetter(Snapshot::getDecay),
          Codec.INT.fieldOf("radiusZX").forGetter(TimeStopEntitySnapshot::getRadiusZX),
          Codec.INT.fieldOf("radiusY").forGetter(TimeStopEntitySnapshot::getRadiusY),
          Codec.INT.fieldOf("duration").forGetter(TimeStopEntitySnapshot::getDuration)
      ).apply(instance, TimeStopEntitySnapshot::new));
  public static final Codec<TimeStopEntitySnapshot> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<ByteBuf, TimeStopEntitySnapshot> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT, o -> o.startTime,
      ByteBufCodecs.VAR_INT, o -> o.decay,
      ByteBufCodecs.VAR_INT, o -> o.radiusZX,
      ByteBufCodecs.VAR_INT, o -> o.radiusY,
      ByteBufCodecs.VAR_INT, o -> o.duration,
      TimeStopEntitySnapshot::new);

  private final int radiusZX, radiusY, duration;
  private AABB aabb;

  public TimeStopEntitySnapshot(LivingEntity player, int decay, int radiusZX, int radiusY, int duration) {
    super(player, decay);
    this.radiusZX = radiusZX;
    this.radiusY = radiusY;
    this.duration = duration;
  }

  public TimeStopEntitySnapshot(int timestamp, int decay, int radiusZX, int radiusY, int duration) {
    super(timestamp, decay);
    this.radiusY = radiusY;
    this.radiusZX = radiusZX;
    this.duration = duration;
  }

  @Override
  public SnapshotType<?> getType() {
    return ModSerializers.TIME_STOP.get();
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

  public static class Type implements SnapshotType<TimeStopEntitySnapshot> {
    @Override
    public Codec<TimeStopEntitySnapshot> codec() {
      return CODEC;
    }

    @Override
    public MapCodec<TimeStopEntitySnapshot> mapCodec() {
      return MAP_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, TimeStopEntitySnapshot> streamCodec() {
      return STREAM_CODEC.cast();
    }
  }
}
