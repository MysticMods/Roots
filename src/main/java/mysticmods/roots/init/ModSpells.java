package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.SpellProperties;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.spells.Spell;
import mysticmods.roots.api.spells.Spells;
import net.minecraft.resources.ResourceKey;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModSpells {
  public static final RegistryEntry<Spell> GROWTH_INFUSION = ritual(Spells.GROWTH_INFUSION, () -> new Spell() {
    @Override
    public ResourceKey<Spell> getKey() {
      return Spells.GROWTH_INFUSION;
    }
  });
  public static final RegistryEntry<SpellProperty<?>> GROWTH_INFUSION_INTERVAL = REGISTRATE.simple("growth_infusion/interval", SpellProperty.class, () -> new SpellProperty<>(Spells.GROWTH_INFUSION, 110, Property.INTEGER_SERIALIZER, SpellProperties.INTERVAL));

  private static RegistryEntry<Spell> ritual(ResourceKey<Spell> key, NonNullSupplier<Spell> builder) {
    return REGISTRATE.simple(key.location().getPath(), Spell.class, builder);
  }

  public static void load() {
  }
}
