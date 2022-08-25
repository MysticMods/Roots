package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.reference.RitualProperties;
import mysticmods.roots.api.reference.Rituals;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.ritual.condition.LevelCondition;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.ritual.*;
import net.minecraft.resources.ResourceKey;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModRituals {
  public static final RegistryEntry<LevelCondition> RUNE_PILLAR_4_HIGH = REGISTRATE.simple("rune_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.runePillar(4)));
  public static final RegistryEntry<LevelCondition> RUNE_PILLAR_3_HIGH = REGISTRATE.simple("rune_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.runePillar(3)));

  public static final RegistryEntry<LevelCondition> ACACIA_PILLAR_4_HIGH = REGISTRATE.simple("acacia_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.ACACIA, 4)));
  public static final RegistryEntry<LevelCondition> ACACIA_PILLAR_3_HIGH = REGISTRATE.simple("acacia_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.ACACIA, 3)));

  public static final RegistryEntry<LevelCondition> BIRCH_PILLAR_4_HIGH = REGISTRATE.simple("birch_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.BIRCH, 4)));
  public static final RegistryEntry<LevelCondition> BIRCH_PILLAR_3_HIGH = REGISTRATE.simple("birch_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.BIRCH, 3)));

  public static final RegistryEntry<LevelCondition> CRIMSON_PILLAR_4_HIGH = REGISTRATE.simple("crimson_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.CRIMSON, 4)));
  public static final RegistryEntry<LevelCondition> CRIMSON_PILLAR_3_HIGH = REGISTRATE.simple("crimson_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.CRIMSON, 3)));

  public static final RegistryEntry<LevelCondition> DARK_OAK_PILLAR_4_HIGH = REGISTRATE.simple("dark_oak_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.DARK_OAK, 4)));
  public static final RegistryEntry<LevelCondition> DARK_OAK_PILLAR_3_HIGH = REGISTRATE.simple("dark_oak_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.DARK_OAK, 3)));

  public static final RegistryEntry<LevelCondition> JUNGLE_PILLAR_4_HIGH = REGISTRATE.simple("jungle_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.JUNGLE, 4)));
  public static final RegistryEntry<LevelCondition> JUNGLE_PILLAR_3_HIGH = REGISTRATE.simple("jungle_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.JUNGLE, 3)));

  public static final RegistryEntry<LevelCondition> OAK_PILLAR_4_HIGH = REGISTRATE.simple("oak_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.OAK, 4)));
  public static final RegistryEntry<LevelCondition> OAK_PILLAR_3_HIGH = REGISTRATE.simple("oak_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.OAK, 3)));

  public static final RegistryEntry<LevelCondition> SPRUCE_PILLAR_4_HIGH = REGISTRATE.simple("spruce_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.SPRUCE, 4)));
  public static final RegistryEntry<LevelCondition> SPRUCE_PILLAR_3_HIGH = REGISTRATE.simple("spruce_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.SPRUCE, 3)));

  public static final RegistryEntry<LevelCondition> WARPED_PILLAR_4_HIGH = REGISTRATE.simple("warped_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.WARPED, 4)));
  public static final RegistryEntry<LevelCondition> WARPED_PILLAR_3_HIGH = REGISTRATE.simple("warped_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.WARPED, 3)));

  public static final RegistryEntry<LevelCondition> WILDWOOD_PILLAR_4_HIGH = REGISTRATE.simple("wildwood_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.WILDWOOD, 4)));
  public static final RegistryEntry<LevelCondition> WILDWOOD_PILLAR_3_HIGH = REGISTRATE.simple("wildwood_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.WILDWOOD, 3)));

  // CRAFTING RITUAL
  public static final RegistryEntry<CraftingRitual> CRAFTING = ritual(Rituals.CRAFTING, CraftingRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> CRAFTING_DURATION = REGISTRATE.simple("crafting/duration", RitualProperty.class, () -> new RitualProperty<>(CRAFTING::get, 160, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> CRAFTING_INTERVAL = REGISTRATE.simple("crafting/interval", RitualProperty.class, () -> new RitualProperty<>(CRAFTING::get, 120, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));

  // ANIMAL HARVEST RITUAL
  public static final RegistryEntry<AnimalHarvestRitual> ANIMAL_HARVEST = ritual(Rituals.ANIMAL_HARVEST, AnimalHarvestRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> ANIMAL_HARVEST_DURATION = REGISTRATE.simple("animal_harvest/duration", RitualProperty.class, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 3200, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> ANIMAL_HARVEST_INTERVAL = REGISTRATE.simple("animal_harvest/interval", RitualProperty.class, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 110, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> ANIMAL_HARVEST_COUNT = REGISTRATE.simple("animal_harvest/count", RitualProperty.class, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 5, Property.INTEGER_SERIALIZER, RitualProperties.COUNT));
  public static final RegistryEntry<RitualProperty<Integer>> ANIMAL_HARVEST_RADIUS_XZ = REGISTRATE.simple("animal_harvest/radius_xz", RitualProperty.class, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> ANIMAL_HARVEST_RADIUS_Y = REGISTRATE.simple("animal_harvest/radius_y", RitualProperty.class, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));
  public static final RegistryEntry<RitualProperty<Float>> ANIMAL_HARVEST_LOOTING_CHANCE = REGISTRATE.simple("animal_harvest/looting_chance", RitualProperty.class, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 0.2f, Property.FLOAT_SERIALIZER, "Chance per operation that the loot level will be set to looting_value"));
  public static final RegistryEntry<RitualProperty<Integer>> ANIMAL_HARVEST_LOOTING_VALUE = REGISTRATE.simple("animal_harvest/looting_value", RitualProperty.class, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 2, Property.INTEGER_SERIALIZER, "The value passed to the loot function if looting_chance was successful."));
  public static final RegistryEntry<RitualProperty<Integer>> ANIMAL_HARVEST_GLOW_DURATION = REGISTRATE.simple("animal_harvest/glow_duration", RitualProperty.class, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 10, Property.INTEGER_SERIALIZER, "The duration of the glow effect applied to entities that have been harvest."));

  // Blooming Ritual
  public static final RegistryEntry<BloomingRitual> BLOOMING = ritual(Rituals.BLOOMING, BloomingRitual::new);

  public static final RegistryEntry<RitualProperty<Integer>> BLOOMING_DURATION = REGISTRATE.simple("blooming/duration", RitualProperty.class, () -> new RitualProperty<>(BLOOMING::get, 3200, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> BLOOMING_INTERVAL = REGISTRATE.simple("blooming/interval", RitualProperty.class, () -> new RitualProperty<>(BLOOMING::get, 100, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> BLOOMING_RADIUS_XZ = REGISTRATE.simple("blooming/radius_xz", RitualProperty.class, () -> new RitualProperty<>(BLOOMING::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> BLOOMING_RADIUS_Y = REGISTRATE.simple("blooming/radius_y", RitualProperty.class, () -> new RitualProperty<>(BLOOMING::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Fire Storm Ritual
  public static final RegistryEntry<FireStormRitual> FIRE_STORM = ritual(Rituals.FIRE_STORM, FireStormRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> FIRE_STORM_DURATION = REGISTRATE.simple("fire_storm/duration", RitualProperty.class, () -> new RitualProperty<>(FIRE_STORM::get, 600, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> FIRE_STORM_INTERVAL = REGISTRATE.simple("fire_storm/interval", RitualProperty.class, () -> new RitualProperty<>(FIRE_STORM::get, 2, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));

  // Frost Lands Ritual
  public static final RegistryEntry<FrostLandsRitual> FROST_LANDS = ritual(Rituals.FROST_LANDS, FrostLandsRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> FROST_LANDS_DURATION = REGISTRATE.simple("frost_lands/duration", RitualProperty.class, () -> new RitualProperty<>(FROST_LANDS::get, 6400, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> FROST_LANDS_INTERVAL = REGISTRATE.simple("frost_lands/interval", RitualProperty.class, () -> new RitualProperty<>(FROST_LANDS::get, 30, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> FROST_LANDS_RADIUS_XZ = REGISTRATE.simple("frost_lands/radius_xz", RitualProperty.class, () -> new RitualProperty<>(FROST_LANDS::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> FROST_LANDS_RADIUS_Y = REGISTRATE.simple("frost_lands/radius_y", RitualProperty.class, () -> new RitualProperty<>(FROST_LANDS::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Gathering ritual
  public static final RegistryEntry<GatheringRitual> GATHERING = ritual(Rituals.GATHERING, GatheringRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> GATHERING_DURATION = REGISTRATE.simple("gathering/duration", RitualProperty.class, () -> new RitualProperty<>(GATHERING::get, 6000, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> GATHERING_INTERVAL = REGISTRATE.simple("gathering/interval", RitualProperty.class, () -> new RitualProperty<>(GATHERING::get, 80, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> GATHERING_RADIUS_XZ = REGISTRATE.simple("gathering/radius_xz", RitualProperty.class, () -> new RitualProperty<>(GATHERING::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> GATHERING_RADIUS_Y = REGISTRATE.simple("gathering/radius_y", RitualProperty.class, () -> new RitualProperty<>(GATHERING::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Germination ritual
  public static final RegistryEntry<GerminationRitual> GERMINATION = ritual(Rituals.GERMINATION, GerminationRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> GERMINATION_DURATION = REGISTRATE.simple("germination/duration", RitualProperty.class, () -> new RitualProperty<>(GERMINATION::get, 6400, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> GERMINATION_INTERVAL = REGISTRATE.simple("germination/interval", RitualProperty.class, () -> new RitualProperty<>(GERMINATION::get, 64, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> GERMINATION_RADIUS_XZ = REGISTRATE.simple("germination/radius_xz", RitualProperty.class, () -> new RitualProperty<>(GERMINATION::get, 19, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> GERMINATION_RADIUS_Y = REGISTRATE.simple("germination/radius_y", RitualProperty.class, () -> new RitualProperty<>(GERMINATION::get, 19, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Healing Aura ritual
  public static final RegistryEntry<HealingAuraRitual> HEALING_AURA = ritual(Rituals.HEALING_AURA, HealingAuraRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> HEALING_AURA_DURATION = REGISTRATE.simple("healing_aura/duration", RitualProperty.class, () -> new RitualProperty<>(HEALING_AURA::get, 800, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> HEALING_AURA_INTERVAL = REGISTRATE.simple("healing_aura/interval", RitualProperty.class, () -> new RitualProperty<>(HEALING_AURA::get, 60, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> HEALING_AURA_RADIUS_XZ = REGISTRATE.simple("healing_aura/radius_xz", RitualProperty.class, () -> new RitualProperty<>(HEALING_AURA::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> HEALING_AURA_RADIUS_Y = REGISTRATE.simple("healing_aura/radius_y", RitualProperty.class, () -> new RitualProperty<>(HEALING_AURA::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Heavy Storms ritual
  public static final RegistryEntry<HeavyStormsRitual> HEAVY_STORMS = ritual(Rituals.HEAVY_STORMS, HeavyStormsRitual::new);

  public static final RegistryEntry<RitualProperty<Integer>> HEAVY_STORMS_DURATION = REGISTRATE.simple("heavy_storms/duration", RitualProperty.class, () -> new RitualProperty<>(HEAVY_STORMS::get, 2400, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> HEAVY_STORMS_INTERVAL = REGISTRATE.simple("heavy_storms/interval", RitualProperty.class, () -> new RitualProperty<>(HEAVY_STORMS::get, 20, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> HEAVY_STORMS_RADIUS_XZ = REGISTRATE.simple("heavy_storms/radius_xz", RitualProperty.class, () -> new RitualProperty<>(HEAVY_STORMS::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> HEAVY_STORMS_RADIUS_Y = REGISTRATE.simple("heavy_storms/radius_y", RitualProperty.class, () -> new RitualProperty<>(HEAVY_STORMS::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Overgrowth ritual
  public static final RegistryEntry<OvergrowthRitual> OVERGROWTH = ritual(Rituals.OVERGROWTH, OvergrowthRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> OVERGROWTH_DURATION = REGISTRATE.simple("overgrowth/duration", RitualProperty.class, () -> new RitualProperty<>(OVERGROWTH::get, 3000, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> OVERGROWTH_INTERVAL = REGISTRATE.simple("overgrowth/interval", RitualProperty.class, () -> new RitualProperty<>(OVERGROWTH::get, 100, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> OVERGROWTH_RADIUS_XZ = REGISTRATE.simple("overgrowth/radius_xz", RitualProperty.class, () -> new RitualProperty<>(OVERGROWTH::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> OVERGROWTH_RADIUS_Y = REGISTRATE.simple("overgrowth/radius_y", RitualProperty.class, () -> new RitualProperty<>(OVERGROWTH::get, 20, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Protection Ritual
  public static final RegistryEntry<ProtectionRitual> PROTECTION = ritual(Rituals.PROTECTION, ProtectionRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> PROTECTION_DURATION = REGISTRATE.simple("protection/duration", RitualProperty.class, () -> new RitualProperty<>(PROTECTION::get, 1200, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> PROTECTION_INTERVAL = REGISTRATE.simple("protection/interval", RitualProperty.class, () -> new RitualProperty<>(PROTECTION::get, 20, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> PROTECTION_RADIUS_XZ = REGISTRATE.simple("protection/radius_xz", RitualProperty.class, () -> new RitualProperty<>(PROTECTION::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> PROTECTION_RADIUS_Y = REGISTRATE.simple("protection/radius_y", RitualProperty.class, () -> new RitualProperty<>(PROTECTION::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Purity ritual
  public static final RegistryEntry<PurityRitual> PURITY = ritual(Rituals.PURITY, PurityRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> PURITY_DURATION = REGISTRATE.simple("purity/duration", RitualProperty.class, () -> new RitualProperty<>(PURITY::get, 1200, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> PURITY_INTERVAL = REGISTRATE.simple("purity/interval", RitualProperty.class, () -> new RitualProperty<>(PURITY::get, 20, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> PURITY_RADIUS_XZ = REGISTRATE.simple("purity/radius_xz", RitualProperty.class, () -> new RitualProperty<>(PURITY::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> PURITY_RADIUS_Y = REGISTRATE.simple("purity/radius_y", RitualProperty.class, () -> new RitualProperty<>(PURITY::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Spreading Forest ritual
  public static final RegistryEntry<SpreadingForestRitual> SPREADING_FOREST = ritual(Rituals.SPREADING_FOREST, SpreadingForestRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> SPREADING_FOREST_DURATION = REGISTRATE.simple("spreading_forest/duration", RitualProperty.class, () -> new RitualProperty<>(SPREADING_FOREST::get, 2400, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> SPREADING_FOREST_INTERVAL = REGISTRATE.simple("spreading_forest/interval", RitualProperty.class, () -> new RitualProperty<>(SPREADING_FOREST::get, 20, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> SPREADING_FOREST_RADIUS_XZ = REGISTRATE.simple("spreading_forest/radius_xz", RitualProperty.class, () -> new RitualProperty<>(SPREADING_FOREST::get, 35, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> SPREADING_FOREST_RADIUS_Y = REGISTRATE.simple("spreading_forest/radius_y", RitualProperty.class, () -> new RitualProperty<>(SPREADING_FOREST::get, 30, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Summon Creatures ritual
  public static final RegistryEntry<SummonCreaturesRitual> SUMMON_CREATURES = ritual(Rituals.SUMMON_CREATURES, SummonCreaturesRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> SUMMON_CREATURES_DURATION = REGISTRATE.simple("summon_creatures/duration", RitualProperty.class, () -> new RitualProperty<>(SUMMON_CREATURES::get, 200, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> SUMMON_CREATURES_INTERVAL = REGISTRATE.simple("summon_creatures/interval", RitualProperty.class, () -> new RitualProperty<>(SUMMON_CREATURES::get, 150, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));

  // Transmutation ritual
  public static final RegistryEntry<TransmutationRitual> TRANSMUTATION = ritual(Rituals.TRANSMUTATION, TransmutationRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> TRANSMUTATION_DURATION = REGISTRATE.simple("transmutation/duration", RitualProperty.class, () -> new RitualProperty<>(TRANSMUTATION::get, 2400, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> TRANSMUTATION_INTERVAL = REGISTRATE.simple("transmutation/interval", RitualProperty.class, () -> new RitualProperty<>(TRANSMUTATION::get, 100, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> TRANSMUTATION_RADIUS_XZ = REGISTRATE.simple("transmutation/radius_xz", RitualProperty.class, () -> new RitualProperty<>(TRANSMUTATION::get, 4, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> TRANSMUTATION_RADIUS_Y = REGISTRATE.simple("transmutation/radius_y", RitualProperty.class, () -> new RitualProperty<>(TRANSMUTATION::get, 4, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Warding ritual
  public static final RegistryEntry<WardingRitual> WARDING = ritual(Rituals.WARDING, WardingRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> WARDING_DURATION = REGISTRATE.simple("warding/duration", RitualProperty.class, () -> new RitualProperty<>(WARDING::get, 1200, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> WARDING_INTERVAL = REGISTRATE.simple("warding/interval", RitualProperty.class, () -> new RitualProperty<>(WARDING::get, 20, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> WARDING_RADIUS_XZ = REGISTRATE.simple("warding/radius_xz", RitualProperty.class, () -> new RitualProperty<>(WARDING::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> WARDING_RADIUS_Y = REGISTRATE.simple("warding/radius_y", RitualProperty.class, () -> new RitualProperty<>(WARDING::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Wildroot Growth ritual
  public static final RegistryEntry<WildrootGrowthRitual> WILDROOT_GROWTH = ritual(Rituals.WILDROOT_GROWTH, WildrootGrowthRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> WILDROOT_GROWTH_DURATION = REGISTRATE.simple("wildroot_growth/duration", RitualProperty.class, () -> new RitualProperty<>(WILDROOT_GROWTH::get, 300, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> WILDROOT_GROWTH_INTERVAL = REGISTRATE.simple("wildroot_growth/interval", RitualProperty.class, () -> new RitualProperty<>(WILDROOT_GROWTH::get, 250, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> WILDROOT_GROWTH_RADIUS_XZ = REGISTRATE.simple("wildroot_growth/radius_xz", RitualProperty.class, () -> new RitualProperty<>(WILDROOT_GROWTH::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> WILDROOT_GROWTH_RADIUS_Y = REGISTRATE.simple("wildroot_growth/radius_y", RitualProperty.class, () -> new RitualProperty<>(WILDROOT_GROWTH::get, 20, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Windwall ritual
  public static final RegistryEntry<WindwallRitual> WINDWALL = ritual(Rituals.WINDWALL, WindwallRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> WINDWALL_DURATION = REGISTRATE.simple("windwall/duration", RitualProperty.class, () -> new RitualProperty<>(WINDWALL::get, 3000, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> WINDWALL_INTERVAL = REGISTRATE.simple("windwall/interval", RitualProperty.class, () -> new RitualProperty<>(WINDWALL::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> WINDWALL_RADIUS_XZ = REGISTRATE.simple("windwall/radius_xz", RitualProperty.class, () -> new RitualProperty<>(WINDWALL::get, 51, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> WINDWALL_RADIUS_Y = REGISTRATE.simple("windwall/radius_y", RitualProperty.class, () -> new RitualProperty<>(WINDWALL::get, 31, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  private static <T extends Ritual> RegistryEntry<T> ritual(ResourceKey<Ritual> key, NonNullSupplier<T> builder) {
    return REGISTRATE.simple(key.location().getPath(), Ritual.class, builder);
  }

  public static void load() {
  }
}
