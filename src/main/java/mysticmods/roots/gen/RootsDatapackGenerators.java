package mysticmods.roots.gen;

import com.mojang.datafixers.util.Pair;
import mysticmods.roots.Roots;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.block.WildRootsBlock;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModEntities;
import mysticmods.roots.init.ModFeatures;
import mysticmods.roots.worldgen.features.placements.DimensionPlacement;
import mysticmods.roots.worldgen.features.placements.HeightmapYRange;
import mysticmods.roots.worldgen.predicate.MatchingTreeTrunkPredicate;
import mysticmods.roots.worldgen.structure.StandingStonesStructure;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = "roots", bus = EventBusSubscriber.Bus.MOD)
public class RootsDatapackGenerators {
  // Salts used for the randomization of structure placements
  public static final int HUT_SALT = 8266497;
  public static final int BARROW_SALT = 314159223;
  public static final int STANDING_STONES_SALT = 14987612;

  @SubscribeEvent
  public static void onGatherData(GatherDataEvent event) {
    CompletableFuture<HolderLookup.Provider> lookupProvider = event.getGenerator().addProvider(
        event.includeServer(),
        (DataProvider.Factory<DatapackBuiltinEntriesProvider>) output -> new DatapackBuiltinEntriesProvider(
            event.getGenerator().getPackOutput(),
            event.getLookupProvider(),
            new RegistrySetBuilder()
                .add(Registries.CONFIGURED_FEATURE, bootstrap -> {
                  HolderGetter<PlacedFeature> placedFeatures = bootstrap.lookup(Registries.PLACED_FEATURE);
                  bootstrap.register(ModFeatures.CONFIGURED_HUGE_BAFFLECAP_KEY, new ConfiguredFeature<>(Feature.HUGE_RED_MUSHROOM, new HugeMushroomFeatureConfiguration(BlockStateProvider.simple(ModBlocks.BAFFLECAP_BLOCK.get().defaultBlockState().setValue(HugeMushroomBlock.DOWN, false)), BlockStateProvider.simple(Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.UP, Boolean.FALSE).setValue(HugeMushroomBlock.DOWN, Boolean.FALSE)), 2)));
                  bootstrap.register(ModFeatures.CONFIGURED_WILD_ROOTS_KEY, new ConfiguredFeature<>(ModFeatures.SUPPORTING_DIRECTIONAL_BLOCK_FEATURE.get(), new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_ROOTS.get().defaultBlockState()))));
                  bootstrap.register(ModFeatures.CONFIGURED_WILD_ROOTS_MOSSY_KEY, new ConfiguredFeature<>(ModFeatures.SUPPORTING_DIRECTIONAL_BLOCK_FEATURE.get(), new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_ROOTS.get().defaultBlockState().setValue(WildRootsBlock.MOSSY, true)))));
                  bootstrap.register(ModFeatures.CONFIGURED_WILD_AUBERGINE_KEY, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_AUBERGINE.get().defaultBlockState()))));
                  bootstrap.register(ModFeatures.CONFIGURED_WILDWOOD_TREE_KEY, new ConfiguredFeature<>(Feature.TREE, ModFeatures.createWildwood().build()));
                  bootstrap.register(ModFeatures.CONFIGURED_WILDWOOD_TREE_BEES_KEY, new ConfiguredFeature<>(Feature.TREE, ModFeatures.createWildwood().decorators(List.of(new BeehiveDecorator(1.0F))).build()));
                  bootstrap.register(ModFeatures.CONFIGURED_WILD_AUBERGINE_PATCH_KEY, new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(20, 2, 2, placedFeatures.getOrThrow(ModFeatures.PLACED_WILD_AUBERGINE_KEY))));
                })
                .add(Registries.PLACED_FEATURE, bootstrap -> {
                  HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = bootstrap.lookup(Registries.CONFIGURED_FEATURE);
                  bootstrap.register(ModFeatures.PLACED_WILD_AUBERGINE_KEY, new PlacedFeature(configuredFeatures.getOrThrow(ModFeatures.CONFIGURED_WILD_AUBERGINE_KEY), List.of(BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.wouldSurvive(ModBlocks.WILD_AUBERGINE.get().defaultBlockState(), BlockPos.ZERO))))));
                  // THESE DO!
                  bootstrap.register(ModFeatures.PLACED_WILD_ROOTS_UNDERGROUND_KEY, new PlacedFeature(configuredFeatures.getOrThrow(ModFeatures.CONFIGURED_WILD_ROOTS_KEY), List.of(
                      CountPlacement.of(40), // How many attempts per chunk
                      InSquarePlacement.spread(), // Randomize x/z to random spot in chunk
                      new HeightmapYRange(ConstantHeight.of(VerticalAnchor.absolute(-32)), Heightmap.Types.WORLD_SURFACE_WG) // Pick spot between y = 6 and heightmap of terrain above
                  )));
                  // TODO: These do not
                  bootstrap.register(ModFeatures.PLACED_WILD_ROOTS_FOREST_KEY, new PlacedFeature(configuredFeatures.getOrThrow(ModFeatures.CONFIGURED_WILD_ROOTS_KEY), List.of(
                      BiomeFilter.biome(),
                      CountPlacement.of(254), // How many attempts per chunk
                      InSquarePlacement.spread(), // Randomize x/z to random spot in chunk
                      HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), // Find surface
                      RandomOffsetPlacement.vertical(ConstantInt.of(1)), // Offset up one to above surface
                      BlockPredicateFilter.forPredicate(MatchingTreeTrunkPredicate.create()), // Check if we are at a tree's log.
                      CountPlacement.of(254), // make 5 new attempts for each position at the log
                      RandomOffsetPlacement.of(UniformInt.of(-2, 2), UniformInt.of(-2, 0)) // Randomize root position to a range of 2 on x/z and can be 0-2 blocks below the log y defaultValue.
                  )));
                  bootstrap.register(ModFeatures.PLACED_WILD_ROOTS_SPARSE_KEY, new PlacedFeature(configuredFeatures.getOrThrow(ModFeatures.CONFIGURED_WILD_ROOTS_KEY), List.of(
                      BiomeFilter.biome(),
                      CountPlacement.of(30), // How many attempts per chunk
                      InSquarePlacement.spread(), // Randomize x/z to random spot in chunk
                      HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), // Find surface
                      RandomOffsetPlacement.vertical(ConstantInt.of(1)), // Offset up one to above surface
                      BlockPredicateFilter.forPredicate(MatchingTreeTrunkPredicate.create()), // Check if we are at a tree's log.
                      CountPlacement.of(3), // make 5 new attempts for each position at the log
                      RandomOffsetPlacement.of(UniformInt.of(-2, 2), UniformInt.of(-2, 0)) // Randomize root position to a range of 2 on x/z and can be 0-2 blocks below the log y defaultValue.
                  )));
                  bootstrap.register(ModFeatures.PLACED_WILD_AUBERGINE_PATCH_KEY, new PlacedFeature(configuredFeatures.getOrThrow(ModFeatures.CONFIGURED_WILD_AUBERGINE_PATCH_KEY), List.of(CountPlacement.of(1), InSquarePlacement.spread(),
                      HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), // Find surface
                      RarityFilter.onAverageOnceEvery(80),
                      DimensionPlacement.of(Set.of(Level.OVERWORLD))
                  )));
                })
                .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, bootstrap -> {
                  HolderGetter<Biome> biomeGetter = bootstrap.lookup(Registries.BIOME);
                  HolderGetter<PlacedFeature> placedGetter = bootstrap.lookup(Registries.PLACED_FEATURE);
                  bootstrap.register(ModFeatures.BEETLE_SPAWNS, new BiomeModifiers.AddSpawnsBiomeModifier(biomeGetter.getOrThrow(RootsTags.Biomes.HAS_BEETLE_SPAWNS), List.of(new MobSpawnSettings.SpawnerData(ModEntities.BEETLE.get(), 5, 2, 4))));
                  bootstrap.register(ModFeatures.DEER_SPAWNS, new BiomeModifiers.AddSpawnsBiomeModifier(biomeGetter.getOrThrow(RootsTags.Biomes.HAS_DEER_SPAWNS), List.of(new MobSpawnSettings.SpawnerData(ModEntities.DEER.get(), 6, 2, 4))));
                  bootstrap.register(ModFeatures.DUCK_SPAWNS, new BiomeModifiers.AddSpawnsBiomeModifier(biomeGetter.getOrThrow(RootsTags.Biomes.HAS_DUCK_SPAWNS), List.of(new MobSpawnSettings.SpawnerData(ModEntities.DUCK.get(), 5, 1, 3))));
                  bootstrap.register(ModFeatures.FENNEC_SPAWNS, new BiomeModifiers.AddSpawnsBiomeModifier(biomeGetter.getOrThrow(RootsTags.Biomes.HAS_FENNEC_SPAWNS), List.of(new MobSpawnSettings.SpawnerData(ModEntities.FENNEC.get(), 4, 1, 3))));
                  bootstrap.register(ModFeatures.OWL_SPAWNS, new BiomeModifiers.AddSpawnsBiomeModifier(biomeGetter.getOrThrow(RootsTags.Biomes.HAS_OWL_SPAWNS), List.of(new MobSpawnSettings.SpawnerData(ModEntities.OWL.get(), 9, 1, 3))));
                  bootstrap.register(ModFeatures.SPROUT_SPAWNS, new BiomeModifiers.AddSpawnsBiomeModifier(biomeGetter.getOrThrow(RootsTags.Biomes.HAS_SPROUT_SPAWNS), List.of(new MobSpawnSettings.SpawnerData(ModEntities.GREEN_SPROUT.get(), 2, 3, 6), new MobSpawnSettings.SpawnerData(ModEntities.RED_SPROUT.get(), 2, 3, 6), new MobSpawnSettings.SpawnerData(ModEntities.TAN_SPROUT.get(), 2, 3, 6), new MobSpawnSettings.SpawnerData(ModEntities.PURPLE_SPROUT.get(), 2, 3, 6))));
                  bootstrap.register(ModFeatures.WILD_AUBERGINES_KEY, new BiomeModifiers.AddFeaturesBiomeModifier(biomeGetter.getOrThrow(RootsTags.Biomes.WILD_AUBERGINE_BIOMES), HolderSet.direct(placedGetter.getOrThrow(ModFeatures.PLACED_WILD_AUBERGINE_KEY)), GenerationStep.Decoration.TOP_LAYER_MODIFICATION));
                  bootstrap.register(ModFeatures.WILD_ROOTS_FOREST_KEY, new BiomeModifiers.AddFeaturesBiomeModifier(biomeGetter.getOrThrow(RootsTags.Biomes.WILD_ROOTS_FOREST_BIOMES), HolderSet.direct(placedGetter.getOrThrow(ModFeatures.PLACED_WILD_ROOTS_FOREST_KEY)), GenerationStep.Decoration.TOP_LAYER_MODIFICATION));
                  bootstrap.register(ModFeatures.WILD_ROOTS_UNDERGROUND_KEY, new BiomeModifiers.AddFeaturesBiomeModifier(biomeGetter.getOrThrow(RootsTags.Biomes.WILD_ROOTS_UNDERGROUND_BIOMES), HolderSet.direct(placedGetter.getOrThrow(ModFeatures.PLACED_WILD_ROOTS_UNDERGROUND_KEY)), GenerationStep.Decoration.UNDERGROUND_DECORATION));
                })
                .add(Registries.TEMPLATE_POOL, bootstrap -> {
                  HolderGetter<StructureTemplatePool> getter = bootstrap.lookup(Registries.TEMPLATE_POOL);
                  Holder<StructureTemplatePool> holder = getter.getOrThrow(Pools.EMPTY);
                  bootstrap.register(ModFeatures.BARROW_START_POOL_KEY, new StructureTemplatePool(holder, List.of(Pair.of(StructurePoolElement.single("roots:barrow1"), 1)), StructureTemplatePool.Projection.RIGID));
                  bootstrap.register(ModFeatures.BARROW_DOWN_POOL_KEY, new StructureTemplatePool(holder, List.of(Pair.of(StructurePoolElement.single("roots:barrow2"), 1)), StructureTemplatePool.Projection.RIGID));
                  bootstrap.register(ModFeatures.HUT_START_POOL_KEY, new StructureTemplatePool(holder, List.of(Pair.of(StructurePoolElement.single("roots:hut_top"), 1)), StructureTemplatePool.Projection.RIGID));
                  bootstrap.register(ModFeatures.HUT_DOWN_POOL_KEY, new StructureTemplatePool(holder, List.of(Pair.of(StructurePoolElement.single("roots:hut_bottom1"), 1), Pair.of(StructurePoolElement.single("roots:hut_bottom2"), 1), Pair.of(StructurePoolElement.single("roots:hut_bottom3"), 1), Pair.of(StructurePoolElement.single("roots:hut_bottom4"), 1), Pair.of(StructurePoolElement.single("roots:hut_bottom5"), 1), Pair.of(StructurePoolElement.single("roots:hut_bottom6"), 1), Pair.of(StructurePoolElement.single("roots:hut_bottom7"), 1), Pair.of(StructurePoolElement.single("roots:hut_bottom8"), 1), Pair.of(StructurePoolElement.single("roots:hut_bottom9"), 1), Pair.of(StructurePoolElement.single("roots:hut_bottom10"), 1), Pair.of(StructurePoolElement.single("roots:hut_bottom11"), 1), Pair.of(StructurePoolElement.single("roots:hut_bottom12"), 1)), StructureTemplatePool.Projection.RIGID));
                  bootstrap.register(ModFeatures.RUINED_HUT_START_POOL_KEY, new StructureTemplatePool(holder, List.of(Pair.of(StructurePoolElement.single("roots:hut_top_ruined"), 1)), StructureTemplatePool.Projection.RIGID));
                  bootstrap.register(ModFeatures.RUINED_HUT_DOWN_POOL_KEY, new StructureTemplatePool(holder, List.of(Pair.of(StructurePoolElement.single("roots:hut_basement_ruined1"), 1), Pair.of(StructurePoolElement.single("roots:hut_basement_ruined2"), 1), Pair.of(StructurePoolElement.single("roots:hut_basement_ruined3"), 1)), StructureTemplatePool.Projection.RIGID));
                })
                .add(Registries.STRUCTURE, bootstrap -> {
                  HolderGetter<Biome> biomeGetter = bootstrap.lookup(Registries.BIOME);
                  HolderGetter<StructureTemplatePool> poolGetter = bootstrap.lookup(Registries.TEMPLATE_POOL);
                  bootstrap.register(ModFeatures.BARROW_KEY, new JigsawStructure(new Structure.StructureSettings(biomeGetter.getOrThrow(RootsTags.Biomes.HAS_BARROW_STRUCTURE), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.ENCAPSULATE), poolGetter.getOrThrow(ModFeatures.BARROW_START_POOL_KEY), Optional.empty(), 1, ConstantHeight.of(VerticalAnchor.absolute(0)), false, Optional.of(Heightmap.Types.WORLD_SURFACE_WG), 80, List.of(), DimensionPadding.ZERO, LiquidSettings.IGNORE_WATERLOGGING));
                  bootstrap.register(ModFeatures.HUT_KEY, new JigsawStructure(new Structure.StructureSettings(biomeGetter.getOrThrow(RootsTags.Biomes.HAS_HUT_STRUCTURES), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN), poolGetter.getOrThrow(ModFeatures.HUT_START_POOL_KEY), Optional.empty(), 1, ConstantHeight.of(VerticalAnchor.absolute(0)), false, Optional.of(Heightmap.Types.WORLD_SURFACE_WG), 80, List.of(), DimensionPadding.ZERO, LiquidSettings.IGNORE_WATERLOGGING));
                  bootstrap.register(ModFeatures.RUINED_HUT_KEY, new JigsawStructure(new Structure.StructureSettings(biomeGetter.getOrThrow(RootsTags.Biomes.HAS_HUT_STRUCTURES), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN), poolGetter.getOrThrow(ModFeatures.RUINED_HUT_START_POOL_KEY), Optional.empty(), 1, ConstantHeight.of(VerticalAnchor.absolute(0)), false, Optional.of(Heightmap.Types.WORLD_SURFACE_WG), 80, List.of(), DimensionPadding.ZERO, LiquidSettings.IGNORE_WATERLOGGING));
                  bootstrap.register(ModFeatures.STANDING_STONES_KEY, new StandingStonesStructure(new Structure.StructureSettings(biomeGetter.getOrThrow(RootsTags.Biomes.HAS_STANDING_STONES), Map.of(MobCategory.CREATURE, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create(new MobSpawnSettings.SpawnerData(ModEntities.GREEN_SPROUT.get(), 1, 3, 4), new MobSpawnSettings.SpawnerData(ModEntities.PURPLE_SPROUT.get(), 1, 3, 4), new MobSpawnSettings.SpawnerData(ModEntities.RED_SPROUT.get(), 1, 3, 4), new MobSpawnSettings.SpawnerData(ModEntities.TAN_SPROUT.get(), 1, 3, 4)))), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN)));
                })
                .add(Registries.STRUCTURE_SET, bootstrap -> {
                  HolderGetter<Structure> structureGetter = bootstrap.lookup(Registries.STRUCTURE);
                  bootstrap.register(ModFeatures.BARROW_SET_KEY, new StructureSet(structureGetter.getOrThrow(ModFeatures.BARROW_KEY), new RandomSpreadStructurePlacement(150, 65, RandomSpreadType.LINEAR, BARROW_SALT)));
                  bootstrap.register(ModFeatures.STANDING_STONES_SET_KEY, new StructureSet(structureGetter.getOrThrow(ModFeatures.STANDING_STONES_KEY), new RandomSpreadStructurePlacement(80, 35, RandomSpreadType.LINEAR, STANDING_STONES_SALT)));
                  bootstrap.register(ModFeatures.HUT_SET_KEY, new StructureSet(List.of(new StructureSet.StructureSelectionEntry(structureGetter.getOrThrow(ModFeatures.HUT_KEY), 1), new StructureSet.StructureSelectionEntry(structureGetter.getOrThrow(ModFeatures.RUINED_HUT_KEY), 1)), new RandomSpreadStructurePlacement(70, 35, RandomSpreadType.LINEAR, HUT_SALT)));

                }),
            Set.of(RootsAPI.MODID)
        )
    ).getRegistryProvider();
  }
}
