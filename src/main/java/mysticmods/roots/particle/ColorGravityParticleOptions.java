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

public record ColorGravityParticleOptions(ParticleType<?> type, int color1, int color2
) implements ParticleOptions {

  public static MapCodec<ColorGravityParticleOptions> codec(ParticleType<?> type) {
    return RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.INT.fieldOf("color1").forGetter(ColorGravityParticleOptions::color1),
        Codec.INT.fieldOf("color2").forGetter(ColorGravityParticleOptions::color2)
    ).apply(instance, (c1, c2) -> new ColorGravityParticleOptions(type, c1, c2)));
  }

  public static StreamCodec<ByteBuf, ColorGravityParticleOptions> streamCodec(ParticleType<?> type) {
    return StreamCodec.composite(
        ByteBufCodecs.INT, ColorGravityParticleOptions::color1,
        ByteBufCodecs.INT, ColorGravityParticleOptions::color2,
        (c1, c2) -> new ColorGravityParticleOptions(type, c1, c2)
    );
  }

  public ColorGravityParticleOptions(ParticleType<?> type, int color) {
    this(type, color, color);
  }

  public ColorGravityParticleOptions(DeferredHolder<ParticleType<?>, ParticleType<ColorGravityParticleOptions>> type, int color1, int color2) {
    this(type.get(), color1, color2);
  }

  public ColorGravityParticleOptions(DeferredHolder<ParticleType<?>, ParticleType<ColorGravityParticleOptions>> type, int color) {
    this(type.get(), color, color);
  }

  @Override
  public ParticleType<?> getType() {
    return type;
  }
}
