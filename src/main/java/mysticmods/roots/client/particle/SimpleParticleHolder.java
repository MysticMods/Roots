package mysticmods.roots.client.particle;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import mysticmods.roots.mixin.client.accessor.AccessorMixinParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleType;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

// Canonical implementation. Compare with client-side `MixinEntity`
public class SimpleParticleHolder implements IParticleHolder {
  private final Map<ParticleType<?>, Particle> particleMap = new Object2ObjectLinkedOpenHashMap<>();

  private final Map<ParticleType<?>, Set<Particle>> particleSetMap = new Object2ObjectLinkedOpenHashMap<>();

  @Nullable
  @Override
  public Particle getParticle(ParticleType<?> type, IParticleTester tester) {
    Set<Particle> particles = particleSetMap.get(type);
    if (particles == null || particles.isEmpty()) {
      return null;
    }

    particles.removeIf(o -> ((AccessorMixinParticle) o).roots_1_21$isRemoved());

    for (Particle particle : particles) {
      if (tester.test(particle)) {
        return particle;
      }
    }

    return null;
  }

  @Nullable
  @Override
  public Particle getParticle(ParticleType<?> type) {
    Particle result = particleMap.get(type);
    if (result == null) {
      return null;
    }

    if (((AccessorMixinParticle) result).roots_1_21$isRemoved()) {
      particleMap.remove(type);
      return null;
    }

    return result;
  }

  @Override
  public void setParticle(ParticleType<?> type, Particle particle) {
    Particle current = getParticle(type);
    if (current == null || ((AccessorMixinParticle) current).roots_1_21$isRemoved()) {
      particleMap.put(type, particle);
    }
  }

  @Override
  public void setParticle(ParticleType<?> type, Particle particle, IParticleTester tester) {
    if (!tester.test(particle)) {
      return;
    }

    Set<Particle> particles = particleSetMap.computeIfAbsent(type, k -> new ObjectLinkedOpenHashSet<>());

    Particle existing = getParticle(type, tester);
    if (existing == null || ((AccessorMixinParticle) existing).roots_1_21$isRemoved()) {
      particles.add(particle);
    }
  }
}
