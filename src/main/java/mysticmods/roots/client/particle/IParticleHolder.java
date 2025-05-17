package mysticmods.roots.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleType;

public interface IParticleHolder {
  Particle roots_1_21$getParticle(ParticleType<?> type);

  boolean roots_1_21$setParticle(ParticleType<?> type, Particle particle);
}
