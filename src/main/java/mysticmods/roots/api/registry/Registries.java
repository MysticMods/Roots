package mysticmods.roots.api.registry;

import mysticmods.roots.api.herbs.Herb;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.ritual.RitualCondition;
import mysticmods.roots.api.spells.Spell;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.function.Supplier;

public class Registries {
  public static Supplier<ForgeRegistry<Herb>> HERB_REGISTRY;
  public static Supplier<ForgeRegistry<Ritual>> RITUAL_REGISTRY;
  public static Supplier<ForgeRegistry<Spell>> SPELL_REGISTRY;
  public static Supplier<ForgeRegistry<Modifier>> MODIFIER_REGISTRY;
  public static Supplier<ForgeRegistry<RitualProperty<?>>> RITUAL_PROPERTY_REGISTRY;
  public static Supplier<ForgeRegistry<SpellProperty<?>>> SPELL_PROPERTY_REGISTRY;
  public static Supplier<ForgeRegistry<RitualCondition>> RITUAL_CONDITION_REGISTRY;
}
