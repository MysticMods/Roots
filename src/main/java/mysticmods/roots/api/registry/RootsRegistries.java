package mysticmods.roots.api.registry;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.growth.*;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.property.PropertySerializer;
import mysticmods.roots.api.property.PropertyType;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.snapshot.SnapshotType;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellModifier;
import mysticmods.roots.api.test.entity.EntityTestType;
import mysticmods.roots.api.test.world.WorldTestType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class RootsRegistries {
  public static final Registry<Ritual> RITUALS = new RegistryBuilder<>(Keys.RITUALS).sync(true).create();
  public static final Registry<Spell> SPELLS = new RegistryBuilder<>(Keys.SPELLS).sync(true).create();
  public static final Registry<Herb> HERBS = new RegistryBuilder<>(Keys.HERBS).sync(true).create();
  public static final Registry<SpellModifier> SPELL_MODIFIERS = new RegistryBuilder<>(Keys.SPELL_MODIFIERS).sync(true)
      .create();
  public static final Registry<PropertyType<?>> PROPERTY_TYPES = new RegistryBuilder<>(Keys.PROPERTY_TYPES).sync(true)
      .create();
  public static final Registry<PropertySerializer<?>> PROPERTY_SERIALIZERS = new RegistryBuilder<>(Keys.PROPERTY_SERIALIZERS).sync(true)
      .create();
  public static final Registry<LevelCondition> LEVEL_CONDITIONS = new RegistryBuilder<>(Keys.LEVEL_CONDITIONS).sync(true)
      .create();
  public static final Registry<PlayerCondition> PLAYER_CONDITIONS = new RegistryBuilder<>(Keys.PLAYER_CONDITIONS).sync(true)
      .create();
  public static final Registry<EntityTestType<?>> ENTITY_TEST_TYPES = new RegistryBuilder<>(Keys.ENTITY_TEST_TYPES).sync(true)
      .create();
  public static final Registry<WorldTestType<?>> WORLD_TEST_TYPES = new RegistryBuilder<>(Keys.WORLD_TEST_TYPES).sync(true)
      .create();
  public static final Registry<SnapshotType<?>> SNAPSHOT_TYPES = new RegistryBuilder<>(Keys.SNAPSHOT_TYPES).sync(true)
      .create();
  public static final Registry<Grove> GROVES = new RegistryBuilder<>(Keys.GROVES).sync(true).create();
  public static final Registry<CanGrowFunction> CAN_GROW_FUNCTIONS = new RegistryBuilder<>(Keys.CAN_GROW_FUNCTIONS).sync(true)
      .create();
  public static final Registry<LightFunction> LIGHT_FUNCTIONS = new RegistryBuilder<>(Keys.LIGHT_FUNCTIONS).sync(true)
      .create();
  public static final Registry<CanHarvestFunction> CAN_HARVEST_FUNCTIONS = new RegistryBuilder<>(Keys.CAN_HARVEST_FUNCTIONS).sync(true)
      .create();

  public static class Keys {
    // Registry keys
    public static ResourceKey<Registry<Herb>> HERBS = key(RootsAPI.rl("herbs"));
    public static ResourceKey<Registry<Ritual>> RITUALS = key(RootsAPI.rl("rituals"));
    public static ResourceKey<Registry<Spell>> SPELLS = key(RootsAPI.rl("spells"));
    public static ResourceKey<Registry<SpellModifier>> SPELL_MODIFIERS = key(RootsAPI.rl("spell_modifiers"));
    public static ResourceKey<Registry<PropertyType<?>>> PROPERTY_TYPES = key(RootsAPI.rl("property_types"));
    public static ResourceKey<Registry<PropertySerializer<?>>> PROPERTY_SERIALIZERS = key(RootsAPI.rl("property_serializers"));
    public static ResourceKey<Registry<LevelCondition>> LEVEL_CONDITIONS = key(RootsAPI.rl("level_conditions"));
    public static ResourceKey<Registry<PlayerCondition>> PLAYER_CONDITIONS = key(RootsAPI.rl("player_conditions"));
    public static ResourceKey<Registry<EntityTestType<?>>> ENTITY_TEST_TYPES = key(RootsAPI.rl("entity_test_types"));
    public static ResourceKey<Registry<WorldTestType<?>>> WORLD_TEST_TYPES = key(RootsAPI.rl("world_test_types"));
    public static ResourceKey<Registry<SnapshotType<?>>> SNAPSHOT_TYPES = key(RootsAPI.rl("snapshot_types"));
    public static ResourceKey<Registry<Grove>> GROVES = key(RootsAPI.rl("groves"));
    public static ResourceKey<Registry<CanGrowFunction>> CAN_GROW_FUNCTIONS = key(RootsAPI.rl("can_grow_functions"));
    public static ResourceKey<Registry<LightFunction>> LIGHT_FUNCTIONS = key(RootsAPI.rl("light_functions"));
    public static ResourceKey<Registry<CanHarvestFunction>> CAN_HARVEST_FUNCTIONS = key(RootsAPI.rl("can_harvest_functions"));

    private static <T> ResourceKey<Registry<T>> key(ResourceLocation name) {
      return ResourceKey.createRegistryKey(name);
    }
  }

}
