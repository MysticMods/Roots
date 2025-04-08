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

public class WildfireEntitySnapshot extends Snapshot {
  public static final MapCodec<WildfireEntitySnapshot> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          Codec.INT.fieldOf("timestamp").forGetter(Snapshot::getStartTime),
          Codec.INT.fieldOf("decay").forGetter(Snapshot::getDecay),
          Codec.FLOAT.fieldOf("damage").forGetter(WildfireEntitySnapshot::getDamage)
      ).apply(instance, WildfireEntitySnapshot::new));
  public static final Codec<WildfireEntitySnapshot> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<ByteBuf, WildfireEntitySnapshot> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT, o -> o.startTime,
      ByteBufCodecs.VAR_INT, o -> o.decay,
      ByteBufCodecs.FLOAT, o -> o.damage,
      WildfireEntitySnapshot::new);

  private final float damage;

  public WildfireEntitySnapshot(LivingEntity player, int decay, float damage) {
    super(player, decay);
    this.damage = damage;
  }

  public WildfireEntitySnapshot(int timestamp, int decay, float damage) {
    super(timestamp, decay);
    this.damage = damage;
  }

  @Override
  public SnapshotType<?> getType() {
    return ModSerializers.WILDFIRE.get();
  }

  public float getDamage() {
    return damage;
  }

  @Override
  public boolean isExpired(Entity entity) {
    if (this.decay == -1) {
      return false;
    }
    return super.isExpired(entity);
  }

  public static class Type implements SnapshotType<WildfireEntitySnapshot> {
    @Override
    public Codec<WildfireEntitySnapshot> codec() {
      return CODEC;
    }

    @Override
    public MapCodec<WildfireEntitySnapshot> mapCodec() {
      return MAP_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, WildfireEntitySnapshot> streamCodec() {
      return STREAM_CODEC.cast();
    }
  }
}
