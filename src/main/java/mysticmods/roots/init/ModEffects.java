package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.effect.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModEffects {
  private static final DeferredRegister<MobEffect> REGISTER = DeferredRegister.create(Registries.MOB_EFFECT, RootsAPI.MODID);

  public static final DeferredHolder<MobEffect, WakefulEffect> WAKEFUL = REGISTER.register("wakeful", WakefulEffect::new);
  public static final DeferredHolder<MobEffect, FriendlyEarthEffect> FRIENDLY_EARTH = REGISTER.register("friendly_earth", FriendlyEarthEffect::new);
  public static final DeferredHolder<MobEffect, SkySoarerEffect> SKY_SOARER = REGISTER.register("sky_soarer", SkySoarerEffect::new);
  public static final DeferredHolder<MobEffect, PetalShellEffect> PETAL_SHELL = REGISTER.register("petal_shell", () -> new PetalShellEffect(MobEffectCategory.BENEFICIAL, 0xcc5ec8));
  public static final DeferredHolder<MobEffect, NondetectionEffect> NONDETECTION = REGISTER.register("nondetection", () -> new NondetectionEffect(MobEffectCategory.BENEFICIAL, 0x29465b));
  // TODO: is it beneficial?
  public static final DeferredHolder<MobEffect, SimpleEffect> GEAS = REGISTER.register("geas", () -> new GeasEffect(MobEffectCategory.BENEFICIAL, 0x850101, true));
  public static final DeferredHolder<MobEffect, SenseDangerEffect> SENSE_DANGER = REGISTER.register("sense_danger", SenseDangerEffect::new);
  public static final DeferredHolder<MobEffect, SimpleEffect> TEMPORAL_MORASS = REGISTER.register("temporal_morass", () -> new SimpleEffect(MobEffectCategory.NEUTRAL, 0x00008b, false));

  static {
    REGISTER.addAlias(RootsAPI.rl("time_stop"), RootsAPI.rl("temporal_morass"));
  }

  public static final DeferredHolder<MobEffect, AquaBubbleEffect> AQUA_BUBBLE = REGISTER.register("aqua_bubble", () -> new AquaBubbleEffect(MobEffectCategory.BENEFICIAL, 0x00ffff));

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
  }
}
