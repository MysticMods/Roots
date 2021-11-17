package mysticmods.roots.init;

import com.tterrag.registrate.providers.ProviderType;
import mysticmods.roots.RootsTags;
import noobanidus.libs.noobutil.data.TagBuilder;

import static mysticmods.roots.Roots.REGISTRATE;
import static mysticmods.roots.RootsTags.Blocks;

@SuppressWarnings("unchecked")
public class ModTags {
  static {
    REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, p -> {
      TagBuilder.BlocksBuilder b = new TagBuilder.BlocksBuilder(p);
      // Internal mod tags
      b.add(Blocks.SOILS, Blocks.EARTH_SOIL, Blocks.AIR_SOIL, Blocks.FIRE_SOIL, Blocks.WATER_SOIL, Blocks.ELEMENTAL_SOIL);
/*      b.add(Blocks.RUNED_LOGS, Blocks.RUNED_ACACIA_LOG, Blocks.RUNED_BIRCH_LOG, Blocks.RUNED_OAK_LOG, Blocks.RUNED_DARK_OAK_LOG, Blocks.RUNED_CRIMSON_STEM, Blocks.RUNED_JUNGLE_LOG, Blocks.RUNED_SPRUCE_LOG, Blocks.RUNED_WARPED_STEM, Blocks.RUNED_WILDWOOD_LOG);*/
      b.add(Blocks.AIR_CROPS, Blocks.CLOUD_BERRY_CROP);
      b.add(Blocks.FIRE_CROPS, Blocks.INFERNAL_BULB_CROP);
      b.add(Blocks.WATER_CROPS, Blocks.DEWGONIA_CROP);
      b.add(Blocks.EARTH_CROPS, Blocks.STALICRIPE_CROP);
      b.add(Blocks.ELEMENTAL_CROPS, Blocks.WATER_CROPS, Blocks.AIR_CROPS, Blocks.EARTH_CROPS, Blocks.FIRE_CROPS);
      b.add(Blocks.CROPS, Blocks.WILDEWHEET_CROP, Blocks.WILDROOT_CROP, Blocks.SPIRIT_HERB_CROP, Blocks.PERESKIA_CROP, Blocks.MOONGLOW_LEAF_CROP, Blocks.ELEMENTAL_CROPS);

      // Forge compat tags
      b.add(Blocks.FORGE_CROPS, Blocks.CROPS);
    });

    REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, p -> {
      // Block copies

      TagBuilder.ItemsBuilder b = new TagBuilder.ItemsBuilder(p);
      b.addBlocks(RootsTags.Items.Blocks.CROPS, RootsTags.Blocks.CROPS);
      b.addBlocks(RootsTags.Items.Blocks.ELEMENTAL_CROPS, RootsTags.Blocks.ELEMENTAL_CROPS);
      b.addBlocks(RootsTags.Items.Blocks.WATER_CROPS, RootsTags.Blocks.WATER_CROPS);
      b.addBlocks(RootsTags.Items.Blocks.EARTH_CROPS, RootsTags.Blocks.EARTH_CROPS);
      b.addBlocks(RootsTags.Items.Blocks.AIR_CROPS, RootsTags.Blocks.AIR_CROPS);
      b.addBlocks(RootsTags.Items.Blocks.FIRE_CROPS, RootsTags.Blocks.FIRE_CROPS);
      b.addBlocks(RootsTags.Items.Blocks.CLOUD_BERRY_CROP, RootsTags.Blocks.CLOUD_BERRY_CROP);
      b.addBlocks(RootsTags.Items.Blocks.DEWGONIA_CROP, RootsTags.Blocks.DEWGONIA_CROP);
      b.addBlocks(RootsTags.Items.Blocks.SPIRIT_HERB_CROP, RootsTags.Blocks.SPIRIT_HERB_CROP);
      b.addBlocks(RootsTags.Items.Blocks.STALICRIPE_CROP, RootsTags.Blocks.STALICRIPE_CROP);
      b.addBlocks(RootsTags.Items.Blocks.WILDEWHEET_CROP, RootsTags.Blocks.WILDEWHEET_CROP);
      b.addBlocks(RootsTags.Items.Blocks.WILDROOT_CROP, RootsTags.Blocks.WILDROOT_CROP);
      b.addBlocks(RootsTags.Items.Blocks.INFERNAL_BULB_CROP, RootsTags.Blocks.INFERNAL_BULB_CROP);
      b.addBlocks(RootsTags.Items.Blocks.MOONGLOW_LEAF_CROP, RootsTags.Blocks.MOONGLOW_LEAF_CROP);
      b.addBlocks(RootsTags.Items.Blocks.PERESKIA_CROP, RootsTags.Blocks.PERESKIA_CROP);
      b.addBlocks(RootsTags.Items.Blocks.SOILS, RootsTags.Blocks.SOILS);
      b.addBlocks(RootsTags.Items.Blocks.WATER_SOIL, RootsTags.Blocks.WATER_SOIL);
      b.addBlocks(RootsTags.Items.Blocks.AIR_SOIL, RootsTags.Blocks.AIR_SOIL);
      b.addBlocks(RootsTags.Items.Blocks.EARTH_SOIL, RootsTags.Blocks.EARTH_SOIL);
      b.addBlocks(RootsTags.Items.Blocks.FIRE_SOIL, RootsTags.Blocks.FIRE_SOIL);
      b.addBlocks(RootsTags.Items.Blocks.ELEMENTAL_SOIL, RootsTags.Blocks.ELEMENTAL_SOIL);
      b.addBlocks(RootsTags.Items.Blocks.RUNED_OBSIDIAN, RootsTags.Blocks.RUNED_OBSIDIAN);
      b.addBlocks(RootsTags.Items.Blocks.RUNESTONE, RootsTags.Blocks.RUNESTONE);
/*      b.addBlocks(RootsTags.Items.Blocks.WILDWOOD_LOGS, RootsTags.Blocks.WILDWOOD_LOGS);*/
/*      b.addBlocks(RootsTags.Items.Blocks.RUNED_LOGS, RootsTags.Blocks.RUNED_LOGS);
      b.addBlocks(RootsTags.Items.Blocks.RUNED_ACACIA_LOG, RootsTags.Blocks.RUNED_ACACIA_LOG);
      b.addBlocks(RootsTags.Items.Blocks.RUNED_DARK_OAK_LOG, RootsTags.Blocks.RUNED_DARK_OAK_LOG);
      b.addBlocks(RootsTags.Items.Blocks.RUNED_OAK_LOG, RootsTags.Blocks.RUNED_OAK_LOG);
      b.addBlocks(RootsTags.Items.Blocks.RUNED_BIRCH_LOG, RootsTags.Blocks.RUNED_BIRCH_LOG);
      b.addBlocks(RootsTags.Items.Blocks.RUNED_JUNGLE_LOG, RootsTags.Blocks.RUNED_JUNGLE_LOG);
      b.addBlocks(RootsTags.Items.Blocks.RUNED_SPRUCE_LOG, RootsTags.Blocks.RUNED_SPRUCE_LOG);
      b.addBlocks(RootsTags.Items.Blocks.RUNED_WILDWOOD_LOG, RootsTags.Blocks.RUNED_WILDWOOD_LOG);
      b.addBlocks(RootsTags.Items.Blocks.RUNED_CRIMSON_STEM, RootsTags.Blocks.RUNED_CRIMSON_STEM);
      b.addBlocks(RootsTags.Items.Blocks.RUNED_WARPED_STEM, RootsTags.Blocks.RUNED_WARPED_STEM);*/
/*      b.addBlocks(RootsTags.Items.Blocks.PLATE, RootsTags.Blocks.PLATE);
      b.addBlocks(RootsTags.Items.Blocks.PYRE, RootsTags.Blocks.PYRE);
      b.addBlocks(RootsTags.Items.Blocks.CRAFTER, RootsTags.Blocks.CRAFTER);
      b.addBlocks(RootsTags.Items.Blocks.IMPOSER, RootsTags.Blocks.IMPOSER);
      b.addBlocks(RootsTags.Items.Blocks.IMBUER, RootsTags.Blocks.IMBUER);
      b.addBlocks(RootsTags.Items.Blocks.MORTAR, RootsTags.Blocks.MORTAR);*/

    });
  }

  public static void load() {
  }
}
