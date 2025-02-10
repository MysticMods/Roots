package mysticmods.roots.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class SimpleParticleType extends ParticleType<SimpleParticleOptions> {
  public SimpleParticleType(boolean overrideLimitter) {
    super(overrideLimitter);
  }

  @Override
  public MapCodec<SimpleParticleOptions> codec() {
    return SimpleParticleOptions.codec(this);
  }

  @Override
  public StreamCodec<? super RegistryFriendlyByteBuf, SimpleParticleOptions> streamCodec() {
    return SimpleParticleOptions.streamCodec(this);
  }
}
