package mysticmods.roots.api;

import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.ritual.RitualModifier;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellModifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;


// Tags
public class RootsTags {
  public static class Blocks {
    // Crops & Adjacent
    // Forge compat tag (filled by ModTags)

    public static final TagKey<Block> STANDING_STONE_CROPS = modTag("standing_stone_crops");

    // General crops (filled by ModTags)
    public static final TagKey<Block> CROPS = modTag("crops");
    public static final TagKey<Block> ELEMENTAL_CROPS = modTag("crops/elemental");

    // Specific crop types (filled in by ModTags)
    public static final TagKey<Block> WATER_CROPS = modTag("crops/elemental/water");
    public static final TagKey<Block> EARTH_CROPS = modTag("crops/elemental/earth");
    public static final TagKey<Block> AIR_CROPS = modTag("crops/elemental/air");
    public static final TagKey<Block> FIRE_CROPS = modTag("crops/elemental/fire");

    // Specific crops (filled in by ModBlocks)
    public static final TagKey<Block> CLOUD_BERRY_CROP = modTag("crops/cloud_berry");
    public static final TagKey<Block> DEWGONIA_CROP = modTag("crops/dewgonia");
    public static final TagKey<Block> SPIRITLEAF_CROP = modTag("crops/spiritleaf");
    public static final TagKey<Block> STALICRIPE_CROP = modTag("crops/stalicripe");
    public static final TagKey<Block> WILDEWHEET_CROP = modTag("crops/wildewheet");
    public static final TagKey<Block> WILDROOT_CROP = modTag("crops/wildroot");
    public static final TagKey<Block> INFERNO_BULB_CROP = modTag("crops/inferno_bulb");
    public static final TagKey<Block> MOONGLOW_CROP = modTag("crops/moonglow");
    public static final TagKey<Block> PERESKIA_CROP = modTag("crops/pereskia");

    // General soils (filled in by ModTags)
    public static final TagKey<Block> SOILS = modTag("soils");

    // Blocks that can be put on soils
    public static final TagKey<Block> SOIL_ELIGIBLE_CROPS = modTag("soil_eligible_crops");

    // Specific soils
    public static final TagKey<Block> WATER_SOIL = modTag("soils/water");
    public static final TagKey<Block> AIR_SOIL = modTag("soils/air");
    public static final TagKey<Block> EARTH_SOIL = modTag("soils/earth");
    public static final TagKey<Block> FIRE_SOIL = modTag("soils/fire");
    public static final TagKey<Block> BASE_ELEMENTAL_SOIL = modTag("soils/base");
    public static final TagKey<Block> ELEMENTAL_SOIL = modTag("soils/elemental");
    public static final TagKey<Block> ALL_SOIL = modTag("soils/all");

    public static final TagKey<Block> RUNED_OBSIDIAN = modTag("runed_obsidian");
    public static final TagKey<Block> RUNESTONE = modTag("runestone");
    public static final TagKey<Block> WILDWOOD_LOGS = modTag("logs/wildwood");

    // Specific types of Runed Logs
    public static final TagKey<Block> RUNED_LOGS = modTag("logs/runed");
    public static final TagKey<Block> RUNED_ACACIA_LOG = modTag("logs/runed/acacia");
    public static final TagKey<Block> RUNED_DARK_OAK_LOG = modTag("logs/runed/dark_oak");
    public static final TagKey<Block> RUNED_OAK_LOG = modTag("logs/runed/oak");
    public static final TagKey<Block> RUNED_BIRCH_LOG = modTag("logs/runed/birch");
    public static final TagKey<Block> RUNED_JUNGLE_LOG = modTag("logs/runed/jungle");
    public static final TagKey<Block> RUNED_SPRUCE_LOG = modTag("logs/runed/spruce");

    public static final TagKey<Block> RUNED_MANGROVE_LOG = modTag("logs/runed/mangrove");
    public static final TagKey<Block> RUNED_WILDWOOD_LOG = modTag("logs/runed/wildwood");
    public static final TagKey<Block> RUNED_CRIMSON_STEM = modTag("logs/runed/crimson");
    public static final TagKey<Block> RUNED_WARPED_STEM = modTag("logs/runed/warped");

    // Grove Stones
    public static final TagKey<Block> GROVE_STONES = modTag("grove_stones");

    public static final TagKey<Block> GROVE_STONE_PRIMAL = modTag("grove_stones/primal");
    public static final TagKey<Block> GROVE_STONE_WILD = modTag("grove_stones/wild");
    public static final TagKey<Block> GROVE_STONE_TWILIGHT = modTag("grove_stones/twilight");
    public static final TagKey<Block> GROVE_STONE_ELEMENTAL = modTag("grove_stones/elemental");
    public static final TagKey<Block> GROVE_STONE_FAIRY = modTag("grove_stones/fairy");
    public static final TagKey<Block> GROVE_STONE_FUNGAL = modTag("grove_stones/fungal");
    public static final TagKey<Block> GROVE_STONE_SPROUTING = modTag("grove_stones/sprouting");

    // Catalyst plates, offering plates and incense plates
    public static final TagKey<Block> PEDESTALS = modTag("pedestals");
    public static final TagKey<Block> RITUAL_PEDESTALS = modTag("pedestals/ritual");
    public static final TagKey<Block> GROVE_PEDESTALS = modTag("pedestals/grove");
    public static final TagKey<Block> LIMITED_PEDESTALS = modTag("pedestals/limited");

    public static final TagKey<Block> DISPLAY_PEDESTALS = modTag("pedestals/display");
    public static final TagKey<Block> AMPLIFIERS = modTag("amplifiers");

    // Pyres (does not include decorative)
    public static final TagKey<Block> PYRES = modTag("pyres");
    public static final TagKey<Block> DECORATIVE_PYRES = modTag("decorative_pyres");
    public static final TagKey<Block> FUNCTIONAL_PYRES = modTag("functional_pyres");

    // Sylvan and runic crafters
    public static final TagKey<Block> GROVE_CRAFTERS = modTag("crafters");

    // Mortars
    public static final TagKey<Block> MORTARS = modTag("mortars");

