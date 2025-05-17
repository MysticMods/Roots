package mysticmods.roots.mixin.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import mysticmods.roots.client.particle.IParticleHolder;
import mysticmods.roots.mixin.client.accessor.AccessorMixinParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.Map;

@Mixin(Entity.class)
public abstract class MixinEntity implements IParticleHolder {
  @Unique
  Map<ParticleType<?>, Particle> roots_1_21$particleMap = null;

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

    if (((AccessorMixinParticle)result).roots_1_21$isRemoved()) {
      roots_1_21$particleMap.remove(type);
      return null;
    }

    return result;
  }

  @Override
  public boolean roots_1_21$setParticle(ParticleType<?> type, Particle particle) {
    if (roots_1_21$particleMap == null) {
      roots_1_21$particleMap = new Object2ObjectLinkedOpenHashMap<>();
    }

    Particle current = roots_1_21$getParticle(type);
    if (current == null || ((AccessorMixinParticle)current).roots_1_21$isRemoved()) {
      roots_1_21$particleMap.put(type, particle);
      return true;
    } else {
      return false;
    }
  }
}
