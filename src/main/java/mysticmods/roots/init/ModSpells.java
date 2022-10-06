package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.reference.SpellProperties;
import mysticmods.roots.api.reference.Spells;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.spell.*;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceKey;

import java.util.List;
import java.util.function.Supplier;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModSpells {
  // Acid Cloud (20 cooldown)
  public static final RegistryEntry<AcidCloudSpell> ACID_CLOUD = spell(Spells.ACID_CLOUD, AcidCloudSpell::new, ChatFormatting.GREEN, () -> List.of(Cost.add(ModHerbs.BAFFLECAP, 0.250), Cost.add(ModHerbs.CLOUD_BERRY, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> ACID_CLOUD_COOLDOWN = property(Spells.ACID_CLOUD, "cooldown", () -> new SpellProperty<>(ACID_CLOUD::get, 20, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Aqua Bubble (1200 cooldown)
  public static final RegistryEntry<AquaBubbleSpell> AQUA_BUBBLE = spell(Spells.AQUA_BUBBLE, AquaBubbleSpell::new, ChatFormatting.AQUA, () -> List.of(Cost.add(ModHerbs.DEWGONIA, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> AQUA_BUBBLE_COOLDOWN = property(Spells.AQUA_BUBBLE, "cooldown", () -> new SpellProperty<>(AQUA_BUBBLE::get, 1200, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // TODO: What does this actually do?
  // Augment (350 cooldown)
  public static final RegistryEntry<AugmentSpell> AUGMENT = spell(Spells.AUGMENT, AugmentSpell::new, ChatFormatting.DARK_GREEN, () -> List.of(Cost.add(ModHerbs.GROVE_MOSS, 0.250), Cost.add(ModHerbs.INFERNO_BULB, 0.125)));
  public static final RegistryEntry<SpellProperty<Integer>> AUGMENT_COOLDOWN = property(Spells.AUGMENT, "cooldown", () -> new SpellProperty<>(AUGMENT::get, 350, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Light Drifter (600 cooldown)
  public static final RegistryEntry<LightDrifterSpell> LIGHT_DRIFTER = spell(Spells.LIGHT_DRIFTER, LightDrifterSpell::new, ChatFormatting.DARK_PURPLE, () -> List.of(Cost.add(ModHerbs.MOONGLOW, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> LIGHT_DRIFTER_COOLDOWN = property(Spells.LIGHT_DRIFTER, "cooldown", () -> new SpellProperty<>(LIGHT_DRIFTER::get, 600, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Magnetism (350 cooldown)
  public static final RegistryEntry<MagnetismSpell> MAGNETISM = spell(Spells.MAGNETISM, MagnetismSpell::new, ChatFormatting.YELLOW, () -> List.of(Cost.add(ModHerbs.SPIRITLEAF, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> MAGNETISM_COOLDOWN = property(Spells.MAGNETISM, "cooldown", () -> new SpellProperty<>(MAGNETISM::get, 350, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Dandelion Winds (20 cooldown)
  public static final RegistryEntry<DandelionWindsSpell> DANDELION_WINDS = spell(Spells.DANDELION_WINDS, DandelionWindsSpell::new, ChatFormatting.YELLOW, () -> List.of(Cost.add(ModHerbs.CLOUD_BERRY, 0.250), Cost.add(ModHerbs.GROVE_MOSS, 0.125)));
  public static final RegistryEntry<SpellProperty<Integer>> DANDELION_WINDS_COOLDOWN = property(Spells.DANDELION_WINDS, "cooldown", () -> new SpellProperty<>(DANDELION_WINDS::get, 20, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Desaturate (500 cooldown)
  public static final RegistryEntry<DesaturateSpell> DESATURATE = spell(Spells.DESATURATE, DesaturateSpell::new, ChatFormatting.GREEN, () -> List.of(Cost.add(ModHerbs.WILDEWHEET, 0.250), Cost.add(ModHerbs.GROVE_MOSS, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> DESATURATE_COOLDOWN = property(Spells.DESATURATE, "cooldown", () -> new SpellProperty<>(DESATURATE::get, 500, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Disarm spell (350 cooldown)
  public static final RegistryEntry<DisarmSpell> DISARM = spell(Spells.DISARM, DisarmSpell::new, ChatFormatting.AQUA, () -> List.of(Cost.add(ModHerbs.SPIRITLEAF, 0.250), Cost.add(ModHerbs.BAFFLECAP, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> DISARM_COOLDOWN = property(Spells.DISARM, "cooldown", () -> new SpellProperty<>(DISARM::get, 350, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // TODO: What does this actually do?
  // Extension spell (350 cooldown)
  public static final RegistryEntry<ExtensionSpell> EXTENSION = spell(Spells.EXTENSION, ExtensionSpell::new, ChatFormatting.BLUE, () -> List.of(Cost.add(ModHerbs.GROVE_MOSS, 0.250), Cost.add(ModHerbs.WILDROOT, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> EXTENSION_COOLDOWN = property(Spells.EXTENSION, "cooldown", () -> new SpellProperty<>(EXTENSION::get, 350, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Nondetection (350 cooldown)
  public static final RegistryEntry<NondetectionSpell> NONDETECTION = spell(Spells.NONDETECTION, NondetectionSpell::new, ChatFormatting.DARK_AQUA, () -> List.of(Cost.add(ModHerbs.SPIRITLEAF, 0.250), Cost.add(ModHerbs.DEWGONIA, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> NONDETECTION_COOLDOWN = property(Spells.NONDETECTION, "cooldown", () -> new SpellProperty<>(NONDETECTION::get, 350, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Fey Light (20 cooldown)
  public static final RegistryEntry<FeyLightSpell> FEY_LIGHT = spell(Spells.FEY_LIGHT, FeyLightSpell::new, ChatFormatting.LIGHT_PURPLE, () -> List.of(Cost.add(ModHerbs.GROVE_MOSS, 0.250), Cost.add(ModHerbs.PERESKIA, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> FEY_LIGHT_COOLDOWN = property(Spells.FEY_LIGHT, "cooldown", () -> new SpellProperty<>(FEY_LIGHT::get, 20, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Geas (80 cooldown)
  public static final RegistryEntry<GeasSpell> GEAS = spell(Spells.GEAS, GeasSpell::new, ChatFormatting.RED, () -> List.of(Cost.add(ModHerbs.BAFFLECAP, 0.250), Cost.add(ModHerbs.GROVE_MOSS, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> GEAS_COOLDOWN = property(Spells.GEAS, "cooldown", () -> new SpellProperty<>(GEAS::get, 80, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Control Undead (320 cooldown)
  public static final RegistryEntry<ControlUndeadSpell> CONTROL_UNDEAD = spell(Spells.CONTROL_UNDEAD, ControlUndeadSpell::new, ChatFormatting.DARK_GREEN, () -> List.of(Cost.add(ModHerbs.BAFFLECAP, 0.250), Cost.add(ModHerbs.MOONGLOW, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> CONTROL_UNDEAD_COOLDOWN = property(Spells.CONTROL_UNDEAD, "cooldown", () -> new SpellProperty<>(CONTROL_UNDEAD::get, 320, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Growth Infusion (20 cooldown)
  public static final RegistryEntry<GrowthInfusionSpell> GROWTH_INFUSION = spell(Spells.GROWTH_INFUSION, GrowthInfusionSpell::new, ChatFormatting.YELLOW, () -> List.of(Cost.add(ModHerbs.GROVE_MOSS, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> GROWTH_INFUSION_COOLDOWN = property(Spells.GROWTH_INFUSION, "cooldown", () -> new SpellProperty<>(GROWTH_INFUSION::get, 20, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  public static final RegistryEntry<SpellProperty<Double>> GROWTH_INFUSION_ADDED_REACH = property(Spells.GROWTH_INFUSION, "added_reach", () -> new SpellProperty<>(GROWTH_INFUSION::get, 0.0, Property.DOUBLE_SERIALIZER, SpellProperties.ADDED_REACH));

  public static final RegistryEntry<SpellProperty<Integer>> GROWTH_INFUSION_BASE_TICKS = property(Spells.GROWTH_INFUSION, "base_ticks", () -> new SpellProperty<>(GROWTH_INFUSION::get, 2, Property.INTEGER_SERIALIZER, "The default number of growth ticks applied by base growth infusion"));

  // Rampant Growth (20 cooldown)
  public static final RegistryEntry<RampantGrowthSpell> RAMPANT_GROWTH = spell(Spells.RAMPANT_GROWTH, RampantGrowthSpell::new, ChatFormatting.YELLOW, () -> List.of(Cost.add(ModHerbs.WILDEWHEET, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> RAMPANT_GROWTH_COOLDOWN = property(Spells.RAMPANT_GROWTH, "cooldown", () -> new SpellProperty<>(RAMPANT_GROWTH::get, 20, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Harvest (25 cooldown)
  public static final RegistryEntry<HarvestSpell> HARVEST = spell(Spells.HARVEST, HarvestSpell::new, ChatFormatting.YELLOW, () -> List.of(Cost.add(ModHerbs.WILDEWHEET, 0.250), Cost.add(ModHerbs.STALICRIPE, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> HARVEST_COOLDOWN = property(Spells.HARVEST, "cooldown", () -> new SpellProperty<>(HARVEST::get, 25, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Life Drain (20 cooldown)
  public static final RegistryEntry<LifeDrainSpell> LIFE_DRAIN = spell(Spells.LIFE_DRAIN, LifeDrainSpell::new, ChatFormatting.DARK_PURPLE, () -> List.of(Cost.add(ModHerbs.WILDEWHEET, 0.250), Cost.add(ModHerbs.STALICRIPE, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> LIFE_DRAIN_COOLDOWN = property(Spells.LIFE_DRAIN, "cooldown", () -> new SpellProperty<>(LIFE_DRAIN::get, 20, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Petal Shell (120 cooldown)
  public static final RegistryEntry<PetalShellSpell> PETAL_SHELL = spell(Spells.PETAL_SHELL, PetalShellSpell::new, ChatFormatting.LIGHT_PURPLE, () -> List.of(Cost.add(ModHerbs.PERESKIA, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> PETAL_SHELL_COOLDOWN = property(Spells.PETAL_SHELL, "cooldown", () -> new SpellProperty<>(PETAL_SHELL::get, 120, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));
  public static final RegistryEntry<SpellProperty<Integer>> PETAL_SHELL_DURATION = property(Spells.PETAL_SHELL, "duration", () -> new SpellProperty<>(PETAL_SHELL::get, 30, Property.INTEGER_SERIALIZER, SpellProperties.DURATION));
  public static final RegistryEntry<SpellProperty<Integer>> PETAL_SHELL_COUNT = property(Spells.PETAL_SHELL, "count", () -> new SpellProperty<>(PETAL_SHELL::get, 3, Property.INTEGER_SERIALIZER, "The number of petal shells."));

  // Radiance (20 cooldown)
  public static final RegistryEntry<RadianceSpell> RADIANCE = spell(Spells.RADIANCE, RadianceSpell::new, ChatFormatting.GOLD, () -> List.of(Cost.add(ModHerbs.INFERNO_BULB, 0.250), Cost.add(ModHerbs.CLOUD_BERRY, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> RADIANCE_COOLDOWN = property(Spells.RADIANCE, "cooldown", () -> new SpellProperty<>(RADIANCE::get, 20, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Rose Thorns (24 cooldown)
  public static final RegistryEntry<RoseThornsSpell> ROSE_THORNS = spell(Spells.ROSE_THORNS, RoseThornsSpell::new, ChatFormatting.RED, () -> List.of(Cost.add(ModHerbs.WILDROOT, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> ROSE_THORNS_COOLDOWN = property(Spells.ROSE_THORNS, "cooldown", () -> new SpellProperty<>(ROSE_THORNS::get, 24, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Sanctuary (20 cooldown)
  public static final RegistryEntry<SanctuarySpell> SANCTUARY = spell(Spells.SANCTUARY, SanctuarySpell::new, ChatFormatting.LIGHT_PURPLE, () -> List.of(Cost.add(ModHerbs.PERESKIA, 0.250), Cost.add(ModHerbs.STALICRIPE, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> SANCTUARY_COOLDOWN = property(Spells.SANCTUARY, "cooldown", () -> new SpellProperty<>(SANCTUARY::get, 20, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Shatter (20 cooldown)
  public static final RegistryEntry<ShatterSpell> SHATTER = spell(Spells.SHATTER, ShatterSpell::new, ChatFormatting.YELLOW, () -> List.of(Cost.add(ModHerbs.STALICRIPE, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> SHATTER_COOLDOWN = property(Spells.SHATTER, "cooldown", () -> new SpellProperty<>(SHATTER::get, 20, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Jaunt (80 cooldown)
  public static final RegistryEntry<JauntSpell> JAUNT = spell(Spells.JAUNT, JauntSpell::new, ChatFormatting.DARK_PURPLE, () -> List.of(Cost.add(ModHerbs.PERESKIA, 0.250), Cost.add(ModHerbs.SPIRITLEAF, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> JAUNT_COOLDOWN = property(Spells.JAUNT, "cooldown", () -> new SpellProperty<>(JAUNT::get, 80, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Storm Cloud (100 cooldown)
  public static final RegistryEntry<StormCloudSpell> STORM_CLOUD = spell(Spells.STORM_CLOUD, StormCloudSpell::new, ChatFormatting.DARK_BLUE, () -> List.of(Cost.add(ModHerbs.DEWGONIA, 0.250), Cost.add(ModHerbs.CLOUD_BERRY, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> STORM_CLOUD_COOLDOWN = property(Spells.STORM_CLOUD, "cooldown", () -> new SpellProperty<>(STORM_CLOUD::get, 100, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Sky Soarer (39 cooldown)
  public static final RegistryEntry<SkySoarerSpell> SKY_SOARER = spell(Spells.SKY_SOARER, SkySoarerSpell::new, ChatFormatting.BLUE, () -> List.of(Cost.add(ModHerbs.CLOUD_BERRY, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> SKY_SOARER_COOLDOWN = property(Spells.SKY_SOARER, "cooldown", () -> new SpellProperty<>(SKY_SOARER::get, 39, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));
  public static final RegistryEntry<SpellProperty<Float>> SKY_SOARER_AMPLIFIER = property(Spells.SKY_SOARER, "amplifier", () -> new SpellProperty<>(SKY_SOARER::get,0.8f, Property.FLOAT_SERIALIZER, "The default movement speed amplifier for Sky Soarer."));
  public static final RegistryEntry<SpellProperty<Float>> SKY_SOARER_BOOSTED_AMPLIFIER = property(Spells.SKY_SOARER, "boosted_amplifier", () -> new SpellProperty<>(SKY_SOARER::get, 0.6f, Property.FLOAT_SERIALIZER, "The movement speed amplifier for Sky Soarer boosted."));
  public static final RegistryEntry<SpellProperty<Integer>> SKY_SOARER_DURATION = property(Spells.SKY_SOARER, "duration", () -> new SpellProperty<>(SKY_SOARER::get, 28, Property.INTEGER_SERIALIZER, SpellProperties.DURATION));
  public static final RegistryEntry<SpellProperty<Integer>> SKY_SOARER_BOOSTED_DURATION = property(Spells.SKY_SOARER, "boosted_duration", () -> new SpellProperty<>(SKY_SOARER::get, 28, Property.INTEGER_SERIALIZER, SpellProperties.EXTENDED_DURATION));

  // Time Stop (320 cooldown)
  public static final RegistryEntry<TimeStopSpell> TIME_STOP = spell(Spells.TIME_STOP, TimeStopSpell::new, ChatFormatting.DARK_BLUE, () -> List.of(Cost.add(ModHerbs.MOONGLOW, 0.250), Cost.add(ModHerbs.STALICRIPE, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> TIME_STOP_COOLDOWN = property(Spells.TIME_STOP, "cooldown", () -> new SpellProperty<>(TIME_STOP::get, 320, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Wildfire (24 cooldown)
  public static final RegistryEntry<WildfireSpell> WILDFIRE = spell(Spells.WILDFIRE, WildfireSpell::new, ChatFormatting.DARK_RED, () -> List.of(Cost.add(ModHerbs.INFERNO_BULB, 0.250)));
  public static final RegistryEntry<SpellProperty<Integer>> WILDFIRE_COOLDOWN = property(Spells.WILDFIRE, "cooldown", () -> new SpellProperty<>(WILDFIRE::get, 24, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  private static <T extends Spell> RegistryEntry<T> spell(ResourceKey<Spell> key, SpellConstructor<T> consturctor, ChatFormatting color, Supplier<List<Cost>> costs) {
    return REGISTRATE.simple(key.location().getPath(), RootsAPI.SPELL_REGISTRY, spellBuilder(consturctor, color, costs));
  }

  private static <T extends Spell> NonNullSupplier<T> spellBuilder(SpellConstructor<T> constructor, ChatFormatting color, Supplier<List<Cost>> costs) {
    return () -> constructor.create(color, costs.get());
  }

  private interface SpellConstructor<T extends Spell> {
    T create(ChatFormatting color, List<Cost> costs);
  }

  private static RegistryEntry<Modifier> modifier(ResourceKey<Spell> key, String name, NonNullSupplier<Modifier> builder) {
    return REGISTRATE.simple(key.location().getPath() + "/" + name, RootsAPI.MODIFIER_REGISTRY, builder);
  }

  // TODO: Use this for Ritual Properties too
  private static <T> RegistryEntry<SpellProperty<T>> property(ResourceKey<Spell> key, String name, NonNullSupplier<SpellProperty<T>> builder) {
    return REGISTRATE.simple(key.location().getPath() + "/" + name, RootsAPI.SPELL_PROPERTY_REGISTRY, builder);
  }

  public static void load() {
  }
}
