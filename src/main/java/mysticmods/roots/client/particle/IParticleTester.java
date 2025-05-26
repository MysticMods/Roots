package mysticmods.roots.client.particle;

import net.minecraft.client.particle.Particle;

@FunctionalInterface
public interface IParticleTester {
  boolean test (Particle particle);
}
