package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.reference.SpellCosts;
import mysticmods.roots.api.reference.SpellProperties;
import mysticmods.roots.api.reference.Spells;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.spell.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;


public class ModSpells {
  private static final DeferredRegister<Spell> REGISTER = DeferredRegister.create(RootsRegistries.Keys.SPELLS, RootsAPI.MODID);

  // Acid Cloud (20 cooldown)
  public static final DeferredHolder<Spell, AcidCloudSpell> ACID_CLOUD = spell(Spells.ACID_CLOUD, AcidCloudSpell::new, ChatFormatting.GREEN, () -> CostInstance.of(CostInstance.ChargeType.CAST, List.of(Cost.add(ModHerbs.BAFFLECAP, SpellCosts.BASE_0250), Cost.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0250))));
  public static final PropertyHolder<Property.IntegerProperty> ACID_CLOUD_COOLDOWN = P.recordProperty("acid_cloud/cooldown", Property.ofInt(20, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.IntegerProperty> ACID_CLOUD_RADIUS_ZX = P.recordProperty("acid_cloud/radius_zx", Property.ofInt(2, "Radius"));
  public static final PropertyHolder<Property.IntegerProperty> ACID_CLOUD_RADIUS_Y = P.recordProperty("acid_cloud/radius_y", Property.ofInt(2, "Radius"));
  public static final PropertyHolder<Property.FloatProperty> ACID_CLOUD_DAMAGE = P.recordProperty("acid_cloud/damage", Property.ofFloat(2.0f, SpellProperties.DAMAGE));
  public static final PropertyHolder<Property.IntegerProperty> ACID_CLOUD_COUNT = P.recordProperty("acid_cloud/count", Property.ofInt(1, SpellProperties.COUNT));

  // Aqua Bubble (1200 cooldown)
  public static final DeferredHolder<Spell, AquaBubbleSpell> AQUA_BUBBLE = spell(Spells.AQUA_BUBBLE, AquaBubbleSpell::new, ChatFormatting.AQUA, () -> CostInstance.of(CostInstance.ChargeType.CAST, List.of(Cost.add(ModHerbs.DEWGONIA, SpellCosts.BASE_0250))));
  public static final PropertyHolder<Property.IntegerProperty> AQUA_BUBBLE_COOLDOWN = P.recordProperty("aqua_bubble/cooldown", Property.ofInt(1200, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.IntegerProperty> AQUA_BUBBLE_DURATION = P.recordProperty("aqua_bubble/duration", Property.ofInt(20 * 90, SpellProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> AQUA_BUBBLE_ABSORPTION = P.recordProperty("aqua_bubble/absorption", Property.ofInt(4, "The amount of damage absorbed by the aqua bubble, 1 equals half a heart."));
  public static final PropertyHolder<Property.FloatProperty> AQUA_BUBBLE_FIRE_REDUCTION = P.recordProperty("aqua_bubble/fire_reduction", Property.ofFloat(0.6f, "The percentage of fire damage reduced by the aqua bubble."));
  public static final PropertyHolder<Property.FloatProperty> AQUA_BUBBLE_LAVA_REDUCTION = P.recordProperty("aqua_bubble/lava_reduction", Property.ofFloat(0.6f, "The percentage of lava damage reduced by the aqua bubble."));

  // Light Drifter (600 cooldown)
  public static final DeferredHolder<Spell, LightDrifterSpell> LIGHT_DRIFTER = spell(Spells.LIGHT_DRIFTER, LightDrifterSpell::new, ChatFormatting.DARK_PURPLE, () -> CostInstance.of(CostInstance.ChargeType.CAST, List.of(Cost.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0250))));
  public static final PropertyHolder<Property.IntegerProperty> LIGHT_DRIFTER_COOLDOWN = P.recordProperty("light_drifter/cooldown", Property.ofInt(600, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.IntegerProperty> LIGHT_DRIFTER_DURATION = P.recordProperty("light_drifter/duration", Property.ofInt(20 * 30, SpellProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> LIGHT_DRIFTER_DISTANCE = P.recordProperty("light_drifter/distance", Property.ofInt(Mth.square(50), "The maximum distance from the player that the light drifter can travel before being recalled."));

  // Magnetism (350 cooldown)
  public static final DeferredHolder<Spell, MagnetismSpell> MAGNETISM = spell(Spells.MAGNETISM, MagnetismSpell::new, ChatFormatting.YELLOW, () -> CostInstance.of(CostInstance.ChargeType.OPERATION, List.of(Cost.add(ModHerbs.SPIRITLEAF, SpellCosts.BASE_0016))));
  public static final PropertyHolder<Property.IntegerProperty> MAGNETISM_COOLDOWN = P.recordProperty("magnetism/cooldown", Property.ofInt(5, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.IntegerProperty> MAGNETISM_RADIUS_ZX = P.recordProperty("magnetism/radius_zx", Property.ofInt(20, SpellProperties.RADIUS_ZX));
  public static final PropertyHolder<Property.IntegerProperty> MAGNETISM_RADIUS_Y = P.recordProperty("magnetism/radius_y", Property.ofInt(10, SpellProperties.RADIUS_Y));

  // Dandelion Winds (20 cooldown)
  public static final DeferredHolder<Spell, DandelionWindsSpell> DANDELION_WINDS = spell(Spells.DANDELION_WINDS, DandelionWindsSpell::new, ChatFormatting.YELLOW, () -> CostInstance.of(CostInstance.ChargeType.CAST, List.of(Cost.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0250), Cost.add(ModHerbs.GROVE_MOSS, SpellCosts.BASE_0125))));
  public static final PropertyHolder<Property.IntegerProperty> DANDELION_WINDS_COOLDOWN = P.recordProperty("dandelion_winds/cooldown", Property.ofInt(20, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.FloatProperty> DANDELION_WINDS_DISTANCE = P.recordProperty("dandelion_winds/distance", Property.ofFloat(0.75f, "The vertical component of the vector used to move entities."));
  public static final PropertyHolder<Property.DoubleProperty> DANDELION_WINDS_RANGE_1 = P.recordProperty("dandelion_winds/range_1", Property.ofDouble(4.0, "The first range increment for calculating the bounding box from the player."));
  public static final PropertyHolder<Property.DoubleProperty> DANDELION_WINDS_RANGE_2 = P.recordProperty("dandelion_winds/range_2", Property.ofDouble(5.0, "The second range increment for calculating the bounding box from the player."));
  public static final PropertyHolder<Property.FloatProperty> DANDELION_WINDS_VERTICAL = P.recordProperty("dandelion_winds/vertical", Property.ofFloat(0.7f, "The percentage of the movement value applied to vertical momentum."));

  // Decay
  public static final DeferredHolder<Spell, DecaySpell> DECAY = spell(Spells.DECAY, DecaySpell::new, ChatFormatting.DARK_GREEN, () -> CostInstance.of(CostInstance.ChargeType.CAST, List.of(Cost.add(ModHerbs.STALICRIPE, SpellCosts.BASE_0250), Cost.add(ModHerbs.WILDEWHEET, SpellCosts.BASE_0250))));
  public static final PropertyHolder<Property.IntegerProperty> DECAY_COOLDOWN = P.recordProperty("decay/cooldown", Property.ofInt(20, SpellProperties.COOLDOWN));

  // Desaturate (500 cooldown)
  public static final DeferredHolder<Spell, DesaturateSpell> DESATURATE = spell(Spells.DESATURATE, DesaturateSpell::new, ChatFormatting.GREEN, () -> CostInstance.of(CostInstance.ChargeType.CAST, List.of(Cost.add(ModHerbs.WILDEWHEET, SpellCosts.BASE_0250), Cost.add(ModHerbs.GROVE_MOSS, SpellCosts.BASE_0250))));
  public static final PropertyHolder<Property.IntegerProperty> DESATURATE_COOLDOWN = P.recordProperty("desaturate/cooldown", Property.ofInt(500, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.FloatProperty> DESATURATE_MULTIPLIER = P.recordProperty("desaturate/multiplier", Property.ofFloat(0.7f, "Amount of health restored per point of food"));

  // Saturate

  public static final DeferredHolder<Spell, SaturateSpell> SATURATE = spell(Spells.SATURATE, SaturateSpell::new, ChatFormatting.DARK_GREEN, () -> CostInstance.of(CostInstance.ChargeType.CAST, List.of(Cost.add(ModHerbs.WILDEWHEET, SpellCosts.BASE_0250), Cost.add(ModHerbs.WILDROOT, SpellCosts.BASE_0250))));
  public static final PropertyHolder<Property.IntegerProperty> SATURATE_COOLDOWN = P.recordProperty("saturate/cooldown", Property.ofInt(500, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.FloatProperty> SATURATE_SATURATION_MULTIPLIER = P.recordProperty("saturate/saturation_multiplier", Property.ofFloat(0.5f, "Amount of saturation give per point of saturation."));
  public static final PropertyHolder<Property.FloatProperty> SATURATE_FOOD_MULTIPLIER = P.recordProperty("saturate/food_multiplier", Property.ofFloat(0.5f, "Amount of food restored per point of food."));

  // Disarm spell (350 cooldown)
  public static final DeferredHolder<Spell, DisarmSpell> DISARM = spell(Spells.DISARM, DisarmSpell::new, ChatFormatting.AQUA, () -> CostInstance.of(CostInstance.ChargeType.OPERATION, List.of(Cost.add(ModHerbs.SPIRITLEAF, SpellCosts.BASE_0125), Cost.add(ModHerbs.BAFFLECAP, SpellCosts.BASE_0125))));
  public static final PropertyHolder<Property.IntegerProperty> DISARM_COOLDOWN = P.recordProperty("disarm/cooldown", Property.ofInt(60, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.IntegerProperty> DISARM_RADIUS_ZX = P.recordProperty("disarm/radius_zx", Property.ofInt(9, SpellProperties.RADIUS_ZX));
  public static final PropertyHolder<Property.IntegerProperty> DISARM_RADIUS_Y = P.recordProperty("disarm/radius_y", Property.ofInt(9, SpellProperties.RADIUS_Y));
  public static final PropertyHolder<Property.FloatProperty> DISARM_DROP_CHANCE = P.recordProperty("disarm/drop_chance", Property.ofFloat(0.35f, "Percentage change for an entity's dropped item to spawn in the world instead of being destroyed."));
  public static final PropertyHolder<Property.IntegerProperty> DISARM_GLOW_DURATION = P.recordProperty("disarm/glow_duration", Property.ofInt(20 * 5, "The duration of the glow effect in ticks on entities that are disarmed."));
  public static final PropertyHolder<Property.IntegerProperty> DISARM_COUNT = P.recordProperty("disarm/count", Property.ofInt(2, "The number of entities that can be disarmed per cast."));

  // Long night vision & sense danger
  // Extension spell (350 cooldown)
  public static final DeferredHolder<Spell, ExtensionSpell> EXTENSION = spell(Spells.EXTENSION, ExtensionSpell::new, ChatFormatting.BLUE, () -> CostInstance.of(CostInstance.ChargeType.CAST, List.of(Cost.add(ModHerbs.GROVE_MOSS, SpellCosts.COMPLEX_1750), Cost.add(ModHerbs.WILDROOT, SpellCosts.COMPLEX_1750))));
  public static final PropertyHolder<Property.IntegerProperty> EXTENSION_COOLDOWN = P.recordProperty("extension/cooldown", Property.ofInt(350, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.IntegerProperty> EXTENSION_NIGHT_VISION_DURATION = P.recordProperty("extension/night_vision_duration", Property.ofInt(4 * 60 * 20, "The duration of the night vision effect in ticks."));
  public static final PropertyHolder<Property.IntegerProperty> EXTENSION_SENSE_DANGER_DURATION = P.recordProperty("extension/sense_danger_duration", Property.ofInt(4 * 60 * 20, "The duration of the sense danger effect in ticks."));
  public static final PropertyHolder<Property.IntegerProperty> EXTENSION_RADIUS_ZX = P.recordProperty("extension/radius_zx", Property.ofInt(40, SpellProperties.RADIUS_ZX));
  public static final PropertyHolder<Property.IntegerProperty> EXTENSION_RADIUS_Y = P.recordProperty("extension/radius_y", Property.ofInt(20, SpellProperties.RADIUS_Y));

  // Nondetection (350 cooldown)
  public static final DeferredHolder<Spell, NondetectionSpell> NONDETECTION = spell(Spells.NONDETECTION, NondetectionSpell::new, ChatFormatting.DARK_AQUA, () -> CostInstance.of(CostInstance.ChargeType.CAST, List.of(Cost.add(ModHerbs.SPIRITLEAF, SpellCosts.BASE_0250), Cost.add(ModHerbs.DEWGONIA, SpellCosts.BASE_0250))));
  public static final PropertyHolder<Property.IntegerProperty> NONDETECTION_COOLDOWN = P.recordProperty("nondetection/cooldown", Property.ofInt(350, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.IntegerProperty> NONDETECTION_DURATION = P.recordProperty("nondetection/duration", Property.ofInt(45 * 60, "The duration of the nondetection effect in ticks."));

  // Sylvan Light (20 cooldown)
  public static final DeferredHolder<Spell, SylvanLightSpell> SYLVAN_LIGHT = spell(Spells.SYLVAN_LIGHT, SylvanLightSpell::new, ChatFormatting.LIGHT_PURPLE, () -> CostInstance.of(CostInstance.ChargeType.CAST, List.of(Cost.add(ModHerbs.GROVE_MOSS, 0.0625), Cost.add(ModHerbs.PERESKIA, 0.0625))));

  static {
    REGISTER.addAlias(RootsAPI.rl("fey_light"), RootsAPI.rl("sylvan_light"));
  }

  public static final PropertyHolder<Property.IntegerProperty> SYLVAN_LIGHT_COOLDOWN = P.recordProperty("sylvan_light/cooldown", Property.ofInt(20, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.DoubleProperty> SYLVAN_LIGHT_MAX_DISTANCE = P.recordProperty("sylvan_light/max_distance", Property.ofDouble(10.0, "The maximum distance a sylvan light can be placed from the caster"));

  // Geas (80 cooldown)
  public static final DeferredHolder<Spell, GeasSpell> GEAS = spell(Spells.GEAS, GeasSpell::new, ChatFormatting.RED, () -> CostInstance.of(CostInstance.ChargeType.CAST, List.of(Cost.add(ModHerbs.BAFFLECAP, SpellCosts.BASE_0250), Cost.add(ModHerbs.GROVE_MOSS, SpellCosts.BASE_0250))));
  public static final PropertyHolder<Property.IntegerProperty> GEAS_COOLDOWN = P.recordProperty("geas/cooldown", Property.ofInt(20, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.IntegerProperty> GEAS_MAX_COOLDOWN = P.recordProperty("geas/max_cooldown", Property.ofInt(80 * 10, "The maximum cooldown for the geas spell when scaling based off maximum health."));
  public static final PropertyHolder<Property.IntegerProperty> GEAS_DURATION = P.recordProperty("geas/duration", Property.ofInt(400, SpellProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> GEAS_COUNT = P.recordProperty("geas/count", Property.ofInt(1, SpellProperties.COUNT));
  public static final PropertyHolder<Property.DoubleProperty> GEAS_MAX_HEALTH = P.recordProperty("geas/max_health", Property.ofDouble(50, "The maximum health an entity can have for the geas spell to affect it."));

  static {
    REGISTER.addAlias(RootsAPI.rl("control_undead"), RootsAPI.rl("summon_undead"));
  }

  // Control Undead (320 cooldown)
  public static final DeferredHolder<Spell, EnslaveUndeadSpell> SUMMON_UNDEAD = spell(Spells.SUMMON_UNDEAD, EnslaveUndeadSpell::new, ChatFormatting.DARK_GREEN, () -> CostInstance.of(CostInstance.ChargeType.CAST, List.of(Cost.add(ModHerbs.BAFFLECAP, SpellCosts.BASE_0250), Cost.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0250))));
  public static final PropertyHolder<Property.IntegerProperty> SUMMON_UNDEAD_COOLDOWN = P.recordProperty("summon_undead/cooldown", Property.ofInt(320, SpellProperties.COOLDOWN));


  // Growth Infusion (20 cooldown)
  public static final DeferredHolder<Spell, GrowthInfusionSpell> GROWTH_INFUSION = spell(Spells.GROWTH_INFUSION, GrowthInfusionSpell::new, ChatFormatting.YELLOW, () -> CostInstance.of(CostInstance.ChargeType.CAST, List.of(Cost.add(ModHerbs.GROVE_MOSS, SpellCosts.BASE_0250))));
  public static final PropertyHolder<Property.IntegerProperty> GROWTH_INFUSION_COOLDOWN = P.recordProperty("growth_infusion/cooldown", Property.ofInt(20, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.DoubleProperty> GROWTH_INFUSION_ADDED_REACH = P.recordProperty("growth_infusion/added_reach", Property.ofDouble(0.0, SpellProperties.ADDED_REACH));

  // Rampant Growth (20 cooldown)
  public static final DeferredHolder<Spell, RampantGrowthSpell> RAMPANT_GROWTH = spell(Spells.RAMPANT_GROWTH, RampantGrowthSpell::new, ChatFormatting.YELLOW, () -> CostInstance.of(CostInstance.ChargeType.OPERATION, List.of(Cost.add(ModHerbs.WILDEWHEET, SpellCosts.BASE_0125), Cost.add(ModHerbs.GROVE_MOSS, SpellCosts.BASE_0031)))); // In theory it should cost 16 wildewheet to grow 20 blocks to full at this rate
  public static final PropertyHolder<Property.IntegerProperty> RAMPANT_GROWTH_COOLDOWN = P.recordProperty("rampant_growth/cooldown", Property.ofInt(0, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.IntegerProperty> RAMPANT_GROWTH_RADIUS_ZX = P.recordProperty("rampant_growth/radius_zx", Property.ofInt(5, SpellProperties.RADIUS_ZX));
  public static final PropertyHolder<Property.IntegerProperty> RAMPANT_GROWTH_RADIUS_Y = P.recordProperty("rampant_growth/radius_y", Property.ofInt(5, SpellProperties.RADIUS_Y));
  public static final PropertyHolder<Property.IntegerProperty> RAMPANT_GROWTH_INTERVAL = P.recordProperty("rampant_growth/interval", Property.ofInt(2, "The interval between growth ticks in ticks."));
  public static final PropertyHolder<Property.IntegerProperty> RAMPANT_GROWTH_COUNT = P.recordProperty("rampant_growth/count", Property.ofInt(9, SpellProperties.COUNT));


  // Harvest (25 cooldown)
  public static final DeferredHolder<Spell, HarvestSpell> HARVEST = spell(Spells.HARVEST, HarvestSpell::new, ChatFormatting.YELLOW, () -> CostInstance.of(CostInstance.ChargeType.OPERATION, List.of(Cost.add(ModHerbs.WILDEWHEET, SpellCosts.BASE_0031), Cost.add(ModHerbs.STALICRIPE, SpellCosts.BASE_0031))));
  public static final PropertyHolder<Property.IntegerProperty> HARVEST_COOLDOWN = P.recordProperty("harvest/cooldown", Property.ofInt(20, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.IntegerProperty> HARVEST_RADIUS_ZX = P.recordProperty("harvest/radius_zx", Property.ofInt(5, SpellProperties.RADIUS_ZX));
  public static final PropertyHolder<Property.IntegerProperty> HARVEST_RADIUS_Y = P.recordProperty("harvest/radius_y", Property.ofInt(5, SpellProperties.RADIUS_Y));

  // Life Drain (20 cooldown)
  public static final DeferredHolder<Spell, LifeDrainSpell> LIFE_DRAIN = spell(Spells.LIFE_DRAIN, LifeDrainSpell::new, ChatFormatting.DARK_PURPLE, () -> CostInstance.of(CostInstance.ChargeType.CAST, List.of(Cost.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0125), Cost.add(ModHerbs.STALICRIPE, SpellCosts.BASE_0125))));
  public static final PropertyHolder<Property.IntegerProperty> LIFE_DRAIN_COOLDOWN = P.recordProperty("life_drain/cooldown", Property.ofInt(0, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.DoubleProperty> LIFE_DRAIN_DISTANCE = P.recordProperty("life_drain/distance", Property.ofDouble(8.0, "The range in blocks for the life drain search."));
  public static final PropertyHolder<Property.IntegerProperty> LIFE_DRAIN_ANGLE = P.recordProperty("life_drain/angle", Property.ofInt(80, "The angle in degrees for the life drain search, centered on the player's view vector."));
  public static final PropertyHolder<Property.FloatProperty> LIFE_DRAIN_DAMAGE = P.recordProperty("life_drain/damage", Property.ofFloat(3.0f, SpellProperties.DAMAGE));
  public static final PropertyHolder<Property.FloatProperty> LIFE_DRAIN_HEAL = P.recordProperty("life_drain/heal", Property.ofFloat(0.5f, "The amount a player should be healed for each entity damaged."));

  // Petal Shell (120 cooldown)
  public static final DeferredHolder<Spell, PetalShellSpell> PETAL_SHELL = spell(Spells.PETAL_SHELL, PetalShellSpell::new, ChatFormatting.LIGHT_PURPLE, () -> CostInstance.of(CostInstance.ChargeType.CAST, List.of(Cost.add(ModHerbs.PERESKIA, SpellCosts.BASE_0250))));
  public static final PropertyHolder<Property.IntegerProperty> PETAL_SHELL_COOLDOWN = P.recordProperty("petal_shell/cooldown", Property.ofInt(120, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.IntegerProperty> PETAL_SHELL_DURATION = P.recordProperty("petal_shell/duration", Property.ofInt(20 * 90, SpellProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> PETAL_SHELL_COUNT = P.recordProperty("petal_shell/count", Property.ofInt(3, "The number of petal shells."));

  // Radiance (20 cooldown)
  public static final DeferredHolder<Spell, RadianceSpell> RADIANCE = spell(Spells.RADIANCE, RadianceSpell::new, ChatFormatting.GOLD, () -> CostInstance.of(CostInstance.ChargeType.CAST, List.of(Cost.add(ModHerbs.INFERNO_BULB, SpellCosts.BASE_0250), Cost.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0250))));
  public static final PropertyHolder<Property.IntegerProperty> RADIANCE_COOLDOWN = P.recordProperty("radiance/cooldown", Property.ofInt(20, SpellProperties.COOLDOWN));

  // Rose Thorns (24 cooldown)
  public static final DeferredHolder<Spell, RoseThornsSpell> ROSE_THORNS = spell(Spells.ROSE_THORNS, RoseThornsSpell::new, ChatFormatting.RED, () -> CostInstance.of(CostInstance.ChargeType.CAST, List.of(Cost.add(ModHerbs.WILDROOT, SpellCosts.BASE_0250))));
  public static final PropertyHolder<Property.IntegerProperty> ROSE_THORNS_COOLDOWN = P.recordProperty("rose_thorns/cooldown", Property.ofInt(24, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.IntegerProperty> ROSE_THORNS_DURATION = P.recordProperty("rose_thorns/duration", Property.ofInt(20 * 18, SpellProperties.DURATION));
  public static final PropertyHolder<Property.DoubleProperty> ROSE_THORNS_RADIUS_ZX = P.recordProperty("rose_thorns/radius_zx", Property.ofDouble(1.2, "The radius of the rose thorns effect in the X and Z axis."));
  public static final PropertyHolder<Property.DoubleProperty> ROSE_THORNS_RADIUS_Y = P.recordProperty("rose_thorns/radius_y", Property.ofDouble(1, "The radius of the rose thorns effect in the Y axis."));
  public static final PropertyHolder<Property.FloatProperty> ROSE_THORNS_DAMAGE = P.recordProperty("rose_thorns/damage", Property.ofFloat(3.5f, SpellProperties.DAMAGE));

/*  // Sanctuary (20 cooldown)
  public static final DeferredHolder<Spell, SanctuarySpell> SANCTUARY = spell(Spells.SANCTUARY, SanctuarySpell::new, ChatFormatting.LIGHT_PURPLE, () -> CostInstance.of(CostInstance.ChargeType.OPERATION, List.of(Cost.add(ModHerbs.PERESKIA, SpellCosts.BASE_0125), Cost.add(ModHerbs.STALICRIPE, SpellCosts.BASE_0125)))); // Charges every time an entity is pushed back -- how much is this?
  public static final PropertyHolder<Property.IntegerProperty> SANCTUARY_COOLDOWN = P.recordProperty("sanctuary/cooldown", Property.ofInt(20, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.IntegerProperty> SANCTUARY_RADIUS_Y = P.recordProperty("sanctuary/radius_y", Property.ofInt(5, "The radius of the sanctuary in the Y axis."));
  public static final PropertyHolder<Property.IntegerProperty> SANCTUARY_RADIUS_XZ = P.recordProperty("sanctuary/radius_xz", Property.ofInt(4, "The radius of the sanctuary in the X and Z axis."));
  public static final PropertyHolder<Property.FloatProperty> SANCTUARY_VELOCITY = P.recordProperty("sanctuary/velocity", Property.ofFloat(0.125f, "The velocity modifier applied to entities inside the sanctuary."));*/

  // Shatter (20 cooldown)
  public static final DeferredHolder<Spell, ShatterSpell> SHATTER = spell(Spells.SHATTER, ShatterSpell::new, ChatFormatting.YELLOW, () -> CostInstance.of(CostInstance.ChargeType.OPERATION, List.of(Cost.add(ModHerbs.STALICRIPE, SpellCosts.BASE_0125)))); // Charges per block broken
  public static final PropertyHolder<Property.IntegerProperty> SHATTER_COOLDOWN = P.recordProperty("shatter/cooldown", Property.ofInt(5, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.IntegerProperty> SHATTER_MAXIMUM_WIDTH = P.recordProperty("shatter/maximum_width", Property.ofInt(2, "Half the maximum width of the shatter effect."));
  public static final PropertyHolder<Property.IntegerProperty> SHATTER_MAXIMUM_HEIGHT = P.recordProperty("shatter/maximum_height", Property.ofInt(2, "Half the maximum height of the shatter effect."));
  public static final PropertyHolder<Property.IntegerProperty> SHATTER_MAXIMUM_DEPTH = P.recordProperty("shatter/maximum_depth", Property.ofInt(2, "Half the maximum depth of the shatter effect."));

  // Jaunt (80 cooldown)
  public static final DeferredHolder<Spell, JauntSpell> JAUNT = spell(Spells.JAUNT, JauntSpell::new, ChatFormatting.DARK_PURPLE, () -> CostInstance.of(CostInstance.ChargeType.OPERATION, List.of(Cost.add(ModHerbs.PERESKIA, SpellCosts.BASE_0031), Cost.add(ModHerbs.SPIRITLEAF, SpellCosts.BASE_0031)))); // Charges per number of blocks moved based on charge
  public static final PropertyHolder<Property.IntegerProperty> JAUNT_COOLDOWN = P.recordProperty("jaunt/cooldown", Property.ofInt(80, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.IntegerProperty> JAUNT_DISTANCE = P.recordProperty("jaunt/distance", Property.ofInt(8, "The number of blocks that Jaunt travels forwards."));
  public static final PropertyHolder<Property.IntegerProperty> JAUNT_MAX_USE = P.recordProperty("jaunt/max_use", Property.ofInt(100, SpellProperties.MAX_USE));

  // Storm Cloud (100 cooldown)
  public static final DeferredHolder<Spell, StormCloudSpell> STORM_CLOUD = spell(Spells.STORM_CLOUD, StormCloudSpell::new, ChatFormatting.DARK_BLUE, () -> CostInstance.of(CostInstance.ChargeType.CAST, List.of(Cost.add(ModHerbs.DEWGONIA, SpellCosts.BASE_0250), Cost.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0250))));
  public static final PropertyHolder<Property.IntegerProperty> STORM_CLOUD_COOLDOWN = P.recordProperty("storm_cloud/cooldown", Property.ofInt(100, SpellProperties.COOLDOWN));

  // Sky Soarer (39 cooldown)
  public static final DeferredHolder<Spell, SkySoarerSpell> SKY_SOARER = spell(Spells.SKY_SOARER, SkySoarerSpell::new, ChatFormatting.BLUE, () -> CostInstance.of(CostInstance.ChargeType.CAST, List.of(Cost.add(ModHerbs.CLOUD_BERRY, SpellCosts.COMPLEX_1250))));
  public static final PropertyHolder<Property.IntegerProperty> SKY_SOARER_COOLDOWN = P.recordProperty("sky_soarer/cooldown", Property.ofInt(39, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.FloatProperty> SKY_SOARER_AMPLIFIER = P.recordProperty("sky_soarer/amplifier", Property.ofFloat(0.9f, "The default movement speed amplifier for Sky Soarer."));
  public static final PropertyHolder<Property.FloatProperty> SKY_SOARER_BOOSTED_AMPLIFIER = P.recordProperty("sky_soarer/boosted_amplifier", Property.ofFloat(0.6f, "The movement speed amplifier for Sky Soarer boosted."));
  public static final PropertyHolder<Property.IntegerProperty> SKY_SOARER_DURATION = P.recordProperty("sky_soarer/duration", Property.ofInt(50, SpellProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> SKY_SOARER_BOOSTED_DURATION = P.recordProperty("sky_soarer/boosted_duration", Property.ofInt(28, SpellProperties.EXTENDED_DURATION));

  // Temporal Morass (320 cooldown)
  public static final DeferredHolder<Spell, TemporalMorassSpell> TEMPORAL_MORASS = spell(Spells.TEMPORAL_MORASS, TemporalMorassSpell::new, ChatFormatting.DARK_BLUE, () -> CostInstance.of(CostInstance.ChargeType.CAST, List.of(Cost.add(ModHerbs.MOONGLOW, SpellCosts.BASE_0250), Cost.add(ModHerbs.STALICRIPE, SpellCosts.BASE_0250))));

  static {
    REGISTER.addAlias(RootsAPI.rl("time_stop"), RootsAPI.rl("temporal_morass"));
  }

  public static final PropertyHolder<Property.IntegerProperty> TEMPORAL_MORASS_COOLDOWN = P.recordProperty("temporal_morass/cooldown", Property.ofInt(320, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.IntegerProperty> TEMPORAL_MORASS_DURATION = P.recordProperty("temporal_morass/duration", Property.ofInt(300, SpellProperties.DURATION));
  public static final PropertyHolder<Property.IntegerProperty> TEMPORAL_MORASS_RADIUS_Y = P.recordProperty("temporal_morass/radius_y", Property.ofInt(5, SpellProperties.RADIUS_Y));
  public static final PropertyHolder<Property.IntegerProperty> TEMPORAL_MORASS_RADIUS_ZX = P.recordProperty("temporal_morass/radius_zx", Property.ofInt(5, SpellProperties.RADIUS_ZX));
  public static final PropertyHolder<Property.IntegerProperty> TEMPORAL_MORASS_AMPLIFIER = P.recordProperty("temporal_morass/amplifier", Property.ofInt(3, "The amplifier for the slowness effect applied by Temporal Morass."));

  // Wildfire (24 cooldown)
  public static final DeferredHolder<Spell, WildfireSpell> WILDFIRE = spell(Spells.WILDFIRE, WildfireSpell::new, ChatFormatting.DARK_RED, () -> CostInstance.of(CostInstance.ChargeType.CAST, List.of(Cost.add(ModHerbs.INFERNO_BULB, SpellCosts.BASE_0250))));
  public static final PropertyHolder<Property.FloatProperty> WILDFIRE_VELOCITY = P.recordProperty("wildfire/velocity", Property.ofFloat(3.2f, "The velocity of the wildfire projectile."));
  public static final PropertyHolder<Property.IntegerProperty> WILDFIRE_COOLDOWN = P.recordProperty("wildfire/cooldown", Property.ofInt(45, SpellProperties.COOLDOWN));
  public static final PropertyHolder<Property.FloatProperty> WILDFIRE_DAMAGE = P.recordProperty("wildfire/damage", Property.ofFloat(4.5f, SpellProperties.DAMAGE));

  private static <T extends Spell> DeferredHolder<Spell, T> spell(ResourceKey<Spell> key, SpellConstructor<T> consturctor, ChatFormatting color, Supplier<CostInstance> costs) {
    return REGISTER.register(key.location().getPath(), spellBuilder(consturctor, color, costs));
  }

  private static <T extends Spell> Supplier<T> spellBuilder(SpellConstructor<T> constructor, ChatFormatting color, Supplier<CostInstance> costs) {
    return () -> constructor.create(color, costs.get());
  }

  private interface SpellConstructor<T extends Spell> {
    T create(ChatFormatting color, CostInstance costs);
  }

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
  }
}
