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

public class SkySoarerSnapshot extends Snapshot {
  public static MapCodec<SkySoarerSnapshot> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          Codec.INT.fieldOf("timestamp").forGetter(Snapshot::getStartTime),
          Codec.INT.fieldOf("decay").forGetter(Snapshot::getDecay),
          Vec3.CODEC.fieldOf("originalMovement").forGetter(SkySoarerSnapshot::getOriginalMovement),
          Vec3.CODEC.fieldOf("vehicleOriginalMovement").forGetter(SkySoarerSnapshot::getVehicleOriginalMovement),
          Codec.FLOAT.fieldOf("amplifier").forGetter(SkySoarerSnapshot::getAmplifier)
      ).apply(instance, SkySoarerSnapshot::new)
  );
  public static Codec<SkySoarerSnapshot> CODEC = MAP_CODEC.codec();
  public static StreamCodec<ByteBuf, SkySoarerSnapshot> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT, o -> o.startTime,
      ByteBufCodecs.VAR_INT, o -> o.decay,
      ExtraStreamCodecs.VEC3, o -> o.originalMovement,
      ExtraStreamCodecs.VEC3, o -> o.vehicleOriginalMovement,
      ByteBufCodecs.FLOAT, o -> o.amplifier,
      SkySoarerSnapshot::new
  );

  private final float amplifier;
  private final Vec3 originalMovement;
  private final Vec3 vehicleOriginalMovement;

  public SkySoarerSnapshot(LivingEntity entity, int decay, Vec3 originalMovement, Vec3 vehicleOriginalMovement, float amplifier) {
    super(entity, decay);
    this.originalMovement = originalMovement;
    this.amplifier = amplifier;
    this.vehicleOriginalMovement = vehicleOriginalMovement;
  }

  public SkySoarerSnapshot(int timestamp, int decay, Vec3 originalMovement, Vec3 vehicleOriginalMovement, float amplifier) {
    super(timestamp, decay);
    this.originalMovement = originalMovement;
    this.amplifier = amplifier;
    this.vehicleOriginalMovement = vehicleOriginalMovement;
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
