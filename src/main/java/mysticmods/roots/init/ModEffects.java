package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import mysticmods.roots.effect.FriendlyEarthEffect;
import mysticmods.roots.effect.SkySoarerEffect;
import net.minecraftforge.registries.RegistryObject;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModEffects {
  public static final RegistryEntry<FriendlyEarthEffect> FRIENDLY_EARTH = REGISTRATE.effect("friendly_earth", FriendlyEarthEffect::new).register();

  public static final RegistryEntry<SkySoarerEffect> SKY_SOARER = REGISTRATE.effect("sky_soarer", SkySoarerEffect::new).register();

  public static void load() {
  }
}
