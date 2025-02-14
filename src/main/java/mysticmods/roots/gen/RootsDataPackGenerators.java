package mysticmods.roots.gen;

import com.mojang.datafixers.util.Pair;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.block.WildRootsBlock;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModEntities;
import mysticmods.roots.init.ModFeatures;
import mysticmods.roots.util.FakePlayerUtil;
import mysticmods.roots.worldgen.features.placements.HeightmapYRange;
import mysticmods.roots.worldgen.predicate.MatchingTreeTrunkPredicate;
import mysticmods.roots.worldgen.structure.StandingStonesStructure;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.providers.SingleEnchantment;
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
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = "roots", bus = EventBusSubscriber.Bus.MOD)
public class RootsDataPackGenerators {
  // Salts used for the randomization of structure placements
  public static final int HUT_SALT = 8266497;
  public static final int BARROW_SALT = 314159223;
  public static final int LARGE_BARROW_SALT = 41415568;
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
                  bootstrap.register(
                      ModFeatures.CONFIGURED_HUGE_BAFFLECAP_KEY,
                      new ConfiguredFeature<>(
                          Feature.HUGE_RED_MUSHROOM,
                          new HugeMushroomFeatureConfiguration(
                              BlockStateProvider.simple(ModBlocks.BAFFLECAP_BLOCK.get().defaultBlockState()
                                  .setValue(HugeMushroomBlock.DOWN, false)),
                              BlockStateProvider.simple(Blocks.MUSHROOM_STEM.defaultBlockState()
                                  .setValue(HugeMushroomBlock.UP, Boolean.FALSE)
                                  .setValue(HugeMushroomBlock.DOWN, Boolean.FALSE)), 2)));
                  bootstrap.register(ModFeatures.CONFIGURED_WILD_ROOTS_KEY,
                      new ConfiguredFeature<>(
                          ModFeatures.SUPPORTING_DIRECTIONAL_BLOCK_FEATURE.get(), new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_ROOTS.get()
                          .defaultBlockState()))));
                  // TODO: Is this used?
                  bootstrap.register(ModFeatures.CONFIGURED_WILD_ROOTS_MOSSY_KEY, new ConfiguredFeature<>(ModFeatures.SUPPORTING_DIRECTIONAL_BLOCK_FEATURE.get(), new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_ROOTS.get()
                      .defaultBlockState().setValue(WildRootsBlock.MOSSY, true)))));
                  ConfiguredFeature<?, ?> wildAubergine = new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_AUBERGINE.get()
                      .defaultBlockState())));
                  PlacedFeature placedWildAubergine = new PlacedFeature(Holder.direct(wildAubergine),
                      List.of(BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.matchesBlocks(Blocks.AIR), BlockPredicate.matchesTag(new BlockPos(0, -1, 0), RootsTags.Blocks.SUPPORTS_WILD_AUBERGINE)))));
                  bootstrap.register(
                      ModFeatures.CONFIGURED_WILD_AUBERGINE_PATCH_KEY, new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(32, 2, 2, Holder.direct(placedWildAubergine))));

                  bootstrap.register(ModFeatures.CONFIGURED_WILDWOOD_TREE_KEY, new ConfiguredFeature<>(Feature.TREE, ModFeatures.createWildwood()
                      .build()));
                  bootstrap.register(ModFeatures.CONFIGURED_WILDWOOD_TREE_BEES_KEY, new ConfiguredFeature<>(Feature.TREE, ModFeatures.createWildwood()
                      .decorators(List.of(new BeehiveDecorator(1.0F))).build()));


                  RuleTest stone = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
                  RuleTest deepslate = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
                  RuleTest granite = new TagMatchTest(RootsTags.Blocks.GRANITE_ORE_REPLACEABLES);
                  List<OreConfiguration.TargetBlockState> silverOre = List.of(
                      OreConfiguration.target(stone, ModBlocks.SILVER_ORE.get().defaultBlockState()),
                      OreConfiguration.target(deepslate, ModBlocks.DEEPSLATE_SILVER_ORE.get().defaultBlockState())
                  );
                  bootstrap.register(ModFeatures.CONFIGURED_SILVER_ORE_KEY, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(silverOre, 9)));
                  bootstrap.register(ModFeatures.CONFIGURED_GRANITE_QUARTZ_KEY, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(granite, ModBlocks.GRANITE_QUARTZ_ORE.get()
                      .defaultBlockState())), 4)));
                  ConfiguredFeature<?, ?> stonepetal = new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.STONEPETAL.get()
                      .defaultBlockState())));
                  PlacedFeature placedStonepetal = new PlacedFeature(Holder.direct(stonepetal),
                      List.of(BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.matchesBlocks(Blocks.AIR), BlockPredicate.matchesTag(new BlockPos(0, -1, 0), RootsTags.Blocks.SUPPORTS_STONEPETAL)))));

                  bootstrap.register(ModFeatures.CONFIGURED_STONEPETAL_PATCH_KEY, new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(95, 3, 4, Holder.direct(placedStonepetal))));
                })
                .add(Registries.PLACED_FEATURE, bootstrap -> {
                  HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = bootstrap.lookup(Registries.CONFIGURED_FEATURE);
                  // THESE DO!
                  bootstrap.register(ModFeatures.PLACED_WILD_ROOTS_UNDERGROUND_KEY, new PlacedFeature(configuredFeatures.getOrThrow(ModFeatures.CONFIGURED_WILD_ROOTS_KEY), List.of(
                      CountPlacement.of(40), // How many attempts per chunk
                      InSquarePlacement.spread(), // Randomize x/z to random spot in chunk
                      new HeightmapYRange(ConstantHeight.of(VerticalAnchor.absolute(-32)), Heightmap.Types.WORLD_SURFACE_WG) // Pick spot between y = 6 and heightmap of terrain above
                  )));
                  bootstrap.register(ModFeatures.PLACED_WILD_ROOTS_FOREST_KEY, new PlacedFeature(configuredFeatures.getOrThrow(ModFeatures.CONFIGURED_WILD_ROOTS_KEY), List.of(
                      BiomeFilter.biome(),
                      CountPlacement.of(4), // How many attempts per chunk
                      InSquarePlacement.spread(), // Randomize x/z to random spot in chunk
                      HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), // Find surface
                      RandomOffsetPlacement.vertical(ConstantInt.of(1)), // Offset up one to above surface
                      BlockPredicateFilter.forPredicate(MatchingTreeTrunkPredicate.create()), // Check if we are at a tree's log.
                      CountPlacement.of(5), // make 5 new attempts for each position at the log
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
                  bootstrap.register(ModFeatures.PLACED_WILD_AUBERGINE_PATCH_KEY, new PlacedFeature(configuredFeatures.getOrThrow(ModFeatures.CONFIGURED_WILD_AUBERGINE_PATCH_KEY), List.of(
                      RarityFilter.onAverageOnceEvery(168),
                      HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), // Find surface
                      BiomeFilter.biome()
                  )));
                  bootstrap.register(ModFeatures.PLACED_SILVER_ORE_KEY,
                      new PlacedFeature(configuredFeatures.getOrThrow(ModFeatures.CONFIGURED_SILVER_ORE_KEY),
                          List.of(
                              CountPlacement.of(4),
                              InSquarePlacement.spread(),
                              HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32)),
                              BiomeFilter.biome()
                          )));
                  bootstrap.register(ModFeatures.PLACED_GRANITE_QUARTZ_KEY,
                      new PlacedFeature(configuredFeatures.getOrThrow(ModFeatures.CONFIGURED_GRANITE_QUARTZ_KEY),
                          List.of(
                              CountPlacement.of(160),
                              InSquarePlacement.spread(),
                              HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(256)),
                              BiomeFilter.biome()
                          )));
                  bootstrap.register(ModFeatures.PLACED_STONEPETAL_PATCH_KEY, new PlacedFeature(configuredFeatures.getOrThrow(ModFeatures.CONFIGURED_STONEPETAL_PATCH_KEY), List.of(
                      RarityFilter.onAverageOnceEvery(32),
                      InSquarePlacement.spread(),
                      HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                      BiomeFilter.biome()
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
                  bootstrap.register(ModFeatures.WILD_AUBERGINES_KEY, new BiomeModifiers.AddFeaturesBiomeModifier(biomeGetter.getOrThrow(RootsTags.Biomes.HAS_WILD_AUBERGINES), HolderSet.direct(placedGetter.getOrThrow(ModFeatures.PLACED_WILD_AUBERGINE_PATCH_KEY)), GenerationStep.Decoration.VEGETAL_DECORATION));
                  bootstrap.register(ModFeatures.WILD_ROOTS_FOREST_KEY, new BiomeModifiers.AddFeaturesBiomeModifier(biomeGetter.getOrThrow(RootsTags.Biomes.HAS_FOREST_WILD_ROOTS), HolderSet.direct(placedGetter.getOrThrow(ModFeatures.PLACED_WILD_ROOTS_FOREST_KEY)), GenerationStep.Decoration.TOP_LAYER_MODIFICATION));
                  bootstrap.register(ModFeatures.WILD_ROOTS_UNDERGROUND_KEY, new BiomeModifiers.AddFeaturesBiomeModifier(biomeGetter.getOrThrow(RootsTags.Biomes.HAS_UNDERGROUND_WILD_ROOTS), HolderSet.direct(placedGetter.getOrThrow(ModFeatures.PLACED_WILD_ROOTS_UNDERGROUND_KEY)), GenerationStep.Decoration.UNDERGROUND_DECORATION));
                  bootstrap.register(ModFeatures.GRANITE_QUARTZ_ORE_KEY, new BiomeModifiers.AddFeaturesBiomeModifier(biomeGetter.getOrThrow(RootsTags.Biomes.HAS_GRANITE_QUARTZ_ORES), HolderSet.direct(placedGetter.getOrThrow(ModFeatures.PLACED_GRANITE_QUARTZ_KEY)), GenerationStep.Decoration.UNDERGROUND_ORES));
                  bootstrap.register(ModFeatures.SILVER_ORE_KEY, new BiomeModifiers.AddFeaturesBiomeModifier(biomeGetter.getOrThrow(RootsTags.Biomes.HAS_SILVER_ORES), HolderSet.direct(placedGetter.getOrThrow(ModFeatures.PLACED_SILVER_ORE_KEY)), GenerationStep.Decoration.UNDERGROUND_ORES));
                  bootstrap.register(ModFeatures.STONEPETAL_KEY, new BiomeModifiers.AddFeaturesBiomeModifier(biomeGetter.getOrThrow(RootsTags.Biomes.HAS_STONEPETALS), HolderSet.direct(placedGetter.getOrThrow(ModFeatures.PLACED_STONEPETAL_PATCH_KEY)), GenerationStep.Decoration.VEGETAL_DECORATION));
                })
                .add(Registries.TEMPLATE_POOL, bootstrap -> {
                  HolderGetter<StructureTemplatePool> getter = bootstrap.lookup(Registries.TEMPLATE_POOL);
                  Holder<StructureTemplatePool> holder = getter.getOrThrow(Pools.EMPTY);
                  bootstrap.register(ModFeatures.LARGE_BARROW_START_POOL_KEY, new StructureTemplatePool(holder, List.of(Pair.of(StructurePoolElement.single("roots:big_barrow"), 1)), StructureTemplatePool.Projection.RIGID));
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
                  bootstrap.register(ModFeatures.BARROW_KEY, new JigsawStructure(new Structure.StructureSettings(biomeGetter.getOrThrow(RootsTags.Biomes.HAS_BARROW_STRUCTURES), Collections.emptyMap(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_BOX), poolGetter.getOrThrow(ModFeatures.BARROW_START_POOL_KEY), Optional.empty(), 1, ConstantHeight.of(VerticalAnchor.absolute(0)), false, Optional.of(Heightmap.Types.WORLD_SURFACE_WG), 80, Collections.emptyList(), DimensionPadding.ZERO, LiquidSettings.IGNORE_WATERLOGGING));
                  // TODO: Sink it slightly, but how?
                  bootstrap.register(ModFeatures.LARGE_BARROW_KEY, new JigsawStructure(new Structure.StructureSettings(biomeGetter.getOrThrow(RootsTags.Biomes.HAS_BARROW_STRUCTURES), Collections.emptyMap(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_BOX), poolGetter.getOrThrow(ModFeatures.LARGE_BARROW_START_POOL_KEY), Optional.empty(), 1, ConstantHeight.of(VerticalAnchor.absolute(0)), false, Optional.of(Heightmap.Types.WORLD_SURFACE_WG), 80, Collections.emptyList(), DimensionPadding.ZERO, LiquidSettings.IGNORE_WATERLOGGING));
                  bootstrap.register(ModFeatures.HUT_KEY, new JigsawStructure(new Structure.StructureSettings(biomeGetter.getOrThrow(RootsTags.Biomes.HAS_HUT_STRUCTURES), Collections.emptyMap(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN), poolGetter.getOrThrow(ModFeatures.HUT_START_POOL_KEY), Optional.empty(), 1, ConstantHeight.of(VerticalAnchor.absolute(0)), false, Optional.of(Heightmap.Types.WORLD_SURFACE_WG), 80, Collections.emptyList(), DimensionPadding.ZERO, LiquidSettings.IGNORE_WATERLOGGING));
                  bootstrap.register(ModFeatures.RUINED_HUT_KEY, new JigsawStructure(new Structure.StructureSettings(biomeGetter.getOrThrow(RootsTags.Biomes.HAS_HUT_STRUCTURES), Collections.emptyMap(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN), poolGetter.getOrThrow(ModFeatures.RUINED_HUT_START_POOL_KEY), Optional.empty(), 1, ConstantHeight.of(VerticalAnchor.absolute(0)), false, Optional.of(Heightmap.Types.WORLD_SURFACE_WG), 80, Collections.emptyList(), DimensionPadding.ZERO, LiquidSettings.IGNORE_WATERLOGGING));
                  bootstrap.register(ModFeatures.STANDING_STONES_KEY, new StandingStonesStructure(new Structure.StructureSettings(biomeGetter.getOrThrow(RootsTags.Biomes.HAS_STANDING_STONES), Map.of(MobCategory.CREATURE, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create(new MobSpawnSettings.SpawnerData(ModEntities.GREEN_SPROUT.get(), 1, 3, 4), new MobSpawnSettings.SpawnerData(ModEntities.PURPLE_SPROUT.get(), 1, 3, 4), new MobSpawnSettings.SpawnerData(ModEntities.RED_SPROUT.get(), 1, 3, 4), new MobSpawnSettings.SpawnerData(ModEntities.TAN_SPROUT.get(), 1, 3, 4)))), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN)));
                })
                .add(Registries.STRUCTURE_SET, bootstrap -> {
                  HolderGetter<Structure> structureGetter = bootstrap.lookup(Registries.STRUCTURE);
                  bootstrap.register(ModFeatures.BARROW_SET_KEY, new StructureSet(structureGetter.getOrThrow(ModFeatures.BARROW_KEY), new RandomSpreadStructurePlacement(150, 65, RandomSpreadType.LINEAR, BARROW_SALT)));
                  bootstrap.register(ModFeatures.LARGE_BARROW_SET_KEY, new StructureSet(structureGetter.getOrThrow(ModFeatures.LARGE_BARROW_KEY), new RandomSpreadStructurePlacement(320, 120, RandomSpreadType.LINEAR, LARGE_BARROW_SALT)));
                  bootstrap.register(ModFeatures.STANDING_STONES_SET_KEY, new StructureSet(structureGetter.getOrThrow(ModFeatures.STANDING_STONES_KEY), new RandomSpreadStructurePlacement(80, 35, RandomSpreadType.LINEAR, STANDING_STONES_SALT)));
                  bootstrap.register(ModFeatures.HUT_SET_KEY, new StructureSet(List.of(new StructureSet.StructureSelectionEntry(structureGetter.getOrThrow(ModFeatures.HUT_KEY), 1), new StructureSet.StructureSelectionEntry(structureGetter.getOrThrow(ModFeatures.RUINED_HUT_KEY), 1)), new RandomSpreadStructurePlacement(70, 35, RandomSpreadType.LINEAR, HUT_SALT)));
                })
                .add(Registries.ENCHANTMENT_PROVIDER, bootstrap -> {
                  HolderGetter<Enchantment> getter = bootstrap.lookup(Registries.ENCHANTMENT);
                  bootstrap.register(FakePlayerUtil.LOOTING_I, new SingleEnchantment(getter.getOrThrow(Enchantments.LOOTING), ConstantInt.of(1)));
                  bootstrap.register(FakePlayerUtil.LOOTING_II, new SingleEnchantment(getter.getOrThrow(Enchantments.LOOTING), ConstantInt.of(2)));
                  bootstrap.register(FakePlayerUtil.LOOTING_III, new SingleEnchantment(getter.getOrThrow(Enchantments.LOOTING), ConstantInt.of(3)));
                }),
            Set.of(RootsAPI.MODID)
        )
    ).getRegistryProvider();
  }
}
