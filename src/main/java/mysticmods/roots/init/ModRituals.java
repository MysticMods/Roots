package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.reference.RitualProperties;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.ritual.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRituals {

  private static final DeferredRegister<Ritual> RITUAL = DeferredRegister.create(RootsRegistries.Keys.RITUALS, RootsAPI.MODID);

  public static final DeferredHolder<Ritual, CraftingRitual> CRAFTING = RITUAL.register("crafting", CraftingRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> CRAFTING_DURATION = P.recordProperty("crafting/duration", Property.ofInt(160, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> CRAFTING_INTERVAL = P.recordProperty("crafting/interval", Property.ofInt(120, RitualProperties.INTERVAL));

  public static final DeferredHolder<Ritual, AnimalHarvestRitual> ANIMAL_HARVEST = RITUAL.register("animal_harvest", AnimalHarvestRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> ANIMAL_HARVEST_DURATION = P.recordProperty("animal_harvest/duration", Property.ofInt(3200, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> ANIMAL_HARVEST_INTERVAL = P.recordProperty("animal_harvest/interval", Property.ofInt(110, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> ANIMAL_HARVEST_RADIUS_XZ = P.recordProperty("animal_harvest/radius_xz", Property.ofInt(8, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> ANIMAL_HARVEST_RADIUS_Y = P.recordProperty("animal_harvest/radius_y", Property.ofInt(6, RitualProperties.RADIUS_Y));
  public static final PropertyHolder<Property.IntegerProperty> ANIMAL_HARVEST_COUNT = P.recordProperty("animal_harvest/count", Property.ofInt(2, "The number of entities to harvest per operation."));
  public static final PropertyHolder<Property.FloatProperty> ANIMAL_HARVEST_LOOTING_CHANCE = P.recordProperty("animal_harvest/looting_chance", Property.ofFloat(0.2f, "Chance per operation that the loot level will be set to looting_value"));
  public static final PropertyHolder<Property.IntegerProperty> ANIMAL_HARVEST_LOOTING_VALUE = P.recordProperty("animal_harvest/looting_value", Property.ofInt(2, "The defaultValue passed to the loot function if looting_chance was successful."));
  public static final PropertyHolder<Property.IntegerProperty> ANIMAL_HARVEST_GLOW_DURATION = P.recordProperty("animal_harvest/glow_duration", Property.ofInt(10, "The duration of the glow effect applied to entities that have been harvest."));

  public static final DeferredHolder<Ritual, BloomingRitual> BLOOMING = RITUAL.register("blooming", BloomingRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> BLOOMING_DURATION = P.recordProperty("blooming/duration", Property.ofInt(3200, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> BLOOMING_INTERVAL = P.recordProperty("blooming/interval", Property.ofInt(100, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> BLOOMING_RADIUS_XZ = P.recordProperty("blooming/radius_xz", Property.ofInt(10, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> BLOOMING_RADIUS_Y = P.recordProperty("blooming/radius_y", Property.ofInt(10, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, FireStormRitual> FIRE_STORM = RITUAL.register("fire_storm", FireStormRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> FIRE_STORM_DURATION = P.recordProperty("fire_storm/duration", Property.ofInt(600, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> FIRE_STORM_INTERVAL = P.recordProperty("fire_storm/interval", Property.ofInt(2, RitualProperties.INTERVAL));

  public static final DeferredHolder<Ritual, FrostLandsRitual> FROST_LANDS = RITUAL.register("frost_lands", FrostLandsRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> FROST_LANDS_DURATION = P.recordProperty("frost_lands/duration", Property.ofInt(6400, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> FROST_LANDS_INTERVAL = P.recordProperty("frost_lands/interval", Property.ofInt(30, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> FROST_LANDS_RADIUS_XZ = P.recordProperty("frost_lands/radius_xz", Property.ofInt(10, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> FROST_LANDS_RADIUS_Y = P.recordProperty("frost_lands/radius_y", Property.ofInt(10, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, GatheringRitual> GATHERING = RITUAL.register("gathering", GatheringRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> GATHERING_DURATION = P.recordProperty("gathering/duration", Property.ofInt(6000, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> GATHERING_INTERVAL = P.recordProperty("gathering/interval", Property.ofInt(80, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> GATHERING_RADIUS_XZ = P.recordProperty("gathering/radius_xz", Property.ofInt(20, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> GATHERING_RADIUS_Y = P.recordProperty("gathering/radius_y", Property.ofInt(15, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, GerminationRitual> GERMINATION = RITUAL.register("germination", GerminationRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> GERMINATION_DURATION = P.recordProperty("germination/duration", Property.ofInt(6400, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> GERMINATION_INTERVAL = P.recordProperty("germination/interval", Property.ofInt(64, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> GERMINATION_RADIUS_XZ = P.recordProperty("germination/radius_xz", Property.ofInt(20, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> GERMINATION_RADIUS_Y = P.recordProperty("germination/radius_y", Property.ofInt(20, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, HealingAuraRitual> HEALING_AURA = RITUAL.register("healing_aura", HealingAuraRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> HEALING_AURA_DURATION = P.recordProperty("healing_aura/duration", Property.ofInt(800, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> HEALING_AURA_INTERVAL = P.recordProperty("healing_aura/interval", Property.ofInt(60, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> HEALING_AURA_RADIUS_XZ = P.recordProperty("healing_aura/radius_xz", Property.ofInt(15, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> HEALING_AURA_RADIUS_Y = P.recordProperty("healing_aura/radius_y", Property.ofInt(15, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, HeavyStormsRitual> HEAVY_STORMS = RITUAL.register("heavy_storms", HeavyStormsRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> HEAVY_STORMS_DURATION = P.recordProperty("heavy_storms/duration", Property.ofInt(2400, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> HEAVY_STORMS_INTERVAL = P.recordProperty("heavy_storms/interval", Property.ofInt(20, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> HEAVY_STORMS_RADIUS_XZ = P.recordProperty("heavy_storms/radius_xz", Property.ofInt(15, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> HEAVY_STORMS_RADIUS_Y = P.recordProperty("heavy_storms/radius_y", Property.ofInt(15, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, OvergrowthRitual> OVERGROWTH = RITUAL.register("overgrowth", OvergrowthRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> OVERGROWTH_DURATION = P.recordProperty("overgrowth/duration", Property.ofInt(1950, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> OVERGROWTH_INTERVAL = P.recordProperty("overgrowth/interval", Property.ofInt(150, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> OVERGROWTH_RADIUS_XZ = P.recordProperty("overgrowth/radius_xz", Property.ofInt(6, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> OVERGROWTH_RADIUS_Y = P.recordProperty("overgrowth/radius_y", Property.ofInt(5, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, ProtectionRitual> PROTECTION = RITUAL.register("protection", ProtectionRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> PROTECTION_DURATION = P.recordProperty("protection/duration", Property.ofInt(1200, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> PROTECTION_INTERVAL = P.recordProperty("protection/interval", Property.ofInt(20, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> PROTECTION_RADIUS_XZ = P.recordProperty("protection/radius_xz", Property.ofInt(8, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> PROTECTION_RADIUS_Y = P.recordProperty("protection/radius_y", Property.ofInt(6, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, PurityRitual> PURITY = RITUAL.register("purity", PurityRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> PURITY_DURATION = P.recordProperty("purity/duration", Property.ofInt(1200, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> PURITY_INTERVAL = P.recordProperty("purity/interval", Property.ofInt(20, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> PURITY_RADIUS_XZ = P.recordProperty("purity/radius_xz", Property.ofInt(8, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> PURITY_RADIUS_Y = P.recordProperty("purity/radius_y", Property.ofInt(4, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, SpreadingForestRitual> SPREADING_FOREST = RITUAL.register("spreading_forest", SpreadingForestRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> SPREADING_FOREST_DURATION = P.recordProperty("spreading_forest/duration", Property.ofInt(2400, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> SPREADING_FOREST_INTERVAL = P.recordProperty("spreading_forest/interval", Property.ofInt(20, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> SPREADING_FOREST_RADIUS_XZ = P.recordProperty("spreading_forest/radius_xz", Property.ofInt(35, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> SPREADING_FOREST_RADIUS_Y = P.recordProperty("spreading_forest/radius_y", Property.ofInt(30, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, SummonCreaturesRitual> SUMMON_CREATURES = RITUAL.register("summon_creatures", SummonCreaturesRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> SUMMON_CREATURES_DURATION = P.recordProperty("summon_creatures/duration", Property.ofInt(200, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> SUMMON_CREATURES_INTERVAL = P.recordProperty("summon_creatures/interval", Property.ofInt(150, RitualProperties.INTERVAL));

  public static final DeferredHolder<Ritual, TransmutationRitual> TRANSMUTATION = RITUAL.register("transmutation", TransmutationRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> TRANSMUTATION_DURATION = P.recordProperty("transmutation/duration", Property.ofInt(2400, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> TRANSMUTATION_INTERVAL
    = P.recordProperty("transmutation/interval", Property.ofInt(100, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> TRANSMUTATION_RADIUS_XZ
    = P.recordProperty("transmutation/radius_xz", Property.ofInt(6, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> TRANSMUTATION_RADIUS_Y
    = P.recordProperty("transmutation/radius_y", Property.ofInt(4, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, WardingRitual> WARDING = RITUAL.register("warding", WardingRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> WARDING_DURATION = P.recordProperty("warding/duration", Property.ofInt(1200, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> WARDING_INTERVAL = P.recordProperty("warding/interval", Property.ofInt(20, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> WARDING_RADIUS_XZ = P.recordProperty("warding/radius_xz", Property.ofInt(8, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> WARDING_RADIUS_Y = P.recordProperty("warding/radius_y", Property.ofInt(6, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, WildrootGrowthRitual> WILDROOT_GROWTH = RITUAL.register("wildroot_growth", WildrootGrowthRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> WILDROOT_GROWTH_DURATION = P.recordProperty("wildroot_growth/duration", Property.ofInt(300, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> WILDROOT_GROWTH_INTERVAL = P.recordProperty("wildroot_growth/interval", Property.ofInt(250, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> WILDROOT_GROWTH_RADIUS_XZ = P.recordProperty("wildroot_growth/radius_xz", Property.ofInt(10, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> WILDROOT_GROWTH_RADIUS_Y = P.recordProperty("wildroot_growth/radius_y", Property.ofInt(4, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, WindwallRitual> WINDWALL = RITUAL.register("windwall", WindwallRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> WINDWALL_DURATION = P.recordProperty("windwall/duration", Property.ofInt(3000, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> WINDWALL_INTERVAL = P.recordProperty("windwall/interval", Property.ofInt(10, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> WINDWALL_RADIUS_XZ = P.recordProperty("windwall/radius_xz", Property.ofInt(51, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> WINDWALL_RADIUS_Y = P.recordProperty("windwall/radius_y", Property.ofInt(31, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, GroveSupplicationRitual> GROVE_SUPPLICATION = RITUAL.register("grove_supplication", GroveSupplicationRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> GROVE_SUPPLICATION_DURATION = P.recordProperty("grove_supplication/duration", Property.ofInt(250, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> GROVE_SUPPLICATION_INTERVAL = P.recordProperty("grove_supplication/interval", Property.ofInt(210, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> GROVE_SUPPLICATION_RADIUS_XZ = P.recordProperty("grove_supplication/radius_xz", Property.ofInt(10, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> GROVE_SUPPLICATION_RADIUS_Y = P.recordProperty("grove_supplication/radius_y", Property.ofInt(10, RitualProperties.RADIUS_Y));

  public static void register(IEventBus bus) {
    RITUAL.register(bus);
  }
}
