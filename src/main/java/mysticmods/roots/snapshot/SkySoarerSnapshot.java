package mysticmods.roots.snapshot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.snapshot.Snapshot;
import mysticmods.roots.api.snapshot.SnapshotType;
import mysticmods.roots.init.ModSerializers;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

// TODO: Why are these not records?
public class SkySoarerSnapshot extends Snapshot {
  public static MapCodec<SkySoarerSnapshot> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          Codec.LONG.fieldOf("timestamp").forGetter(Snapshot::getStartTime),
          Codec.INT.fieldOf("decay").forGetter(Snapshot::getDecay),
          Vec3.CODEC.fieldOf("originalMovement").forGetter(SkySoarerSnapshot::getOriginalMovement),
          Vec3.CODEC.fieldOf("vehicleOriginalMovement").forGetter(SkySoarerSnapshot::getVehicleOriginalMovement),
          Codec.FLOAT.fieldOf("amplifier").forGetter(SkySoarerSnapshot::getAmplifier),
          Codec.INT.fieldOf("duration").forGetter(SkySoarerSnapshot::getDuration),
          Codec.INT.fieldOf("durationCount").forGetter(SkySoarerSnapshot::getDurationCount),
          Codec.INT.fieldOf("amplifierCount").forGetter(SkySoarerSnapshot::getAmplifierCount),
          Codec.BOOL.fieldOf("friendlyEarth").forGetter(SkySoarerSnapshot::hasFriendlyEarth)
      ).apply(instance, SkySoarerSnapshot::new)
  );
  public static Codec<SkySoarerSnapshot> CODEC = MAP_CODEC.codec();
  public static StreamCodec<ByteBuf, SkySoarerSnapshot> STREAM_CODEC = ExtraStreamCodecs.composite(
      ByteBufCodecs.VAR_LONG, o -> o.startTime,
      ByteBufCodecs.VAR_INT, o -> o.decay,
      ExtraStreamCodecs.VEC3, o -> o.originalMovement,
      ExtraStreamCodecs.VEC3, o -> o.vehicleOriginalMovement,
      ByteBufCodecs.FLOAT, o -> o.amplifier,
      ByteBufCodecs.VAR_INT, o -> o.duration,
      ByteBufCodecs.VAR_INT, o -> o.durationCount,
      ByteBufCodecs.VAR_INT, o -> o.amplifierCount,
      ByteBufCodecs.BOOL, o -> o.friendlyEarth,
      SkySoarerSnapshot::new
  );

  private final float amplifier;
  private final Vec3 originalMovement;
  private final Vec3 vehicleOriginalMovement;
  private final int duration, amplifierCount, durationCount;
  private final boolean friendlyEarth;

  public SkySoarerSnapshot(LivingEntity entity, int decay, Vec3 originalMovement, Vec3 vehicleOriginalMovement, float amplifier, int duration, int amplifierCount, int durationCount, boolean friendlyEarth) {
    super(entity, decay);
    this.originalMovement = originalMovement;
    this.amplifier = amplifier;
    this.vehicleOriginalMovement = vehicleOriginalMovement;
    this.duration = duration;
    this.amplifierCount = amplifierCount;
    this.durationCount = durationCount;
    this.friendlyEarth = friendlyEarth;
  }

  public SkySoarerSnapshot(long timestamp, int decay, Vec3 originalMovement, Vec3 vehicleOriginalMovement, float amplifier, int duration, int amplifierCount, int durationCount, boolean friendlyEarth) {
    super(timestamp, decay);
    this.originalMovement = originalMovement;
    this.amplifier = amplifier;
    this.vehicleOriginalMovement = vehicleOriginalMovement;
    this.duration = duration;
    this.amplifierCount = amplifierCount;
    this.durationCount = durationCount;
    this.friendlyEarth = friendlyEarth;
  }

  public Vec3 getOriginalMovement() {
    return originalMovement;
  }

  public float getAmplifier() {
    return amplifier;
  }

  public Vec3 getVehicleOriginalMovement() {
    return vehicleOriginalMovement;
  }

  public int getDuration() {
    return duration;
  }

  public int getDurationCount() {
    return durationCount;
  }

  public int getAmplifierCount() {
    return amplifierCount;
  }

  public boolean hasFriendlyEarth() {
    return friendlyEarth;
  }

  @Override
  public SnapshotType<?> getType() {
    return ModSerializers.SKY_SOARER.get();
  }

  public static class Type implements SnapshotType<SkySoarerSnapshot> {
    @Override
    public Codec<SkySoarerSnapshot> codec() {
      return CODEC;
    }

    @Override
    public MapCodec<SkySoarerSnapshot> mapCodec() {
      return MAP_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, SkySoarerSnapshot> streamCodec() {
      return STREAM_CODEC.cast();
    }
  }
}
