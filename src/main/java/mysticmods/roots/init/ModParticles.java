package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.particle.SimpleParticleOptions;
import mysticmods.roots.particle.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModParticles {
  private static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, RootsAPI.MODID);

  public static final DeferredHolder<ParticleType<?>, ParticleType<SimpleParticleOptions>> PYRE = PARTICLES.register("pyre", () -> new SimpleParticleType(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<SimpleParticleOptions>> PYRE_LEAF = PARTICLES.register("pyre_leaf", () -> new SimpleParticleType(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<SimpleParticleOptions>> FEY_LIGHT = PARTICLES.register("fey_light", () -> new SimpleParticleType(false));

  public static void register(IEventBus bus) {
    PARTICLES.register(bus);
  }
}
