package mysticmods.roots;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.Block;

public class RootsTags {
  public static class Blocks extends RootsTags {
    // Crops & Adjacent
    // Forge compat tag (filled by ModTags)
    public static TagKey<Block> FORGE_CROPS = compatTag("crops");

    // Minecraft compat tag (filled by ModTags)
    public static TagKey<Block> MINECRAFT_LOGS_THAT_BURN = compatTag("minecraft", "logs_that_burn");
    public static TagKey<Block> MINECRAFT_LOGS = compatTag("minecraft", "logs");

    // General crops (filled by ModTags)
    public static TagKey<Block> CROPS = modTag("crops");
    public static TagKey<Block> ELEMENTAL_CROPS = modTag("crops/elemental");

    // Specific crop types (filled in by ModTags)
    public static TagKey<Block> WATER_CROPS = modTag("crops/elemental/water");
    public static TagKey<Block> EARTH_CROPS = modTag("crops/elemental/earth");
    public static TagKey<Block> AIR_CROPS = modTag("crops/elemental/air");
    public static TagKey<Block> FIRE_CROPS = modTag("crops/elemental/fire");

    // Specific crops (filled in by ModBlocks)
    public static TagKey<Block> CLOUD_BERRY_CROP = modTag("crops/cloud_berry");
    public static TagKey<Block> DEWGONIA_CROP = modTag("crops/dewgonia");
    public static TagKey<Block> SPIRIT_HERB_CROP = modTag("crops/spirit_herb");
    public static TagKey<Block> STALICRIPE_CROP = modTag("crops/stalicripe");
    public static TagKey<Block> WILDEWHEET_CROP = modTag("crops/wildewheet");
    public static TagKey<Block> WILDROOT_CROP = modTag("crops/wildroot");
    public static TagKey<Block> INFERNAL_BULB_CROP = modTag("crops/infernal_bulb");
    public static TagKey<Block> MOONGLOW_LEAF_CROP = modTag("crops/moonglow_leaf");
    public static TagKey<Block> PERESKIA_CROP = modTag("crops/pereskia");

    // General soils (filled in by ModTags)
    public static TagKey<Block> SOILS = modTag("soils");

    // Specific soils
    public static TagKey<Block> WATER_SOIL = modTag("soils/water");
    public static TagKey<Block> AIR_SOIL = modTag("soils/air");
    public static TagKey<Block> EARTH_SOIL = modTag("soils/earth");
    public static TagKey<Block> FIRE_SOIL = modTag("soils/fire");
    public static TagKey<Block> ELEMENTAL_SOIL = modTag("soils/elemental");

    public static TagKey<Block> RUNED_OBSIDIAN = modTag("runed_obsidian");
    public static TagKey<Block> RUNESTONE = modTag("runestone");
    public static TagKey<Block> WILDWOOD_LOGS = modTag("logs/wildwood");

    // Specific types of Runed Logs
    public static TagKey<Block> RUNED_LOGS = modTag("logs/runed");
    public static TagKey<Block> RUNED_ACACIA_LOG = modTag("logs/runed/acacia");
    public static TagKey<Block> RUNED_DARK_OAK_LOG = modTag("logs/runed/dark_oak");
    public static TagKey<Block> RUNED_OAK_LOG = modTag("logs/runed/oak");
    public static TagKey<Block> RUNED_BIRCH_LOG = modTag("logs/runed/birch");
    public static TagKey<Block> RUNED_JUNGLE_LOG = modTag("logs/runed/jungle");
    public static TagKey<Block> RUNED_SPRUCE_LOG = modTag("logs/runed/spruce");
    public static TagKey<Block> RUNED_WILDWOOD_LOG = modTag("logs/runed/wildwood");
    public static TagKey<Block> RUNED_CRIMSON_STEM = modTag("logs/runed/crimson");
    public static TagKey<Block> RUNED_WARPED_STEM = modTag("logs/runed/warped");

    // Grove Stones
    public static TagKey<Block> GROVE_STONES = modTag("grove_stones");

    public static TagKey<Block> GROVE_STONE_PRIMAL = modTag("grove_stones/primal");

