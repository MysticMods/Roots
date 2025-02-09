package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.particle.SimpleParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModParticles {
  private static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, RootsAPI.MODID);

  public static final DeferredHolder<ParticleType<?>, ParticleType<SimpleParticleOptions>> SINGLE_PIXEL = PARTICLES.register("single_pixel", () -> new SimpleParticleOptions.Type(false));

  public static void register(IEventBus bus) {
    PARTICLES.register(bus);
  }
}