    // Valid capstones for runed/runestone pillars
    public static final TagKey<Block> RUNE_CAPSTONES = modTag("capstones/runes/rune");
    public static final TagKey<Block> RUNE_PILLARS = modTag("pillars/runes/rune");
    public static final TagKey<Block> RUNED_CAPSTONES = modTag("capstones/runes/runed");
    public static final TagKey<Block> RUNED_PILLARS = modTag("pillars/runes/runed");
    public static final TagKey<Block> RUNES_CAPSTONES = modTag("capstones/runes/any");
    public static final TagKey<Block> RUNES_PILLARS = modTag("pillars/runes/any");

    public static final TagKey<Block> CAPSTONES = modTag("capstones/any");
    public static final TagKey<Block> PILLARS = modTag("pillars/any");

    public static final TagKey<Block> ACACIA_CAPSTONES = modTag("capstones/log/acacia");
    public static final TagKey<Block> DARK_OAK_CAPSTONES = modTag("capstones/log/dark_oak");
    public static final TagKey<Block> OAK_CAPSTONES = modTag("capstones/log/oak");
    public static final TagKey<Block> BIRCH_CAPSTONES = modTag("capstones/log/birch");
    public static final TagKey<Block> JUNGLE_CAPSTONES = modTag("capstones/log/jungle");
    public static final TagKey<Block> SPRUCE_CAPSTONES = modTag("capstones/log/spruce");
    public static final TagKey<Block> WILDWOOD_CAPSTONES = modTag("capstones/log/wildwood");
    public static final TagKey<Block> MANGROVE_CAPSTONES = modTag("capstones/log/mangrove");
    public static final TagKey<Block> CRIMSON_CAPSTONES = modTag("capstones/log/crimson");
    public static final TagKey<Block> WARPED_CAPSTONES = modTag("capstones/log/warped");

    public static final TagKey<Block> LOG_PILLARS = modTag("pillars/log/any");
    public static final TagKey<Block> LOG_CAPSTONES = modTag("capstones/log/any");

    public static final TagKey<Block> ACACIA_PILLARS = modTag("pillars/log/acacia");
    public static final TagKey<Block> DARK_OAK_PILLARS = modTag("pillars/log/dark_oak");
    public static final TagKey<Block> OAK_PILLARS = modTag("pillars/log/oak");
    public static final TagKey<Block> BIRCH_PILLARS = modTag("pillars/log/birch");
    public static final TagKey<Block> JUNGLE_PILLARS = modTag("pillars/log/jungle");
    public static final TagKey<Block> SPRUCE_PILLARS = modTag("pillars/log/spruce");
    public static final TagKey<Block> WILDWOOD_PILLARS = modTag("pillars/log/wildwood");
    public static final TagKey<Block> MANGROVE_PILLARS = modTag("pillars/log/mangrove");
    public static final TagKey<Block> CRIMSON_PILLARS = modTag("pillars/log/crimson");
    public static final TagKey<Block> WARPED_PILLARS = modTag("pillars/log/warped");

    // Crops that should not be affected by the growth spells
    public static final TagKey<Block> GROWTH_BLACKLIST = modTag("growth/blacklist");
    // Crops that should still receive growth ticks even if they are considered "grown"
    public static final TagKey<Block> GROWTH_FORCE = modTag("growth/force");
    // Crops that should receive a reduced number of growth ticks
    public static final TagKey<Block> GROWTH_REDUCE = modTag("growth/reduce");

    public static final TagKey<Block> SUPPORTS_WILD_ROOTS = modTag("supports_wild_roots");
    public static final TagKey<Block> SUPPORTS_HANGING_MOSS = modTag("supports_manging_moss");

    public static final TagKey<Block> SUPPORTS_WILD_AUBERGINE = modTag("supports_wild_aubergine");

    public static final TagKey<Block> SUPPORTS_STONEPETAL = modTag("supports_stonepetal");

    public static final TagKey<Block> STONEPETAL = modTag("stonepetal");

    public static final TagKey<Block> SHORT_GRASS = modTag("short_grass");
    public static final TagKey<Block> TALL_GRASS = modTag("tall_grass");

    public static final TagKey<Block> NYI = modTag("nyi");
    public static final TagKey<Block> WIP = modTag("wip");

    // Doesn't exist so I have to make it
    public static final TagKey<Block> LEVERS = compatTag("levers"); // SKIP

    // TODO: Hell sprouts aren't currently active
    public static final TagKey<Block> SUPPORTS_HELL_SPROUT_SPAWN = modTag("supports_hell_sprout_spawn");
    public static final TagKey<Block> SUPPORTS_MELODY_SPROUT_SPAWN = modTag("supports_melody_sprout_spawn");
    public static final TagKey<Block> SUPPORTS_SNOW_SPROUT_SPAWN = modTag("supports_snow_sprout_spawn");
    public static final TagKey<Block> BAFFLECAP_CONVERSION = modTag("converts_to_bafflecap");


    // Don't exist, have to make
    public static final TagKey<Block> SILVER_ORE = compatTag("ores/silver"); // SKIP
    public static final TagKey<Block> QUARTZ_ORE = compatTag("ores/quartz"); // SKIP
    public static final TagKey<Block> SILVER_STORAGE = compatTag("storage_blocks/silver"); // SKIP
    public static final TagKey<Block> RAW_SILVER_STORAGE = compatTag("storage_blocks/raw_silver"); // SKIP


    public static final TagKey<Block> GROVE_MOSS = modTag("grove_moss");

    // Bark recipes
    public static final TagKey<Block> OAK_LOGS_TO_STRIP = modTag("bark_logs/oak");
    public static final TagKey<Block> BIRCH_LOGS_TO_STRIP = modTag("bark_logs/birch");
    public static final TagKey<Block> SPRUCE_LOGS_TO_STRIP = modTag("bark_logs/spruce");
    public static final TagKey<Block> JUNGLE_LOGS_TO_STRIP = modTag("bark_logs/jungle");
    public static final TagKey<Block> ACACIA_LOGS_TO_STRIP = modTag("bark_logs/acacia");
    public static final TagKey<Block> DARK_OAK_LOGS_TO_STRIP = modTag("bark_logs/dark_oak");
    public static final TagKey<Block> MANGROVE_LOGS_TO_STRIP = modTag("bark_logs/mangrove");
    public static final TagKey<Block> CRIMSON_STEMS_TO_STRIP = modTag("bark_logs/crimson");
    public static final TagKey<Block> WARPED_STEMS_TO_STRIP = modTag("bark_logs/warped");
    public static final TagKey<Block> WILDWOOD_LOGS_TO_STRIP = modTag("bark_logs/wildwood");
    public static final TagKey<Block> GRANITE_ORE_REPLACEABLES = modTag("granite_ore_replaceables");

