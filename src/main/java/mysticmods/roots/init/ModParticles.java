package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.particle.ColorGravityParticleOptions;
import mysticmods.roots.particle.ColorGravityParticleType;
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
  public static final DeferredHolder<ParticleType<?>, ParticleType<ColorGravityParticleOptions>> PYRE = PARTICLES.register("pyre", () -> new ColorGravityParticleType(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<ColorGravityParticleOptions>> METEOR = PARTICLES.register("meteor", () -> new ColorGravityParticleType(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<ColorGravityParticleOptions>> PYRE_LEAF = PARTICLES.register("pyre_leaf", () -> new ColorGravityParticleType(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<ColorGravityParticleOptions>> FEY_LIGHT = PARTICLES.register("fey_light", () -> new ColorGravityParticleType(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<ColorGravityParticleOptions>> WILDFIRE = PARTICLES.register("wildfire", () -> new ColorGravityParticleType(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<ColorGravityParticleOptions>> GROWTH = PARTICLES.register("growth", () -> new ColorGravityParticleType(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<ColorGravityParticleOptions>> CHANNEL_TARGET = PARTICLES.register("channel_target", () -> new ColorGravityParticleType(false));

  public static void register(IEventBus bus) {
    PARTICLES.register(bus);
  }
}
