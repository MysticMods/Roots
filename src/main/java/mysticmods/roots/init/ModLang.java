package mysticmods.roots.init;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;

import static mysticmods.roots.Roots.REGISTRATE;

@SuppressWarnings("unchecked")
public class ModLang {
  static {
    REGISTRATE.addDataGenerator(ProviderType.LANG, b -> {
      b.add("itemGroup.roots", "Roots");

      b.add("roots.tooltip.token.spell", "Spell: %s");
      b.add("roots.tooltip.token.modifier", "Modifier: %s");
      b.add("roots.tooltip.token.unlock", "Right-Click to unlock.");
      b.add("roots.tooltip.token.unlocked", "You've already unlocked this.");
      b.add("roots.tooltip.token.available_modifiers", "Available modifiers:");
      b.add("roots.tooltip.token.enabled_modifiers", "Enabled modifiers:");
      b.add("roots.tooltip.token.ritual", "Ritual: %s");

      b.add("roots.tooltip.cost.herb_cost", "%s %s");
      b.add("roots.tooltip.cost.cost_amount", "x%s");
      b.add("roots.tooltip.cost.cost_multiplier", "+%s");

      b.add("roots.tooltip.staff.selected", "Selected Slot: %s");
      b.add("roots.tooltip.staff.no_spell", "No spell selected.");

      b.add("roots.item.staff.with_spell", "Staff (%s)");

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

  public static String getComplexDescription(String value) {
    String[] split = value.split("/");
    return String.format("%s: %s", RegistrateLangProvider.toEnglishName(split[0]), RegistrateLangProvider.toEnglishName(split[1]));
  }

  public static void load() {
  }
}
