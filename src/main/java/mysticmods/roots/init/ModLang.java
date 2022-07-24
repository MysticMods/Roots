package mysticmods.roots.init;

import com.tterrag.registrate.providers.ProviderType;

import static mysticmods.roots.Roots.REGISTRATE;

@SuppressWarnings("unchecked")
public class ModLang {
  static {
    REGISTRATE.addDataGenerator(ProviderType.LANG, b -> {
      b.add("roots.tooltip.token.spell", "Unlocks the spell: %s");
      b.add("roots.tooltip.token.modifier", "Unlocks the modifier: %s");
    });
  }

  public static void load() {
  }
}
