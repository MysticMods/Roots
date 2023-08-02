package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.reference.RitualProperties;
import mysticmods.roots.api.reference.Rituals;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.ritual.*;
import net.minecraft.resources.ResourceKey;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModRituals {

  // CRAFTING RITUAL
  public static final RegistryEntry<CraftingRitual> CRAFTING = ritual(Rituals.CRAFTING, CraftingRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> CRAFTING_DURATION = REGISTRATE.simple("crafting/duration", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(CRAFTING::get, 160, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> CRAFTING_INTERVAL = REGISTRATE.simple("crafting/interval", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(CRAFTING::get, 120, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));

  // ANIMAL HARVEST RITUAL
  public static final RegistryEntry<AnimalHarvestRitual> ANIMAL_HARVEST = ritual(Rituals.ANIMAL_HARVEST, AnimalHarvestRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> ANIMAL_HARVEST_DURATION = REGISTRATE.simple("animal_harvest/duration", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 3200, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> ANIMAL_HARVEST_INTERVAL = REGISTRATE.simple("animal_harvest/interval", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 110, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> ANIMAL_HARVEST_COUNT = REGISTRATE.simple("animal_harvest/count", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 5, Property.INTEGER_SERIALIZER, RitualProperties.COUNT));
  public static final RegistryEntry<RitualProperty<Integer>> ANIMAL_HARVEST_RADIUS_XZ = REGISTRATE.simple("animal_harvest/radius_xz", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> ANIMAL_HARVEST_RADIUS_Y = REGISTRATE.simple("animal_harvest/radius_y", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));
  public static final RegistryEntry<RitualProperty<Float>> ANIMAL_HARVEST_LOOTING_CHANCE = REGISTRATE.simple("animal_harvest/looting_chance", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 0.2f, Property.FLOAT_SERIALIZER, "Chance per operation that the loot level will be set to looting_value"));
  public static final RegistryEntry<RitualProperty<Integer>> ANIMAL_HARVEST_LOOTING_VALUE = REGISTRATE.simple("animal_harvest/looting_value", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 2, Property.INTEGER_SERIALIZER, "The value passed to the loot function if looting_chance was successful."));
  public static final RegistryEntry<RitualProperty<Integer>> ANIMAL_HARVEST_GLOW_DURATION = REGISTRATE.simple("animal_harvest/glow_duration", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(ANIMAL_HARVEST::get, 10, Property.INTEGER_SERIALIZER, "The duration of the glow effect applied to entities that have been harvest."));

  // Blooming Ritual
  public static final RegistryEntry<BloomingRitual> BLOOMING = ritual(Rituals.BLOOMING, BloomingRitual::new);

  public static final RegistryEntry<RitualProperty<Integer>> BLOOMING_DURATION = REGISTRATE.simple("blooming/duration", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(BLOOMING::get, 3200, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> BLOOMING_INTERVAL = REGISTRATE.simple("blooming/interval", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(BLOOMING::get, 100, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> BLOOMING_RADIUS_XZ = REGISTRATE.simple("blooming/radius_xz", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(BLOOMING::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> BLOOMING_RADIUS_Y = REGISTRATE.simple("blooming/radius_y", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(BLOOMING::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Fire Storm Ritual
  public static final RegistryEntry<FireStormRitual> FIRE_STORM = ritual(Rituals.FIRE_STORM, FireStormRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> FIRE_STORM_DURATION = REGISTRATE.simple("fire_storm/duration", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(FIRE_STORM::get, 600, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> FIRE_STORM_INTERVAL = REGISTRATE.simple("fire_storm/interval", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(FIRE_STORM::get, 2, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));

  // Frost Lands Ritual
  public static final RegistryEntry<FrostLandsRitual> FROST_LANDS = ritual(Rituals.FROST_LANDS, FrostLandsRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> FROST_LANDS_DURATION = REGISTRATE.simple("frost_lands/duration", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(FROST_LANDS::get, 6400, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> FROST_LANDS_INTERVAL = REGISTRATE.simple("frost_lands/interval", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(FROST_LANDS::get, 30, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> FROST_LANDS_RADIUS_XZ = REGISTRATE.simple("frost_lands/radius_xz", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(FROST_LANDS::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> FROST_LANDS_RADIUS_Y = REGISTRATE.simple("frost_lands/radius_y", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(FROST_LANDS::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Gathering ritual
  public static final RegistryEntry<GatheringRitual> GATHERING = ritual(Rituals.GATHERING, GatheringRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> GATHERING_DURATION = REGISTRATE.simple("gathering/duration", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(GATHERING::get, 6000, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> GATHERING_INTERVAL = REGISTRATE.simple("gathering/interval", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(GATHERING::get, 80, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> GATHERING_RADIUS_XZ = REGISTRATE.simple("gathering/radius_xz", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(GATHERING::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> GATHERING_RADIUS_Y = REGISTRATE.simple("gathering/radius_y", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(GATHERING::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Germination ritual
  public static final RegistryEntry<GerminationRitual> GERMINATION = ritual(Rituals.GERMINATION, GerminationRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> GERMINATION_DURATION = REGISTRATE.simple("germination/duration", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(GERMINATION::get, 6400, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> GERMINATION_INTERVAL = REGISTRATE.simple("germination/interval", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(GERMINATION::get, 64, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> GERMINATION_RADIUS_XZ = REGISTRATE.simple("germination/radius_xz", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(GERMINATION::get, 19, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> GERMINATION_RADIUS_Y = REGISTRATE.simple("germination/radius_y", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(GERMINATION::get, 19, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Healing Aura ritual
  public static final RegistryEntry<HealingAuraRitual> HEALING_AURA = ritual(Rituals.HEALING_AURA, HealingAuraRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> HEALING_AURA_DURATION = REGISTRATE.simple("healing_aura/duration", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(HEALING_AURA::get, 800, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> HEALING_AURA_INTERVAL = REGISTRATE.simple("healing_aura/interval", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(HEALING_AURA::get, 60, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> HEALING_AURA_RADIUS_XZ = REGISTRATE.simple("healing_aura/radius_xz", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(HEALING_AURA::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> HEALING_AURA_RADIUS_Y = REGISTRATE.simple("healing_aura/radius_y", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(HEALING_AURA::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Heavy Storms ritual
  public static final RegistryEntry<HeavyStormsRitual> HEAVY_STORMS = ritual(Rituals.HEAVY_STORMS, HeavyStormsRitual::new);

  public static final RegistryEntry<RitualProperty<Integer>> HEAVY_STORMS_DURATION = REGISTRATE.simple("heavy_storms/duration", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(HEAVY_STORMS::get, 2400, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> HEAVY_STORMS_INTERVAL = REGISTRATE.simple("heavy_storms/interval", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(HEAVY_STORMS::get, 20, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> HEAVY_STORMS_RADIUS_XZ = REGISTRATE.simple("heavy_storms/radius_xz", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(HEAVY_STORMS::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> HEAVY_STORMS_RADIUS_Y = REGISTRATE.simple("heavy_storms/radius_y", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(HEAVY_STORMS::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Overgrowth ritual
  public static final RegistryEntry<OvergrowthRitual> OVERGROWTH = ritual(Rituals.OVERGROWTH, OvergrowthRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> OVERGROWTH_DURATION = REGISTRATE.simple("overgrowth/duration", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(OVERGROWTH::get, 3000, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> OVERGROWTH_INTERVAL = REGISTRATE.simple("overgrowth/interval", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(OVERGROWTH::get, 100, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> OVERGROWTH_RADIUS_XZ = REGISTRATE.simple("overgrowth/radius_xz", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(OVERGROWTH::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> OVERGROWTH_RADIUS_Y = REGISTRATE.simple("overgrowth/radius_y", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(OVERGROWTH::get, 5, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Protection Ritual
  public static final RegistryEntry<ProtectionRitual> PROTECTION = ritual(Rituals.PROTECTION, ProtectionRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> PROTECTION_DURATION = REGISTRATE.simple("protection/duration", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(PROTECTION::get, 1200, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> PROTECTION_INTERVAL = REGISTRATE.simple("protection/interval", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(PROTECTION::get, 20, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> PROTECTION_RADIUS_XZ = REGISTRATE.simple("protection/radius_xz", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(PROTECTION::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> PROTECTION_RADIUS_Y = REGISTRATE.simple("protection/radius_y", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(PROTECTION::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Purity ritual
  public static final RegistryEntry<PurityRitual> PURITY = ritual(Rituals.PURITY, PurityRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> PURITY_DURATION = REGISTRATE.simple("purity/duration", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(PURITY::get, 1200, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> PURITY_INTERVAL = REGISTRATE.simple("purity/interval", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(PURITY::get, 20, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> PURITY_RADIUS_XZ = REGISTRATE.simple("purity/radius_xz", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(PURITY::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> PURITY_RADIUS_Y = REGISTRATE.simple("purity/radius_y", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(PURITY::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Spreading Forest ritual
  public static final RegistryEntry<SpreadingForestRitual> SPREADING_FOREST = ritual(Rituals.SPREADING_FOREST, SpreadingForestRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> SPREADING_FOREST_DURATION = REGISTRATE.simple("spreading_forest/duration", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(SPREADING_FOREST::get, 2400, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> SPREADING_FOREST_INTERVAL = REGISTRATE.simple("spreading_forest/interval", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(SPREADING_FOREST::get, 20, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> SPREADING_FOREST_RADIUS_XZ = REGISTRATE.simple("spreading_forest/radius_xz", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(SPREADING_FOREST::get, 35, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> SPREADING_FOREST_RADIUS_Y = REGISTRATE.simple("spreading_forest/radius_y", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(SPREADING_FOREST::get, 30, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Summon Creatures ritual
  public static final RegistryEntry<SummonCreaturesRitual> SUMMON_CREATURES = ritual(Rituals.SUMMON_CREATURES, SummonCreaturesRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> SUMMON_CREATURES_DURATION = REGISTRATE.simple("summon_creatures/duration", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(SUMMON_CREATURES::get, 200, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> SUMMON_CREATURES_INTERVAL = REGISTRATE.simple("summon_creatures/interval", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(SUMMON_CREATURES::get, 150, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));

  // Transmutation ritual
  public static final RegistryEntry<TransmutationRitual> TRANSMUTATION = ritual(Rituals.TRANSMUTATION, TransmutationRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> TRANSMUTATION_DURATION = REGISTRATE.simple("transmutation/duration", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(TRANSMUTATION::get, 2400, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> TRANSMUTATION_INTERVAL = REGISTRATE.simple("transmutation/interval", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(TRANSMUTATION::get, 100, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> TRANSMUTATION_RADIUS_XZ = REGISTRATE.simple("transmutation/radius_xz", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(TRANSMUTATION::get, 4, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> TRANSMUTATION_RADIUS_Y = REGISTRATE.simple("transmutation/radius_y", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(TRANSMUTATION::get, 4, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Warding ritual
  public static final RegistryEntry<WardingRitual> WARDING = ritual(Rituals.WARDING, WardingRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> WARDING_DURATION = REGISTRATE.simple("warding/duration", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(WARDING::get, 1200, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> WARDING_INTERVAL = REGISTRATE.simple("warding/interval", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(WARDING::get, 20, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> WARDING_RADIUS_XZ = REGISTRATE.simple("warding/radius_xz", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(WARDING::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> WARDING_RADIUS_Y = REGISTRATE.simple("warding/radius_y", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(WARDING::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Wildroot Growth ritual
  public static final RegistryEntry<WildrootGrowthRitual> WILDROOT_GROWTH = ritual(Rituals.WILDROOT_GROWTH, WildrootGrowthRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> WILDROOT_GROWTH_DURATION = REGISTRATE.simple("wildroot_growth/duration", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(WILDROOT_GROWTH::get, 300, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> WILDROOT_GROWTH_INTERVAL = REGISTRATE.simple("wildroot_growth/interval", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(WILDROOT_GROWTH::get, 250, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> WILDROOT_GROWTH_RADIUS_XZ = REGISTRATE.simple("wildroot_growth/radius_xz", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(WILDROOT_GROWTH::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> WILDROOT_GROWTH_RADIUS_Y = REGISTRATE.simple("wildroot_growth/radius_y", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(WILDROOT_GROWTH::get, 20, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Windwall ritual
  public static final RegistryEntry<WindwallRitual> WINDWALL = ritual(Rituals.WINDWALL, WindwallRitual::new);
  public static final RegistryEntry<RitualProperty<Integer>> WINDWALL_DURATION = REGISTRATE.simple("windwall/duration", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(WINDWALL::get, 3000, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final RegistryEntry<RitualProperty<Integer>> WINDWALL_INTERVAL = REGISTRATE.simple("windwall/interval", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(WINDWALL::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final RegistryEntry<RitualProperty<Integer>> WINDWALL_RADIUS_XZ = REGISTRATE.simple("windwall/radius_xz", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(WINDWALL::get, 51, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final RegistryEntry<RitualProperty<Integer>> WINDWALL_RADIUS_Y = REGISTRATE.simple("windwall/radius_y", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(WINDWALL::get, 31, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  // Grove Supplication ritual
  public static final RegistryEntry<GroveSupplicationRitual> GROVE_SUPPLICATION = ritual(Rituals.GROVE_SUPPLICATION, GroveSupplicationRitual::new);

  public static final RegistryEntry<RitualProperty<Integer>> GROVE_SUPPLICATION_DURATION = REGISTRATE.simple("grove_supplication/duration", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(GROVE_SUPPLICATION::get, 250, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));

  public static final RegistryEntry<RitualProperty<Integer>> GROVE_SUPPLICATION_INTERVAL = REGISTRATE.simple("grove_supplication/interval", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(GROVE_SUPPLICATION::get, 210, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));

  public static final RegistryEntry<RitualProperty<Integer>> GROVE_SUPPLICATION_RADIUS_XZ = REGISTRATE.simple("grove_supplication/radius_xz", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(GROVE_SUPPLICATION::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));

  public static final RegistryEntry<RitualProperty<Integer>> GROVE_SUPPLICATION_RADIUS_Y = REGISTRATE.simple("grove_supplication/radius_y", RootsAPI.RITUAL_PROPERTY_REGISTRY, () -> new RitualProperty<>(GROVE_SUPPLICATION::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  private static <T extends Ritual> RegistryEntry<T> ritual(ResourceKey<Ritual> key, NonNullSupplier<T> builder) {
    return REGISTRATE.simple(key.location().getPath(), RootsAPI.RITUAL_REGISTRY, builder);
  }

  public static void load() {
  }
}
