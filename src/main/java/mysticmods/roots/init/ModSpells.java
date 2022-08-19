package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import mysticmods.roots.api.herbs.Cost;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.reference.SpellProperties;
import mysticmods.roots.api.reference.Spells;
import mysticmods.roots.api.spells.Spell;
import mysticmods.roots.spell.GrowthInfusionSpell;
import net.minecraft.resources.ResourceKey;

import java.util.List;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModSpells {
  public static final RegistryEntry<Spell> GROWTH_INFUSION = spell(Spells.GROWTH_INFUSION, () -> new GrowthInfusionSpell(List.of(Cost.add(ModHerbs.SACRED_MOSS, 0.325))));

  public static final RegistryEntry<Modifier> GROWTH_INFUSION_AREA = modifier(Spells.GROWTH_INFUSION, "area", () -> new Modifier(GROWTH_INFUSION, List.of(Cost.add(ModHerbs.SACRED_MOSS, 0.325))));

  public static final RegistryEntry<SpellProperty<Integer>> GROWTH_INFUSION_INTERVAL = property(Spells.GROWTH_INFUSION, "interval", () -> new SpellProperty<>(GROWTH_INFUSION, 110, Property.INTEGER_SERIALIZER, SpellProperties.INTERVAL));

  private static RegistryEntry<Spell> spell(ResourceKey<Spell> key, NonNullSupplier<Spell> builder) {
    return REGISTRATE.simple(key.location().getPath(), Spell.class, builder);
  }

  private static RegistryEntry<Modifier> modifier(ResourceKey<Spell> key, String name, NonNullSupplier<Modifier> builder) {
    return REGISTRATE.simple(key.location().getPath() + "/" + name, Modifier.class, builder);
  }

  private static <T> RegistryEntry<SpellProperty<T>> property(ResourceKey<Spell> key, String name, NonNullSupplier<SpellProperty<T>> builder) {
    return REGISTRATE.simple(key.location().getPath() + "/" + name, SpellProperty.class, builder);
  }

  public static void load() {
  }
}
