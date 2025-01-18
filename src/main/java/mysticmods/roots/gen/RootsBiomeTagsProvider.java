package mysticmods.roots.gen;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RootsBiomeTagsProvider extends BiomeTagsProvider {
  public RootsBiomeTagsProvider(PackOutput arg, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
    super(arg, completableFuture, RootsAPI.MODID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    //noinspection unchecked
    this.tag(RootsTags.Biomes.HAS_BARROW_STRUCTURE).addTags(BiomeTags.IS_SAVANNA, BiomeTags.HAS_VILLAGE_PLAINS, Tags.Biomes.IS_PLAINS);

    //noinspection unchecked
    this.tag(RootsTags.Biomes.HAS_BEETLE_SPAWNS).addTags(BiomeTags.IS_JUNGLE, BiomeTags.HAS_JUNGLE_TEMPLE, BiomeTags.HAS_VILLAGE_PLAINS, BiomeTags.IS_FOREST, BiomeTags.HAS_RUINED_PORTAL_SWAMP, Tags.Biomes.IS_FOREST, Tags.Biomes.IS_JUNGLE, Tags.Biomes.IS_SWAMP, Tags.Biomes.IS_PLAINS);

    //noinspection unchecked
    this.tag(RootsTags.Biomes.HAS_DEER_SPAWNS).addTags(BiomeTags.IS_FOREST, BiomeTags.IS_TAIGA, BiomeTags.HAS_VILLAGE_PLAINS, Tags.Biomes.IS_TAIGA, Tags.Biomes.IS_PLAINS, Tags.Biomes.IS_COLD_OVERWORLD);

    //noinspection unchecked
    this.tag(RootsTags.Biomes.HAS_DUCK_SPAWNS).addTags(BiomeTags.IS_RIVER, BiomeTags.IS_BEACH, BiomeTags.HAS_RUINED_PORTAL_SWAMP, Tags.Biomes.IS_SWAMP, Tags.Biomes.IS_RIVER, Tags.Biomes.IS_BEACH);

    //noinspection unchecked
    this.tag(RootsTags.Biomes.HAS_FENNEC_SPAWNS).addTags(BiomeTags.IS_BADLANDS, BiomeTags.HAS_DESERT_PYRAMID, BiomeTags.IS_SAVANNA, Tags.Biomes.IS_DESERT, Tags.Biomes.IS_SAVANNA, Tags.Biomes.IS_BADLANDS, Tags.Biomes.IS_SANDY);

    //noinspection unchecked
    this.tag(RootsTags.Biomes.HAS_HUT_STRUCTURES).addTags(BiomeTags.IS_SAVANNA, BiomeTags.HAS_VILLAGE_PLAINS, Tags.Biomes.IS_PLAINS);

    //noinspection unchecked
    this.tag(RootsTags.Biomes.HAS_OWL_SPAWNS).addTags(BiomeTags.IS_FOREST, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_TAIGA, BiomeTags.HAS_VILLAGE_SNOWY, Tags.Biomes.IS_FOREST, Tags.Biomes.IS_TAIGA, Tags.Biomes.IS_COLD_OVERWORLD, Tags.Biomes.IS_MOUNTAIN);

    //noinspection unchecked
    this.tag(RootsTags.Biomes.HAS_SPROUT_SPAWNS).addTags(BiomeTags.IS_FOREST, BiomeTags.IS_RIVER, BiomeTags.IS_BEACH, BiomeTags.HAS_RUINED_PORTAL_SWAMP, BiomeTags.HAS_JUNGLE_TEMPLE, BiomeTags.IS_JUNGLE, Tags.Biomes.IS_FOREST, Tags.Biomes.IS_RIVER, Tags.Biomes.IS_BEACH);
    //noinspection unchecked
    this.tag(RootsTags.Biomes.HAS_STANDING_STONES).addTags(BiomeTags.IS_SAVANNA, BiomeTags.HAS_VILLAGE_PLAINS, Tags.Biomes.IS_PLAINS);
    //noinspection unchecked
    this.tag(RootsTags.Biomes.WILD_AUBERGINE_BIOMES).addTags(BiomeTags.IS_FOREST, BiomeTags.HAS_VILLAGE_PLAINS, Tags.Biomes.IS_FOREST, Tags.Biomes.IS_PLAINS);
    //noinspection unchecked
    this.tag(RootsTags.Biomes.WILD_ROOTS_FOREST_BIOMES).addTags(BiomeTags.IS_TAIGA, BiomeTags.IS_JUNGLE, BiomeTags.IS_SAVANNA, BiomeTags.IS_FOREST, BiomeTags.HAS_RUINED_PORTAL_SWAMP, Tags.Biomes.IS_CONIFEROUS_TREE, Tags.Biomes.IS_JUNGLE_TREE, Tags.Biomes.IS_DECIDUOUS_TREE, Tags.Biomes.IS_SWAMP);
    //noinspection unchecked
    this.tag(RootsTags.Biomes.WILD_ROOTS_SPARSE_BIOMES).addTags(BiomeTags.HAS_VILLAGE_PLAINS, Tags.Biomes.IS_PLAINS);
    //noinspection unchecked
    this.tag(RootsTags.Biomes.WILD_ROOTS_UNDERGROUND_BIOMES).addTags(BiomeTags.IS_OVERWORLD, Tags.Biomes.IS_OVERWORLD);

    //noinspection unchecked
    this.tag(RootsTags.Biomes.HAS_GRANITE_QUARTZ_ORE).addTags(BiomeTags.IS_OVERWORLD, Tags.Biomes.IS_OVERWORLD);
    //noinspection unchecked
    this.tag(RootsTags.Biomes.HAS_GRANITE_QUARTZ_ORE).addTags(BiomeTags.IS_OVERWORLD, Tags.Biomes.IS_OVERWORLD);

    this.tag(RootsTags.Biomes.HAS_STONEPETAL).addTag(Tags.Biomes.IS_MOUNTAIN);
  }

  @Override
  public String getName() {
    return "Roots Biome Tags";
  }
}