package mysticmods.roots.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;

public record ColorGravityParticleOptions(ParticleType<?> type, int color1, int color2,
                                          float gravity) implements ParticleOptions {

  public ColorGravityParticleOptions(ParticleType<?> type, int color, float gravity) {
    this(type, color, color, gravity);
  }

  public ColorGravityParticleOptions(DeferredHolder<ParticleType<?>, ParticleType<ColorGravityParticleOptions>> type, int color1, int color2, float gravity) {
    this(type.get(), color1, color2, gravity);
  }

  public ColorGravityParticleOptions(DeferredHolder<ParticleType<?>, ParticleType<ColorGravityParticleOptions>> type, int color, float gravity) {
    this(type.get(), color, color, gravity);
  }

  public static MapCodec<ColorGravityParticleOptions> codec(ParticleType<?> type) {
    return RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.INT.fieldOf("color1").forGetter(ColorGravityParticleOptions::color1),
        Codec.INT.fieldOf("color2").forGetter(ColorGravityParticleOptions::color2),
        Codec.FLOAT.fieldOf("gravity").forGetter(ColorGravityParticleOptions::gravity)
    ).apply(instance, (c1, c2, gravity) -> new ColorGravityParticleOptions(type, c1, c2, gravity)));
  }

  public static StreamCodec<ByteBuf, ColorGravityParticleOptions> streamCodec(ParticleType<?> type) {
    return StreamCodec.composite(
        ByteBufCodecs.INT, o -> o.color1(),
        ByteBufCodecs.INT, o -> o.color2(),
        ByteBufCodecs.FLOAT, o -> o.gravity(),
        (c1, c2, gr) -> new ColorGravityParticleOptions(type, c1, c2, gr)
    );
  }

  @Override
  public ParticleType<?> getType() {
    return type;
  }
}
