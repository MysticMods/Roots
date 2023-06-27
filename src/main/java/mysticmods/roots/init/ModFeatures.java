package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.worldgen.features.SupportingDirectionalBlockFeature;
import mysticmods.roots.worldgen.features.placements.HeightmapYRange;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModFeatures {
  private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registry.FEATURE_REGISTRY, RootsAPI.MODID);
  private static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, RootsAPI.MODID);
  private static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, RootsAPI.MODID);
  private static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER = DeferredRegister.create(Registry.PLACEMENT_MODIFIER_REGISTRY, RootsAPI.MODID);

  public static RegistryObject<PlacementModifierType<HeightmapYRange>> HEIGHTMAP_Y_RANGE = PLACEMENT_MODIFIER.register("heightmap_y_range", () -> () -> HeightmapYRange.CODEC);

  public static RegistryObject<ConfiguredFeature<HugeMushroomFeatureConfiguration, ?>> HUGE_BAFFLECAP = CONFIGURED_FEATURES.register("huge_bafflecap", () -> new ConfiguredFeature<>(Feature.HUGE_RED_MUSHROOM, new HugeMushroomFeatureConfiguration(BlockStateProvider.simple(ModBlocks.BAFFLECAP_BLOCK.getDefaultState().setValue(HugeMushroomBlock.DOWN, false)), BlockStateProvider.simple(Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.UP, Boolean.FALSE).setValue(HugeMushroomBlock.DOWN, Boolean.FALSE)), 2)));

  public static RegistryObject<SupportingDirectionalBlockFeature> SUPPORTING_DIRECTIONAL_BLOCK_FEATURE = FEATURES.register("supporting_directional_block_feature", () -> new SupportingDirectionalBlockFeature(SimpleBlockConfiguration.CODEC));
  public static RegistryObject<ConfiguredFeature<SimpleBlockConfiguration, ?>> WILD_ROOTS_CONFIGURED_FEATURE = CONFIGURED_FEATURES.register("wild_roots", () -> new ConfiguredFeature<>(SUPPORTING_DIRECTIONAL_BLOCK_FEATURE.get(), new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_ROOTS.getDefaultState()))));
  public static RegistryObject<PlacedFeature> WILD_ROOTS_UNDERGROUND_PLACED_FEATURE = PLACED_FEATURES.register("wild_roots_underground", () -> new PlacedFeature(Holder.direct(WILD_ROOTS_CONFIGURED_FEATURE.get()), List.of(
          CountPlacement.of(60), // How many attempts per chunk
          InSquarePlacement.spread(), // Randomize x/z to random spot in chunk
          new HeightmapYRange(ConstantHeight.of(VerticalAnchor.absolute(6)), Heightmap.Types.WORLD_SURFACE_WG) // Pick spot between y = 6 and heightmap of terrain above
  )));
  public static RegistryObject<PlacedFeature> WILD_ROOTS_TREE_PLACED_FEATURE = PLACED_FEATURES.register("wild_roots_trees", () -> new PlacedFeature(Holder.direct(WILD_ROOTS_CONFIGURED_FEATURE.get()), List.of(
          CountPlacement.of(20), // How many attempts per chunk
          InSquarePlacement.spread(), // Randomize x/z to random spot in chunk
          HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), // Find surface
          RandomOffsetPlacement.vertical(ConstantInt.of(1)), // Offset up one to above surface
          BlockPredicateFilter.forPredicate(BlockPredicate.matchesTag(BlockTags.LOGS_THAT_BURN)), // Check if we are at a tree's log. Will have false positives with structures made of logs
          CountPlacement.of(5), // make 5 new attempts for each position at the log
          RandomOffsetPlacement.of(UniformInt.of(-2, 2), UniformInt.of(-2, 0)) // Randomize root position to a range of 2 on x/z and can be 0-2 blocks below the log y value.
  )));

  public static void register (IEventBus bus) {
    FEATURES.register(bus);
    CONFIGURED_FEATURES.register(bus);
    PLACED_FEATURES.register(bus);
    PLACEMENT_MODIFIER.register(bus);
  }



  public static void load () {
  }
}