    // Catalyst plates, offering plates and incense plates
    public static TagKey<Block> PEDESTALS = modTag("pedestals");
    public static TagKey<Block> RITUAL_PEDESTALS = modTag("pedestals/ritual");
    public static TagKey<Block> GROVE_PEDESTALS = modTag("pedestals/grove");

    // Pyres (does not include decorative)
    public static TagKey<Block> PYRES = modTag("pyres");

    // Fey and runic crafters
    public static TagKey<Block> CRAFTERS = modTag("crafters");

    // Mortars
    public static TagKey<Block> MORTARS = modTag("mortars");

    static TagKey<Block> modTag(String name) {
      return BlockTags.create(new ResourceLocation(RootsAPI.MODID, name));
    }

    static TagKey<Block> compatTag(String name) {
      return BlockTags.create(new ResourceLocation("forge", name));
    }

    static TagKey<Block> compatTag(String prefix, String name) {
      return BlockTags.create(new ResourceLocation(prefix, name));
    }
  }

  public static class Items extends RootsTags {
    public static TagKey<Item> SEEDS = modTag("seeds");
    public static TagKey<Item> CLOUD_BERRY_SEEDS = modTag("seeds/cloud_berry");
    public static TagKey<Item> DEWGONIA_SEEDS = modTag("seeds/dewgonia");
    public static TagKey<Item> INFERNAL_BULB_SEEDS = modTag("seeds/infernal_bulb");
    public static TagKey<Item> MOONGLOW_LEAF_SEEDS = modTag("seeds/moonglow_leaf");
    public static TagKey<Item> PERESKIA_SEEDS = modTag("seeds/pereskia");
    public static TagKey<Item> SPIRIT_HERB_SEEDS = modTag("seeds/spirit_herb");
    public static TagKey<Item> STALICRIPE_SEEDS = modTag("seeds/stalicripe");
    public static TagKey<Item> WILDEWHEET_SEEDS = modTag("seeds/wildewheet");
    public static TagKey<Item> WILDROOT_SEEDS = modTag("seeds/wildroot");

    public static TagKey<Item> FORGE_KNIVES = compatTag("tools/knife");

    public static TagKey<Item> WILDWOOD_LOGS = modTag("wildwood_logs");

    public static TagKey<Item> CROPS = modTag("crops");
    public static TagKey<Item> ELEMENTAL_CROPS = modTag("crops/elemental");
    public static TagKey<Item> WATER_CROPS = modTag("crops/elemental/water");
    public static TagKey<Item> EARTH_CROPS = modTag("crops/elemental/earth");
    public static TagKey<Item> AIR_CROPS = modTag("crops/elemental/air");
    public static TagKey<Item> FIRE_CROPS = modTag("crops/elemental/fire");
    public static TagKey<Item> CLOUD_BERRY_CROP = modTag("crops/cloud_berry");
    public static TagKey<Item> DEWGONIA_CROP = modTag("crops/dewgonia");
    public static TagKey<Item> SPIRIT_HERB_CROP = modTag("crops/spirit_herb");
    public static TagKey<Item> STALICRIPE_CROP = modTag("crops/stalicripe");
    public static TagKey<Item> WILDEWHEET_CROP = modTag("crops/wildewheet");
    public static TagKey<Item> WILDROOT_CROP = modTag("crops/wildroot");
    public static TagKey<Item> SACRED_MOSS_CROP = modTag("crops/sacred_moss");
    public static TagKey<Item> INFERNAL_BULB_CROP = modTag("crops/infernal_bulb");
    public static TagKey<Item> MOONGLOW_LEAF_CROP = modTag("crops/moonglow_leaf");
    public static TagKey<Item> PERESKIA_CROP = modTag("crops/pereskia");

    public static TagKey<Item> BARKS = modTag("barks");
    public static TagKey<Item> ACACIA_BARK = modTag("barks/acacia");
    public static TagKey<Item> BIRCH_BARK = modTag("barks/birch");
    public static TagKey<Item> DARK_OAK_BARK = modTag("barks/dark_oak");
    public static TagKey<Item> JUNGLE_BARK = modTag("barks/jungle");
    public static TagKey<Item> OAK_BARK = modTag("barks/oak");
    public static TagKey<Item> SPRUCE_BARK = modTag("barks/spruce");
    public static TagKey<Item> WILDWOOD_BARK = modTag("barks/wildwood");
    public static TagKey<Item> CRIMSON_BARK = modTag("barks/crimson");
    public static TagKey<Item> WARPED_BARK = modTag("barks/warped");
    public static TagKey<Item> MIXED_BARK = modTag("barks/mixed");