    public static final TagKey<Block> BLOOMING_ELIGIBLE_FLOWERS = modTag("ritual/blooming/eligible_flowers");
    public static final TagKey<Block> BLOOMING_ELIGIBLE_PEDESTAL_FLOWERS = modTag("ritual/blooming/eligible_pedestal_flowers");

    public static final TagKey<Block> SPREADING_MUSHROOMS = modTag("spreading_mushrooms");

    public static final TagKey<Block> FORAGEABLE_SINGLE_BLOCKS = modTag("forageable/single");
    public static final TagKey<Block> FORAGEABLE_DOUBLE_BLOCKS = modTag("forageable/double");
    public static final TagKey<Block> FORAGEABLES = modTag("forageable/all");

    public static final TagKey<Block> SHATTER_EXCLUDE = modTag("spells/shatter/exclude");
    public static final TagKey<Block> SHATTER_INCLUDE = modTag("spells/shatter/include");

    public static final TagKey<Block> RAMPANT_GROWTH_EXCLUDE_MODE = modTag("spells/rampant_growth/exclude");
    public static final TagKey<Block> ALLOW_CASTING_TOOL_RIGHT_CLICK = modTag("allow_casting_tool_right_click");

    public static final TagKey<Block> SPROUT_REPUTATION_CROPS = modTag("grove/sprout/crops");
    public static final TagKey<Block> ELEMENTAL_REPUTATION_CROPS = modTag("grove/elemental/crops");

    public static final TagKey<Block> UNDERWATER_FARMLAND = modTag("underwater_farmland");

    public static final TagKey<Block> FARMLANDS = compatTag("farmlands");

    public static final TagKey<Block> GROVE_CONSUMERS = modTag("grove_consumers");

    public static final TagKey<Block> FAIRY_GROVE_GENERATORS = modTag("grove_generators/fairy");
    public static final TagKey<Block> FAIRY_GROVE_PATHS = modTag("grove_generators/fairy/paths");
    public static final TagKey<Block> WILD_GROVE_GENERATORS = modTag("grove_generators/wild");
    public static final TagKey<Block> SPROUTING_GROVE_GENERATORS = modTag("grove_generators/sprouting");
    public static final TagKey<Block> FUNGAL_GROVE_GENERATORS = modTag("grove_generators/fungal");
    public static final TagKey<Block> TWILIGHT_GROVE_GENERATORS = modTag("grove_generators/twilight");
    public static final TagKey<Block> ELEMENTAL_GROVE_GENERATORS = modTag("grove_generators/elemental");

    public static final TagKey<Block> PYRE_HUD_RENDERER = modTag("pyre_hud_layer");
    public static final TagKey<Block> GROVE_CRAFTER_HUD_RENDERER = modTag("grove_crafter_hud_layer");
    public static final TagKey<Block> MORTAR_HUD_RENDERER = modTag("mortar_hud_layer");
    public static final TagKey<Block> GROVE_STONE_HUD_RENDERER = modTag("grove_stone_hud_layer");

    public static final TagKey<Block> HUTS = modTag("huts");

    public static final TagKey<Block> BAFFLECAP_HUTS = modTag("huts/bafflecap");
    public static final TagKey<Block> RED_HUTS = modTag("huts/red");
    public static final TagKey<Block> BROWN_HUTS = modTag("huts/brown");
    public static final TagKey<Block> CRIMSON_HUTS = modTag("huts/crimson");
    public static final TagKey<Block> WARPED_HUTS = modTag("huts/warped");

    public static final TagKey<Block> GROWTH_AMPLIFIER_GRASSES = modTag("growth_amplifier_grasses");

    public static final TagKey<Block> NETHER_DOORS = modTag("doors/nether");

    private static TagKey<Block> modTag(String name) {
      return BlockTags.create(RootsAPI.rl(name));
    }

    private static TagKey<Block> compatTag(String name) {
      return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
    }

    private static TagKey<Block> compatTag(String prefix, String name) {
      return BlockTags.create(ResourceLocation.fromNamespaceAndPath(prefix, name));
    }
  }

  public static class Items {
    public static final TagKey<Item> SEEDS = modTag("seeds");
    public static final TagKey<Item> CLOUD_BERRY_SEEDS = modTag("seeds/cloud_berry");
    public static final TagKey<Item> DEWGONIA_SEEDS = modTag("seeds/dewgonia");
    public static final TagKey<Item> INFERNO_BULB_SEEDS = modTag("seeds/inferno_bulb");
    public static final TagKey<Item> MOONGLOW_SEEDS = modTag("seeds/moonglow");
    public static final TagKey<Item> PERESKIA_SEEDS = modTag("seeds/pereskia");
    public static final TagKey<Item> SPIRITLEAF_SEEDS = modTag("seeds/spiritleaf");
    public static final TagKey<Item> STALICRIPE_SEEDS = modTag("seeds/stalicripe");
    public static final TagKey<Item> WILDEWHEET_SEEDS = modTag("seeds/wildewheet");
    public static final TagKey<Item> WILDROOT_SEEDS = modTag("seeds/wildroot");
    public static final TagKey<Item> AUBERGINE_SEEDS = modTag("seeds/aubergine");

