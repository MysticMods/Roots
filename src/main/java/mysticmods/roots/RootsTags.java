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
    public static Tags.IOptionalNamedTag<Block> CROPS = modTag("crops");
    public static Tags.IOptionalNamedTag<Block> CLOUD_BERRY_CROP = modTag("crops/cloud_berry");
    public static Tags.IOptionalNamedTag<Block> DEWGONIA_CROP = modTag("crops/dewgonia");
    public static Tags.IOptionalNamedTag<Block> SPIRIT_HERB_CROP = modTag("crops/spirit_herb");
    public static Tags.IOptionalNamedTag<Block> STALICRIPE_CROP = modTag("crops/stalicripe");
    public static Tags.IOptionalNamedTag<Block> WILDEWHEET_CROP = modTag("crops/wildewheet");
    public static Tags.IOptionalNamedTag<Block> WILDROOT_CROP = modTag("crops/wildroot");
    public static Tags.IOptionalNamedTag<Block> INFERNAL_BULB_CROP = modTag("crops/infernal_bulb");
    public static Tags.IOptionalNamedTag<Block> MOONGLOW_LEAF_CROP = modTag("crops/moonglow_leaf");
    public static Tags.IOptionalNamedTag<Block> PERESKIA_CROP = modTag("crops/pereskia");

    public static Tags.IOptionalNamedTag<Block> RUNED_OBSIDIAN = modTag("runed_obsidian");
    public static Tags.IOptionalNamedTag<Block> RUNESTONE = modTag("runestone");

    public static Tags.IOptionalNamedTag<Block> SOILS = modTag("soils");
    public static Tags.IOptionalNamedTag<Block> WATER_SOIL = modTag("soils/water");
    public static Tags.IOptionalNamedTag<Block> AIR_SOIL = modTag("soils/air");
    public static Tags.IOptionalNamedTag<Block> EARTH_SOIL = modTag("soils/earth");
    public static Tags.IOptionalNamedTag<Block> FIRE_SOIL = modTag("soils/fire");
    public static Tags.IOptionalNamedTag<Block> ELEMENTAL_SOIL = modTag("soils/elemental");

    public static Tags.IOptionalNamedTag<Block> WILDWOOD_LOGS = modTag("wildwood_logs");

    // Specific types of Runed Logs
    public static Tags.IOptionalNamedTag<Block> RUNED_LOGS = modTag("runed_logs");
    public static Tags.IOptionalNamedTag<Block> RUNED_ACACIA_LOG = modTag("runed_logs/acacia");
    public static Tags.IOptionalNamedTag<Block> RUNED_DARK_OAK_LOG = modTag("runed_logs/dark_oak");
    public static Tags.IOptionalNamedTag<Block> RUNED_OAK_LOG = modTag("runed_logs/oak");
    public static Tags.IOptionalNamedTag<Block> RUNED_BIRCH_LOG = modTag("runed_logs/birch");
    public static Tags.IOptionalNamedTag<Block> RUNED_JUNGLE_LOG = modTag("runed_logs/jungle");
    public static Tags.IOptionalNamedTag<Block> RUNED_SPRUCE_LOG = modTag("runed_logs/spruce");
    public static Tags.IOptionalNamedTag<Block> RUNED_WILDWOOD_LOG = modTag("runed_logs/wildwood");
    public static Tags.IOptionalNamedTag<Block> RUNED_CRIMSON_STEM = modTag("runed_logs/crimson");
    public static Tags.IOptionalNamedTag<Block> RUNED_WARPED_STEM = modTag("runed_logs/warped");

    static Tags.IOptionalNamedTag<Block> modTag(String name) {
      return BlockTags.createOptional(new ResourceLocation(Roots.MODID, name));
    }

    static Tags.IOptionalNamedTag<Block> compatTag(String name) {
      return BlockTags.createOptional(new ResourceLocation("forge", name));
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

    static Tags.IOptionalNamedTag<Item> modTag(String name)  {
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
