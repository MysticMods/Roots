package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herbs.Herb;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spells.Spell;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class ModRegistries {
  private static final DeferredRegister<Herb> DEFERRED_HERB_REGISTRY = DeferredRegister.create(RootsAPI.HERB_REGISTRY, RootsAPI.MODID);
  private static final DeferredRegister<Ritual> DEFERRED_RITUAL_REGISTRY = DeferredRegister.create(RootsAPI.RITUAL_REGISTRY, RootsAPI.MODID);
  private static final DeferredRegister<Spell> DEFERRED_SPELL_REGISTRY = DeferredRegister.create(RootsAPI.SPELL_REGISTRY, RootsAPI.MODID);
  private static final DeferredRegister<RitualProperty<?>> DEFERRED_RITUAL_PROPERTY_REGISTRY = DeferredRegister.create(RootsAPI.RITUAL_PROPERTY_REGISTRY, RootsAPI.MODID);
  private static final DeferredRegister<SpellProperty<?>> DEFERRED_SPELL_PROPERTY_REGISTRY = DeferredRegister.create(RootsAPI.SPELL_PROPERTY_REGISTRY, RootsAPI.MODID);

  public static Supplier<IForgeRegistry<Herb>> HERB_REGISTRY = DEFERRED_HERB_REGISTRY.makeRegistry(Herb.class, () -> new RegistryBuilder<Herb>().disableSaving().disableSync());
  public static Supplier<IForgeRegistry<Ritual>> RITUAL_REGISTRY = DEFERRED_RITUAL_REGISTRY.makeRegistry(Ritual.class, () -> new RegistryBuilder<Ritual>().disableSync().disableSaving());
  public static Supplier<IForgeRegistry<Spell>> SPELL_REGISTRY = DEFERRED_SPELL_REGISTRY.makeRegistry(Spell.class, () -> new RegistryBuilder<Spell>().disableSaving().disableSync());
  public static Supplier<IForgeRegistry<RitualProperty<?>>> RITUAL_PROPERTY_REGISTRY = DEFERRED_RITUAL_PROPERTY_REGISTRY.makeRegistry(c(RitualProperty.class), () -> new RegistryBuilder<RitualProperty<?>>().disableSync().disableSaving());
  public static Supplier<IForgeRegistry<SpellProperty<?>>> SPELL_PROPERTY_REGISTRY = DEFERRED_SPELL_PROPERTY_REGISTRY.makeRegistry(c(SpellProperty.class), () -> new RegistryBuilder<SpellProperty<?>>().disableSync().disableSaving());

  private static <T> Class<T> c(Class<?> cls) {
    return (Class<T>) cls;
  }

  public static void register(IEventBus bus) {
    DEFERRED_HERB_REGISTRY.register(bus);
    DEFERRED_RITUAL_REGISTRY.register(bus);
    DEFERRED_SPELL_REGISTRY.register(bus);
    DEFERRED_RITUAL_PROPERTY_REGISTRY.register(bus);
    DEFERRED_SPELL_PROPERTY_REGISTRY.register(bus);
  }

  public static void load() {
  }
}