    public static final TagKey<Item> CROPS = modTag("crops");
    public static final TagKey<Item> ELEMENTAL_CROPS = modTag("crops/elemental");
    public static final TagKey<Item> WATER_CROPS = modTag("crops/elemental/water");
    public static final TagKey<Item> EARTH_CROPS = modTag("crops/elemental/earth");
    public static final TagKey<Item> AIR_CROPS = modTag("crops/elemental/air");
    public static final TagKey<Item> FIRE_CROPS = modTag("crops/elemental/fire");
    public static final TagKey<Item> CLOUD_BERRY_CROP = modTag("crops/cloud_berry");
    public static final TagKey<Item> DEWGONIA_CROP = modTag("crops/dewgonia");
    public static final TagKey<Item> SPIRITLEAF_CROP = modTag("crops/spiritleaf");
    public static final TagKey<Item> STALICRIPE_CROP = modTag("crops/stalicripe");
    public static final TagKey<Item> WILDEWHEET_CROP = modTag("crops/wildewheet");
    public static final TagKey<Item> WILDROOT_CROP = modTag("crops/wildroot");
    public static final TagKey<Item> GROVE_MOSS_CROP = modTag("crops/grove_moss");
    public static final TagKey<Item> INFERNO_BULB_CROP = modTag("crops/inferno_bulb");
    public static final TagKey<Item> MOONGLOW_CROP = modTag("crops/moonglow");
    public static final TagKey<Item> PERESKIA_CROP = modTag("crops/pereskia");

    public static final TagKey<Item> AUBERGINE_CROP = modTag("crops/aubergine");
    public static final TagKey<Item> BAFFLECAP_CROP = modTag("crops/bafflecap");

    public static final TagKey<Item> BARKS = modTag("barks");
    public static final TagKey<Item> BARKS_THAT_BURN = modTag("barks_that_burn");
    public static final TagKey<Item> ACACIA_BARK = modTag("barks/acacia");
    public static final TagKey<Item> BIRCH_BARK = modTag("barks/birch");
    public static final TagKey<Item> DARK_OAK_BARK = modTag("barks/dark_oak");
    public static final TagKey<Item> JUNGLE_BARK = modTag("barks/jungle");
    public static final TagKey<Item> OAK_BARK = modTag("barks/oak");
    public static final TagKey<Item> SPRUCE_BARK = modTag("barks/spruce");
    public static final TagKey<Item> WILDWOOD_BARK = modTag("barks/wildwood");
    public static final TagKey<Item> MANGROVE_BARK = modTag("barks/mangrove");
    public static final TagKey<Item> CRIMSON_BARK = modTag("barks/crimson");
    public static final TagKey<Item> WARPED_BARK = modTag("barks/warped");
    public static final TagKey<Item> MIXED_BARK = modTag("barks/mixed");

    public static final TagKey<Item> BOTTLES = modTag("bottles");

    public static final TagKey<Item> POUCHES = modTag("pouches");
    public static final TagKey<Item> CREATIVE_POUCHES = modTag("pouches/creative");
    public static final TagKey<Item> ALL_POUCHES = modTag("pouches/all");

    public static final TagKey<Item> QUIVERS = modTag("quivers");

    public static final TagKey<Item> GROVE_CRAFTER_ACTIVATION = modTag("grove_crafter_activation");
    public static final TagKey<Item> FUNGAL_TRANSMUTER_ACTIVATION = modTag("fungal_transmuter_activation");
    public static final TagKey<Item> MORTAR_ACTIVATION = modTag("mortar_activation");
    public static final TagKey<Item> PYRE_ACTIVATION = modTag("pyre_activation");

    public static final TagKey<Item> FLINT = modTag("flint");
    public static final TagKey<Item> STONELIKE = modTag("stonelike");
    public static final TagKey<Item> CASTING_TOOLS = modTag("casting_tools");

    public static final TagKey<Item> RUNESTONE_HERBS = modTag("runestone_herbs");

    public static final TagKey<Item> NYI = modTag("nyi");
    public static final TagKey<Item> WIP = modTag("wip");
    public static final TagKey<Item> SOILS = modTag("soils");
    public static final TagKey<Item> WATER_SOIL = modTag("soils/water");
    public static final TagKey<Item> AIR_SOIL = modTag("soils/air");
    public static final TagKey<Item> EARTH_SOIL = modTag("soils/earth");
    public static final TagKey<Item> FIRE_SOIL = modTag("soils/fire");
    public static final TagKey<Item> BASE_ELEMENTAL_SOIL = modTag("soils/base");
    public static final TagKey<Item> ALL_SOIL = modTag("soils/all");
    public static final TagKey<Item> ELEMENTAL_SOIL = modTag("soils/elemental");
    public static final TagKey<Item> RUNED_OBSIDIAN = modTag("runed_obsidian");
    public static final TagKey<Item> RUNESTONE = modTag("runestone");
    public static final TagKey<Item> WILDWOOD_LOGS = modTag("logs/wildwood");
    public static final TagKey<Item> RUNED_LOGS = modTag("logs/runed");
    public static final TagKey<Item> RUNED_ACACIA_LOG = modTag("logs/runed/acacia");
    public static final TagKey<Item> RUNED_DARK_OAK_LOG = modTag("logs/runed/dark_oak");
    public static final TagKey<Item> RUNED_OAK_LOG = modTag("logs/runed/oak");
    public static final TagKey<Item> RUNED_BIRCH_LOG = modTag("logs/runed/birch");
    public static final TagKey<Item> RUNED_JUNGLE_LOG = modTag("logs/runed/jungle");
    public static final TagKey<Item> RUNED_SPRUCE_LOG = modTag("logs/runed/spruce");
    public static final TagKey<Item> RUNED_MANGROVE_LOG = modTag("logs/runed/mangrove");
    public static final TagKey<Item> RUNED_WILDWOOD_LOG = modTag("logs/runed/wildwood");
    public static final TagKey<Item> RUNED_CRIMSON_STEM = modTag("logs/runed/crimson");
    public static final TagKey<Item> RUNED_WARPED_STEM = modTag("logs/runed/warped");
    public static final TagKey<Item> GROVE_STONES = modTag("grove_stones");
    public static final TagKey<Item> GROVE_STONE_PRIMAL = modTag("grove_stones/primal");
    public static final TagKey<Item> GROVE_STONE_WILD = modTag("grove_stones/wild");
    public static final TagKey<Item> GROVE_STONE_TWILIGHT = modTag("grove_stones/twilight");
    public static final TagKey<Item> GROVE_STONE_ELEMENTAL = modTag("grove_stones/elemental");
    public static final TagKey<Item> GROVE_STONE_FAIRY = modTag("grove_stones/fairy");
    public static final TagKey<Item> GROVE_STONE_FUNGAL = modTag("grove_stones/fungal");
    public static final TagKey<Item> GROVE_STONE_SPROUTING = modTag("grove_stones/sprouting");
    public static final TagKey<Item> PEDESTALS = modTag("pedestals");
    public static final TagKey<Item> RITUAL_PEDESTALS = modTag("pedestals/ritual");
    public static final TagKey<Item> GROVE_PEDESTALS = modTag("pedestals/grove");
    public static final TagKey<Item> LIMITED_PEDESTALS = modTag("pedestals/limited");
    public static final TagKey<Item> DISPLAY_PEDESTALS = modTag("pedestals/display");
    public static final TagKey<Item> PYRES = modTag("pyres");
    public static final TagKey<Item> DECORATIVE_PYRES = modTag("decorative_pyres");
    public static final TagKey<Item> FUNCTIONAL_PYRES = modTag("functional_pyres");
    public static final TagKey<Item> GROVE_CRAFTERS = modTag("crafters");
    public static final TagKey<Item> MORTARS = modTag("mortars");
    public static final TagKey<Item> RUNIC_DUST = modTag("dusts/runic");
    public static final TagKey<Item> RUNIC_SHEARS = modTag("runic_shears");
    public static final TagKey<Item> VEGETABLES = modTag("vegetables");
    public static final TagKey<Item> COOKED_VEGETABLES = modTag("cooked_vegetables");
    public static final TagKey<Item> PROTEINS = modTag("proteins");
    public static final TagKey<Item> COOKED_SEAFOOD = modTag("cooked_seafood");
    public static final TagKey<Item> KNIVES = modTag("knives");

