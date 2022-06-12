package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import mysticmods.roots.api.ritual.Ritual;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModRituals {
  public static final RegistryEntry<Ritual> TRANSMUTATION = REGISTRATE.simple("transmutation", Ritual.class, () -> new Ritual());

  public static void load() {
  }
}