    public static TagKey<Item> GROVE_CRAFTER_ACTIVATION = modTag("grove_crafter_activation");
    public static TagKey<Item> MORTAR_ACTIVATION = modTag("mortar_activation");
    public static TagKey<Item> PYRE_ACTIVATION = modTag("pyre_activation");

    public static TagKey<Item> CASTING_TOOLS = modTag("casting_tools");

    // These are all filled in by ModTags
    public static class Blocks extends RootsTags {
      public static TagKey<Item> SOILS = modTag("soils");
      public static TagKey<Item> WATER_SOIL = modTag("soils/water");
      public static TagKey<Item> AIR_SOIL = modTag("soils/air");
      public static TagKey<Item> EARTH_SOIL = modTag("soils/earth");
      public static TagKey<Item> FIRE_SOIL = modTag("soils/fire");
      public static TagKey<Item> ELEMENTAL_SOIL = modTag("soils/elemental");

      public static TagKey<Item> RUNED_OBSIDIAN = modTag("runed_obsidian");
      public static TagKey<Item> RUNESTONE = modTag("runestone");
      public static TagKey<Item> WILDWOOD_LOGS = modTag("logs/wildwood");

      public static TagKey<Item> RUNED_LOGS = modTag("logs/runed");
      public static TagKey<Item> RUNED_ACACIA_LOG = modTag("logs/runed/acacia");
      public static TagKey<Item> RUNED_DARK_OAK_LOG = modTag("logs/runed/dark_oak");
      public static TagKey<Item> RUNED_OAK_LOG = modTag("logs/runed/oak");
      public static TagKey<Item> RUNED_BIRCH_LOG = modTag("logs/runed/birch");
      public static TagKey<Item> RUNED_JUNGLE_LOG = modTag("logs/runed/jungle");
      public static TagKey<Item> RUNED_SPRUCE_LOG = modTag("logs/runed/spruce");
      public static TagKey<Item> RUNED_WILDWOOD_LOG = modTag("logs/runed/wildwood");
      public static TagKey<Item> RUNED_CRIMSON_STEM = modTag("logs/runed/crimson");
      public static TagKey<Item> RUNED_WARPED_STEM = modTag("logs/runed/warped");

      public static TagKey<Item> GROVE_STONES = modTag("grove_stones");
      public static TagKey<Item> GROVE_STONE_PRIMAL = modTag("grove_stones/primal");

      public static TagKey<Item> PEDESTALS = modTag("pedestals");
      public static TagKey<Item> RITUAL_PEDESTALS = modTag("pedestals/ritual");
      public static TagKey<Item> GROVE_PEDESTALS = modTag("pedestals/grove");
      public static TagKey<Item> PYRES = modTag("pyres");
      public static TagKey<Item> CRAFTERS = modTag("crafters");
      public static TagKey<Item> MORTARS = modTag("mortars");
    }

    static TagKey<Item> modTag(String name) {
      return ItemTags.create(new ResourceLocation(RootsAPI.MODID, name));
    }

    static TagKey<Item> compatTag(String name) {
      return ItemTags.create(new ResourceLocation("forge", name));
    }
  }

  public static class Potions extends RootsTags {
    public static TagKey<Potion> RANDOM_BLACKLIST = compatTag("random_potion_blacklist");

    static TagKey<Potion> modTag(String name) {
      return TagKey.create(Registry.POTION_REGISTRY, new ResourceLocation(RootsAPI.MODID, name));
    }

    static TagKey<Potion> compatTag(String name) {
      return TagKey.create(Registry.POTION_REGISTRY, new ResourceLocation("forge", name));
    }
  }

  public static class Entities extends RootsTags {
    public static TagKey<EntityType<?>> ANIMAL_HARVEST = modTag("animal_harvest_entities");
    public static TagKey<EntityType<?>> PACIFIST = modTag("pacifist");

    static TagKey<EntityType<?>> modTag(String name) {
      return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(RootsAPI.MODID, name));
    }

    static TagKey<EntityType<?>> compatTag(String name) {
      return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("forge", name));
    }
  }
}
