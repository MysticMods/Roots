package mysticmods.roots.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleType;

public interface IMixinParticleHolder extends IParticleHolder {
  Particle roots_1_21$getParticle(ParticleType<?> type);

  Particle roots_1_21$getParticle(ParticleType<?> type, IParticleTester tester);

  void roots_1_21$setParticle(ParticleType<?> type, Particle particle);

  void roots_1_21$setParticle(ParticleType<?> type, Particle particle, IParticleTester tester);

  @Override
  default Particle getParticle(ParticleType<?> type) {
    return roots_1_21$getParticle(type);
  }

  @Override
  default Particle getParticle(ParticleType<?> type, IParticleTester tester) {
    return roots_1_21$getParticle(type, tester);
  }

  @Override
  default void setParticle(ParticleType<?> type, Particle particle) {
    roots_1_21$setParticle(type, particle);
  }

  @Override
  default void setParticle(ParticleType<?> type, Particle particle, IParticleTester tester) {
    roots_1_21$setParticle(type, particle, tester);
  }
}
