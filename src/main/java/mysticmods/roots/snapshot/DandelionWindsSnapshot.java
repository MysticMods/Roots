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

public class DandelionWindsSnapshot extends Snapshot {
  public static final MapCodec<DandelionWindsSnapshot> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          Codec.LONG.fieldOf("timestamp").forGetter(Snapshot::getStartTime),
          Codec.INT.fieldOf("decay").forGetter(Snapshot::getDecay),
          Codec.FLOAT.fieldOf("deflectionChance").forGetter(DandelionWindsSnapshot::getDeflectionChance),
          Codec.BOOL.fieldOf("vortex").forGetter(DandelionWindsSnapshot::hasVortex),
          Codec.INT.fieldOf("vortexCooldown").forGetter(DandelionWindsSnapshot::getVortexCooldown),
          Codec.BOOL.fieldOf("gusts").forGetter(DandelionWindsSnapshot::hasGusts),
          Codec.INT.fieldOf("gustsCooldown").forGetter(DandelionWindsSnapshot::getGustsCooldown)
      ).apply(instance, DandelionWindsSnapshot::new));
  public static final Codec<DandelionWindsSnapshot> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<ByteBuf, DandelionWindsSnapshot> STREAM_CODEC = ExtraStreamCodecs.composite(
      ByteBufCodecs.VAR_LONG, o -> o.startTime,
      ByteBufCodecs.VAR_INT, o -> o.decay,
      ByteBufCodecs.FLOAT, o -> o.deflectionChance,
      ByteBufCodecs.BOOL, o -> o.vortex,
      ByteBufCodecs.VAR_INT, o -> o.vortexCooldown,
      ByteBufCodecs.BOOL, o -> o.gusts,
      ByteBufCodecs.VAR_INT, o -> o.gustsCooldown,
      DandelionWindsSnapshot::new);

  private final float deflectionChance;
  private final int vortexCooldown, gustsCooldown;
  private final boolean vortex, gusts;

  public DandelionWindsSnapshot(LivingEntity entity, int decay, float deflectionChance, boolean vortex, int vortexCooldown, boolean gusts, int gustsCooldown) {
    super(entity, decay);
    this.deflectionChance = deflectionChance;
    this.vortex = vortex;
    this.vortexCooldown = vortexCooldown;
    this.gusts = gusts;
    this.gustsCooldown = gustsCooldown;
  }

  public DandelionWindsSnapshot(long timestamp, int decay, float deflectionChance, boolean vortex, int vortexCooldown, boolean gusts, int gustsCooldown) {
    super(timestamp, decay);
    this.deflectionChance = deflectionChance;
    this.vortex = vortex;
    this.vortexCooldown = vortexCooldown;
    this.gusts = gusts;
    this.gustsCooldown = gustsCooldown;
  }

  public int getVortexCooldown() {
    return vortexCooldown;
  }

  public boolean hasVortex() {
    return vortex;
  }

  public int getGustsCooldown() {
    return gustsCooldown;
  }

  public boolean hasGusts() {
    return gusts;
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
