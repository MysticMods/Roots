package mysticmods.roots;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeTagHandler;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public class RootsTags {
  public static class Blocks extends RootsTags {
    // Crops & Adjacent
    // Forge compat tag (filled by ModTags)
    public static Tags.IOptionalNamedTag<Block> FORGE_CROPS = compatTag("crops");

    // Minecraft compat tag (filled by ModTags)
    public static Tags.IOptionalNamedTag<Block> MINECRAFT_LOGS_THAT_BURN = compatTag("minecraft", "logs_that_burn");
    public static Tags.IOptionalNamedTag<Block> MINECRAFT_LOGS = compatTag("minecraft", "logs");

    // General crops (filled by ModTags)
    public static Tags.IOptionalNamedTag<Block> CROPS = modTag("crops");
    public static Tags.IOptionalNamedTag<Block> ELEMENTAL_CROPS = modTag("crops/elemental");

    // Specific crop types (filled in by ModTags)
    public static Tags.IOptionalNamedTag<Block> WATER_CROPS = modTag("crops/elemental/water");
    public static Tags.IOptionalNamedTag<Block> EARTH_CROPS = modTag("crops/elemental/earth");
    public static Tags.IOptionalNamedTag<Block> AIR_CROPS = modTag("crops/elemental/air");
    public static Tags.IOptionalNamedTag<Block> FIRE_CROPS = modTag("crops/elemental/fire");

    // Specific crops (filled in by ModBlocks)
    public static Tags.IOptionalNamedTag<Block> CLOUD_BERRY_CROP = modTag("crops/cloud_berry");
    public static Tags.IOptionalNamedTag<Block> DEWGONIA_CROP = modTag("crops/dewgonia");
    public static Tags.IOptionalNamedTag<Block> SPIRIT_HERB_CROP = modTag("crops/spirit_herb");
    public static Tags.IOptionalNamedTag<Block> STALICRIPE_CROP = modTag("crops/stalicripe");
    public static Tags.IOptionalNamedTag<Block> WILDEWHEET_CROP = modTag("crops/wildewheet");
    public static Tags.IOptionalNamedTag<Block> WILDROOT_CROP = modTag("crops/wildroot");
    public static Tags.IOptionalNamedTag<Block> INFERNAL_BULB_CROP = modTag("crops/infernal_bulb");
    public static Tags.IOptionalNamedTag<Block> MOONGLOW_LEAF_CROP = modTag("crops/moonglow_leaf");
    public static Tags.IOptionalNamedTag<Block> PERESKIA_CROP = modTag("crops/pereskia");

    // General soils (filled in by ModTags)
    public static Tags.IOptionalNamedTag<Block> SOILS = modTag("soils");

    // Specific soils
    public static Tags.IOptionalNamedTag<Block> WATER_SOIL = modTag("soils/water");
    public static Tags.IOptionalNamedTag<Block> AIR_SOIL = modTag("soils/air");
    public static Tags.IOptionalNamedTag<Block> EARTH_SOIL = modTag("soils/earth");
    public static Tags.IOptionalNamedTag<Block> FIRE_SOIL = modTag("soils/fire");
    public static Tags.IOptionalNamedTag<Block> ELEMENTAL_SOIL = modTag("soils/elemental");

    public static Tags.IOptionalNamedTag<Block> RUNED_OBSIDIAN = modTag("runed_obsidian");
    public static Tags.IOptionalNamedTag<Block> RUNESTONE = modTag("runestone");
    public static Tags.IOptionalNamedTag<Block> WILDWOOD_LOGS = modTag("logs/wildwood");

    // Specific types of Runed Logs
    public static Tags.IOptionalNamedTag<Block> RUNED_LOGS = modTag("logs/runed");
    public static Tags.IOptionalNamedTag<Block> RUNED_ACACIA_LOG = modTag("logs/runed/acacia");
    public static Tags.IOptionalNamedTag<Block> RUNED_DARK_OAK_LOG = modTag("logs/runed/dark_oak");
    public static Tags.IOptionalNamedTag<Block> RUNED_OAK_LOG = modTag("logs/runed/oak");
    public static Tags.IOptionalNamedTag<Block> RUNED_BIRCH_LOG = modTag("logs/runed/birch");
    public static Tags.IOptionalNamedTag<Block> RUNED_JUNGLE_LOG = modTag("logs/runed/jungle");
    public static Tags.IOptionalNamedTag<Block> RUNED_SPRUCE_LOG = modTag("logs/runed/spruce");
    public static Tags.IOptionalNamedTag<Block> RUNED_WILDWOOD_LOG = modTag("logs/runed/wildwood");
    public static Tags.IOptionalNamedTag<Block> RUNED_CRIMSON_STEM = modTag("logs/runed/crimson");
    public static Tags.IOptionalNamedTag<Block> RUNED_WARPED_STEM = modTag("logs/runed/warped");

    // Catalyst plates, offering plates and incense plates
    public static Tags.IOptionalNamedTag<Block> PLATE = modTag("plate");

    // Pyres (does not include decorative)
    public static Tags.IOptionalNamedTag<Block> PYRE = modTag("pyre");

    // Fey and runic crafters
    public static Tags.IOptionalNamedTag<Block> CRAFTER = modTag("crafter");

    // Imposers
    public static Tags.IOptionalNamedTag<Block> IMPOSER = modTag("imposer");

    // Imbuer
    public static Tags.IOptionalNamedTag<Block> IMBUER = modTag("imbuer");

    // Mortars
    public static Tags.IOptionalNamedTag<Block> MORTAR = modTag("mortar");

    static Tags.IOptionalNamedTag<Block> modTag(String name) {
      return BlockTags.createOptional(new ResourceLocation(Roots.MODID, name));
    }

    static Tags.IOptionalNamedTag<Block> compatTag(String name) {
      return BlockTags.createOptional(new ResourceLocation("forge", name));
    }

    static Tags.IOptionalNamedTag<Block> compatTag(String prefix, String name) {
      return BlockTags.createOptional(new ResourceLocation(prefix, name));
    }
  }

  public static class Items extends RootsTags {
    public static Tags.IOptionalNamedTag<Item> SEEDS = modTag("seeds");
    public static Tags.IOptionalNamedTag<Item> CLOUD_BERRY_SEEDS = modTag("seeds/cloud_berry");
    public static Tags.IOptionalNamedTag<Item> DEWGONIA_SEEDS = modTag("seeds/dewgonia");
    public static Tags.IOptionalNamedTag<Item> INFERNAL_BULB_SEEDS = modTag("seeds/infernal_bulb");
    public static Tags.IOptionalNamedTag<Item> MOONGLOW_LEAF_SEEDS = modTag("seeds/moonglow_leaf");
    public static Tags.IOptionalNamedTag<Item> PERESKIA_SEEDS = modTag("seeds/pereskia");
    public static Tags.IOptionalNamedTag<Item> SPIRIT_HERB_SEEDS = modTag("seeds/spirit_herb");
    public static Tags.IOptionalNamedTag<Item> STALICRIPE_SEEDS = modTag("seeds/stalicripe");
    public static Tags.IOptionalNamedTag<Item> WILDEWHEET_SEEDS = modTag("seeds/wildewheet");
    public static Tags.IOptionalNamedTag<Item> WILDROOT_SEEDS = modTag("seeds/wildroot");

    public static Tags.IOptionalNamedTag<Item> FORGE_KNIVES = compatTag("tools/knife");

    public static Tags.IOptionalNamedTag<Item> WILDWOOD_LOGS = modTag("wildwood_logs");

    public static Tags.IOptionalNamedTag<Item> CROPS = modTag("crops");
    public static Tags.IOptionalNamedTag<Item> ELEMENTAL_CROPS = modTag("crops/elemental");
    public static Tags.IOptionalNamedTag<Item> WATER_CROPS = modTag("crops/elemental/water");
    public static Tags.IOptionalNamedTag<Item> EARTH_CROPS = modTag("crops/elemental/earth");
    public static Tags.IOptionalNamedTag<Item> AIR_CROPS = modTag("crops/elemental/air");
    public static Tags.IOptionalNamedTag<Item> FIRE_CROPS = modTag("crops/elemental/fire");
    public static Tags.IOptionalNamedTag<Item> CLOUD_BERRY_CROP = modTag("crops/cloud_berry");
    public static Tags.IOptionalNamedTag<Item> DEWGONIA_CROP = modTag("crops/dewgonia");
    public static Tags.IOptionalNamedTag<Item> SPIRIT_HERB_CROP = modTag("crops/spirit_herb");
    public static Tags.IOptionalNamedTag<Item> STALICRIPE_CROP = modTag("crops/stalicripe");
    public static Tags.IOptionalNamedTag<Item> WILDEWHEET_CROP = modTag("crops/wildewheet");
    public static Tags.IOptionalNamedTag<Item> WILDROOT_CROP = modTag("crops/wildroot");
    public static Tags.IOptionalNamedTag<Item> INFERNAL_BULB_CROP = modTag("crops/infernal_bulb");
    public static Tags.IOptionalNamedTag<Item> MOONGLOW_LEAF_CROP = modTag("crops/moonglow_leaf");
    public static Tags.IOptionalNamedTag<Item> PERESKIA_CROP = modTag("crops/pereskia");

    // These are all filled in by ModTags
    public static class Blocks extends RootsTags {
      public static Tags.IOptionalNamedTag<Item> SOILS = modTag("soils");
      public static Tags.IOptionalNamedTag<Item> WATER_SOIL = modTag("soils/water");
      public static Tags.IOptionalNamedTag<Item> AIR_SOIL = modTag("soils/air");
      public static Tags.IOptionalNamedTag<Item> EARTH_SOIL = modTag("soils/earth");
      public static Tags.IOptionalNamedTag<Item> FIRE_SOIL = modTag("soils/fire");
      public static Tags.IOptionalNamedTag<Item> ELEMENTAL_SOIL = modTag("soils/elemental");

      public static Tags.IOptionalNamedTag<Item> RUNED_OBSIDIAN = modTag("runed_obsidian");
      public static Tags.IOptionalNamedTag<Item> RUNESTONE = modTag("runestone");
      public static Tags.IOptionalNamedTag<Item> WILDWOOD_LOGS = modTag("logs/wildwood");

      public static Tags.IOptionalNamedTag<Item> RUNED_LOGS = modTag("logs/runed");
      public static Tags.IOptionalNamedTag<Item> RUNED_ACACIA_LOG = modTag("logs/runed/acacia");
      public static Tags.IOptionalNamedTag<Item> RUNED_DARK_OAK_LOG = modTag("logs/runed/dark_oak");
      public static Tags.IOptionalNamedTag<Item> RUNED_OAK_LOG = modTag("logs/runed/oak");
      public static Tags.IOptionalNamedTag<Item> RUNED_BIRCH_LOG = modTag("logs/runed/birch");
      public static Tags.IOptionalNamedTag<Item> RUNED_JUNGLE_LOG = modTag("logs/runed/jungle");
      public static Tags.IOptionalNamedTag<Item> RUNED_SPRUCE_LOG = modTag("logs/runed/spruce");
      public static Tags.IOptionalNamedTag<Item> RUNED_WILDWOOD_LOG = modTag("logs/runed/wildwood");
      public static Tags.IOptionalNamedTag<Item> RUNED_CRIMSON_STEM = modTag("logs/runed/crimson");
      public static Tags.IOptionalNamedTag<Item> RUNED_WARPED_STEM = modTag("logs/runed/warped");

      public static Tags.IOptionalNamedTag<Item> PLATE = modTag("plate");
      public static Tags.IOptionalNamedTag<Item> PYRE = modTag("pyre");
      public static Tags.IOptionalNamedTag<Item> CRAFTER = modTag("crafter");
      public static Tags.IOptionalNamedTag<Item> IMPOSER = modTag("imposer");
      public static Tags.IOptionalNamedTag<Item> IMBUER = modTag("imbuer");
      public static Tags.IOptionalNamedTag<Item> MORTAR = modTag("mortar");
    }

    static Tags.IOptionalNamedTag<Item> modTag(String name) {
      return ItemTags.createOptional(new ResourceLocation(Roots.MODID, name));
    }

    static Tags.IOptionalNamedTag<Item> compatTag(String name) {
      return ItemTags.createOptional(new ResourceLocation("forge", name));
    }
  }

  public static class Potions extends RootsTags {
    public static ITag.INamedTag<Potion> RANDOM_BLACKLIST = compatTag("random_potion_blacklist");

    static ITag.INamedTag<Potion> modTag(String name) {
      return ForgeTagHandler.createOptionalTag(ForgeRegistries.POTION_TYPES, new ResourceLocation(Roots.MODID, name));
    }

    static ITag.INamedTag<Potion> compatTag(String name) {
      return ForgeTagHandler.createOptionalTag(ForgeRegistries.POTION_TYPES, new ResourceLocation("forge", name));
    }
  }
}