    public static final TagKey<Item> LEVERS = compatTag("levers");

    public static final TagKey<Item> GROVE_MOSS_HERB = modTag("herbs/grove_moss");
    public static final TagKey<Item> INFERNO_BULB_HERB = modTag("herbs/inferno_bulb");
    public static final TagKey<Item> MOONGLOW_HERB = modTag("herbs/moonglow");
    public static final TagKey<Item> PERESKIA_HERB = modTag("herbs/pereskia");
    public static final TagKey<Item> SPIRITLEAF_HERB = modTag("herbs/spiritleaf");
    public static final TagKey<Item> STALICRIPE_HERB = modTag("herbs/stalicripe");
    public static final TagKey<Item> WILDEWHEET_HERB = modTag("herbs/wildewheet");
    public static final TagKey<Item> WILDROOT_HERB = modTag("herbs/wildroot");
    public static final TagKey<Item> CLOUD_BERRY_HERB = modTag("herbs/cloud_berry");
    public static final TagKey<Item> DEWGONIA_HERB = modTag("herbs/dewgonia");
    public static final TagKey<Item> BAFFLECAP_HERB = modTag("herbs/bafflecap");
    public static final TagKey<Item> HERBS = modTag("herbs");

    public static final TagKey<Item> OWL_FOOD = modTag("tempt/owl");
    public static final TagKey<Item> BEETLE_FOOD = modTag("tempt/beetle");
    public static final TagKey<Item> DUCK_FOOD = modTag("tempt/duck");
    public static final TagKey<Item> FENNEC_FOOD = modTag("tempt/fennec");
    public static final TagKey<Item> SPROUT_FOOD = modTag("tempt/sprout");
    public static final TagKey<Item> DEER_FOOD = modTag("tempt/deer");
    public static final TagKey<Item> JERBOA_FOOD = modTag("tempt/jerboa");
    public static final TagKey<Item> CARAPACE = modTag("carapace");
    public static final TagKey<Item> PELT = modTag("pelt");
    public static final TagKey<Item> ANTLERS = modTag("antlers");
    public static final TagKey<Item> COPPER_ITEMS = modTag("copper_items");
    public static final TagKey<Item> COPPER_NUGGET = compatTag("nuggets/copper");
    public static final TagKey<Item> RAW_SILVER = compatTag("raw_materials/silver");
    public static final TagKey<Item> SILVER_INGOT = compatTag("ingots/silver");
    public static final TagKey<Item> STONEPETAL = modTag("stonepetal");
    public static final TagKey<Item> SHORT_GRASS = modTag("short_grass");
    public static final TagKey<Item> TALL_GRASS = modTag("tall_grass");

    // These are all filled in by ModTags

    public static final TagKey<Item> SILVER_ORE = compatTag("ores/silver");
    public static final TagKey<Item> QUARTZ_ORE = compatTag("ores/quartz");
    public static final TagKey<Item> SILVER_STORAGE = compatTag("storage_blocks/silver");
    public static final TagKey<Item> RAW_SILVER_STORAGE = compatTag("storage_blocks/raw_silver");
    public static final TagKey<Item> SILVER_NUGGET = compatTag("nuggets/silver");
    public static final TagKey<Item> SILVER_ITEMS = modTag("silver_items");
    public static final TagKey<Item> SKIPPED_FOODS = modTag("skipped_foods");

    public static final TagKey<Item> DISABLE_DISARMING = modTag("disable_disarming");

    public static final TagKey<Item> BLOOMING_ELIGIBLE_FLOWERS = modTag("ritual/blooming/eligible_flowers");
    public static final TagKey<Item> BLOOMING_ELIGIBLE_PEDESTAL_FLOWERS = modTag("ritual/blooming/eligible_pedestal_flowers");

    // Capstones etc
    public static final TagKey<Item> RUNE_CAPSTONES = modTag("capstones/runes/rune");
    public static final TagKey<Item> RUNE_PILLARS = modTag("pillars/runes/rune");
    public static final TagKey<Item> RUNED_CAPSTONES = modTag("capstones/runes/runed");
    public static final TagKey<Item> RUNED_PILLARS = modTag("pillars/runes/runed");
    public static final TagKey<Item> RUNES_CAPSTONES = modTag("capstones/runes/any");
    public static final TagKey<Item> RUNES_PILLARS = modTag("pillars/runes/any");

    public static final TagKey<Item> CAPSTONES = modTag("capstones/any");
    public static final TagKey<Item> PILLARS = modTag("pillars/any");

