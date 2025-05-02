package mysticmods.roots.init;

import com.google.common.base.Suppliers;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.condition.*;
import mysticmods.roots.api.test.world.PartialBlockState;
import mysticmods.roots.api.test.world.PartialBlockStateMatchWorldTest;
import mysticmods.roots.block.crop.ThreeStageCropBlock;
import mysticmods.roots.condition.*;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Map;
import java.util.function.Supplier;


public class ModConditions {
  private static final DeferredRegister<ILevelConditionType<?>> LEVEL = DeferredRegister.create(RootsRegistries.LEVEL_CONDITIONS, RootsAPI.MODID);
  private static final DeferredRegister<IPlayerConditionType<?>> PLAYER = DeferredRegister.create(RootsRegistries.PLAYER_CONDITIONS, RootsAPI.MODID);

  // TODO: This needs to not be a map
  public static final Map<String, Supplier<CanonicalRepresentation>> SPECIAL_REPRESENTATIONS = new Object2ObjectOpenHashMap<>();
  public static final DeferredHolder<ILevelConditionType<?>, ILevelConditionType<GroveStoneCondition>> GROVE_STONE_CONDITION_TYPE = LEVEL.register("grove_stone_condition", GroveStoneCondition.Type::new);
  public static final DeferredHolder<ILevelConditionType<?>, ILevelConditionType<PillarCondition>> PILLAR_CONDITION_TYPE = LEVEL.register("pillar_condition", PillarCondition.Type::new);
  public static final DeferredHolder<ILevelConditionType<?>, ILevelConditionType<FluidSourcePropertyCondition>> FLUID_SOURCE_CONDITION_TYPE = LEVEL.register("fluid_source_property_condition", FluidSourcePropertyCondition.Type::new);
  public static final DeferredHolder<ILevelConditionType<?>, ILevelConditionType<BlockStatePropertyCondition>> BLOCK_STATE_CONDITION_TYPE = LEVEL.register("block_state_property_condition", BlockStatePropertyCondition.Type::new);
  public static final DeferredHolder<ILevelConditionType<?>, ILevelConditionType<OvergrowthCondition>> OVERGROWTH_CONDITION_TYPE = LEVEL.register("overgrowth_condition", OvergrowthCondition.Type::new);

  public static final Supplier<ILevelCondition> RUNESTONE_PILLAR_4_HIGH = Suppliers.memoize(() -> new PillarCondition(PillarType.ANY_RUNE, 4));
  public static final Supplier<ILevelCondition> RUNESTONE_PILLAR_3_HIGH = Suppliers.memoize(() -> new PillarCondition(PillarType.ANY_RUNE, 3));
  public static final Supplier<ILevelCondition> ANY_GROVE_STONE_ACTIVE = Suppliers.memoize(() -> new GroveStoneCondition(GroveType.ANY, true));
  public static final Supplier<ILevelCondition> ANY_GROVE_STONE_INACTIVE = Suppliers.memoize(() -> new GroveStoneCondition(GroveType.ANY, false, true));
  public static final Supplier<ILevelCondition> ANY_GROVE_STONE = Suppliers.memoize(() -> new GroveStoneCondition(GroveType.ANY, false, false));
  private static final String WILDROOT_CROP = "mature_wildroot_crop";
  public static final Supplier<ILevelCondition> MATURE_WILDROOT_CROP = Suppliers.memoize(() -> new BlockStatePropertyCondition(WILDROOT_CROP, new PartialBlockStateMatchWorldTest(new PartialBlockState(ModBlocks.WILDROOT_CROP.get()
      .defaultBlockState().setValue(ThreeStageCropBlock.AGE, 3), ThreeStageCropBlock.AGE))));
  static {
    SPECIAL_REPRESENTATIONS.put(WILDROOT_CROP, Suppliers.memoize(() -> new CanonicalRepresentation(
        Blocks.FARMLAND,
        new PartialBlockState(ModBlocks.WILDROOT_CROP.get()
            .defaultBlockState().setValue(ThreeStageCropBlock.AGE, 3), ThreeStageCropBlock.AGE))));
  }
  public static final Supplier<ILevelCondition> OVERGROWTH = Suppliers.memoize(OvergrowthCondition::getInstance);

  public static final Supplier<IPlayerCondition> FUNGAL_RANK_1 = Suppliers.memoize(() -> new GroveRankReputation(ModGroves.FUNGAL.get(), 1));

  public static final DeferredHolder<IPlayerConditionType<?>, IPlayerConditionType<GroveRankReputation>> GROVE_RANK_CONDITION_TYPE = PLAYER.register("grove_rank_condition", GroveRankReputation.Type::new);

  public static void register(IEventBus bus) {
    LEVEL.register(bus);
    PLAYER.register(bus);
  }
}
