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
    public static Tags.IOptionalNamedTag<Block> CROPS = modTag("crops");
    public static Tags.IOptionalNamedTag<Block> CLOUD_BERRY_CROP = modTag("crops/cloud_berry");
    public static Tags.IOptionalNamedTag<Block> DEWGONIA_CROP = modTag("crops/dewgonia");
    public static Tags.IOptionalNamedTag<Block> INFERNAL_BULB_CROP = modTag("crops/infernal_bulb");
    public static Tags.IOptionalNamedTag<Block> MOONGLOW_LEAF_CROP = modTag("crops/moonglow_leaf");
    public static Tags.IOptionalNamedTag<Block> PERESKIA_CROP = modTag("crops/pereskia");
    public static Tags.IOptionalNamedTag<Block> SPIRIT_HERB_CROP = modTag("crops/spirit_herb");
    public static Tags.IOptionalNamedTag<Block> STALICRIPE_CROP = modTag("crops/stalicripe");
    public static Tags.IOptionalNamedTag<Block> WILDEWHEET_CROP = modTag("crops/wildewheet");
    public static Tags.IOptionalNamedTag<Block> WILDROOT_CROP = modTag("crops/wildroot");

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
