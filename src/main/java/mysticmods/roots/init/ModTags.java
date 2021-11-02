package mysticmods.roots.init;

import com.tterrag.registrate.providers.ProviderType;
import mysticmods.roots.RootsTags;
import noobanidus.libs.noobutil.data.TagBuilder;

import static mysticmods.roots.Roots.REGISTRATE;
import static mysticmods.roots.RootsTags.*;

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
    });
  }

  public static void load() {
  }
}
