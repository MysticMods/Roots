package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.block.WildRootsBlock;
import mysticmods.roots.test.block.BlockPropertyMatchTest;
import mysticmods.roots.worldgen.features.SupportingDirectionalBlockFeature;
import mysticmods.roots.worldgen.features.placements.DimensionPlacement;
import mysticmods.roots.worldgen.features.placements.HeightmapYRange;
import mysticmods.roots.worldgen.predicate.MatchingTreeBranchPredicate;
import mysticmods.roots.worldgen.predicate.MatchingTreeTrunkPredicate;
import mysticmods.roots.worldgen.structure.StandingStonePiece;
import mysticmods.roots.worldgen.structure.StandingStonesStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.OptionalInt;
import java.util.Set;

public class ModFeatures {
  private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registry.FEATURE_REGISTRY, RootsAPI.MODID);
  private static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, RootsAPI.MODID);
  private static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, RootsAPI.MODID);
  private static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER = DeferredRegister.create(Registry.PLACEMENT_MODIFIER_REGISTRY, RootsAPI.MODID);
  private static final DeferredRegister<BlockPredicateType<?>> BLOCK_PREDICATES = DeferredRegister.create(Registry.BLOCK_PREDICATE_TYPE_REGISTRY, RootsAPI.MODID);
  private static final DeferredRegister<RuleTestType<?>> RULE_TEST_TYPES = DeferredRegister.create(Registry.RULE_TEST_REGISTRY, RootsAPI.MODID);
  private static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES = DeferredRegister.create(Registry.STRUCTURE_PIECE_REGISTRY, RootsAPI.MODID);
  private static final DeferredRegister<StructureType<?>> STRUCTURES = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, RootsAPI.MODID);

  public static RegistryObject<RuleTestType<BlockPropertyMatchTest>> BLOCK_PROPERTY_MATCH_TEST = RULE_TEST_TYPES.register("block_property_match_test", () -> () -> BlockPropertyMatchTest.CODEC);

  public static RegistryObject<BlockPredicateType<MatchingTreeTrunkPredicate>> MATCHING_TREE_TRUNK_PREDICATE = BLOCK_PREDICATES.register("matching_tree", () -> () -> MatchingTreeTrunkPredicate.CODEC);

  public static RegistryObject<BlockPredicateType<MatchingTreeBranchPredicate>> MATCHING_TREE_BRANCH_PREDICATE = BLOCK_PREDICATES.register("matching_tree_branch", () -> () -> MatchingTreeBranchPredicate.CODEC);
  public static RegistryObject<PlacementModifierType<HeightmapYRange>> HEIGHTMAP_Y_RANGE = PLACEMENT_MODIFIER.register("heightmap_y_range", () -> () -> HeightmapYRange.CODEC);
  public static RegistryObject<PlacementModifierType<DimensionPlacement>> DIMENSION_PLACEMENT = PLACEMENT_MODIFIER.register("dimension_placement", () -> () -> DimensionPlacement.CODEC);

  // Features
  public static RegistryObject<SupportingDirectionalBlockFeature> SUPPORTING_DIRECTIONAL_BLOCK_FEATURE = FEATURES.register("supporting_directional_block_feature", () -> new SupportingDirectionalBlockFeature(SimpleBlockConfiguration.CODEC));

  // Configured Features
  public static RegistryObject<ConfiguredFeature<HugeMushroomFeatureConfiguration, ?>> HUGE_BAFFLECAP = CONFIGURED_FEATURES.register("huge_bafflecap", () -> new ConfiguredFeature<>(Feature.HUGE_RED_MUSHROOM, new HugeMushroomFeatureConfiguration(BlockStateProvider.simple(ModBlocks.BAFFLECAP_BLOCK.getDefaultState().setValue(HugeMushroomBlock.DOWN, false)), BlockStateProvider.simple(Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.UP, Boolean.FALSE).setValue(HugeMushroomBlock.DOWN, Boolean.FALSE)), 2)));

  public static RegistryObject<ConfiguredFeature<?, ?>> WILD_ROOTS_CONFIGURED_FEATURE = CONFIGURED_FEATURES.register("wild_roots", () -> new ConfiguredFeature<>(SUPPORTING_DIRECTIONAL_BLOCK_FEATURE.get(), new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_ROOTS.getDefaultState()))));

  public static RegistryObject<ConfiguredFeature<?, ?>> WILD_ROOTS_PLAINS_CONFIGURED_FEATURE = CONFIGURED_FEATURES.register("wild_roots_mossy", () -> new ConfiguredFeature<>(SUPPORTING_DIRECTIONAL_BLOCK_FEATURE.get(), new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_ROOTS.getDefaultState().setValue(WildRootsBlock.MOSSY, true)))));

  private static final RegistryObject<ConfiguredFeature<?, ?>> CONFIGURED_WILD_AUBERGINE = CONFIGURED_FEATURES.register("wild_aubergine", () -> new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_AUBERGINE.getDefaultState()))));

  private static final RegistryObject<ConfiguredFeature<?, ?>> CONFIGURED_WILD_AUBERGINE_PATCH = CONFIGURED_FEATURES.register("wild_aubergine_patch", () -> new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(20, 3, 2, ModFeatures.WILD_AUBERGINE.getHolder().get())));

   private static TreeConfiguration.TreeConfigurationBuilder createWildwood() {
      return (new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(ModBlocks.WILDWOOD_LOG.get()), new FancyTrunkPlacer(12, 1, 1), BlockStateProvider.simple(ModBlocks.WILDWOOD_LEAVES.get()), new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines();
   }

  public static final RegistryObject<ConfiguredFeature<TreeConfiguration, ?>> WILDWOOD_TREE = CONFIGURED_FEATURES.register("wildwood_tree", () -> new ConfiguredFeature<>(Feature.TREE, createWildwood().build()));

   public static final RegistryObject<ConfiguredFeature<TreeConfiguration, ?>> WILDWOOD_TREE_BEES = CONFIGURED_FEATURES.register("wildwood_tree_bees", () -> new ConfiguredFeature<>(Feature.TREE, createWildwood().decorators(List.of(new BeehiveDecorator(1.0F))).build()));

  // Place features
  public static RegistryObject<PlacedFeature> WILD_ROOTS_UNDERGROUND_PLACED_FEATURE = PLACED_FEATURES.register("wild_roots_underground", () -> new PlacedFeature(Holder.direct(WILD_ROOTS_CONFIGURED_FEATURE.get()), List.of(
    CountPlacement.of(40), // How many attempts per chunk
    InSquarePlacement.spread(), // Randomize x/z to random spot in chunk
    new HeightmapYRange(ConstantHeight.of(VerticalAnchor.absolute(-32)), Heightmap.Types.WORLD_SURFACE_WG) // Pick spot between y = 6 and heightmap of terrain above
  )));

  public static RegistryObject<PlacedFeature> WILD_ROOTS_FOREST_PLACED_FEATURE = PLACED_FEATURES.register("wild_roots_forest", () -> new PlacedFeature(Holder.direct(WILD_ROOTS_CONFIGURED_FEATURE.get()), List.of(
    BiomeFilter.biome(),
    CountPlacement.of(3), // How many attempts per chunk
    InSquarePlacement.spread(), // Randomize x/z to random spot in chunk
    HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), // Find surface
    RandomOffsetPlacement.vertical(ConstantInt.of(1)), // Offset up one to above surface
    BlockPredicateFilter.forPredicate(MatchingTreeTrunkPredicate.create()), // Check if we are at a tree's log.
    CountPlacement.of(2), // make 5 new attempts for each position at the log
    RandomOffsetPlacement.of(UniformInt.of(-2, 2), UniformInt.of(-2, 0)) // Randomize root position to a range of 2 on x/z and can be 0-2 blocks below the log y value.
  )));

  public static RegistryObject<PlacedFeature> WILD_ROOTS_SPARSE_PLACED_FEATURE = PLACED_FEATURES.register("wild_roots_sparse", () -> new PlacedFeature(WILD_ROOTS_PLAINS_CONFIGURED_FEATURE.getHolder().get(), List.of(
    BiomeFilter.biome(),
    CountPlacement.of(30), // How many attempts per chunk
    InSquarePlacement.spread(), // Randomize x/z to random spot in chunk
    HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), // Find surface
    RandomOffsetPlacement.vertical(ConstantInt.of(1)), // Offset up one to above surface
    BlockPredicateFilter.forPredicate(MatchingTreeTrunkPredicate.create()), // Check if we are at a tree's log.
    CountPlacement.of(3), // make 5 new attempts for each position at the log
    RandomOffsetPlacement.of(UniformInt.of(-2, 2), UniformInt.of(-2, 0)) // Randomize root position to a range of 2 on x/z and can be 0-2 blocks below the log y value.
  )));

  private static final RegistryObject<PlacedFeature> WILD_AUBERGINE = PLACED_FEATURES.register("wild_aubergine", () -> new PlacedFeature(ModFeatures.CONFIGURED_WILD_AUBERGINE.getHolder().get(), List.of(
    BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.wouldSurvive(ModBlocks.WILD_AUBERGINE.getDefaultState(), BlockPos.ZERO)))
  )));

  public static final RegistryObject<StructurePieceType> STANDING_STONES_PIECE = STRUCTURE_PIECE_TYPES.register("standing_stones_piece", () -> (pContext, pTag) -> new StandingStonePiece(pTag));

  public static final RegistryObject<StructureType<StandingStonesStructure>> STANDING_STONES = STRUCTURES.register("standing_stones", () -> () -> StandingStonesStructure.CODEC);

  public static final RegistryObject<PlacedFeature> WILD_AUBERGINE_PATCH = PLACED_FEATURES.register("wild_aubergine_patch", () -> new PlacedFeature(ModFeatures.CONFIGURED_WILD_AUBERGINE_PATCH.getHolder().get(), List.of(
    CountPlacement.of(1),
    InSquarePlacement.spread(),
    HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), // Find surface
    RarityFilter.onAverageOnceEvery(60),
    DimensionPlacement.of(Set.of(Level.OVERWORLD))
  )));

  public static void register(IEventBus bus) {
    FEATURES.register(bus);
    CONFIGURED_FEATURES.register(bus);
    PLACED_FEATURES.register(bus);
    PLACEMENT_MODIFIER.register(bus);
    BLOCK_PREDICATES.register(bus);
    RULE_TEST_TYPES.register(bus);
    STRUCTURES.register(bus);
    STRUCTURE_PIECE_TYPES.register(bus);
  }


  public static void load() {
  }
}
