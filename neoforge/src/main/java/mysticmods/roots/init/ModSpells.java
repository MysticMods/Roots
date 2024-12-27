package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.reference.SpellCosts;
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
  public static final RegistryEntry<AcidCloudSpell> ACID_CLOUD = spell(Spells.ACID_CLOUD, AcidCloudSpell::new, ChatFormatting.GREEN, () -> List.of(Cost.add(ModHerbs.BAFFLECAP, SpellCosts.BASE_0250), Cost.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0250)));
  public static final RegistryEntry<SpellProperty<Integer>> ACID_CLOUD_COOLDOWN = property(Spells.ACID_CLOUD, "cooldown", () -> new SpellProperty<>(ACID_CLOUD::get, 20, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  public static final RegistryEntry<SpellProperty<Integer>> ACID_CLOUD_RADIUS_ZX = property(Spells.ACID_CLOUD, "radius_zx", () -> new SpellProperty<>(ACID_CLOUD::get, 4, Property.INTEGER_SERIALIZER, "Radius"));
  public static final RegistryEntry<SpellProperty<Integer>> ACID_CLOUD_RADIUS_Y = property(Spells.ACID_CLOUD, "radius_y", () -> new SpellProperty<>(ACID_CLOUD::get, 2, Property.INTEGER_SERIALIZER, "Radius"));
  public static final RegistryEntry<SpellProperty<Float>> ACID_CLOUD_DAMAGE = property(Spells.ACID_CLOUD, "damage", () -> new SpellProperty<>(ACID_CLOUD::get, 2.0f, Property.FLOAT_SERIALIZER, SpellProperties.DAMAGE));
  public static final RegistryEntry<SpellProperty<Integer>> ACID_CLOUD_COUNT = property(Spells.ACID_CLOUD, "count", () -> new SpellProperty<>(ACID_CLOUD::get, 1, Property.INTEGER_SERIALIZER, SpellProperties.COUNT));

  // Aqua Bubble (1200 cooldown)
  public static final RegistryEntry<AquaBubbleSpell> AQUA_BUBBLE = spell(Spells.AQUA_BUBBLE, AquaBubbleSpell::new, ChatFormatting.AQUA, () -> List.of(Cost.add(ModHerbs.DEWGONIA, SpellCosts.BASE_0250)));
  public static final RegistryEntry<SpellProperty<Integer>> AQUA_BUBBLE_COOLDOWN = property(Spells.AQUA_BUBBLE, "cooldown", () -> new SpellProperty<>(AQUA_BUBBLE::get, 1200, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // TODO: What does this actually do?
  // Augment (350 cooldown)
  public static final RegistryEntry<AugmentSpell> AUGMENT = spell(Spells.AUGMENT, AugmentSpell::new, ChatFormatting.DARK_GREEN, () -> List.of(Cost.add(ModHerbs.GROVE_MOSS, SpellCosts.BASE_0250), Cost.add(ModHerbs.INFERNO_BULB, SpellCosts.BASE_0125)));
  public static final RegistryEntry<SpellProperty<Integer>> AUGMENT_COOLDOWN = property(Spells.AUGMENT, "cooldown", () -> new SpellProperty<>(AUGMENT::get, 350, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Light Drifter (600 cooldown)
  public static final RegistryEntry<LightDrifterSpell> LIGHT_DRIFTER = spell(Spells.LIGHT_DRIFTER, LightDrifterSpell::new, ChatFormatting.DARK_PURPLE, () -> List.of(Cost.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0250)));
  public static final RegistryEntry<SpellProperty<Integer>> LIGHT_DRIFTER_COOLDOWN = property(Spells.LIGHT_DRIFTER, "cooldown", () -> new SpellProperty<>(LIGHT_DRIFTER::get, 600, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Magnetism (350 cooldown)
  public static final RegistryEntry<MagnetismSpell> MAGNETISM = spell(Spells.MAGNETISM, MagnetismSpell::new, ChatFormatting.YELLOW, () -> List.of(Cost.add(ModHerbs.SPIRITLEAF, SpellCosts.BASE_0250)));
  public static final RegistryEntry<SpellProperty<Integer>> MAGNETISM_COOLDOWN = property(Spells.MAGNETISM, "cooldown", () -> new SpellProperty<>(MAGNETISM::get, 350, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  public static final RegistryEntry<SpellProperty<Integer>> MAGNETISM_RADIUS_ZX = property(Spells.MAGNETISM, "radius_zx", () -> new SpellProperty<>(MAGNETISM::get, 20, Property.INTEGER_SERIALIZER, SpellProperties.RADIUS_ZX));
  public static final RegistryEntry<SpellProperty<Integer>> MAGNETISM_RADIUS_Y = property(Spells.MAGNETISM, "radius_y", () -> new SpellProperty<>(MAGNETISM::get, 10, Property.INTEGER_SERIALIZER, SpellProperties.RADIUS_Y));

  // Dandelion Winds (20 cooldown)
  public static final RegistryEntry<DandelionWindsSpell> DANDELION_WINDS = spell(Spells.DANDELION_WINDS, DandelionWindsSpell::new, ChatFormatting.YELLOW, () -> List.of(Cost.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0250), Cost.add(ModHerbs.GROVE_MOSS, SpellCosts.BASE_0125)));
  public static final RegistryEntry<SpellProperty<Integer>> DANDELION_WINDS_COOLDOWN = property(Spells.DANDELION_WINDS, "cooldown", () -> new SpellProperty<>(DANDELION_WINDS::get, 20, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));
  public static final RegistryEntry<SpellProperty<Float>> DANDELION_WINDS_DISTANCE = property(Spells.DANDELION_WINDS, "distance", () -> new SpellProperty<>(DANDELION_WINDS::get, 0.75f, Property.FLOAT_SERIALIZER, "The vertical component of the vector used to move entities."));
  public static final RegistryEntry<SpellProperty<Double>> DANDELION_WINDS_RANGE_1 = property(Spells.DANDELION_WINDS, "range_1", () -> new SpellProperty<>(DANDELION_WINDS::get, 4.0, Property.DOUBLE_SERIALIZER, "The first range increment for calculating the bounding box from the player."));
  public static final RegistryEntry<SpellProperty<Double>> DANDELION_WINDS_RANGE_2 = property(Spells.DANDELION_WINDS, "range_2", () -> new SpellProperty<>(DANDELION_WINDS::get, 5.0, Property.DOUBLE_SERIALIZER, "The second range increment for calculating the bounding box from the player."));

  // Desaturate (500 cooldown)
  public static final RegistryEntry<DesaturateSpell> DESATURATE = spell(Spells.DESATURATE, DesaturateSpell::new, ChatFormatting.GREEN, () -> List.of(Cost.add(ModHerbs.WILDEWHEET, SpellCosts.BASE_0250), Cost.add(ModHerbs.GROVE_MOSS, SpellCosts.BASE_0250)));
  public static final RegistryEntry<SpellProperty<Integer>> DESATURATE_COOLDOWN = property(Spells.DESATURATE, "cooldown", () -> new SpellProperty<>(DESATURATE::get, 500, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));
  public static final RegistryEntry<SpellProperty<Float>> DESATURATE_MULTIPLIER = property(Spells.DESATURATE, "multiplier", () -> new SpellProperty<>(DESATURATE::get, 0.7f, Property.FLOAT_SERIALIZER, "Amount of health restored per point of food"));

  // Saturate

  public static final RegistryEntry<SaturateSpell> SATURATE = spell(Spells.SATURATE, SaturateSpell::new, ChatFormatting.DARK_GREEN, () -> List.of(Cost.add(ModHerbs.WILDEWHEET, SpellCosts.BASE_0250), Cost.add(ModHerbs.WILDROOT, SpellCosts.BASE_0250)));

  public static final RegistryEntry<SpellProperty<Integer>> SATURATE_COOLDOWN = property(Spells.SATURATE, "cooldown", () -> new SpellProperty<>(SATURATE::get, 500, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  public static final RegistryEntry<SpellProperty<Float>> SATURATE_SATURATION_MULTIPLIER = property(Spells.SATURATE, "saturation_multiplier", () -> new SpellProperty<>(SATURATE::get, 0.5f, Property.FLOAT_SERIALIZER, "Amount of saturation give per point of saturation."));
  public static final RegistryEntry<SpellProperty<Float>> SATURATE_FOOD_MULTIPLIER = property(Spells.SATURATE, "food_multiplier", () -> new SpellProperty<>(SATURATE::get, 0.5f, Property.FLOAT_SERIALIZER, "Amount of food restored per point of food."));

  // Disarm spell (350 cooldown)
  public static final RegistryEntry<DisarmSpell> DISARM = spell(Spells.DISARM, DisarmSpell::new, ChatFormatting.AQUA, () -> List.of(Cost.add(ModHerbs.SPIRITLEAF, SpellCosts.BASE_0250), Cost.add(ModHerbs.BAFFLECAP, SpellCosts.BASE_0250)));
  public static final RegistryEntry<SpellProperty<Integer>> DISARM_COOLDOWN = property(Spells.DISARM, "cooldown", () -> new SpellProperty<>(DISARM::get, 350, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));
  public static final RegistryEntry<SpellProperty<Integer>> DISARM_RADIUS_ZX = property(Spells.DISARM, "radius_zx", () -> new SpellProperty<>(DISARM::get, 5, Property.INTEGER_SERIALIZER, SpellProperties.RADIUS_ZX));
  public static final RegistryEntry<SpellProperty<Integer>> DISARM_RADIUS_Y = property(Spells.DISARM, "radius_y", () -> new SpellProperty<>(DISARM::get, 5, Property.INTEGER_SERIALIZER, SpellProperties.RADIUS_Y));
  public static final RegistryEntry<SpellProperty<Float>> DISARM_DROP_CHANCE = property(Spells.DISARM, "drop_chance", () -> new SpellProperty<>(DISARM::get, 0.35f, Property.FLOAT_SERIALIZER, "Percentage change for an entity's dropped item to spawn in the world instead of being destroyed."));

  // Long night vision & sense danger
  // Extension spell (350 cooldown)
  public static final RegistryEntry<ExtensionSpell> EXTENSION = spell(Spells.EXTENSION, ExtensionSpell::new, ChatFormatting.BLUE, () -> List.of(Cost.add(ModHerbs.GROVE_MOSS, SpellCosts.COMPLEX_1750), Cost.add(ModHerbs.WILDROOT, SpellCosts.COMPLEX_1750)));
  public static final RegistryEntry<SpellProperty<Integer>> EXTENSION_COOLDOWN = property(Spells.EXTENSION, "cooldown", () -> new SpellProperty<>(EXTENSION::get, 20, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));
  public static final RegistryEntry<SpellProperty<Integer>> EXTENSION_NIGHT_VISION_DURATION = property(Spells.EXTENSION, "night_vision_duration", () -> new SpellProperty<>(EXTENSION::get, 4 * 60 * 20, Property.INTEGER_SERIALIZER, "The duration of the night vision effect in ticks."));
  public static final RegistryEntry<SpellProperty<Integer>> EXTENSION_SENSE_DANGER_DURATION = property(Spells.EXTENSION, "sense_danger_duration", () -> new SpellProperty<>(EXTENSION::get, 4 * 60 * 20, Property.INTEGER_SERIALIZER, "The duration of the sense danger effect in ticks."));
  public static final RegistryEntry<SpellProperty<Integer>> EXTENSION_RADIUS_ZX = property(Spells.EXTENSION, "radius_zx", () -> new SpellProperty<>(EXTENSION::get, 40, Property.INTEGER_SERIALIZER, SpellProperties.RADIUS_ZX));
  public static final RegistryEntry<SpellProperty<Integer>> EXTENSION_RADIUS_Y = property(Spells.EXTENSION, "radius_y", () -> new SpellProperty<>(EXTENSION::get, 20, Property.INTEGER_SERIALIZER, SpellProperties.RADIUS_Y));

  // Nondetection (350 cooldown)
  public static final RegistryEntry<NondetectionSpell> NONDETECTION = spell(Spells.NONDETECTION, NondetectionSpell::new, ChatFormatting.DARK_AQUA, () -> List.of(Cost.add(ModHerbs.SPIRITLEAF, SpellCosts.BASE_0250), Cost.add(ModHerbs.DEWGONIA, SpellCosts.BASE_0250)));
  public static final RegistryEntry<SpellProperty<Integer>> NONDETECTION_COOLDOWN = property(Spells.NONDETECTION, "cooldown", () -> new SpellProperty<>(NONDETECTION::get, 350, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Fey Light (20 cooldown)
  public static final RegistryEntry<FeyLightSpell> FEY_LIGHT = spell(Spells.FEY_LIGHT, FeyLightSpell::new, ChatFormatting.LIGHT_PURPLE, () -> List.of(Cost.add(ModHerbs.GROVE_MOSS, 0.0625), Cost.add(ModHerbs.PERESKIA, 0.0625)));
  public static final RegistryEntry<SpellProperty<Integer>> FEY_LIGHT_COOLDOWN = property(Spells.FEY_LIGHT, "cooldown", () -> new SpellProperty<>(FEY_LIGHT::get, 20, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  public static final RegistryEntry<SpellProperty<Double>> FEY_LIGHT_MAX_DISTANCE = property(Spells.FEY_LIGHT, "max_distance", () -> new SpellProperty<>(FEY_LIGHT::get, 10.0, Property.DOUBLE_SERIALIZER, "The maximum distance a fey light can be placed from the caster"));

  // Geas (80 cooldown)
  public static final RegistryEntry<GeasSpell> GEAS = spell(Spells.GEAS, GeasSpell::new, ChatFormatting.RED, () -> List.of(Cost.add(ModHerbs.BAFFLECAP, SpellCosts.BASE_0250), Cost.add(ModHerbs.GROVE_MOSS, SpellCosts.BASE_0250)));
  public static final RegistryEntry<SpellProperty<Integer>> GEAS_COOLDOWN = property(Spells.GEAS, "cooldown", () -> new SpellProperty<>(GEAS::get, 80, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Control Undead (320 cooldown)
  public static final RegistryEntry<ControlUndeadSpell> CONTROL_UNDEAD = spell(Spells.CONTROL_UNDEAD, ControlUndeadSpell::new, ChatFormatting.DARK_GREEN, () -> List.of(Cost.add(ModHerbs.BAFFLECAP, SpellCosts.BASE_0250), Cost.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0250)));
  public static final RegistryEntry<SpellProperty<Integer>> CONTROL_UNDEAD_COOLDOWN = property(Spells.CONTROL_UNDEAD, "cooldown", () -> new SpellProperty<>(CONTROL_UNDEAD::get, 320, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Growth Infusion (20 cooldown)
  public static final RegistryEntry<GrowthInfusionSpell> GROWTH_INFUSION = spell(Spells.GROWTH_INFUSION, GrowthInfusionSpell::new, ChatFormatting.YELLOW, () -> List.of(Cost.add(ModHerbs.GROVE_MOSS, SpellCosts.BASE_0250)));
  public static final RegistryEntry<SpellProperty<Integer>> GROWTH_INFUSION_COOLDOWN = property(Spells.GROWTH_INFUSION, "cooldown", () -> new SpellProperty<>(GROWTH_INFUSION::get, 20, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  public static final RegistryEntry<SpellProperty<Double>> GROWTH_INFUSION_ADDED_REACH = property(Spells.GROWTH_INFUSION, "added_reach", () -> new SpellProperty<>(GROWTH_INFUSION::get, 0.0, Property.DOUBLE_SERIALIZER, SpellProperties.ADDED_REACH));

  public static final RegistryEntry<SpellProperty<Integer>> GROWTH_INFUSION_BASE_TICKS = property(Spells.GROWTH_INFUSION, "base_ticks", () -> new SpellProperty<>(GROWTH_INFUSION::get, 2, Property.INTEGER_SERIALIZER, "The default number of growth ticks applied by base growth infusion"));

  // Rampant Growth (20 cooldown)
  public static final RegistryEntry<RampantGrowthSpell> RAMPANT_GROWTH = spell(Spells.RAMPANT_GROWTH, RampantGrowthSpell::new, ChatFormatting.YELLOW, () -> List.of(Cost.add(ModHerbs.WILDEWHEET, SpellCosts.BASE_0250)));
  public static final RegistryEntry<SpellProperty<Integer>> RAMPANT_GROWTH_COOLDOWN = property(Spells.RAMPANT_GROWTH, "cooldown", () -> new SpellProperty<>(RAMPANT_GROWTH::get, 20, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Harvest (25 cooldown)
  public static final RegistryEntry<HarvestSpell> HARVEST = spell(Spells.HARVEST, HarvestSpell::new, ChatFormatting.YELLOW, () -> List.of(Cost.add(ModHerbs.WILDEWHEET, SpellCosts.BASE_0250), Cost.add(ModHerbs.STALICRIPE, SpellCosts.BASE_0250)));
  public static final RegistryEntry<SpellProperty<Integer>> HARVEST_COOLDOWN = property(Spells.HARVEST, "cooldown", () -> new SpellProperty<>(HARVEST::get, 25, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Life Drain (20 cooldown)
  public static final RegistryEntry<LifeDrainSpell> LIFE_DRAIN = spell(Spells.LIFE_DRAIN, LifeDrainSpell::new, ChatFormatting.DARK_PURPLE, () -> List.of(Cost.add(ModHerbs.WILDEWHEET, SpellCosts.BASE_0250), Cost.add(ModHerbs.STALICRIPE, SpellCosts.BASE_0250)));
  public static final RegistryEntry<SpellProperty<Integer>> LIFE_DRAIN_COOLDOWN = property(Spells.LIFE_DRAIN, "cooldown", () -> new SpellProperty<>(LIFE_DRAIN::get, 20, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));
  public static final RegistryEntry<SpellProperty<Double>> LIFE_DRAIN_DISTANCE = property(Spells.LIFE_DRAIN, "distance", () -> new SpellProperty<>(LIFE_DRAIN::get, 3.0, Property.DOUBLE_SERIALIZER, "The first value used when calculating the vector from the caster."));
  public static final RegistryEntry<SpellProperty<Double>> LIFE_DRAIN_BOUNDS = property(Spells.LIFE_DRAIN, "bounds", () -> new SpellProperty<>(LIFE_DRAIN::get, 2.0, Property.DOUBLE_SERIALIZER, "The second value used when calculating the size of the bounding box from the caster's look vector."));
  public static final RegistryEntry<SpellProperty<Float>> LIFE_DRAIN_DAMAGE = property(Spells.LIFE_DRAIN, "damage", () -> new SpellProperty<>(LIFE_DRAIN::get, 3.0F, Property.FLOAT_SERIALIZER, SpellProperties.DAMAGE));
  public static final RegistryEntry<SpellProperty<Float>> LIFE_DRAIN_HEAL = property(Spells.LIFE_DRAIN, "heal", () -> new SpellProperty<>(LIFE_DRAIN::get, 0.5F, Property.FLOAT_SERIALIZER, "The amount a player should be healed for each entity damaged."));

  // Petal Shell (120 cooldown)
  public static final RegistryEntry<PetalShellSpell> PETAL_SHELL = spell(Spells.PETAL_SHELL, PetalShellSpell::new, ChatFormatting.LIGHT_PURPLE, () -> List.of(Cost.add(ModHerbs.PERESKIA, SpellCosts.BASE_0250)));
  public static final RegistryEntry<SpellProperty<Integer>> PETAL_SHELL_COOLDOWN = property(Spells.PETAL_SHELL, "cooldown", () -> new SpellProperty<>(PETAL_SHELL::get, 120, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));
  public static final RegistryEntry<SpellProperty<Integer>> PETAL_SHELL_DURATION = property(Spells.PETAL_SHELL, "duration", () -> new SpellProperty<>(PETAL_SHELL::get, 100, Property.INTEGER_SERIALIZER, SpellProperties.DURATION));
  public static final RegistryEntry<SpellProperty<Integer>> PETAL_SHELL_COUNT = property(Spells.PETAL_SHELL, "count", () -> new SpellProperty<>(PETAL_SHELL::get, 3, Property.INTEGER_SERIALIZER, "The number of petal shells."));

  // Radiance (20 cooldown)
  public static final RegistryEntry<RadianceSpell> RADIANCE = spell(Spells.RADIANCE, RadianceSpell::new, ChatFormatting.GOLD, () -> List.of(Cost.add(ModHerbs.INFERNO_BULB, SpellCosts.BASE_0250), Cost.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0250)));
  public static final RegistryEntry<SpellProperty<Integer>> RADIANCE_COOLDOWN = property(Spells.RADIANCE, "cooldown", () -> new SpellProperty<>(RADIANCE::get, 20, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Rose Thorns (24 cooldown)
  public static final RegistryEntry<RoseThornsSpell> ROSE_THORNS = spell(Spells.ROSE_THORNS, RoseThornsSpell::new, ChatFormatting.RED, () -> List.of(Cost.add(ModHerbs.WILDROOT, SpellCosts.BASE_0250)));
  public static final RegistryEntry<SpellProperty<Integer>> ROSE_THORNS_COOLDOWN = property(Spells.ROSE_THORNS, "cooldown", () -> new SpellProperty<>(ROSE_THORNS::get, 24, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Sanctuary (20 cooldown)
  public static final RegistryEntry<SanctuarySpell> SANCTUARY = spell(Spells.SANCTUARY, SanctuarySpell::new, ChatFormatting.LIGHT_PURPLE, () -> List.of(Cost.add(ModHerbs.PERESKIA, SpellCosts.BASE_0250), Cost.add(ModHerbs.STALICRIPE, SpellCosts.BASE_0250)));
  public static final RegistryEntry<SpellProperty<Integer>> SANCTUARY_COOLDOWN = property(Spells.SANCTUARY, "cooldown", () -> new SpellProperty<>(SANCTUARY::get, 20, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));
  public static final RegistryEntry<SpellProperty<Integer>> SANCTUARY_RADIUS_Y = property(Spells.SANCTUARY, "radius_y", () -> new SpellProperty<>(SANCTUARY::get, 5, Property.INTEGER_SERIALIZER, "The radius of the sanctuary in the Y axis."));
  public static final RegistryEntry<SpellProperty<Integer>> SANCTUARY_RADIUS_XZ = property(Spells.SANCTUARY, "radius_xz", () -> new SpellProperty<>(SANCTUARY::get, 4, Property.INTEGER_SERIALIZER, "The radius of the sanctuary in the X and Z axis."));
  public static final RegistryEntry<SpellProperty<Float>> SANCTUARY_VELOCITY = property(Spells.SANCTUARY, "velocity", () -> new SpellProperty<>(SANCTUARY::get, 0.125f, Property.FLOAT_SERIALIZER, "The velocity modifier applied to entities inside the sanctuary."));

  // Shatter (20 cooldown)
  public static final RegistryEntry<ShatterSpell> SHATTER = spell(Spells.SHATTER, ShatterSpell::new, ChatFormatting.YELLOW, () -> List.of(Cost.add(ModHerbs.STALICRIPE, SpellCosts.BASE_0250)));
  public static final RegistryEntry<SpellProperty<Integer>> SHATTER_COOLDOWN = property(Spells.SHATTER, "cooldown", () -> new SpellProperty<>(SHATTER::get, 20, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Jaunt (80 cooldown)
  public static final RegistryEntry<JauntSpell> JAUNT = spell(Spells.JAUNT, JauntSpell::new, ChatFormatting.DARK_PURPLE, () -> List.of(Cost.add(ModHerbs.PERESKIA, SpellCosts.BASE_0250), Cost.add(ModHerbs.SPIRITLEAF, SpellCosts.BASE_0250)));
  public static final RegistryEntry<SpellProperty<Integer>> JAUNT_COOLDOWN = property(Spells.JAUNT, "cooldown", () -> new SpellProperty<>(JAUNT::get, 80, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  public static final RegistryEntry<SpellProperty<Integer>> JAUNT_DISTANCE = property(Spells.JAUNT, "distance", () -> new SpellProperty<>(JAUNT::get, 8, Property.INTEGER_SERIALIZER, "The number of blocks that Jaunt travels forwards."));

  // Storm Cloud (100 cooldown)
  public static final RegistryEntry<StormCloudSpell> STORM_CLOUD = spell(Spells.STORM_CLOUD, StormCloudSpell::new, ChatFormatting.DARK_BLUE, () -> List.of(Cost.add(ModHerbs.DEWGONIA, SpellCosts.BASE_0250), Cost.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0250)));
  public static final RegistryEntry<SpellProperty<Integer>> STORM_CLOUD_COOLDOWN = property(Spells.STORM_CLOUD, "cooldown", () -> new SpellProperty<>(STORM_CLOUD::get, 100, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Sky Soarer (39 cooldown)
  public static final RegistryEntry<SkySoarerSpell> SKY_SOARER = spell(Spells.SKY_SOARER, SkySoarerSpell::new, ChatFormatting.BLUE, () -> List.of(Cost.add(ModHerbs.CLOUD_BERRY, SpellCosts.COMPLEX_1875)));
  public static final RegistryEntry<SpellProperty<Integer>> SKY_SOARER_COOLDOWN = property(Spells.SKY_SOARER, "cooldown", () -> new SpellProperty<>(SKY_SOARER::get, 30, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));
  public static final RegistryEntry<SpellProperty<Float>> SKY_SOARER_AMPLIFIER = property(Spells.SKY_SOARER, "amplifier", () -> new SpellProperty<>(SKY_SOARER::get,0.9f, Property.FLOAT_SERIALIZER, "The default movement speed amplifier for Sky Soarer."));
  public static final RegistryEntry<SpellProperty<Float>> SKY_SOARER_BOOSTED_AMPLIFIER = property(Spells.SKY_SOARER, "boosted_amplifier", () -> new SpellProperty<>(SKY_SOARER::get, 0.6f, Property.FLOAT_SERIALIZER, "The movement speed amplifier for Sky Soarer boosted."));
  public static final RegistryEntry<SpellProperty<Integer>> SKY_SOARER_DURATION = property(Spells.SKY_SOARER, "duration", () -> new SpellProperty<>(SKY_SOARER::get, 50, Property.INTEGER_SERIALIZER, SpellProperties.DURATION));
  public static final RegistryEntry<SpellProperty<Integer>> SKY_SOARER_BOOSTED_DURATION = property(Spells.SKY_SOARER, "boosted_duration", () -> new SpellProperty<>(SKY_SOARER::get, 28, Property.INTEGER_SERIALIZER, SpellProperties.EXTENDED_DURATION));

  // Time Stop (320 cooldown)
  public static final RegistryEntry<TimeStopSpell> TIME_STOP = spell(Spells.TIME_STOP, TimeStopSpell::new, ChatFormatting.DARK_BLUE, () -> List.of(Cost.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0250), Cost.add(ModHerbs.STALICRIPE, SpellCosts.BASE_0250)));
  public static final RegistryEntry<SpellProperty<Integer>> TIME_STOP_COOLDOWN = property(Spells.TIME_STOP, "cooldown", () -> new SpellProperty<>(TIME_STOP::get, 320, Property.INTEGER_SERIALIZER, SpellProperties.COOLDOWN));

  // Wildfire (24 cooldown)
  public static final RegistryEntry<WildfireSpell> WILDFIRE = spell(Spells.WILDFIRE, WildfireSpell::new, ChatFormatting.DARK_RED, () -> List.of(Cost.add(ModHerbs.INFERNO_BULB, SpellCosts.BASE_0250)));
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
