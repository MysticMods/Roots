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
  public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SYLVAN_LIGHT_EMITTER = PARTICLES.register("sylvan_light_emitter", () -> new SimpleParticleType(false));
  static {
    PARTICLES.addAlias(RootsAPI.rl("fey_light_emitter"), RootsAPI.rl("sylvan_light_emitter"));
  }
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> DISARM_EMITTER = PARTICLES.register("disarm_emitter", () -> new RootsParticleOptions.Type(false));
  // Sky Soarer Emitter
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> SKY_SOARER_EMITTER = PARTICLES.register("sky_soarer_emitter", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> LIFE_DRAIN_EMITTER = PARTICLES.register("life_drain_emitter", () -> new RootsParticleOptions.Type(false));

  // Actual particles
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> PYRE = PARTICLES.register("pyre", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> METEOR = PARTICLES.register("meteor", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> PYRE_LEAF = PARTICLES.register("pyre_leaf", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> SYLVAN_LIGHT = PARTICLES.register("sylvan_light", () -> new RootsParticleOptions.Type(false));
  static {
    PARTICLES.addAlias(RootsAPI.rl("fey_light"), RootsAPI.rl("sylvan_light"));
  }
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> WILDFIRE = PARTICLES.register("wildfire", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> GROWTH = PARTICLES.register("growth", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> ANIMAL_HARVEST = PARTICLES.register("animal_harvest", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> CHANNEL_TARGET = PARTICLES.register("channel_target", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> CHANNEL = PARTICLES.register("channel", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> CHANNEL_FAIL = PARTICLES.register("channel_fail", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> CHANNEL_JAUNT = PARTICLES.register("channel_jaunt", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> SPROUT_PORTAL = PARTICLES.register("sprout_portal", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> SPIRAL = PARTICLES.register("spiral", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> GROVE_STONE = PARTICLES.register("grove_stone", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> WHIRLWIND = PARTICLES.register("whirlwind", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> TEST = PARTICLES.register("test", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, SimpleParticleType> AIR_BUBBLE = PARTICLES.register("air_bubble", () -> new SimpleParticleType(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> DISARM = PARTICLES.register("disarm", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> SMOKE = PARTICLES.register("smoke", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> FOG = PARTICLES.register("fog", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> WIND = PARTICLES.register("wind", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> SKY_SOARER = PARTICLES.register("sky_soarer", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> SKY_SOARER_PUFF = PARTICLES.register("sky_soarer_puff", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> EXTENSION = PARTICLES.register("extension", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> SHATTER_BEAM = PARTICLES.register("shatter_beam", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> MAGNETISM = PARTICLES.register("magnetism", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> PETAL_SHELL = PARTICLES.register("petal_shell", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> LIFE_DRAIN = PARTICLES.register("life_drain", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> LIFE_DRAINED = PARTICLES.register("life_drained", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> DANDELION = PARTICLES.register("dandelion", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> PETAL = PARTICLES.register("petal", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> HARVEST = PARTICLES.register("harvest", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> DESATURATE = PARTICLES.register("desaturate", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> SATURATE = PARTICLES.register("saturate", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> HEAL = PARTICLES.register("heal", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> NONDETECTION = PARTICLES.register("nondetection", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> SANCTUARY = PARTICLES.register("sanctuary", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> ROSE_THORNS = PARTICLES.register("rose_thorns", () -> new RootsParticleOptions.Type(false));
  public static final DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> LIGHT = PARTICLES.register("light", () -> new RootsParticleOptions.Type(false));

  public static void register(IEventBus bus) {
    PARTICLES.register(bus);
  }
}