    public static final TagKey<Item> ACACIA_CAPSTONES = modTag("capstones/log/acacia");
    public static final TagKey<Item> DARK_OAK_CAPSTONES = modTag("capstones/log/dark_oak");
    public static final TagKey<Item> OAK_CAPSTONES = modTag("capstones/log/oak");
    public static final TagKey<Item> BIRCH_CAPSTONES = modTag("capstones/log/birch");
    public static final TagKey<Item> JUNGLE_CAPSTONES = modTag("capstones/log/jungle");
    public static final TagKey<Item> SPRUCE_CAPSTONES = modTag("capstones/log/spruce");
    public static final TagKey<Item> WILDWOOD_CAPSTONES = modTag("capstones/log/wildwood");
    public static final TagKey<Item> MANGROVE_CAPSTONES = modTag("capstones/log/mangrove");
    public static final TagKey<Item> CRIMSON_CAPSTONES = modTag("capstones/log/crimson");
    public static final TagKey<Item> WARPED_CAPSTONES = modTag("capstones/log/warped");

    public static final TagKey<Item> LOG_PILLARS = modTag("pillars/log/any");
    public static final TagKey<Item> LOG_CAPSTONES = modTag("capstones/log/any");
    public static final TagKey<Item> ACACIA_PILLARS = modTag("pillars/log/acacia");
    public static final TagKey<Item> DARK_OAK_PILLARS = modTag("pillars/log/dark_oak");
    public static final TagKey<Item> OAK_PILLARS = modTag("pillars/log/oak");
    public static final TagKey<Item> BIRCH_PILLARS = modTag("pillars/log/birch");
    public static final TagKey<Item> JUNGLE_PILLARS = modTag("pillars/log/jungle");
    public static final TagKey<Item> SPRUCE_PILLARS = modTag("pillars/log/spruce");
    public static final TagKey<Item> WILDWOOD_PILLARS = modTag("pillars/log/wildwood");
    public static final TagKey<Item> MANGROVE_PILLARS = modTag("pillars/log/mangrove");
    public static final TagKey<Item> CRIMSON_PILLARS = modTag("pillars/log/crimson");
    public static final TagKey<Item> WARPED_PILLARS = modTag("pillars/log/warped");
    public static final TagKey<Item> SPROUT_BREEDING_REWARDS = modTag("sprout_breeding_rewards");

    public static final TagKey<Item> FORAGING_ELIGIBLE = modTag("foraging_eligible");
    public static final TagKey<Item> SYLVAN_LEATHERS = modTag("sylvan_leather");
    public static final TagKey<Item> DYEABLE = modTag("dyeable");
    public static final TagKey<Item> CHARMS = modTag("charms");
    public static final TagKey<Item> CHARM_ALERT = modTag("charms/alertness");

    public static final TagKey<Item> CURIOS_CHARMS = curiosTag("charm");
    public static final TagKey<Item> CURIOS_TOMES = curiosTag("tome");
    public static final TagKey<Item> CURIOS_BELTS = curiosTag("belt");

    public static final TagKey<Item> BEETLE_ARMOR = modTag("armor/beetle");
    public static final TagKey<Item> ANTLER_ARMOR = modTag("armor/antler");
    public static final TagKey<Item> COPPER_ARMOR = modTag("armor/copper");

    // Items that, when held in off-hand transforms a log into a runed log equivalent
    public static final TagKey<Item> RUNED_LOG_HERBS = modTag("runed_log_herbs");

    public static final TagKey<Item> ROTTEN_FLESH = modTag("rotten_flesh");

    public static final TagKey<Item> GROWTH_AMPLIFIER_GRASSES = modTag("growth_amplifier_grasses");

    public static final TagKey<Item> NETHER_DOORS = modTag("doors/nether");
    public static final TagKey<Item> ADJUSTABLE_ITEM = modTag("adjustable_item");
    public static final TagKey<Item> APPLES = modTag("apples");
    public static final TagKey<Item> DANDELIONS = modTag("dandelions");
    public static final TagKey<Item> LILACS = modTag("lilacs");
    public static final TagKey<Item> ROSES = modTag("roses");
    public static final TagKey<Item> PEONIES = modTag("peonies");
    public static final TagKey<Item> POPPIES = modTag("poppies");

    protected static TagKey<Item> modTag(String name) {
      return ItemTags.create(RootsAPI.rl(name));
    }

    protected static TagKey<Item> compatTag(String name) {
      return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
    }

    protected static TagKey<Item> curiosTag(String name) {
      return ItemTags.create(ResourceLocation.fromNamespaceAndPath("curios", name));
    }
  }

  public static class MobEffects extends RootsTags {
    public static final TagKey<MobEffect> SUPPRESS_PARTICLES = modTag("suppress_particles");

    public static final TagKey<MobEffect> PURITY_FORCE_EXCLUDE = modTag("purity_force_exclude");
    public static final TagKey<MobEffect> PURITY_FORCE_INCLUDE = modTag("purity_force_include");
    public static final TagKey<MobEffect> GEAS = modTag("geas");

    static TagKey<MobEffect> modTag(String name) {
      return TagKey.create(Registries.MOB_EFFECT, RootsAPI.rl(name));
    }
  }

  public static class Potions extends RootsTags {
    public static final TagKey<Potion> RANDOM_BLACKLIST = compatTag("random_potion_blacklist");

    static TagKey<Potion> modTag(String name) {
      return TagKey.create(Registries.POTION, RootsAPI.rl(name));
    }

    static TagKey<Potion> compatTag(String name) {
      return TagKey.create(Registries.POTION, ResourceLocation.fromNamespaceAndPath("c", name));
    }
  }

  public static class Entities extends RootsTags {
    public static final TagKey<EntityType<?>> AUGMENTABLE = modTag("augmentable");
    public static final TagKey<EntityType<?>> ANIMAL_HARVEST = modTag("animal_harvest_entities");
    public static final TagKey<EntityType<?>> ANIMAL_HARVEST_EXCLUDE = modTag("rituals/animal_harvest/exclude");
    public static final TagKey<EntityType<?>> PACIFIST = modTag("pacifist");

    public static final TagKey<EntityType<?>> BOATS = modTag("boats");
    public static final TagKey<EntityType<?>> SQUID = modTag("squid");

    public static final TagKey<EntityType<?>> RUNIC_SHEARS_OVERRIDE = modTag("runic_shears_override");

    public static final TagKey<EntityType<?>> SYLVAN_LEATHER = modTag("sylvan_leather");

