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

public class RoseThornsEntitySnapshot extends Snapshot {
  public static final MapCodec<RoseThornsEntitySnapshot> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          Codec.INT.fieldOf("timestamp").forGetter(Snapshot::getStartTime),
          Codec.INT.fieldOf("decay").forGetter(Snapshot::getDecay),
          Codec.DOUBLE.fieldOf("radiusZX").forGetter(RoseThornsEntitySnapshot::getRadiusZX),
          Codec.DOUBLE.fieldOf("radiusY").forGetter(RoseThornsEntitySnapshot::getRadiusY),
          Codec.INT.fieldOf("duration").forGetter(RoseThornsEntitySnapshot::getDuration),
          Codec.FLOAT.fieldOf("damage").forGetter(RoseThornsEntitySnapshot::getDamage)
      ).apply(instance, RoseThornsEntitySnapshot::new));
  public static final Codec<RoseThornsEntitySnapshot> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<ByteBuf, RoseThornsEntitySnapshot> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT, o -> o.startTime,
      ByteBufCodecs.VAR_INT, o -> o.decay,
      ByteBufCodecs.DOUBLE, o -> o.radiusZX,
      ByteBufCodecs.DOUBLE, o -> o.radiusY,
      ByteBufCodecs.VAR_INT, o -> o.duration,
      ByteBufCodecs.FLOAT, o -> o.damage,
      RoseThornsEntitySnapshot::new);

  private final double radiusZX, radiusY;
  private final int duration;
  private final float damage;
  private AABB aabb;

  public RoseThornsEntitySnapshot(LivingEntity player, int decay, double radiusZX, double radiusY, int duration, float damage) {
    super(player, decay);
    this.radiusZX = radiusZX;
    this.radiusY = radiusY;
    this.duration = duration;
    this.damage = damage;
  }

  public RoseThornsEntitySnapshot(int timestamp, int decay, double radiusZX, double radiusY, int duration, float damage) {
    super(timestamp, decay);
    this.radiusY = radiusY;
    this.radiusZX = radiusZX;
    this.duration = duration;
    this.damage = damage;
  }

  @Override
  public SnapshotType<?> getType() {
    return ModSerializers.ROSE_THORNS.get();
  }

  public double getRadiusZX() {
    return radiusZX;
  }

  public double getRadiusY() {
    return radiusY;
  }

  public int getDuration() {
    return duration;
  }

  public float getDamage() {
    return damage;
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

  public static class Type implements SnapshotType<RoseThornsEntitySnapshot> {
    @Override
    public Codec<RoseThornsEntitySnapshot> codec() {
      return CODEC;
    }

    @Override
    public MapCodec<RoseThornsEntitySnapshot> mapCodec() {
      return MAP_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, RoseThornsEntitySnapshot> streamCodec() {
      return STREAM_CODEC.cast();
    }
  }
}
