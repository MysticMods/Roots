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
import net.minecraft.world.phys.AABB;

public class ExtensionSnapshot extends Snapshot {
  public static final MapCodec<ExtensionSnapshot> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          Codec.LONG.fieldOf("timestamp").forGetter(Snapshot::getStartTime),
          Codec.INT.fieldOf("decay").forGetter(Snapshot::getDecay),
          Codec.INT.fieldOf("radiusZX").forGetter(ExtensionSnapshot::getRadiusZX),
          Codec.INT.fieldOf("radiusY").forGetter(ExtensionSnapshot::getRadiusY)
      ).apply(instance, ExtensionSnapshot::new));
  public static final Codec<ExtensionSnapshot> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<ByteBuf, ExtensionSnapshot> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_LONG, o -> o.startTime,
      ByteBufCodecs.VAR_INT, o -> o.decay,
      ByteBufCodecs.VAR_INT, o -> o.radiusZX,
      ByteBufCodecs.VAR_INT, o -> o.radiusY,
      ExtensionSnapshot::new);

  private final int radiusZX, radiusY;
  private AABB aabb;

  public ExtensionSnapshot(LivingEntity player, int decay, int radiusZX, int radiusY) {
    super(player, decay);
    this.radiusZX = radiusZX;
    this.radiusY = radiusY;
  }

  public ExtensionSnapshot(long timestamp, int decay, int radiusZX, int radiusY) {
    super(timestamp, decay);
    this.radiusY = radiusY;
    this.radiusZX = radiusZX;
  }

  @Override
  public SnapshotType<?> getType() {
    return ModSerializers.EXTENSION.get();
  }

  public int getRadiusZX() {
    return radiusZX;
  }

  public int getRadiusY() {
    return radiusY;
  }

  public AABB getAABB() {
    if (aabb == null) {
      aabb = new AABB(-radiusZX, -radiusY, -radiusZX, radiusZX, radiusY, radiusZX);
    }
    return aabb;
  }

  public static class Type implements SnapshotType<ExtensionSnapshot> {
    @Override
    public Codec<ExtensionSnapshot> codec() {
      return CODEC;
    }

    @Override
    public MapCodec<ExtensionSnapshot> mapCodec() {
      return MAP_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ExtensionSnapshot> streamCodec() {
      return STREAM_CODEC.cast();
    }
  }
}
