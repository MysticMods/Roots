package mysticmods.roots.api.registry;

import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.snapshot.SnapshotSerializer;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.function.Supplier;

public class Registries {
  public static Supplier<ForgeRegistry<Herb>> HERB_REGISTRY;
  public static Supplier<ForgeRegistry<Ritual>> RITUAL_REGISTRY;
  public static Supplier<ForgeRegistry<Spell>> SPELL_REGISTRY;
  public static Supplier<ForgeRegistry<Modifier>> MODIFIER_REGISTRY;
  public static Supplier<ForgeRegistry<RitualProperty<?>>> RITUAL_PROPERTY_REGISTRY;
  public static Supplier<ForgeRegistry<SpellProperty<?>>> SPELL_PROPERTY_REGISTRY;
  public static Supplier<ForgeRegistry<LevelCondition>> LEVEL_CONDITION_REGISTRY;
  public static Supplier<ForgeRegistry<PlayerCondition>> PLAYER_CONDITION_REGISTRY;

  public static Supplier<ForgeRegistry<SnapshotSerializer<?>>> SNAPSHOT_SERIALIZER_REGISTRY;
  public static Supplier<ForgeRegistry<EntityType<?>>> ENTITY_REGISTRY;
}
