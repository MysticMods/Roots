package mysticmods.roots.api.registry;

import mysticmods.roots.api.herbs.Herb;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.ritual.condition.LevelCondition;
import mysticmods.roots.api.ritual.condition.PlayerCondition;
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
  public static Supplier<ForgeRegistry<LevelCondition>> RITUAL_LEVEL_CONDITION;
  public static Supplier<ForgeRegistry<PlayerCondition>> RITUAL_PLAYER_CONDITION;
  public static Supplier<ForgeRegistry<EntityType<?>>> ENTITIES;
}
