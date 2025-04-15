package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModParticles {
  private static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, RootsAPI.MODID);

  // Emitters
  public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FEY_LIGHT_EMITTER = PARTICLES.register("fey_light_emitter", () -> new SimpleParticleType(false));

  // Actual particles
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> PYRE = PARTICLES.register("pyre", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> METEOR = PARTICLES.register("meteor", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> PYRE_LEAF = PARTICLES.register("pyre_leaf", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> FEY_LIGHT = PARTICLES.register("fey_light", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> WILDFIRE = PARTICLES.register("wildfire", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> GROWTH = PARTICLES.register("growth", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> ANIMAL_HARVEST = PARTICLES.register("animal_harvest", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> CHANNEL_TARGET = PARTICLES.register("channel_target", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> SPROUT_PORTAL = PARTICLES.register("sprout_portal", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> SPIRAL = PARTICLES.register("spiral", () -> new RootsParticleOptions.Type(false));

  public static void register(IEventBus bus) {
    PARTICLES.register(bus);
  }
}
