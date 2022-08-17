package mysticmods.roots.api.registry;

import mysticmods.roots.api.herbs.Herb;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.property.ModifierProperty;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.ritual.RitualCondition;
import mysticmods.roots.api.spells.Spell;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class Registries {
  public static Supplier<IForgeRegistry<Herb>> HERB_REGISTRY;
  public static Supplier<IForgeRegistry<Ritual>> RITUAL_REGISTRY;
  public static Supplier<IForgeRegistry<Spell>> SPELL_REGISTRY;
  public static Supplier<IForgeRegistry<Modifier>> MODIFIER_REGISTRY;
  public static Supplier<IForgeRegistry<RitualProperty<?>>> RITUAL_PROPERTY_REGISTRY;
  public static Supplier<IForgeRegistry<SpellProperty<?>>> SPELL_PROPERTY_REGISTRY;
  public static Supplier<IForgeRegistry<ModifierProperty<?>>> MODIFIER_PROPERTY_REGISTRY;

  public static Supplier<IForgeRegistry<RitualCondition>> RITUAL_CONDITION_REGISTRY;
}
