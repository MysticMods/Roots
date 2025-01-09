package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.test.block.BlockPropertyMatchTest;
import mysticmods.roots.worldgen.features.SupportingDirectionalBlockFeature;
import mysticmods.roots.worldgen.features.placements.DimensionPlacement;
import mysticmods.roots.worldgen.features.placements.HeightmapYRange;
import mysticmods.roots.worldgen.predicate.MatchingTreeBranchPredicate;
import mysticmods.roots.worldgen.predicate.MatchingTreeTrunkPredicate;
import mysticmods.roots.worldgen.structure.StandingStonePiece;
import mysticmods.roots.worldgen.structure.StandingStonesStructure;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.OptionalInt;

public class ModFeatures {
  private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE, RootsAPI.MODID);
  private static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER = DeferredRegister.create(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, RootsAPI.MODID);
  private static final DeferredRegister<BlockPredicateType<?>> BLOCK_PREDICATES = DeferredRegister.create(BuiltInRegistries.BLOCK_PREDICATE_TYPE, RootsAPI.MODID);
  private static final DeferredRegister<RuleTestType<?>> RULE_TEST_TYPES = DeferredRegister.create(BuiltInRegistries.RULE_TEST, RootsAPI.MODID);
  private static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES = DeferredRegister.create(BuiltInRegistries.STRUCTURE_PIECE, RootsAPI.MODID);
  private static final DeferredRegister<StructureType<?>> STRUCTURES = DeferredRegister.create(BuiltInRegistries.STRUCTURE_TYPE, RootsAPI.MODID);

  public static final DeferredHolder<StructurePieceType, StructurePieceType> STANDING_STONES_PIECE = STRUCTURE_PIECE_TYPES.register("standing_stones_piece", () -> (pContext, pTag) -> new StandingStonePiece(pTag));

  public static final DeferredHolder<StructureType<?>, StructureType<StandingStonesStructure>> STANDING_STONES = STRUCTURES.register("standing_stones", () -> () -> StandingStonesStructure.CODEC);

  public static DeferredHolder<RuleTestType<?>, RuleTestType<BlockPropertyMatchTest>> BLOCK_PROPERTY_MATCH_TEST = RULE_TEST_TYPES.register("block_property_match_test", () -> () -> BlockPropertyMatchTest.CODEC);

  public static DeferredHolder<BlockPredicateType<?>, BlockPredicateType<MatchingTreeTrunkPredicate>> MATCHING_TREE_TRUNK_PREDICATE = BLOCK_PREDICATES.register("matching_tree", () -> () -> MatchingTreeTrunkPredicate.CODEC);

  public static DeferredHolder<BlockPredicateType<?>, BlockPredicateType<MatchingTreeBranchPredicate>> MATCHING_TREE_BRANCH_PREDICATE = BLOCK_PREDICATES.register("matching_tree_branch", () -> () -> MatchingTreeBranchPredicate.CODEC);
  public static DeferredHolder<PlacementModifierType<?>, PlacementModifierType<HeightmapYRange>> HEIGHTMAP_Y_RANGE = PLACEMENT_MODIFIER.register("heightmap_y_range", () -> () -> HeightmapYRange.CODEC);
  public static DeferredHolder<PlacementModifierType<?>, PlacementModifierType<DimensionPlacement>> DIMENSION_PLACEMENT = PLACEMENT_MODIFIER.register("dimension_placement", () -> () -> DimensionPlacement.CODEC);

  // Features
  public static DeferredHolder<Feature<?>, SupportingDirectionalBlockFeature> SUPPORTING_DIRECTIONAL_BLOCK_FEATURE = FEATURES.register("supporting_directional_block_feature", () -> new SupportingDirectionalBlockFeature(SimpleBlockConfiguration.CODEC));

  public static ResourceKey<ConfiguredFeature<?, ?>> CONFIGURED_HUGE_BAFFLECAP_KEY = ResourceKey.create(Registries.CONFIGURED_FEATURE, RootsAPI.rl("huge_bafflecap"));
  public static ResourceKey<ConfiguredFeature<?, ?>> CONFIGURED_WILD_ROOTS_KEY = ResourceKey.create(Registries.CONFIGURED_FEATURE, RootsAPI.rl("wild_roots"));
  public static ResourceKey<ConfiguredFeature<?, ?>> CONFIGURED_WILD_ROOTS_MOSSY_KEY = ResourceKey.create(Registries.CONFIGURED_FEATURE, RootsAPI.rl("wild_roots_mossy"));
  public static ResourceKey<ConfiguredFeature<?, ?>> CONFIGURED_WILD_AUBERGINE_KEY = ResourceKey.create(Registries.CONFIGURED_FEATURE, RootsAPI.rl("wild_aubergine"));
  public static ResourceKey<ConfiguredFeature<?, ?>> CONFIGURED_WILD_AUBERGINE_PATCH_KEY = ResourceKey.create(Registries.CONFIGURED_FEATURE, RootsAPI.rl("wild_aubergine_patch"));
  public static ResourceKey<ConfiguredFeature<?, ?>> CONFIGURED_WILDWOOD_TREE_KEY = ResourceKey.create(Registries.CONFIGURED_FEATURE, RootsAPI.rl("wildwood_tree"));
  public static ResourceKey<ConfiguredFeature<?, ?>> CONFIGURED_WILDWOOD_TREE_BEES_KEY = ResourceKey.create(Registries.CONFIGURED_FEATURE, RootsAPI.rl("wildwood_tree_bees"));
  public static ResourceKey<PlacedFeature> PLACED_WILD_ROOTS_UNDERGROUND_KEY = ResourceKey.create(Registries.PLACED_FEATURE, RootsAPI.rl("wild_roots_underground"));
  public static ResourceKey<PlacedFeature> PLACED_WILD_ROOTS_FOREST_KEY = ResourceKey.create(Registries.PLACED_FEATURE, RootsAPI.rl("wild_roots_forest"));
  public static ResourceKey<PlacedFeature> PLACED_WILD_ROOTS_SPARSE_KEY = ResourceKey.create(Registries.PLACED_FEATURE, RootsAPI.rl("wild_roots_sparse"));
  public static ResourceKey<PlacedFeature> PLACED_WILD_AUBERGINE_KEY = ResourceKey.create(Registries.PLACED_FEATURE, RootsAPI.rl("wild_aubergine"));
  public static final ResourceKey<PlacedFeature> PLACED_WILD_AUBERGINE_PATCH_KEY = ResourceKey.create(Registries.PLACED_FEATURE, RootsAPI.rl("wild_aubergine_patch"));

  public static ResourceKey<BiomeModifier> BEETLE_SPAWNS = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, RootsAPI.rl("beetle_spawns"));
  public static ResourceKey<BiomeModifier> DEER_SPAWNS = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, RootsAPI.rl("deer_spawns"));
  public static ResourceKey<BiomeModifier> DUCK_SPAWNS = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, RootsAPI.rl("duck_spawns"));
  public static ResourceKey<BiomeModifier> FENNEC_SPAWNS = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, RootsAPI.rl("fennec_spawns"));
  public static ResourceKey<BiomeModifier> OWL_SPAWNS = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, RootsAPI.rl("owl_spawns"));
  public static ResourceKey<BiomeModifier> SPROUT_SPAWNS = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, RootsAPI.rl("sprout_spawns"));
  public static ResourceKey<BiomeModifier> WILD_AUBERGINES_KEY = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, RootsAPI.rl("wild_aubergines"));
  public static ResourceKey<BiomeModifier> WILD_ROOTS_FOREST_KEY = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, RootsAPI.rl("wild_roots_forest"));
  public static ResourceKey<BiomeModifier> WILD_ROOTS_UNDERGROUND_KEY = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, RootsAPI.rl("wild_roots_underground"));

  public static ResourceKey<Structure> BARROW_KEY = ResourceKey.create(Registries.STRUCTURE, RootsAPI.rl("barrow"));
  public static ResourceKey<StructureTemplatePool> BARROW_START_POOL_KEY = ResourceKey.create(Registries.TEMPLATE_POOL, RootsAPI.rl("barrow_pool/start_pool"));
  public static ResourceKey<StructureTemplatePool> BARROW_DOWN_POOL_KEY = ResourceKey.create(Registries.TEMPLATE_POOL, RootsAPI.rl("barrow_pool/down_pool"));
  public static ResourceKey<Structure> STANDING_STONES_KEY = ResourceKey.create(Registries.STRUCTURE, RootsAPI.rl("standing_stones"));
  public static ResourceKey<Structure> HUT_KEY = ResourceKey.create(Registries.STRUCTURE, RootsAPI.rl("hut"));
  public static ResourceKey<StructureTemplatePool> HUT_DOWN_POOL_KEY = ResourceKey.create(Registries.TEMPLATE_POOL, RootsAPI.rl("hut_pool/down_pool"));
  public static ResourceKey<StructureTemplatePool> HUT_START_POOL_KEY = ResourceKey.create(Registries.TEMPLATE_POOL, RootsAPI.rl("hut_pool/start_pool"));
  public static ResourceKey<Structure> RUINED_HUT_KEY = ResourceKey.create(Registries.STRUCTURE, RootsAPI.rl("ruined_hut"));
  public static ResourceKey<StructureTemplatePool> RUINED_HUT_DOWN_POOL_KEY = ResourceKey.create(Registries.TEMPLATE_POOL, RootsAPI.rl("hut_ruined_pool/down_pool"));
  public static ResourceKey<StructureTemplatePool> RUINED_HUT_START_POOL_KEY = ResourceKey.create(Registries.TEMPLATE_POOL, RootsAPI.rl("hut_ruined_pool/start_pool"));

  public static ResourceKey<StructureSet> BARROW_SET_KEY = ResourceKey.create(Registries.STRUCTURE_SET, RootsAPI.rl("barrow"));
  public static ResourceKey<StructureSet> HUT_SET_KEY = ResourceKey.create(Registries.STRUCTURE_SET, RootsAPI.rl("hut"));
  public static ResourceKey<StructureSet> STANDING_STONES_SET_KEY = ResourceKey.create(Registries.STRUCTURE_SET, RootsAPI.rl("standing_stones"));

  public static TreeConfiguration.TreeConfigurationBuilder createWildwood() {
    return (new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(ModBlocks.WILDWOOD_LOG.get()), new FancyTrunkPlacer(12, 1, 1), BlockStateProvider.simple(ModBlocks.WILDWOOD_LEAVES.get()), new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines();
  }

  public static void register(IEventBus bus) {
    FEATURES.register(bus);
    PLACEMENT_MODIFIER.register(bus);
    BLOCK_PREDICATES.register(bus);
    RULE_TEST_TYPES.register(bus);
    STRUCTURES.register(bus);
    STRUCTURE_PIECE_TYPES.register(bus);
  }
}