    public static final TagKey<EntityType<?>> FORCE_HOSTILE = modTag("force_hostile");
    public static final TagKey<EntityType<?>> FORCE_FRIENDLY = modTag("force_friendly");

    public static final TagKey<EntityType<?>> DISABLE_DISARM = modTag("disable_disarm");
    public static final TagKey<EntityType<?>> HEALABLE_ICE_CREATURES = modTag("healable_ice_creatures");

    public static final TagKey<EntityType<?>> WINDWALL_FORCE_EXCLUDE = modTag("windwall_force_exclude");
    public static final TagKey<EntityType<?>> WINDWALL_FORCE_INCLUDE = modTag("windwall_force_include");

    public static final TagKey<EntityType<?>> ZOMBIE_VILLAGERS = modTag("zombie_villagers");
    public static final TagKey<EntityType<?>> ZOMBIE_VILLAGERS_EXCLUDE = modTag("zombie_villagers_exclude");

    public static final TagKey<EntityType<?>> TEMPORAL_MORASS_EXCLUDE = modTag("temporal_morass_exclude");
    public static final TagKey<EntityType<?>> ROSE_THORNS_EXCLUDE = modTag("rose_thorns_exclude");
    public static final TagKey<EntityType<?>> ALERTNESS = modTag("alertness");
    public static final TagKey<EntityType<?>> ALLOW_CASTING_TOOL_RIGHT_CLICK = modTag("allow_casting_tool_right_click");
    public static final TagKey<EntityType<?>> ADD_TENTACLE_LOOT = modTag("add_tentacle_loot");

    public static final TagKey<EntityType<?>> WITHERS = modTag("withers");
    public static final TagKey<EntityType<?>> DRAGONS = modTag("dragons");
    public static final TagKey<EntityType<?>> TRADERS = modTag("traders");
    public static final TagKey<EntityType<?>> UNDEAD = modTag("undead");
    public static final TagKey<EntityType<?>> SPROUTS = modTag("sprouts");

    public static final TagKey<EntityType<?>> END_ANIMALS = modTag("end_animals");
    public static final TagKey<EntityType<?>> SNOW_ANIMALS = modTag("snow_animals");
    public static final TagKey<EntityType<?>> HELL_ANIMALS = modTag("hell_animals");

    public static final TagKey<EntityType<?>> MELODY_SPROUT = modTag("sprouts/melody");
    public static final TagKey<EntityType<?>> SNOW_SPROUT = modTag("sprouts/snow");
    public static final TagKey<EntityType<?>> HELL_SPROUT = modTag("sprouts/hell");

    public static final TagKey<EntityType<?>> RED_SPROUT = modTag("sprouts/red");
    public static final TagKey<EntityType<?>> TAN_SPROUT = modTag("sprouts/tan");
    public static final TagKey<EntityType<?>> GREEN_SPROUT = modTag("sprouts/green");
    public static final TagKey<EntityType<?>> PURPLE_SPROUT = modTag("sprouts/purple");

    public static final TagKey<EntityType<?>> SPECIAL_SPROUTS = modTag("sprouts/special");
    public static final TagKey<EntityType<?>> NORMAL_SPROUTS = modTag("sprouts/normal");

    public static final TagKey<EntityType<?>> SHOULD_RENDER_HUD = modTag("should_render_hud");
    public static final TagKey<EntityType<?>> PLAYERS = modTag("players");

    static TagKey<EntityType<?>> modTag(String name) {
      return TagKey.create(Registries.ENTITY_TYPE, RootsAPI.rl(name));
    }

