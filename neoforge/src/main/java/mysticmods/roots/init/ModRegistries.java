package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.snapshot.SnapshotSerializer;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.test.entity.EntityTestType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid=RootsAPI.MODID, bus=EventBusSubscriber.Bus.MOD)
public class ModRegistries {
  // Deferred registers
  static final DeferredRegister<Herb> DEFERRED_HERB_REGISTRY = DeferredRegister.create(RootsAPI.HERB_REGISTRY, RootsAPI.MODID);
  static final DeferredRegister<Ritual> DEFERRED_RITUAL_REGISTRY = DeferredRegister.create(RootsAPI.RITUAL_REGISTRY, RootsAPI.MODID);
  static final DeferredRegister<Spell> DEFERRED_SPELL_REGISTRY = DeferredRegister.create(RootsAPI.SPELL_REGISTRY, RootsAPI.MODID);
  static final DeferredRegister<Modifier> DEFERRED_MODIFIER_REGISTRY = DeferredRegister.create(RootsAPI.MODIFIER_REGISTRY, RootsAPI.MODID);
  static final DeferredRegister<RitualProperty<?>> DEFERRED_RITUAL_PROPERTY_REGISTRY = DeferredRegister.create(RootsAPI.RITUAL_PROPERTY_REGISTRY, RootsAPI.MODID);
  static final DeferredRegister<SpellProperty<?>> DEFERRED_SPELL_PROPERTY_REGISTRY = DeferredRegister.create(RootsAPI.SPELL_PROPERTY_REGISTRY, RootsAPI.MODID);
  static final DeferredRegister<LevelCondition> DEFERRED_LEVEL_CONDITION_REGISTRY = DeferredRegister.create(RootsAPI.LEVEL_CONDITION_REGISTRY, RootsAPI.MODID);
  static final DeferredRegister<PlayerCondition> DEFERRED_PLAYER_CONDITION_REGISTRY = DeferredRegister.create(RootsAPI.PLAYER_CONDITION_REGISTRY, RootsAPI.MODID);
  static final DeferredRegister<SnapshotSerializer<?>> DEFERRED_SNAPSHOT_SERIALIZER_REGISTRY = DeferredRegister.create(RootsAPI.SNAPSHOT_SERIALIZER_REGISTRY, RootsAPI.MODID);
   static final DeferredRegister<EntityTestType<?>> DEFERRED_ENTITY_TEST_TYPE_REGISTRY = DeferredRegister.create(RootsAPI.ENTITY_TEST_TYPE_REGISTRY, RootsAPI.MODID);
  static final DeferredRegister<Grove> DEFERRED_GROVE_REGISTRY = DeferredRegister.create(RootsAPI.GROVE_REGISTRY, RootsAPI.MODID);

  static {
    DEFERRED_HERB_REGISTRY.makeRegistry((o) -> {});
    RootsRegistries.HERB_REGISTRY = DEFERRED_HERB_REGISTRY.getRegistry();
    DEFERRED_RITUAL_REGISTRY.makeRegistry((o) -> {});
    RootsRegistries.RITUAL_REGISTRY = DEFERRED_RITUAL_REGISTRY.getRegistry();
    DEFERRED_SPELL_REGISTRY.makeRegistry((o) -> {});
    RootsRegistries.SPELL_REGISTRY = DEFERRED_SPELL_REGISTRY.getRegistry();
    DEFERRED_MODIFIER_REGISTRY.makeRegistry((o) -> {});
    RootsRegistries.MODIFIER_REGISTRY = DEFERRED_MODIFIER_REGISTRY.getRegistry();
    DEFERRED_RITUAL_PROPERTY_REGISTRY.makeRegistry((o) -> {});
    RootsRegistries.RITUAL_PROPERTY_REGISTRY = DEFERRED_RITUAL_PROPERTY_REGISTRY.getRegistry();
    DEFERRED_SPELL_PROPERTY_REGISTRY.makeRegistry((o) -> {});
    RootsRegistries.SPELL_PROPERTY_REGISTRY = DEFERRED_SPELL_PROPERTY_REGISTRY.getRegistry();
    DEFERRED_LEVEL_CONDITION_REGISTRY.makeRegistry((o) -> {});
    RootsRegistries.LEVEL_CONDITION_REGISTRY = DEFERRED_LEVEL_CONDITION_REGISTRY.getRegistry();
    DEFERRED_PLAYER_CONDITION_REGISTRY.makeRegistry((o) -> {});
    RootsRegistries.PLAYER_CONDITION_REGISTRY = DEFERRED_PLAYER_CONDITION_REGISTRY.getRegistry();
    DEFERRED_SNAPSHOT_SERIALIZER_REGISTRY.makeRegistry((o) -> {});
    RootsRegistries.SNAPSHOT_SERIALIZER_REGISTRY = DEFERRED_SNAPSHOT_SERIALIZER_REGISTRY.getRegistry();
    DEFERRED_ENTITY_TEST_TYPE_REGISTRY.makeRegistry((o) -> {});
    RootsRegistries.ENTITY_TEST_TYPE = DEFERRED_ENTITY_TEST_TYPE_REGISTRY.getRegistry();
    DEFERRED_GROVE_REGISTRY.makeRegistry((o) -> {});
    RootsRegistries.GROVE_REGISTRY = DEFERRED_GROVE_REGISTRY.getRegistry();
  }

  public static void register(IEventBus bus) {
    DEFERRED_HERB_REGISTRY.register(bus);
    DEFERRED_RITUAL_REGISTRY.register(bus);
    DEFERRED_SPELL_REGISTRY.register(bus);
    DEFERRED_MODIFIER_REGISTRY.register(bus);
    DEFERRED_RITUAL_PROPERTY_REGISTRY.register(bus);
    DEFERRED_SPELL_PROPERTY_REGISTRY.register(bus);
    DEFERRED_LEVEL_CONDITION_REGISTRY.register(bus);
    DEFERRED_PLAYER_CONDITION_REGISTRY.register(bus);
    DEFERRED_SNAPSHOT_SERIALIZER_REGISTRY.register(bus);
    DEFERRED_ENTITY_TEST_TYPE_REGISTRY.register(bus);
    DEFERRED_GROVE_REGISTRY.register(bus);
  }

  public static void load() {
  }
}

