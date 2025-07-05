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
  public static final PropertyHolder<Property.IntegerProperty> ANIMAL_HARVEST_COUNT = P.recordProperty("animal_harvest/count", Property.ofInt(1, "The number of entities to harvest per operation."));
  public static final PropertyHolder<Property.FloatProperty> ANIMAL_HARVEST_LOOTING_CHANCE = P.recordProperty("animal_harvest/looting_chance", Property.ofFloat(0.2f, "Chance per operation that the loot level will be set to looting_value"));
  public static final PropertyHolder<Property.IntegerProperty> ANIMAL_HARVEST_LOOTING_VALUE = P.recordProperty("animal_harvest/looting_value", Property.ofInt(1, "The defaultValue passed to the loot function if looting_chance was successful."));
  public static final PropertyHolder<Property.IntegerProperty> ANIMAL_HARVEST_GLOW_DURATION = P.recordProperty("animal_harvest/glow_duration", Property.ofInt(20, "The duration of the glow effect applied to entities that have been harvest."));
  public static final PropertyHolder<Property.IntegerProperty> ANIMAL_HARVEST_STACK_LIMIT = P.recordProperty("animal_harvest/stack_limit", Property.ofInt(1, "The maximum stack size of each item harvested. Set to -1 to not modify."));
  public static final PropertyHolder<Property.IntegerProperty> ANIMAL_HARVEST_STACK_COUNT_LIMIT = P.recordProperty("animal_harvest/stack_count_limit", Property.ofInt(1, "If multiple items are dropped, the maximum number of items will be selected at random and the others discarded. Set to -1 to allow all items to drop."));

  public static final DeferredHolder<Ritual, AugmentationRitual> AUGMENTATION = RITUAL.register("augmentation", AugmentationRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> AUGMENTATION_DURATION = P.recordProperty("augmentation/duration", Property.ofInt(1200, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> AUGMENTATION_INTERVAL = P.recordProperty("augmentation/interval", Property.ofInt(20, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> AUGMENTATION_RADIUS_XZ = P.recordProperty("augmentation/radius_xz", Property.ofInt(8, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> AUGMENTATION_RADIUS_Y = P.recordProperty("augmentation/radius_y", Property.ofInt(6, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, BloomingRitual> BLOOMING = RITUAL.register("blooming", BloomingRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> BLOOMING_DURATION = P.recordProperty("blooming/duration", Property.ofInt(3200, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> BLOOMING_INTERVAL = P.recordProperty("blooming/interval", Property.ofInt(100, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> BLOOMING_RADIUS_XZ = P.recordProperty("blooming/radius_xz", Property.ofInt(10, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> BLOOMING_RADIUS_Y = P.recordProperty("blooming/radius_y", Property.ofInt(10, RitualProperties.RADIUS_Y));
  public static final PropertyHolder<Property.IntegerProperty> BLOOMING_COUNT = P.recordProperty("blooming/count", Property.ofInt(1, "The number of flowers to place per interval."));

  public static final DeferredHolder<Ritual, FireStormRitual> FIRE_STORM = RITUAL.register("fire_storm", FireStormRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> FIRE_STORM_DURATION = P.recordProperty("fire_storm/duration", Property.ofInt(600, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> FIRE_STORM_INTERVAL = P.recordProperty("fire_storm/interval", Property.ofInt(60, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> FIRE_STORM_RADIUS_XZ = P.recordProperty("fire_storm/radius_xz", Property.ofInt(10, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> FIRE_STORM_RADIUS_Y = P.recordProperty("fire_storm/radius_y", Property.ofInt(30, RitualProperties.RADIUS_Y));
  public static final PropertyHolder<Property.IntegerProperty> FIRE_STORM_COUNT = P.recordProperty("fire_storm/count", Property.ofInt(8, "The maximum number of meteors that can exist in the ritual area at once."));

  public static final DeferredHolder<Ritual, FrostLandsRitual> FROST_LANDS = RITUAL.register("frost_lands", FrostLandsRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> FROST_LANDS_FLUID_COUNT = P.recordProperty("frost_lands/fluid_count", Property.ofInt(2, "The number of fluid blocks to tweak per interval."));
  public static final PropertyHolder<Property.IntegerProperty> FROST_LANDS_COUNT = P.recordProperty("frost_lands/count", Property.ofInt(5, "The number of blocks to tweak per interval."));
  public static final PropertyHolder<Property.FloatProperty> FROST_LANDS_LAYER_CHANCE = P.recordProperty("frost_lands/layer_chance", Property.ofFloat(0.1f, "The chance per interval that a snow layer will be added to an existing layer"));
  public static final PropertyHolder<Property.FloatProperty> FROST_LANDS_POWDER_CHANCE = P.recordProperty("frost_lands/powder_chance", Property.ofFloat(0.04f, "The chance per interval that a full snow layer will convert to powdered snow."));
  public static final PropertyHolder<Property.FloatProperty> FROST_LANDS_ICE_CHANCE = P.recordProperty("frost_lands/ice_chance", Property.ofFloat(0.05f, "The chance per interval that an ice block will be converted into packed or blue ice."));
  public static final PropertyHolder<Property.IntegerProperty> FROST_LANDS_DURATION = P.recordProperty("frost_lands/duration", Property.ofInt(6400, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> FROST_LANDS_INTERVAL = P.recordProperty("frost_lands/interval", Property.ofInt(30, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> FROST_LANDS_RADIUS_XZ = P.recordProperty("frost_lands/radius_xz", Property.ofInt(10, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> FROST_LANDS_RADIUS_Y = P.recordProperty("frost_lands/radius_y", Property.ofInt(10, RitualProperties.RADIUS_Y));
  public static final PropertyHolder<Property.IntegerProperty> FROST_LANDS_INTERVAL_HEAL = P.recordProperty("frost_lands/heal_interval", Property.ofInt(30, "The interval at which tagged entities are healed."));
  public static final PropertyHolder<Property.FloatProperty> FROST_LANDS_SPAWN_CHANCE = P.recordProperty("frost_lands/spawn_chance", Property.ofFloat(0.06f, "The chance per tick that a snowman will spawn."));

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
  public static final PropertyHolder<Property.FloatProperty> HEALING_AURA_PLAYER_HEAL_AMOUNT = P.recordProperty("healing_aura/player_heal_amount", Property.ofFloat(1, "The amount of health to heal per interval."));
  public static final PropertyHolder<Property.FloatProperty> HEALING_AURA_ENTITY_HEAL_AMOUNT = P.recordProperty("healing_aura/entity_heal_amount", Property.ofFloat(1, "The amount of health to heal per interval."));

  public static final DeferredHolder<Ritual, HeavyStormsRitual> HEAVY_STORMS = RITUAL.register("heavy_storms", HeavyStormsRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> HEAVY_STORMS_DURATION = P.recordProperty("heavy_storms/duration", Property.ofInt(2400, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> HEAVY_STORMS_WEATHER_DURATION = P.recordProperty("heavy_storms/weather_duration", Property.ofInt(200, "How long storms and rain should continue for."));
  public static final PropertyHolder<Property.IntegerProperty> HEAVY_STORMS_INTERVAL = P.recordProperty("heavy_storms/interval", Property.ofInt(20, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> HEAVY_STORMS_RADIUS_XZ = P.recordProperty("heavy_storms/radius_xz", Property.ofInt(15, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> HEAVY_STORMS_RADIUS_Y = P.recordProperty("heavy_storms/radius_y", Property.ofInt(15, RitualProperties.RADIUS_Y));
  public static final PropertyHolder<Property.FloatProperty> HEAVY_STORMS_LIGHTNING_CHANCE = P.recordProperty("heavy_storms/lightning_chance", Property.ofFloat(0.06f, "The chance per interval that a lightning bolt will strike."));
  public static final PropertyHolder<Property.BooleanProperty> HEAVY_STORMS_CAUSES_RAIN = P.recordProperty("heavy_storms/causes_rain", Property.ofBool(true, "Whether the ritual causes rain."));
  public static final PropertyHolder<Property.BooleanProperty> HEAVY_STORMS_CAUSES_THUNDER = P.recordProperty("heavy_storms/causes_thunder", Property.ofBool(true, "Whether the ritual causes thunder."));

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
  public static final PropertyHolder<Property.IntegerProperty> PROTECTION_DAY_LENGTH = P.recordProperty("protection/day_length", Property.ofInt(24000, "The length of a day."));
  public static final PropertyHolder<Property.IntegerProperty> PROTECTION_NIGHT_THRESHOLD = P.recordProperty("protection/night_threshold", Property.ofInt(12000, "The point at which day transitions to night."));
  public static final PropertyHolder<Property.BooleanProperty> PROTECTION_CLEARS_WEATHER = P.recordProperty("protection/clears_weather", Property.ofBool(true, "Whether the ritual should clear weather."));
  public static final PropertyHolder<Property.BooleanProperty> PROTECTION_SHORTENS_NIGHT = P.recordProperty("protection/shortens_night", Property.ofBool(true, "Whether the ritual shortens the length of the night by the defined value."));
  public static final PropertyHolder<Property.BooleanProperty> PROTECTION_LENGTHENS_DAY = P.recordProperty("protection/lengthens_day", Property.ofBool(true, "Whether the ritual lengthens the length of the day by the defined value."));
  public static final PropertyHolder<Property.FloatProperty> PROTECTION_DAY_SPEED = P.recordProperty("protection/day_speed", Property.ofFloat(0.3f, "The fractional value that is applied to the time per tick during the 'day' period. The default of 0.3 means that the length of the day is increased by about 60%. -1 represents no difference."));
  public static final PropertyHolder<Property.FloatProperty> PROTECTION_NIGHT_SPEED = P.recordProperty("protection/night_speed", Property.ofFloat(2f, "The fractional value that is applied to the time per tick during the 'night' period. The default of 2 means that the length of the night is reduced by about 50%. -1 represents no difference."));
  public static final PropertyHolder<Property.IntegerProperty> PROTECTION_CLEAR_DURATION = P.recordProperty("protection/clear_duration", Property.ofInt(20 * 60 * 20, "How long the weather is cleared for, if the weather is cleared."));

  public static final DeferredHolder<Ritual, PurityRitual> PURITY = RITUAL.register("purity", PurityRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> PURITY_DURATION = P.recordProperty("purity/duration", Property.ofInt(1200, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> PURITY_INTERVAL = P.recordProperty("purity/interval", Property.ofInt(20, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> PURITY_RADIUS_XZ = P.recordProperty("purity/radius_xz", Property.ofInt(8, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> PURITY_RADIUS_Y = P.recordProperty("purity/radius_y", Property.ofInt(4, RitualProperties.RADIUS_Y));
  public static final PropertyHolder<Property.BooleanProperty> PURITY_CONVERT_ZOMBIES = P.recordProperty("purity/convert_zombies", Property.ofBool(true, "Whether the ritual should reduce the conversion time of zombie villagers."));
  public static final PropertyHolder<Property.IntegerProperty> PURITY_CONVERSION_ADDITION = P.recordProperty("purity/conversion_addition", Property.ofInt(1, "The number of ticks to reduce the remaining conversion duration for zombie villagers; this is applied every teck, rather than every interval."));
  public static final PropertyHolder<Property.IntegerProperty> PURITY_POTION_COUNT = P.recordProperty("purity/potion_count", Property.ofInt(1, "The number of potion effects to clear per entity per interval."));

  public static final DeferredHolder<Ritual, SpreadingForestRitual> SPREADING_FOREST = RITUAL.register("spreading_forest", SpreadingForestRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> SPREADING_FOREST_DURATION = P.recordProperty("spreading_forest/duration", Property.ofInt(2400, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> SPREADING_FOREST_INTERVAL = P.recordProperty("spreading_forest/interval", Property.ofInt(20, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> SPREADING_FOREST_RADIUS_XZ = P.recordProperty("spreading_forest/radius_xz", Property.ofInt(35, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> SPREADING_FOREST_RADIUS_Y = P.recordProperty("spreading_forest/radius_y", Property.ofInt(30, RitualProperties.RADIUS_Y));

  public static final DeferredHolder<Ritual, SummonCreaturesRitual> SUMMON_CREATURES = RITUAL.register("summon_creatures", SummonCreaturesRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> SUMMON_CREATURES_DURATION = P.recordProperty("summon_creatures/duration", Property.ofInt(200, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> SUMMON_CREATURES_INTERVAL = P.recordProperty("summon_creatures/interval", Property.ofInt(150, RitualProperties.INTERVAL));

  public static final DeferredHolder<Ritual, WardingRitual> WARDING = RITUAL.register("warding", WardingRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> WARDING_DURATION = P.recordProperty("warding/duration", Property.ofInt(1200, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> WARDING_INTERVAL = P.recordProperty("warding/interval", Property.ofInt(20, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> WARDING_RADIUS_XZ = P.recordProperty("warding/radius_xz", Property.ofInt(8, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> WARDING_RADIUS_Y = P.recordProperty("warding/radius_y", Property.ofInt(6, RitualProperties.RADIUS_Y));
  public static final PropertyHolder<Property.IntegerProperty> WARDING_POTION_DURATION = P.recordProperty("warding/resistance_duration", Property.ofInt(60, "The duration of the resistance effect applied to entities."));
  public static final PropertyHolder<Property.IntegerProperty> WARDING_POTION_AMPLIFIER = P.recordProperty("warding/resistance_amplifier", Property.ofInt(10, "The amplifier of the resistance effect applied to entities."));

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
  public static final PropertyHolder<Property.FloatProperty> WINDWALL_KNOCKBACK_STRENGTH = P.recordProperty("windwall/knockback_strength", Property.ofFloat(1.0f, "The strength of the knockback applied to entities."));
  public static final PropertyHolder<Property.DoubleProperty> WINDWALL_MINIMUM_Y_VELOCITY = P.recordProperty("windwall/min_y_velocity", Property.ofDouble(0.4, "The minimum y velocity applied to entities."));
  public static final PropertyHolder<Property.DoubleProperty> WINDWALL_HEIGHT_PERCENTAGE = P.recordProperty("windwall/height_percentage", Property.ofDouble(0.1, "The percentage of the height of the relevant entity that will be added to their y velocity."));

  public static final DeferredHolder<Ritual, GroveSupplicationRitual> GROVE_SUPPLICATION = RITUAL.register("grove_supplication", GroveSupplicationRitual::new);
  public static final PropertyHolder<Property.IntegerProperty> GROVE_SUPPLICATION_DURATION = P.recordProperty("grove_supplication/duration", Property.ofInt(250, RitualProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> GROVE_SUPPLICATION_INTERVAL = P.recordProperty("grove_supplication/interval", Property.ofInt(210, RitualProperties.INTERVAL));
  public static final PropertyHolder<Property.IntegerProperty> GROVE_SUPPLICATION_RADIUS_XZ = P.recordProperty("grove_supplication/radius_xz", Property.ofInt(10, RitualProperties.RADIUS_XZ));
  public static final PropertyHolder<Property.IntegerProperty> GROVE_SUPPLICATION_RADIUS_Y = P.recordProperty("grove_supplication/radius_y", Property.ofInt(10, RitualProperties.RADIUS_Y));

  public static void register(IEventBus bus) {
    RITUAL.register(bus);
  }
}
