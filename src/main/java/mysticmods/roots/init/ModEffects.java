package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import mysticmods.roots.effect.SenseDangerEffect;
import mysticmods.roots.effect.SimpleEffect;
import mysticmods.roots.effect.SkySoarerEffect;
import mysticmods.roots.effect.WakefulEffect;
import net.minecraft.world.effect.MobEffectCategory;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModEffects {
  public static final RegistryEntry<WakefulEffect> WAKEFUL = REGISTRATE.effect("wakeful", WakefulEffect::new).register();
  public static final RegistryEntry<SimpleEffect> FRIENDLY_EARTH = REGISTRATE.effect("friendly_earth", () -> new SimpleEffect(MobEffectCategory.BENEFICIAL, 0x946434)).register();
  public static final RegistryEntry<SkySoarerEffect> SKY_SOARER = REGISTRATE.effect("sky_soarer", SkySoarerEffect::new).register();
  public static final RegistryEntry<SimpleEffect> PETAL_SHELL = REGISTRATE.effect("petal_shell", () -> new SimpleEffect(MobEffectCategory.BENEFICIAL, 0xcc5ec8)).register();
  public static final RegistryEntry<SenseDangerEffect> SENSE_DANGER = REGISTRATE.effect("sense_danger", SenseDangerEffect::new).register();

  public static void load() {
  }
}
