package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import mysticmods.roots.effect.FriendlyEarthEffect;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModEffects {
  public static final RegistryEntry<FriendlyEarthEffect> FRIENDLY_EARTH = REGISTRATE.effect("friendly_earth", FriendlyEarthEffect::new).register();

  public static void load() {
  }
}
