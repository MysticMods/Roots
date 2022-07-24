package mysticmods.roots.init;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import mysticmods.roots.api.herbs.Herb;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.property.SpellProperties;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spells.Spell;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

import static mysticmods.roots.Roots.REGISTRATE;

@SuppressWarnings("unchecked")
public class ModLang {
  static {
    REGISTRATE.addDataGenerator(ProviderType.LANG, b -> {
      b.add("roots.tooltip.token.spell", "Unlocks the spell: %s");
      b.add("roots.tooltip.token.modifier", "Unlocks the modifier: %s");

      for (Spell spell : Registries.SPELL_REGISTRY.get().getValues()) {
        b.add(spell.getDescriptionId(), RegistrateLangProvider.toEnglishName(spell.getKey().getPath()));
      }
      for (Ritual ritual : Registries.RITUAL_REGISTRY.get().getValues()) {
        b.add(ritual.getDescriptionId(), RegistrateLangProvider.toEnglishName(ritual.getKey().getPath()));
      }
      for (Herb herb : Registries.HERB_REGISTRY.get().getValues()) {
        b.add(herb.getDescriptionId(), RegistrateLangProvider.toEnglishName(herb.getKey().getPath()));
      }
      for (RitualProperty<?> property : Registries.RITUAL_PROPERTY_REGISTRY.get().getValues()) {
        b.add(property.getDescriptionId(), getComplexDescription(property.getKey().getPath()));
      }
      for (SpellProperty<?> property : Registries.SPELL_PROPERTY_REGISTRY.get().getValues()) {
        b.add(property.getDescriptionId(), getComplexDescription(property.getKey().getPath()));
      }
      for (Modifier modifier : Registries.MODIFIER_REGISTRY.get().getValues()) {
        b.add(modifier.getDescriptionId(), RegistrateLangProvider.toEnglishName(modifier.getKey().getPath()));
      }
    });
  }

  public static String getComplexDescription (String value) {
    String[] split = value.split("/");
    return String.format("%s: %s", RegistrateLangProvider.toEnglishName(split[0]), RegistrateLangProvider.toEnglishName(split[1]));
  }

  public static void load() {
  }
}
