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

public record SimpleParticleOptions(ParticleType<?> type, int color1, int color2,
                                    float gravity) implements ParticleOptions {

  public static MapCodec<SimpleParticleOptions> codec(ParticleType<?> type) {
    return RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.INT.fieldOf("color1").forGetter(SimpleParticleOptions::color1),
        Codec.INT.fieldOf("color2").forGetter(SimpleParticleOptions::color2),
        Codec.FLOAT.fieldOf("gravity").forGetter(SimpleParticleOptions::gravity)
    ).apply(instance, (c1, c2, gravity) -> new SimpleParticleOptions(type, c1, c2, gravity)));
  }

  public static StreamCodec<ByteBuf, SimpleParticleOptions> streamCodec(ParticleType<?> type) {
    return StreamCodec.composite(
        ByteBufCodecs.INT, o -> o.color1(),
        ByteBufCodecs.INT, o -> o.color2(),
        ByteBufCodecs.FLOAT, o -> o.gravity(),
        (c1, c2, gr) -> new SimpleParticleOptions(type, c1, c2, gr)
    );
  }

  public SimpleParticleOptions(ParticleType<?> type, int color, float gravity) {
    this(type, color, color, gravity);
  }

  public SimpleParticleOptions(DeferredHolder<ParticleType<?>, ParticleType<SimpleParticleOptions>> type, int color1, int color2, float gravity) {
    this(type.get(), color1, color2, gravity);
  }

  public SimpleParticleOptions(DeferredHolder<ParticleType<?>, ParticleType<SimpleParticleOptions>> type, int color, float gravity) {
    this(type.get(), color, color, gravity);
  }

  @Override
  public ParticleType<?> getType() {
    return type;
  }
}
