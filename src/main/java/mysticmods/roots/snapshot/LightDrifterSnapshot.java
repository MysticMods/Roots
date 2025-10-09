package mysticmods.roots.snapshot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.snapshot.Snapshot;
import mysticmods.roots.api.snapshot.SnapshotType;
import mysticmods.roots.init.ModSerializers;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class LightDrifterSnapshot extends Snapshot {
  public static final MapCodec<LightDrifterSnapshot> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          Codec.LONG.fieldOf("timestamp").forGetter(Snapshot::getStartTime),
          Codec.INT.fieldOf("decay").forGetter(Snapshot::getDecay),
          Codec.INT.fieldOf("duration").forGetter(LightDrifterSnapshot::getDuration),
          Codec.INT.fieldOf("max_distance").forGetter(LightDrifterSnapshot::getMaxDistance),
          UUIDUtil.CODEC.fieldOf("player").forGetter(LightDrifterSnapshot::getPlayer)
      ).apply(instance, LightDrifterSnapshot::new));
  public static final Codec<LightDrifterSnapshot> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<ByteBuf, LightDrifterSnapshot> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_LONG, o -> o.startTime,
      ByteBufCodecs.VAR_INT, o -> o.decay,
      ByteBufCodecs.VAR_INT, o -> o.duration,
      ByteBufCodecs.VAR_INT, o -> o.maxDistance,
      UUIDUtil.STREAM_CODEC, o -> o.playerId,
      LightDrifterSnapshot::new);

  private final int duration, maxDistance;
  private final UUID playerId;

  public LightDrifterSnapshot(LivingEntity entity, int decay, int duration, int maxDistance, UUID player) {
    super(entity, decay);
    this.duration = duration;
    this.maxDistance = maxDistance;
    this.playerId = player;
  }

  public LightDrifterSnapshot(long timestamp, int decay, int duration, int maxDistance, UUID player) {
    super(timestamp, decay);
    this.duration = duration;
    this.maxDistance = maxDistance;
    this.playerId = player;
  }



  @Override
  public SnapshotType<?> getType() {
    return ModSerializers.LIGHT_DRIFTER.get();
  }

  public int getDuration () {
    return duration;
  }

  public int getMaxDistance () {
    return maxDistance;
  }

  public UUID getPlayer () {
    return playerId;
  }

  @Override
  public boolean isExpired(Entity entity) {
    if (this.decay == -1) {
      return false;
    }
    return super.isExpired(entity);
  }

  public static class Type implements SnapshotType<LightDrifterSnapshot> {
    @Override
    public Codec<LightDrifterSnapshot> codec() {
      return CODEC;
    }

    @Override
    public MapCodec<LightDrifterSnapshot> mapCodec() {
      return MAP_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, LightDrifterSnapshot> streamCodec() {
      return STREAM_CODEC.cast();
    }
  }
}
