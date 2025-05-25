package mysticmods.roots.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;

public record RootsParticleOptions(ParticleType<?> type, int color1, int color2,
                                   int entityId, int casterId) implements ParticleOptions {

  public static MapCodec<RootsParticleOptions> codec(ParticleType<?> type) {
    return RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.INT.fieldOf("color1").forGetter(RootsParticleOptions::color1),
        Codec.INT.fieldOf("color2").forGetter(RootsParticleOptions::color2),
        Codec.INT.fieldOf("entityId").forGetter(RootsParticleOptions::entityId),
        Codec.INT.fieldOf("casterId").forGetter(RootsParticleOptions::casterId)
    ).apply(instance, (a, b, c, d) -> new RootsParticleOptions(type, a, b, c, d)));
  }

  public static StreamCodec<ByteBuf, RootsParticleOptions> streamCodec(ParticleType<?> type) {
    return StreamCodec.composite(
        ByteBufCodecs.VAR_INT, RootsParticleOptions::color1,
        ByteBufCodecs.VAR_INT, RootsParticleOptions::color2,
        ByteBufCodecs.VAR_INT, RootsParticleOptions::entityId,
        ByteBufCodecs.VAR_INT, RootsParticleOptions::casterId,
        (c1, c2, e, f) -> new RootsParticleOptions(type, c1, c2, e, f)
    );
  }

  public RootsParticleOptions(ParticleType<?> type, int color) {
    this(type, color, color, -1, -1);
  }

  public RootsParticleOptions(DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> type, int color1, int color2) {
    this(type.get(), color1, color2, -1, -1);
  }

  public RootsParticleOptions(DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> type, int color) {
    this(type.get(), color, color, -1, -1);
  }

  public RootsParticleOptions(DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> type, int color1, int color2, int entityId) {
    this(type.get(), color1, color2, entityId, -1);
  }

  public RootsParticleOptions(DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> type, int color1, int color2, int entityId, int casterId) {
    this(type.get(), color1, color2, entityId, casterId);
  }

  public RootsParticleOptions(ParticleType<?> type, int color1, int color2, int entityId) {
    this(type, color1, color2, entityId, -1);
  }

  @Override
  public ParticleType<?> getType() {
    return type;
  }

  public static class Type extends ParticleType<RootsParticleOptions> {
    public Type(boolean overrideLimitter) {
      super(overrideLimitter);
    }

    @Override
    public MapCodec<RootsParticleOptions> codec() {
      return RootsParticleOptions.codec(this);
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, RootsParticleOptions> streamCodec() {
      return RootsParticleOptions.streamCodec(this);
    }
  }
}
