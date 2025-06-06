package mysticmods.roots.config;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.RitualInformation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = RootsAPI.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ConfigManager {

  private static final ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
  private static final ModConfigSpec.Builder CLIENT_BUILDER = new ModConfigSpec.Builder();

  public static List<AbstractConfig> CONFIGS = new ArrayList<>();

  public static ModConfigSpec.BooleanValue ALERTNESS_VISUAL;
  public static ModConfigSpec.IntValue ALERTNESS_DURATION;
  public static ModConfigSpec.BooleanValue ALERTNESS_SOUND;
  public static ModConfigSpec.BooleanValue ALERTNESS_TAG;

  public static ModConfigSpec.BooleanValue EXPERIENCE_ORBS;
  public static ModConfigSpec.BooleanValue PACIFIST_DISABLED;
  public static ModConfigSpec.IntValue AOE_BOUNDING_BOX_X;
  public static ModConfigSpec.IntValue AOE_BOUNDING_BOX_Y;
  public static ModConfigSpec.IntValue AOE_BOUNDING_BOX_Z;

  public static ModConfigSpec.IntValue PYRE_BOUNDS_X;
  public static ModConfigSpec.IntValue PYRE_BOUNDS_Y;
  public static ModConfigSpec.IntValue PYRE_BOUNDS_Z;

  public static ModConfigSpec.IntValue RANK_1_GROVE_BOUNDS_ZX;
  public static ModConfigSpec.IntValue RANK_1_GROVE_BOUNDS_Y;

  public static ModConfigSpec.IntValue RANK_2_GROVE_BOUNDS_ZX;
  public static ModConfigSpec.IntValue RANK_2_GROVE_BOUNDS_Y;

  public static ModConfigSpec.IntValue RANK_3_GROVE_BOUNDS_ZX;
  public static ModConfigSpec.IntValue RANK_3_GROVE_BOUNDS_Y;

  public static ModConfigSpec.IntValue RANK_4_GROVE_BOUNDS_ZX;
  public static ModConfigSpec.IntValue RANK_4_GROVE_BOUNDS_Y;

  public static ModConfigSpec.IntValue GROVE_STONE_POWER_RANGE_X;
  public static ModConfigSpec.IntValue GROVE_STONE_POWER_RANGE_Y;
  public static ModConfigSpec.IntValue GROVE_STONE_POWER_RANGE_Z;

  public static ModConfigSpec.BooleanValue DROP_AUBERGINE_SEEDS;
  public static ModConfigSpec.BooleanValue DROP_WILDROOT;
  public static ModConfigSpec.BooleanValue DROP_GROVE_SPORES;

  public static ModConfigSpec.IntValue SPROUT_BREEDING_REWARDS_DEFAULT_CHANCE;

  public static ModConfigSpec.BooleanValue DEBUG_REPUTATION;
  public static ModConfigSpec.BooleanValue SUPPRESS_REPUTATION_CHANGES;

  public static ModConfigSpec.EnumValue<RitualInformation.RitualResolutionType> RITUAL_RESOLUTION_TYPE;

  public static ModConfigSpec.BooleanValue DELAYED_PARTICLES;


  public static ModConfigSpec COMMON_CONFIG;
  public static ModConfigSpec CLIENT_CONFIG;

  static {
    COMMON_BUILDER.comment("magnetism-related configuration").push("magnetism");
    EXPERIENCE_ORBS = COMMON_BUILDER.comment("whether or not experience orbs should be teleported when using magnetism")
        .define("move_experience_orbs", true);
    COMMON_BUILDER.pop();
    COMMON_BUILDER.comment("Reputation-related configurations").push("reputation_config");
    PACIFIST_DISABLED = COMMON_BUILDER.comment("whether or not the Untrue Pacifist advancement is granted or utilized")
        .define("pacifist_disabled", false);
    COMMON_BUILDER.pop();
    COMMON_BUILDER.comment("Runic Shears configuration options").push("runic_shears");
    AOE_BOUNDING_BOX_X = COMMON_BUILDER.comment("the X half value for the size of the Runic Shears aoe bounding box")
        .defineInRange("aoe_bounding_box_x", 3, 1, Integer.MAX_VALUE);
    AOE_BOUNDING_BOX_Y = COMMON_BUILDER.comment("the Y half value for the size of the Runic Shears aoe bounding box")
        .defineInRange("aoe_bounding_box_y", 3, 1, Integer.MAX_VALUE);
    AOE_BOUNDING_BOX_Z = COMMON_BUILDER.comment("the Z half value for the size of the Runic Shears aoe bounding box")
        .defineInRange("aoe_bounding_box_z", 3, 1, Integer.MAX_VALUE);
    COMMON_BUILDER.pop();
    COMMON_BUILDER.comment("Options for Pyres and Rituals").push("pyre");
    PYRE_BOUNDS_X = COMMON_BUILDER.comment("the X half value for the size of the Pyre aoe bounding box")
        .defineInRange("pyre_bounds_x", 10, 1, Integer.MAX_VALUE);
    PYRE_BOUNDS_Y = COMMON_BUILDER.comment("the Y half value for the size of the Pyre aoe bounding box")
        .defineInRange("pyre_bounds_y", 10, 1, Integer.MAX_VALUE);
    PYRE_BOUNDS_Z = COMMON_BUILDER.comment("the Z half value for the size of the Pyre aoe bounding box")
        .defineInRange("pyre_bounds_z", 10, 1, Integer.MAX_VALUE);
    COMMON_BUILDER.pop();
    COMMON_BUILDER.comment("Options for Grove Stones").push("grove_stones");
    RANK_1_GROVE_BOUNDS_ZX = COMMON_BUILDER.comment("the X and Z half value for the size of the Rank 1 Grove Stone aoe bounding box")
        .defineInRange("rank_1_grove_bounds_zx", 3, 1, Integer.MAX_VALUE);
    RANK_1_GROVE_BOUNDS_Y = COMMON_BUILDER.comment("the Y half value for the size of the Rank 1 Grove Stone aoe bounding box")
        .defineInRange("rank_1_grove_bounds_y", 3, 1, Integer.MAX_VALUE);
    RANK_2_GROVE_BOUNDS_ZX = COMMON_BUILDER.comment("the X and Z half value for the size of the Rank 2 Grove Stone aoe bounding box")
        .defineInRange("rank_2_grove_bounds_zx", 8, 1, Integer.MAX_VALUE);
    RANK_2_GROVE_BOUNDS_Y = COMMON_BUILDER.comment("the Y half value for the size of the Rank 2 Grove Stone aoe bounding box")
        .defineInRange("rank_2_grove_bounds_y", 8, 1, Integer.MAX_VALUE);
    RANK_3_GROVE_BOUNDS_ZX = COMMON_BUILDER.comment("the X and Z half value for the size of the Rank 3 Grove Stone aoe bounding box")
        .defineInRange("rank_3_grove_bounds_zx", 15, 1, Integer.MAX_VALUE);
    RANK_3_GROVE_BOUNDS_Y = COMMON_BUILDER.comment("the Y half value for the size of the Rank 3 Grove Stone aoe bounding box")
        .defineInRange("rank_3_grove_bounds_y", 15, 1, Integer.MAX_VALUE);
    RANK_4_GROVE_BOUNDS_ZX = COMMON_BUILDER.comment("the X and Z half value for the size of the Rank 4 Grove Stone aoe bounding box")
        .defineInRange("rank_4_grove_bounds_zx", 30, 1, Integer.MAX_VALUE);
    RANK_4_GROVE_BOUNDS_Y = COMMON_BUILDER.comment("the Y half value for the size of the Rank 4 Grove Stone aoe bounding box")
        .defineInRange("rank_4_grove_bounds_y", 30, 1, Integer.MAX_VALUE);
    GROVE_STONE_POWER_RANGE_X = COMMON_BUILDER.comment("the X half value for the size of the Grove Stone power range")
        .defineInRange("grove_stone_power_range_x", 30, 1, Integer.MAX_VALUE);
    GROVE_STONE_POWER_RANGE_Y = COMMON_BUILDER.comment("the Y half value for the size of the Grove Stone power range")
        .defineInRange("grove_stone_power_range_y", 30, 1, Integer.MAX_VALUE);
    GROVE_STONE_POWER_RANGE_Z = COMMON_BUILDER.comment("the Z half value for the size of the Grove Stone power range")
        .defineInRange("grove_stone_power_range_z", 30, 1, Integer.MAX_VALUE);

    COMMON_BUILDER.comment("Options for sprout breeding rewards").push("sprouts");
    SPROUT_BREEDING_REWARDS_DEFAULT_CHANCE = COMMON_BUILDER.comment("the default chance for a sprout breeding reward to be given when not contained within the sprout breeding rewards data map (only applies to items within the roots:sprout_breeding_rewards tag)")
        .defineInRange("default_chance", 10, 1, 100);
    COMMON_BUILDER.pop();
    COMMON_BUILDER.comment("Options for the Alertness charm").push("alertness");
    ALERTNESS_VISUAL = COMMON_BUILDER.comment("whether or not the Alertness charm should display a visual effect")
        .define("visual", true);
    ALERTNESS_DURATION = COMMON_BUILDER.comment("the duration of the Alertness charm effect in ticks")
        .defineInRange("duration", 200, 1, Integer.MAX_VALUE);
    ALERTNESS_SOUND = COMMON_BUILDER.comment("whether or not the Alertness charm should play a sound when activated")
        .define("sound", true);
    ALERTNESS_TAG = COMMON_BUILDER.comment("whether or not entities should be filtered to those in the roots:alertness entity tag")
        .define("tag", false);
    COMMON_BUILDER.pop();
    COMMON_BUILDER.push("debug");
    DEBUG_REPUTATION = COMMON_BUILDER.comment("if true, will send messages for all reputation gains and losses")
        .define("debug_reputation", false);
    COMMON_BUILDER.pop();
    COMMON_BUILDER.push("ritual_conflict");
    RITUAL_RESOLUTION_TYPE = COMMON_BUILDER.comment("how rituals (heavy storms, protection) will be resolved on the server: [protection_priority=the protection ritual will always suppress the weather effects of heavy storms, storm_priority=the heavy storms ritual will always change the weather even with protection running, age_priority=the ritual started earliest will have priority]").defineEnum("ritual_resolution_type", RitualInformation.RitualResolutionType.AGE_PRIORITY);
    COMMON_BUILDER.pop();
    COMMON_BUILDER.push("grass_drops");
    DROP_AUBERGINE_SEEDS = COMMON_BUILDER.comment("whether or not aubergine seeds should drop from grass")
        .define("drop_aubergine_seeds", true);
    DROP_WILDROOT = COMMON_BUILDER.comment("whether or not wildroot should drop from grass")
        .define("drop_wildroot", true);
    DROP_GROVE_SPORES = COMMON_BUILDER.comment("whether or not grove spores should drop from grass")
        .define("drop_grove_spores", true);
    COMMON_BUILDER.pop();

    CLIENT_BUILDER.push("debug");
    SUPPRESS_REPUTATION_CHANGES = CLIENT_BUILDER.comment("if true, will suppress all reputation changes from being display on the client")
        .define("suppress_reputation_changes", true);
    CLIENT_BUILDER.pop();
    CLIENT_BUILDER.push("particles");
    DELAYED_PARTICLES = CLIENT_BUILDER.comment("if true, translucent particles will be rendered with quads sorted to improve rendering and prevent overlapping translucent particles from disappearing")
        .define("delayed_particles", true);
    CLIENT_BUILDER.pop();

    COMMON_CONFIG = COMMON_BUILDER.build();
    CLIENT_CONFIG = CLIENT_BUILDER.build();
  }

  @SubscribeEvent
  public static void onConfigReload(ModConfigEvent.Reloading event) {
    configReload(event);
  }

  @SubscribeEvent
  public static void onConfigLoaded(ModConfigEvent.Loading event) {
    configReload(event);
  }

  public static void configReload(ModConfigEvent event) {
    CONFIGS.forEach(AbstractConfig::reset);
  }
}
