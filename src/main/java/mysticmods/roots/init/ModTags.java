package mysticmods.roots.init;

import com.tterrag.registrate.providers.ProviderType;
import mysticmods.roots.RootsTags;
import noobanidus.libs.noobutil.data.TagBuilder;

import static mysticmods.roots.Roots.REGISTRATE;

@SuppressWarnings("unchecked")
public class ModTags {
  static {
    REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, p -> {
      TagBuilder.BlocksBuilder b = new TagBuilder.BlocksBuilder(p);
      b.add(RootsTags.Blocks.SOILS, RootsTags.Blocks.EARTH_SOIL, RootsTags.Blocks.AIR_SOIL, RootsTags.Blocks.FIRE_SOIL, RootsTags.Blocks.WATER_SOIL, RootsTags.Blocks.ELEMENTAL_SOIL);
      b.add(RootsTags.Blocks.RUNED_LOGS, RootsTags.Blocks.RUNED_ACACIA_LOG, RootsTags.Blocks.RUNED_BIRCH_LOG, RootsTags.Blocks.RUNED_OAK_LOG, RootsTags.Blocks.RUNED_DARK_OAK_LOG, RootsTags.Blocks.RUNED_CRIMSON_STEM, RootsTags.Blocks.RUNED_JUNGLE_LOG, RootsTags.Blocks.RUNED_SPRUCE_LOG, RootsTags.Blocks.RUNED_WARPED_STEM, RootsTags.Blocks.RUNED_WILDWOOD_LOG);
    });
  }

  public static void load() {
  }
}
