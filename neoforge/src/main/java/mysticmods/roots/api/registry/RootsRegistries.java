package mysticmods.roots.api.registry;

import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.snapshot.SnapshotSerializer;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.test.entity.EntityTestType;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public class RootsRegistries {
  public static Supplier<Registry<Herb>> HERB_REGISTRY;
  public static Supplier<Registry<Ritual>> RITUAL_REGISTRY;
  public static Supplier<Registry<Spell>> SPELL_REGISTRY;
  public static Supplier<Registry<Modifier>> MODIFIER_REGISTRY;
  public static Supplier<Registry<RitualProperty<?>>> RITUAL_PROPERTY_REGISTRY;
  public static Supplier<Registry<SpellProperty<?>>> SPELL_PROPERTY_REGISTRY;
  public static Supplier<Registry<LevelCondition>> LEVEL_CONDITION_REGISTRY;
  public static Supplier<Registry<PlayerCondition>> PLAYER_CONDITION_REGISTRY;

  public static Supplier<Registry<SnapshotSerializer<?>>> SNAPSHOT_SERIALIZER_REGISTRY;
  public static Supplier<Registry<EntityType<?>>> ENTITY_REGISTRY;
  public static Supplier<Registry<EntityTestType<?>>> ENTITY_TEST_TYPE;
  public static Supplier<Registry<Grove>> GROVE_REGISTRY;
}
