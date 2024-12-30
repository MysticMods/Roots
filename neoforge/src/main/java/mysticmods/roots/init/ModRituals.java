package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.reference.RitualProperties;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.ritual.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRituals {

  private static final DeferredRegister<Ritual> RITUAL = DeferredRegister.create(RootsRegistries.Keys.RITUALS, RootsAPI.MODID);
  private static final DeferredRegister<RitualProperty<?>> RITUAL_PROPERTY = DeferredRegister.create(RootsRegistries.Keys.RITUAL_PROPERTIES, RootsAPI.MODID);

  public static final DeferredHolder<Ritual, CraftingRitual> CRAFTING = RITUAL.register("crafting", CraftingRitual::new);
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> CRAFTING_DURATION = RITUAL_PROPERTY.register("crafting/duration", () -> new RitualProperty<>(CRAFTING::get, 160, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> CRAFTING_INTERVAL = RITUAL_PROPERTY.register("crafting/interval", () -> new RitualProperty<>(CRAFTING::get, 120, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));


  public static final DeferredHolder<Ritual, AnimalHarvestRitual> ANIMAL_HARVEST = RITUAL.register("animal_harvest", AnimalHarvestRitual::new);
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> ANIMAL_HARVEST_DURATION = RITUAL_PROPERTY.register("animal_harvest/duration", () -> new RitualProperty<>(ANIMAL_HARVEST::get, 3200, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> ANIMAL_HARVEST_INTERVAL = RITUAL_PROPERTY.register("animal_harvest/interval", () -> new RitualProperty<>(ANIMAL_HARVEST::get, 110, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> ANIMAL_HARVEST_COUNT = RITUAL_PROPERTY.register("animal_harvest/count", () -> new RitualProperty<>(ANIMAL_HARVEST::get, 5, Property.INTEGER_SERIALIZER, RitualProperties.COUNT));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> ANIMAL_HARVEST_RADIUS_XZ = RITUAL_PROPERTY.register("animal_harvest/radius_xz", () -> new RitualProperty<>(ANIMAL_HARVEST::get, 8, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> ANIMAL_HARVEST_RADIUS_Y = RITUAL_PROPERTY.register("animal_harvest/radius_y", () -> new RitualProperty<>(ANIMAL_HARVEST::get, 6, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Float>> ANIMAL_HARVEST_LOOTING_CHANCE = RITUAL_PROPERTY.register("animal_harvest/looting_chance", () -> new RitualProperty<>(ANIMAL_HARVEST::get, 0.2f, Property.FLOAT_SERIALIZER, "Chance per operation that the loot level will be set to looting_value"));
  // looting value
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> ANIMAL_HARVEST_LOOTING_VALUE = RITUAL_PROPERTY.register("animal_harvest/looting_value", () -> new RitualProperty<>(ANIMAL_HARVEST::get, 2, Property.INTEGER_SERIALIZER, "The value passed to the loot function if looting_chance was successful."));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> ANIMAL_HARVEST_GLOW_DURATION = RITUAL_PROPERTY.register("animal_harvest/glow_duration", () -> new RitualProperty<>(ANIMAL_HARVEST::get, 10, Property.INTEGER_SERIALIZER, "The duration of the glow effect applied to entities that have been harvest."));

  public static final DeferredHolder<Ritual, BloomingRitual> BLOOMING = RITUAL.register("blooming", BloomingRitual::new);
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> BLOOMING_DURATION = RITUAL_PROPERTY.register("blooming/duration", () -> new RitualProperty<>(BLOOMING::get, 3200, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> BLOOMING_INTERVAL = RITUAL_PROPERTY.register("blooming/interval", () -> new RitualProperty<>(BLOOMING::get, 100, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> BLOOMING_RADIUS_XZ = RITUAL_PROPERTY.register("blooming/radius_xz", () -> new RitualProperty<>(BLOOMING::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> BLOOMING_RADIUS_Y = RITUAL_PROPERTY.register("blooming/radius_y", () -> new RitualProperty<>(BLOOMING::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));


  public static final DeferredHolder<Ritual, FireStormRitual> FIRE_STORM = RITUAL.register("fire_storm", FireStormRitual::new);
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> FIRE_STORM_DURATION = RITUAL_PROPERTY.register("fire_storm/duration", () -> new RitualProperty<>(FIRE_STORM::get, 600, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> FIRE_STORM_INTERVAL = RITUAL_PROPERTY.register("fire_storm/interval", () -> new RitualProperty<>(FIRE_STORM::get, 2, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));


  public static final DeferredHolder<Ritual, FrostLandsRitual> FROST_LANDS = RITUAL.register("frost_lands", FrostLandsRitual::new);
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> FROST_LANDS_DURATION = RITUAL_PROPERTY.register("frost_lands/duration", () -> new RitualProperty<>(FROST_LANDS::get, 6400, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> FROST_LANDS_INTERVAL = RITUAL_PROPERTY.register("frost_lands/interval", () -> new RitualProperty<>(FROST_LANDS::get, 30, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> FROST_LANDS_RADIUS_XZ = RITUAL_PROPERTY.register("frost_lands/radius_xz", () -> new RitualProperty<>(FROST_LANDS::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> FROST_LANDS_RADIUS_Y = RITUAL_PROPERTY.register("frost_lands/radius_y", () -> new RitualProperty<>(FROST_LANDS::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, GatheringRitual> GATHERING = RITUAL.register("gathering", GatheringRitual::new);
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> GATHERING_DURATION = RITUAL_PROPERTY.register("gathering/duration", () -> new RitualProperty<>(GATHERING::get, 6000, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> GATHERING_INTERVAL = RITUAL_PROPERTY.register("gathering/interval", () -> new RitualProperty<>(GATHERING::get, 80, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> GATHERING_RADIUS_XZ = RITUAL_PROPERTY.register("gathering/radius_xz", () -> new RitualProperty<>(GATHERING::get, 20, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> GATHERING_RADIUS_Y = RITUAL_PROPERTY.register("gathering/radius_y", () -> new RitualProperty<>(GATHERING::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, GerminationRitual> GERMINATION = RITUAL.register("germination", GerminationRitual::new);
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> GERMINATION_DURATION = RITUAL_PROPERTY.register("germination/duration", () -> new RitualProperty<>(GERMINATION::get, 6400, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> GERMINATION_INTERVAL = RITUAL_PROPERTY.register("germination/interval", () -> new RitualProperty<>(GERMINATION::get, 64, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> GERMINATION_RADIUS_XZ = RITUAL_PROPERTY.register("germination/radius_xz", () -> new RitualProperty<>(GERMINATION::get, 20, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> GERMINATION_RADIUS_Y = RITUAL_PROPERTY.register("germination/radius_y", () -> new RitualProperty<>(GERMINATION::get, 20, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, HealingAuraRitual> HEALING_AURA = RITUAL.register("healing_aura", HealingAuraRitual::new);
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> HEALING_AURA_DURATION = RITUAL_PROPERTY.register("healing_aura/duration", () -> new RitualProperty<>(HEALING_AURA::get, 800, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> HEALING_AURA_INTERVAL = RITUAL_PROPERTY.register("healing_aura/interval", () -> new RitualProperty<>(HEALING_AURA::get, 60, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> HEALING_AURA_RADIUS_XZ = RITUAL_PROPERTY.register("healing_aura/radius_xz", () -> new RitualProperty<>(HEALING_AURA::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> HEALING_AURA_RADIUS_Y = RITUAL_PROPERTY.register("healing_aura/radius_y", () -> new RitualProperty<>(HEALING_AURA::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, HeavyStormsRitual> HEAVY_STORMS = RITUAL.register("heavy_storms", HeavyStormsRitual::new);
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> HEAVY_STORMS_DURATION = RITUAL_PROPERTY.register("heavy_storms/duration", () -> new RitualProperty<>(HEAVY_STORMS::get, 2400, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> HEAVY_STORMS_INTERVAL = RITUAL_PROPERTY.register("heavy_storms/interval", () -> new RitualProperty<>(HEAVY_STORMS::get, 20, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> HEAVY_STORMS_RADIUS_XZ = RITUAL_PROPERTY.register("heavy_storms/radius_xz", () -> new RitualProperty<>(HEAVY_STORMS::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> HEAVY_STORMS_RADIUS_Y = RITUAL_PROPERTY.register("heavy_storms/radius_y", () -> new RitualProperty<>(HEAVY_STORMS::get, 15, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, OvergrowthRitual> OVERGROWTH = RITUAL.register("overgrowth", OvergrowthRitual::new);
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> OVERGROWTH_DURATION = RITUAL_PROPERTY.register("overgrowth/duration", () -> new RitualProperty<>(OVERGROWTH::get, 1950, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> OVERGROWTH_INTERVAL = RITUAL_PROPERTY.register("overgrowth/interval", () -> new RitualProperty<>(OVERGROWTH::get, 150, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> OVERGROWTH_RADIUS_XZ = RITUAL_PROPERTY.register("overgrowth/radius_xz", () -> new RitualProperty<>(OVERGROWTH::get, 6, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> OVERGROWTH_RADIUS_Y = RITUAL_PROPERTY.register("overgrowth/radius_y", () -> new RitualProperty<>(OVERGROWTH::get, 5, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, ProtectionRitual> PROTECTION = RITUAL.register("protection", ProtectionRitual::new);
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> PROTECTION_DURATION = RITUAL_PROPERTY.register("protection/duration", () -> new RitualProperty<>(PROTECTION::get, 1200, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> PROTECTION_INTERVAL = RITUAL_PROPERTY.register("protection/interval", () -> new RitualProperty<>(PROTECTION::get, 20, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> PROTECTION_RADIUS_XZ = RITUAL_PROPERTY.register("protection/radius_xz", () -> new RitualProperty<>(PROTECTION::get, 8, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> PROTECTION_RADIUS_Y = RITUAL_PROPERTY.register("protection/radius_y", () -> new RitualProperty<>(PROTECTION::get, 6, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));


  public static final DeferredHolder<Ritual, PurityRitual> PURITY = RITUAL.register("purity", PurityRitual::new);
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> PURITY_DURATION = RITUAL_PROPERTY.register("purity/duration", () -> new RitualProperty<>(PURITY::get, 1200, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> PURITY_INTERVAL = RITUAL_PROPERTY.register("purity/interval", () -> new RitualProperty<>(PURITY::get, 20, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> PURITY_RADIUS_XZ = RITUAL_PROPERTY.register("purity/radius_xz", () -> new RitualProperty<>(PURITY::get, 8, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> PURITY_RADIUS_Y = RITUAL_PROPERTY.register("purity/radius_y", () -> new RitualProperty<>(PURITY::get, 4, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, SpreadingForestRitual> SPREADING_FOREST = RITUAL.register("spreading_forest", SpreadingForestRitual::new);
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> SPREADING_FOREST_DURATION = RITUAL_PROPERTY.register("spreading_forest/duration", () -> new RitualProperty<>(SPREADING_FOREST::get, 2400, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> SPREADING_FOREST_INTERVAL = RITUAL_PROPERTY.register("spreading_forest/interval", () -> new RitualProperty<>(SPREADING_FOREST::get, 20, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> SPREADING_FOREST_RADIUS_XZ = RITUAL_PROPERTY.register("spreading_forest/radius_xz", () -> new RitualProperty<>(SPREADING_FOREST::get, 35, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> SPREADING_FOREST_RADIUS_Y = RITUAL_PROPERTY.register("spreading_forest/radius_y", () -> new RitualProperty<>(SPREADING_FOREST::get, 30, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, SummonCreaturesRitual> SUMMON_CREATURES = RITUAL.register("summon_creatures", SummonCreaturesRitual::new);
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> SUMMON_CREATURES_DURATION = RITUAL_PROPERTY.register("summon_creatures/duration", () -> new RitualProperty<>(SUMMON_CREATURES::get, 200, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> SUMMON_CREATURES_INTERVAL = RITUAL_PROPERTY.register("summon_creatures/interval", () -> new RitualProperty<>(SUMMON_CREATURES::get, 150, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));

  public static final DeferredHolder<Ritual, TransmutationRitual> TRANSMUTATION = RITUAL.register("transmutation", TransmutationRitual::new);
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> TRANSMUTATION_DURATION = RITUAL_PROPERTY.register("transmutation/duration", () -> new RitualProperty<>(TRANSMUTATION::get, 2400, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> TRANSMUTATION_INTERVAL = RITUAL_PROPERTY.register("transmutation/interval", () -> new RitualProperty<>(TRANSMUTATION::get, 100, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> TRANSMUTATION_RADIUS_XZ = RITUAL_PROPERTY.register("transmutation/radius_xz", () -> new RitualProperty<>(TRANSMUTATION::get, 6, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> TRANSMUTATION_RADIUS_Y = RITUAL_PROPERTY.register("transmutation/radius_y", () -> new RitualProperty<>(TRANSMUTATION::get, 4, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, WardingRitual> WARDING = RITUAL.register("warding", WardingRitual::new);
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> WARDING_DURATION = RITUAL_PROPERTY.register("warding/duration", () -> new RitualProperty<>(WARDING::get, 1200, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> WARDING_INTERVAL = RITUAL_PROPERTY.register("warding/interval", () -> new RitualProperty<>(WARDING::get, 20, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> WARDING_RADIUS_XZ = RITUAL_PROPERTY.register("warding/radius_xz", () -> new RitualProperty<>(WARDING::get, 8, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> WARDING_RADIUS_Y = RITUAL_PROPERTY.register("warding/radius_y", () -> new RitualProperty<>(WARDING::get, 6, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, WildrootGrowthRitual> WILDROOT_GROWTH = RITUAL.register("wildroot_growth", WildrootGrowthRitual::new);
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> WILDROOT_GROWTH_DURATION = RITUAL_PROPERTY.register("wildroot_growth/duration", () -> new RitualProperty<>(WILDROOT_GROWTH::get, 300, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> WILDROOT_GROWTH_INTERVAL = RITUAL_PROPERTY.register("wildroot_growth/interval", () -> new RitualProperty<>(WILDROOT_GROWTH::get, 250, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> WILDROOT_GROWTH_RADIUS_XZ = RITUAL_PROPERTY.register("wildroot_growth/radius_xz", () -> new RitualProperty<>(WILDROOT_GROWTH::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> WILDROOT_GROWTH_RADIUS_Y = RITUAL_PROPERTY.register("wildroot_growth/radius_y", () -> new RitualProperty<>(WILDROOT_GROWTH::get, 4, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, WindwallRitual> WINDWALL = RITUAL.register("windwall", WindwallRitual::new);
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> WINDWALL_DURATION = RITUAL_PROPERTY.register("windwall/duration", () -> new RitualProperty<>(WINDWALL::get, 3000, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> WINDWALL_INTERVAL = RITUAL_PROPERTY.register("windwall/interval", () -> new RitualProperty<>(WINDWALL::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> WINDWALL_RADIUS_XZ = RITUAL_PROPERTY.register("windwall/radius_xz", () -> new RitualProperty<>(WINDWALL::get, 51, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> WINDWALL_RADIUS_Y = RITUAL_PROPERTY.register("windwall/radius_y", () -> new RitualProperty<>(WINDWALL::get, 31, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, GroveSupplicationRitual> GROVE_SUPPLICATION = RITUAL.register("grove_supplication", GroveSupplicationRitual::new);
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> GROVE_SUPPLICATION_DURATION = RITUAL_PROPERTY.register("grove_supplication/duration", () -> new RitualProperty<>(GROVE_SUPPLICATION::get, 250, Property.INTEGER_SERIALIZER, RitualProperties.DURATION));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> GROVE_SUPPLICATION_INTERVAL = RITUAL_PROPERTY.register("grove_supplication/interval", () -> new RitualProperty<>(GROVE_SUPPLICATION::get, 210, Property.INTEGER_SERIALIZER, RitualProperties.INTERVAL));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> GROVE_SUPPLICATION_RADIUS_XZ = RITUAL_PROPERTY.register("grove_supplication/radius_xz", () -> new RitualProperty<>(GROVE_SUPPLICATION::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_XZ));
  public static final DeferredHolder<RitualProperty<?>, RitualProperty<Integer>> GROVE_SUPPLICATION_RADIUS_Y = RITUAL_PROPERTY.register("grove_supplication/radius_y", () -> new RitualProperty<>(GROVE_SUPPLICATION::get, 10, Property.INTEGER_SERIALIZER, RitualProperties.RADIUS_Y));

  public static void register(IEventBus bus) {
    RITUAL.register(bus);
    RITUAL_PROPERTY.register(bus);
  }
}
