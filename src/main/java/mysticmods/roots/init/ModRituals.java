package mysticmods.roots.init;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.ritual.Rituals;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModRituals {
  public static final RegistryEntry<Ritual> TRANSMUTATION = ritual(Rituals.TRANSMUTATION, () -> new Ritual());
  public static final RegistryEntry<Property.RitualProperty<?>> TRANSMUTATION_COUNT = REGISTRATE.simple("transmutation/count", Property.RitualProperty.class, () -> new Property.RitualProperty<>(Rituals.TRANSMUTATION, 10, Property.INTEGER_SERIALIZER));

  static {
    REGISTRATE.addDataGenerator(ProviderType.RECIPE, (p) -> {

    });
  }

  private static RegistryEntry<Ritual> ritual (ResourceKey<Ritual> key, NonNullSupplier<Ritual> builder) {
    return REGISTRATE.simple(key.location().getPath(), Ritual.class, builder);
  }

  public static void load() {
  }
}
