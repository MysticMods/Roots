package mysticmods.roots.mixin.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import mysticmods.roots.client.particle.IMixinParticleHolder;
import mysticmods.roots.client.particle.IParticleTester;
import mysticmods.roots.mixin.client.accessor.AccessorMixinParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

@Mixin(Entity.class)
public abstract class MixinEntity$ParticleHolder implements IMixinParticleHolder {
  @Unique
  Map<ParticleType<?>, Particle> roots_1_21$particleMap = null;

  @Unique
  Map<ParticleType<?>, Set<Particle>> roots_1_21$particleSetMap = null;

  @Nullable
  @Override
  public Particle roots_1_21$getParticle(ParticleType<?> type, IParticleTester tester) {
    if (roots_1_21$particleSetMap == null) {
      return null;
    }

    Set<Particle> particles = roots_1_21$particleSetMap.get(type);
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
  public Particle roots_1_21$getParticle(ParticleType<?> type) {
    if (roots_1_21$particleMap == null) {
      return null;
    }
    Particle result = roots_1_21$particleMap.get(type);
    if (result == null) {
      return null;
    }

    if (((AccessorMixinParticle) result).roots_1_21$isRemoved()) {
      roots_1_21$particleMap.remove(type);
      return null;
    }

    return result;
  }

  @Override
  public void roots_1_21$setParticle(ParticleType<?> type, Particle particle) {
    if (roots_1_21$particleMap == null) {
      roots_1_21$particleMap = new Object2ObjectLinkedOpenHashMap<>();
    }

    Particle current = roots_1_21$getParticle(type);
    if (current == null || ((AccessorMixinParticle) current).roots_1_21$isRemoved()) {
      roots_1_21$particleMap.put(type, particle);
    }
  }

  @Override
  public void roots_1_21$setParticle(ParticleType<?> type, Particle particle, IParticleTester tester) {
    if (roots_1_21$particleSetMap == null) {
      roots_1_21$particleSetMap = new Object2ObjectLinkedOpenHashMap<>();
    }

    if (!tester.test(particle)) {
      return;
    }

    Set<Particle> particles = roots_1_21$particleSetMap.computeIfAbsent(type, k -> new ObjectLinkedOpenHashSet<>());

    Particle existing = roots_1_21$getParticle(type, tester);
    if (existing == null || ((AccessorMixinParticle) existing).roots_1_21$isRemoved()) {
      particles.add(particle);
    }
  }
}
