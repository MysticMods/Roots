package mysticmods.roots.init;

import com.tterrag.registrate.providers.ProviderType;
import mysticmods.roots.RootsTags;
import net.minecraft.tags.BlockTags;
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
      b.add(Blocks.RUNED_LOGS, Blocks.RUNED_ACACIA_LOG, Blocks.RUNED_BIRCH_LOG, Blocks.RUNED_OAK_LOG, Blocks.RUNED_DARK_OAK_LOG, Blocks.RUNED_CRIMSON_STEM, Blocks.RUNED_JUNGLE_LOG, Blocks.RUNED_SPRUCE_LOG, Blocks.RUNED_WARPED_STEM, Blocks.RUNED_WILDWOOD_LOG);
      b.add(Blocks.AIR_CROPS, Blocks.CLOUD_BERRY_CROP);
      b.add(Blocks.FIRE_CROPS, Blocks.INFERNAL_BULB_CROP);
      b.add(Blocks.WATER_CROPS, Blocks.DEWGONIA_CROP);
      b.add(Blocks.EARTH_CROPS, Blocks.STALICRIPE_CROP);
      b.add(Blocks.ELEMENTAL_CROPS, Blocks.WATER_CROPS, Blocks.AIR_CROPS, Blocks.EARTH_CROPS, Blocks.FIRE_CROPS);
      b.add(Blocks.CROPS, Blocks.WILDEWHEET_CROP, Blocks.WILDROOT_CROP, Blocks.SPIRIT_HERB_CROP, Blocks.PERESKIA_CROP, Blocks.MOONGLOW_LEAF_CROP, Blocks.ELEMENTAL_CROPS);

      // Forge compat tags
      b.add(Blocks.FORGE_CROPS, Blocks.CROPS);

      // Logs that burn
      b.add(Blocks.MINECRAFT_LOGS_THAT_BURN, Blocks.WILDWOOD_LOGS);
      b.add(Blocks.MINECRAFT_LOGS, Blocks.WILDWOOD_LOGS);
    });

    REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, p -> {
      // Block copies

      TagBuilder.ItemsBuilder b = new TagBuilder.ItemsBuilder(p);
      b.addBlocks(RootsTags.Items.Blocks.SOILS, RootsTags.Blocks.SOILS);
      b.addBlocks(RootsTags.Items.Blocks.WATER_SOIL, RootsTags.Blocks.WATER_SOIL);
      b.addBlocks(RootsTags.Items.Blocks.AIR_SOIL, RootsTags.Blocks.AIR_SOIL);
      b.addBlocks(RootsTags.Items.Blocks.EARTH_SOIL, RootsTags.Blocks.EARTH_SOIL);
      b.addBlocks(RootsTags.Items.Blocks.FIRE_SOIL, RootsTags.Blocks.FIRE_SOIL);
      b.addBlocks(RootsTags.Items.Blocks.ELEMENTAL_SOIL, RootsTags.Blocks.ELEMENTAL_SOIL);
      b.addBlocks(RootsTags.Items.Blocks.RUNED_OBSIDIAN, RootsTags.Blocks.RUNED_OBSIDIAN);
      b.addBlocks(RootsTags.Items.Blocks.RUNESTONE, RootsTags.Blocks.RUNESTONE);
      b.addBlocks(RootsTags.Items.Blocks.WILDWOOD_LOGS, RootsTags.Blocks.WILDWOOD_LOGS);
      b.addBlocks(RootsTags.Items.Blocks.RUNED_LOGS, RootsTags.Blocks.RUNED_LOGS);
      b.addBlocks(RootsTags.Items.Blocks.RUNED_ACACIA_LOG, RootsTags.Blocks.RUNED_ACACIA_LOG);
      b.addBlocks(RootsTags.Items.Blocks.RUNED_DARK_OAK_LOG, RootsTags.Blocks.RUNED_DARK_OAK_LOG);
      b.addBlocks(RootsTags.Items.Blocks.RUNED_OAK_LOG, RootsTags.Blocks.RUNED_OAK_LOG);
      b.addBlocks(RootsTags.Items.Blocks.RUNED_BIRCH_LOG, RootsTags.Blocks.RUNED_BIRCH_LOG);
      b.addBlocks(RootsTags.Items.Blocks.RUNED_JUNGLE_LOG, RootsTags.Blocks.RUNED_JUNGLE_LOG);
      b.addBlocks(RootsTags.Items.Blocks.RUNED_SPRUCE_LOG, RootsTags.Blocks.RUNED_SPRUCE_LOG);
      b.addBlocks(RootsTags.Items.Blocks.RUNED_WILDWOOD_LOG, RootsTags.Blocks.RUNED_WILDWOOD_LOG);
      b.addBlocks(RootsTags.Items.Blocks.RUNED_CRIMSON_STEM, RootsTags.Blocks.RUNED_CRIMSON_STEM);
      b.addBlocks(RootsTags.Items.Blocks.RUNED_WARPED_STEM, RootsTags.Blocks.RUNED_WARPED_STEM);

      b.add(RootsTags.Items.AIR_CROPS, RootsTags.Items.CLOUD_BERRY_CROP);
      b.add(RootsTags.Items.WATER_CROPS, RootsTags.Items.DEWGONIA_CROP);
      b.add(RootsTags.Items.EARTH_CROPS, RootsTags.Items.STALICRIPE_CROP);
      b.add(RootsTags.Items.FIRE_CROPS, RootsTags.Items.INFERNAL_BULB_CROP);

      b.add(RootsTags.Items.CROPS, RootsTags.Items.AIR_CROPS, RootsTags.Items.FIRE_CROPS, RootsTags.Items.EARTH_CROPS, RootsTags.Items.WATER_CROPS, RootsTags.Items.WILDEWHEET_CROP, RootsTags.Items.SPIRIT_HERB_CROP, RootsTags.Items.PERESKIA_CROP, RootsTags.Items.WILDROOT_CROP, RootsTags.Items.MOONGLOW_LEAF_CROP);

      // TODO: Copying crops to the actual item tag?
    });
  }

  public static void load() {
  }
}
