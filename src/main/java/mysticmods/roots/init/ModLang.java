package mysticmods.roots.init;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

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
      b.add("roots.tooltip.staff.no_spell", "No spell.");
      b.add("roots.tooltip.staff.spell_in_slot", "%s: %s%s");
      b.add("roots.tooltip.staff.is_selected", " (Selected)");

      b.add("roots.tooltip.hold_shift", "[Hold %s for more information.]");
      b.add("roots.tooltip.shift", "Shift");

      b.add("roots.item.staff.with_spell", "Staff (%s)");

      b.add("roots.drinks.slow_regen", "Gives a burst of revitalizing energy.");
      b.add("roots.drinks.wakefulness", "Perks you up, night or day; shoos those scary phantoms away!");
      b.add("roots.drinks.sour", "Sour and awful to drink! Leaves you hungry.");
      b.add("message.dandelion_cordial", "You feel well-rested!");

      b.add("roots.message.recipe.requires", "Requires: %s");
      b.add("roots.message.recipe.failures", "A number of conditions were not met:");
      b.add("roots.message.staff.missing_herbs", "Unable to cast %s, missing herbs.");

      // Squid-related stuff
      b.add("roots.message.squid.cooldown", "Give it time to produce more ink!");
      b.add("roots.message.runic_shears.cooldown", "More time must pass before this entity can be sheared again.");
      b.add("roots.subtitles.entity.squid.milk", "Squid milked");

      b.add("roots.subtitles.entity.fennec.aggro", "Fennec yips");
      b.add("roots.subtitles.entity.fennec.bark", "Fennec barks");
      b.add("roots.subtitles.entity.fennec.bite", "Fennec bites");
      b.add("roots.subtitles.entity.fennec.death", "Fennec dies");
      b.add("roots.subtitles.entity.fennec.eat", "Fennec eats");
      b.add("roots.subtitles.entity.fennec.idle", "Fennec yips");
      b.add("roots.subtitles.entity.fennec.sleep", "Fennec sleeps");
      b.add("roots.subtitles.entity.fennec.sniff", "Fennec sniffs");
      b.add("roots.subtitles.entity.fennec.spit", "Fennec spits");
      b.add("roots.subtitles.entity.sprout.ambient", "Sprout wanders");
      b.add("roots.subtitles.entity.duck.quack", "Duck quacks");
      b.add("roots.subtitles.entity.deer.ambient", "Deer squeals");

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
      for (LevelCondition condition : Registries.LEVEL_CONDITION_REGISTRY.get().getValues()) {
        b.add(condition.getDescriptionId(), RegistrateLangProvider.toEnglishName(condition.getKey().getPath()));
      }
    });
  }

  public static String getComplexDescription(String value) {
    String[] split = value.split("/");
    return String.format("%s: %s", RegistrateLangProvider.toEnglishName(split[0]), RegistrateLangProvider.toEnglishName(split[1]));
  }

  public static void load() {
  }

  public static MutableComponent holdShift () {
    return Component.translatable("roots.tooltip.hold_shift", Component.translatable("roots.tooltip.shift").setStyle(Style.EMPTY.withBold(true).withUnderlined(true).withColor(ChatFormatting.DARK_GRAY)));
  }
}
