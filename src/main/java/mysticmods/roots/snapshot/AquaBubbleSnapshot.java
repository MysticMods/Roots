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

public class AquaBubbleSnapshot extends Snapshot {
  public static final MapCodec<AquaBubbleSnapshot> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          Codec.INT.fieldOf("timestamp").forGetter(Snapshot::getStartTime),
          Codec.INT.fieldOf("decay").forGetter(Snapshot::getDecay),
          Codec.INT.fieldOf("absorption").forGetter(AquaBubbleSnapshot::getAbsorption),
          Codec.FLOAT.fieldOf("lavaResistance").forGetter(AquaBubbleSnapshot::getLavaResistance),
          Codec.FLOAT.fieldOf("fireResistance").forGetter(AquaBubbleSnapshot::getFireResistance)
      ).apply(instance, AquaBubbleSnapshot::new));
  public static final Codec<AquaBubbleSnapshot> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<ByteBuf, AquaBubbleSnapshot> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT, o -> o.startTime,
      ByteBufCodecs.VAR_INT, o -> o.decay,
      ByteBufCodecs.VAR_INT, o -> o.absorption,
      ByteBufCodecs.FLOAT, o -> o.lavaResistance,
      ByteBufCodecs.FLOAT, o -> o.fireResistance,
      AquaBubbleSnapshot::new);

  private final int absorption;
  private final float lavaResistance, fireResistance;

  public AquaBubbleSnapshot(LivingEntity player, int decay, int absorption, float lavaResistance, float fireResistance) {
    super(player, decay);
    this.absorption = absorption;
    this.lavaResistance = lavaResistance;
    this.fireResistance = fireResistance;

  }

  public AquaBubbleSnapshot(int timestamp, int decay, int absorption, float lavaResistance, float fireResistance) {
    super(timestamp, decay);
    this.absorption = absorption;
    this.lavaResistance = lavaResistance;
    this.fireResistance = fireResistance;
  }

  public int getAbsorption() {
    return absorption;
  }

  public float getLavaResistance() {
    return lavaResistance;
  }

  public float getFireResistance() {
    return fireResistance;
  }

  @Override
  public SnapshotType<?> getType() {
    return ModSerializers.AQUA_BUBBLE.get();
  }

  public static class Type implements SnapshotType<AquaBubbleSnapshot> {
    @Override
    public Codec<AquaBubbleSnapshot> codec() {
      return CODEC;
    }

    @Override
    public MapCodec<AquaBubbleSnapshot> mapCodec() {
      return MAP_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, AquaBubbleSnapshot> streamCodec() {
      return STREAM_CODEC.cast();
    }
  }
}
