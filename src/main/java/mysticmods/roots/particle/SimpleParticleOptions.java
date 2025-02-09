package mysticmods.roots.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.init.ModParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record SimpleParticleOptions (int r, int g, int b, float gravity) implements ParticleOptions {
  public static final MapCodec<SimpleParticleOptions> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          Codec.INT.fieldOf("r").forGetter(SimpleParticleOptions::r),
          Codec.INT.fieldOf("g").forGetter(SimpleParticleOptions::g),
          Codec.INT.fieldOf("b").forGetter(SimpleParticleOptions::b),
          Codec.FLOAT.fieldOf("gravity").forGetter(SimpleParticleOptions::gravity)
      ).apply(instance, SimpleParticleOptions::new)
  );
  public static final StreamCodec<ByteBuf, SimpleParticleOptions> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.INT, o -> o.r,
      ByteBufCodecs.INT, o -> o.g,
      ByteBufCodecs.INT, o -> o.b,
      ByteBufCodecs.FLOAT, o -> o.gravity,
      SimpleParticleOptions::new
  );

  public SimpleParticleOptions (int color, float gravity) {
    this((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, gravity);
  }

  @Override
  public ParticleType<?> getType() {
    return ModParticles.SINGLE_PIXEL.get();
  }

  public static class Type extends ParticleType<SimpleParticleOptions> {
    public Type(boolean overrideLimitter) {
      super(overrideLimitter);
    }

    @Override
    public MapCodec<SimpleParticleOptions> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, SimpleParticleOptions> streamCodec() {
      return STREAM_CODEC;
    }
  }
}
