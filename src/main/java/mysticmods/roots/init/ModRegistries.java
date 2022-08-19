package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herbs.Herb;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.registry.VanillaRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.ritual.RitualCondition;
import mysticmods.roots.api.spells.Spell;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

public class ModRegistries {
  private static final DeferredRegister<Herb> DEFERRED_HERB_REGISTRY = DeferredRegister.create(RootsAPI.HERB_REGISTRY, RootsAPI.MODID);
  private static final DeferredRegister<Ritual> DEFERRED_RITUAL_REGISTRY = DeferredRegister.create(RootsAPI.RITUAL_REGISTRY, RootsAPI.MODID);
  private static final DeferredRegister<Spell> DEFERRED_SPELL_REGISTRY = DeferredRegister.create(RootsAPI.SPELL_REGISTRY, RootsAPI.MODID);
  private static final DeferredRegister<Modifier> DEFERRED_MODIFIER_REGISTRY = DeferredRegister.create(RootsAPI.MODIFIER_REGISTRY, RootsAPI.MODID);
  private static final DeferredRegister<RitualProperty<?>> DEFERRED_RITUAL_PROPERTY_REGISTRY = DeferredRegister.create(RootsAPI.RITUAL_PROPERTY_REGISTRY, RootsAPI.MODID);
  private static final DeferredRegister<SpellProperty<?>> DEFERRED_SPELL_PROPERTY_REGISTRY = DeferredRegister.create(RootsAPI.SPELL_PROPERTY_REGISTRY, RootsAPI.MODID);
  private static final DeferredRegister<RitualCondition> DEFERRED_RITUAL_CONDITION_REGISTRY = DeferredRegister.create(RootsAPI.RITUAL_CONDITION_REGISTRY, RootsAPI.MODID);

  static {
    Registries.HERB_REGISTRY = ForgeRegistryWrapper.of(DEFERRED_HERB_REGISTRY.makeRegistry(Herb.class, () -> new RegistryBuilder<Herb>().disableSaving().disableSync()));
    Registries.RITUAL_REGISTRY = ForgeRegistryWrapper.of(DEFERRED_RITUAL_REGISTRY.makeRegistry(Ritual.class, () -> new RegistryBuilder<Ritual>().disableSync().disableSaving()));
    Registries.SPELL_REGISTRY = ForgeRegistryWrapper.of(DEFERRED_SPELL_REGISTRY.makeRegistry(Spell.class, () -> new RegistryBuilder<Spell>().disableSaving().disableSync()));
    Registries.MODIFIER_REGISTRY = ForgeRegistryWrapper.of(DEFERRED_MODIFIER_REGISTRY.makeRegistry(Modifier.class, () -> new RegistryBuilder<Modifier>().disableSaving().disableSync()));
    Registries.RITUAL_PROPERTY_REGISTRY = ForgeRegistryWrapper.of(DEFERRED_RITUAL_PROPERTY_REGISTRY.makeRegistry(c(RitualProperty.class), () -> new RegistryBuilder<RitualProperty<?>>().disableSync().disableSaving()));
    Registries.SPELL_PROPERTY_REGISTRY = ForgeRegistryWrapper.of(DEFERRED_SPELL_PROPERTY_REGISTRY.makeRegistry(c(SpellProperty.class), () -> new RegistryBuilder<SpellProperty<?>>().disableSync().disableSaving()));
    Registries.RITUAL_CONDITION_REGISTRY = ForgeRegistryWrapper.of(DEFERRED_RITUAL_CONDITION_REGISTRY.makeRegistry(RitualCondition.class, () -> new RegistryBuilder<RitualCondition>().disableSync().disableSaving()));
    VanillaRegistries.ENTITIES = ForgeRegistryWrapper.of(() -> ForgeRegistries.ENTITIES);
  }

  private static <T> Class<T> c(Class<?> cls) {
    return (Class<T>) cls;
  }

  public static void register(IEventBus bus) {
    DEFERRED_HERB_REGISTRY.register(bus);
    DEFERRED_RITUAL_REGISTRY.register(bus);
    DEFERRED_SPELL_REGISTRY.register(bus);
    DEFERRED_MODIFIER_REGISTRY.register(bus);
    DEFERRED_RITUAL_PROPERTY_REGISTRY.register(bus);
    DEFERRED_SPELL_PROPERTY_REGISTRY.register(bus);
    DEFERRED_RITUAL_CONDITION_REGISTRY.register(bus);
  }

  public static void load() {
  }

  public static class ForgeRegistryWrapper<T extends IForgeRegistryEntry<T>> implements Supplier<ForgeRegistry<T>> {
    private final Supplier<IForgeRegistry<T>> registrySupplier;

    private ForgeRegistry<T> registry = null;

    protected ForgeRegistryWrapper(Supplier<IForgeRegistry<T>> registrySupplier) {
      this.registrySupplier = registrySupplier;
    }

    @Override
    public ForgeRegistry<T> get() {
      if (registry == null) {
        registry = (ForgeRegistry<T>) registrySupplier.get();
      }
      return registry;
    }

    public static <T extends IForgeRegistryEntry<T>> ForgeRegistryWrapper<T> of(Supplier<IForgeRegistry<T>> registrySupplier) {
      return new ForgeRegistryWrapper<>(registrySupplier);
    }
  }
}