    static TagKey<EntityType<?>> compatTag(String name) {
      return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("c", name));
    }
  }

  public static class Rituals extends RootsTags {
    public static final TagKey<Ritual> NYI = modTag("nyi");
    public static final TagKey<Ritual> WIP = modTag("wip");

    public static final TagKey<Ritual> PRIMAL = modTag("primal");
    public static final TagKey<Ritual> FAIRY = modTag("fairy");
    public static final TagKey<Ritual> TWILIGHT = modTag("twilight");
    public static final TagKey<Ritual> FUNGAL = modTag("fungal");
    public static final TagKey<Ritual> SPROUTING = modTag("sprouting");
    public static final TagKey<Ritual> ELEMENTAL = modTag("elemental");
    public static final TagKey<Ritual> WILD = modTag("wild");
    public static final TagKey<Ritual> HOLLOW = modTag("hollow");

    static TagKey<Ritual> modTag(String name) {
      return TagKey.create(RootsRegistries.Keys.RITUALS, RootsAPI.rl(name));
    }

    static TagKey<Ritual> compatTag(String name) {
      return TagKey.create(RootsRegistries.Keys.RITUALS, ResourceLocation.fromNamespaceAndPath("c", name));
    }
  }

  public static class Spells extends RootsTags {
    public static final TagKey<Spell> NYI = modTag("nyi");
    public static final TagKey<Spell> WIP = modTag("wip");

    public static final TagKey<Spell> ADJUSTABLE_SPELL = modTag("adjustable");

    public static final TagKey<Spell> PRIMAL = modTag("primal");
    public static final TagKey<Spell> FAIRY = modTag("fairy");
    public static final TagKey<Spell> TWILIGHT = modTag("twilight");
    public static final TagKey<Spell> FUNGAL = modTag("fungal");
    public static final TagKey<Spell> SPROUTING = modTag("sprouting");
    public static final TagKey<Spell> ELEMENTAL = modTag("elemental");
    public static final TagKey<Spell> WILD = modTag("wild");
    public static final TagKey<Spell> HOLLOW = modTag("hollow");

    public static final TagKey<Spell> GEAS_ACTION = modTag("action/geas");


    static TagKey<Spell> modTag(String name) {
      return TagKey.create(RootsRegistries.Keys.SPELLS, RootsAPI.rl(name));
    }

    static TagKey<Spell> compatTag(String name) {
      return TagKey.create(RootsRegistries.Keys.SPELLS, ResourceLocation.fromNamespaceAndPath("c", name));
    }
  }

  public static class SpellModifiers extends RootsTags {
    public static final TagKey<SpellModifier> NYI = modTag("nyi");

    static TagKey<SpellModifier> modTag(String name) {
      return TagKey.create(RootsRegistries.Keys.SPELL_MODIFIERS, RootsAPI.rl(name));
    }

    static TagKey<SpellModifier> compatTag(String name) {
      return TagKey.create(RootsRegistries.Keys.SPELL_MODIFIERS, ResourceLocation.fromNamespaceAndPath("c", name));
    }
  }

  public static class RitualModifiers extends RootsTags {
    public static final TagKey<RitualModifier> NYI = modTag("nyi");

    static TagKey<RitualModifier> modTag(String name) {
      return TagKey.create(RootsRegistries.Keys.RITUAL_MODIFIERS, RootsAPI.rl(name));
    }

    static TagKey<RitualModifier> compatTag(String name) {
      return TagKey.create(RootsRegistries.Keys.RITUAL_MODIFIERS, ResourceLocation.fromNamespaceAndPath("c", name));
    }
  }

  public static class Herbs extends RootsTags {
    public static final TagKey<Herb> ELEMENTAL = modTag("elemental");
    public static final TagKey<Herb> FIRE = modTag("elemental/fire");
    public static final TagKey<Herb> WATER = modTag("elemental/water");
    public static final TagKey<Herb> EARTH = modTag("elemental/earth");
    public static final TagKey<Herb> AIR = modTag("elemental/air");

    public static final TagKey<Herb> PRIMAL = modTag("primal");
    public static final TagKey<Herb> FAIRY = modTag("fairy");
    public static final TagKey<Herb> TWILIGHT = modTag("twilight");
    public static final TagKey<Herb> FUNGAL = modTag("fungal");
    public static final TagKey<Herb> SPROUTING = modTag("sprouting");
    public static final TagKey<Herb> WILD = modTag("wild");
    public static final TagKey<Herb> HOLLOW = modTag("hollow");

    static TagKey<Herb> modTag(String name) {
      return TagKey.create(RootsRegistries.Keys.HERBS, RootsAPI.rl(name));
    }

    static TagKey<Herb> compatTag(String name) {
      return TagKey.create(RootsRegistries.Keys.HERBS, ResourceLocation.fromNamespaceAndPath("c", name));
    }
  }

  public static class Groves extends RootsTags {
    public static final TagKey<Grove> ANY = modTag("any");
    public static final TagKey<Grove> PRIMAL = modTag("primal");
    public static final TagKey<Grove> FAIRY = modTag("fairy");
    public static final TagKey<Grove> TWILIGHT = modTag("twilight");
    public static final TagKey<Grove> FUNGAL = modTag("fungal");
    public static final TagKey<Grove> SPROUTING = modTag("sprouting");
    public static final TagKey<Grove> ELEMENTAL = modTag("elemental");
    public static final TagKey<Grove> WILD = modTag("wild");

    static TagKey<Grove> modTag(String name) {
      return TagKey.create(RootsRegistries.Keys.GROVES, RootsAPI.rl(name));
    }

    static TagKey<Grove> compatTag(String name) {
      return TagKey.create(RootsRegistries.Keys.GROVES, ResourceLocation.fromNamespaceAndPath("c", name));
    }
  }

  public static class Biomes {
    public static final TagKey<Biome> HAS_HANGING_MOSS = modTag("has_hanging_moss");
    public static final TagKey<Biome> HAS_BARROW_STRUCTURES = modTag("has_barrow_structure");
    public static final TagKey<Biome> HAS_BEETLE_SPAWNS = modTag("has_beetle_spawns");
    public static final TagKey<Biome> HAS_DEER_SPAWNS = modTag("has_deer_spawns");
    public static final TagKey<Biome> HAS_JERBOA_SPAWNS = modTag("has_jerboa_spawns");
    public static final TagKey<Biome> HAS_DUCK_SPAWNS = modTag("has_duck_spawns");
    public static final TagKey<Biome> HAS_FENNEC_SPAWNS = modTag("has_fennec_spawns");
    public static final TagKey<Biome> HAS_HUT_STRUCTURES = modTag("has_hut_structures");
    public static final TagKey<Biome> HAS_OWL_SPAWNS = modTag("has_owl_spawns");
    public static final TagKey<Biome> HAS_SPROUT_SPAWNS = modTag("has_sprout_spawns");
    public static final TagKey<Biome> HAS_SNOW_SPROUT_SPAWNS = modTag("has_snow_sprout_spawns");
    public static final TagKey<Biome> HAS_MELODY_SPROUT_SPAWNS = modTag("has_melody_sprout_spawns");
    public static final TagKey<Biome> HAS_STANDING_STONES = modTag("has_standing_stones");
    public static final TagKey<Biome> HAS_WILD_AUBERGINES = modTag("has_wild_aubergines");
    public static final TagKey<Biome> HAS_FOREST_WILD_ROOTS = modTag("has_forest_wild_roots");
    public static final TagKey<Biome> HAS_SPARSE_WILD_ROOTS = modTag("has_sparse_wild_roots");
    public static final TagKey<Biome> HAS_UNDERGROUND_WILD_ROOTS = modTag("has_underground_wild_roots");
    public static final TagKey<Biome> HAS_SILVER_ORES = modTag("has_silver_ores");
    public static final TagKey<Biome> HAS_GRANITE_QUARTZ_ORES = modTag("has_granite_quartz_ores");
    public static final TagKey<Biome> HAS_STONEPETALS = modTag("has_stonepetals");

    static TagKey<Biome> modTag(String name) {
      return TagKey.create(Registries.BIOME, RootsAPI.rl(name));
    }

    static TagKey<Biome> compatTag(String name) {
      return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("c", name));
    }
  }

  public static class DamageTypes {
    public static final TagKey<DamageType> IS_LAVA = modTag("is_lava");

    static TagKey<DamageType> modTag(String name) {
      return TagKey.create(Registries.DAMAGE_TYPE, RootsAPI.rl(name));
    }
  }

  public static class Atrtibutes {
    public static final TagKey<Attribute> GRAMARY_ATTRIBUTES = modTag("gramary_attributes");
    public static final TagKey<Attribute> AUGMENTABLE = modTag("augmentable");

    static TagKey<Attribute> modTag(String name) {
      return TagKey.create(Registries.ATTRIBUTE, RootsAPI.rl(name));
    }
  }
}
