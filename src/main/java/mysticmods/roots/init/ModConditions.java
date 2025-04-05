package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.faction.GroveType;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.test.world.PartialBlockState;
import mysticmods.roots.api.test.world.PartialBlockStateMatchWorldTest;
import mysticmods.roots.block.crop.ThreeStageCropBlock;
import net.minecraft.tags.FluidTags;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModConditions {
  private static final DeferredRegister<LevelCondition> LEVEL = DeferredRegister.create(RootsRegistries.LEVEL_CONDITIONS, RootsAPI.MODID);
  public static final DeferredHolder<LevelCondition, LevelCondition> RUNE_PILLAR_4_HIGH = LEVEL.register("4_high_rune_pillar", () -> LevelCondition.runePillar(4));
  public static final DeferredHolder<LevelCondition, LevelCondition> RUNE_PILLAR_3_HIGH = LEVEL.register("3_high_rune_pillar", () -> LevelCondition.runePillar(3));
  public static final DeferredHolder<LevelCondition, LevelCondition> LOG_PILLAR_4_HIGH = LEVEL.register("4_high_log_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.ANY_LOG, 4));
  public static final DeferredHolder<LevelCondition, LevelCondition> LOG_PILLAR_3_HIGH = LEVEL.register("3_high_log_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.ANY_LOG, 3));
  public static final DeferredHolder<LevelCondition, LevelCondition> ACACIA_PILLAR_4_HIGH = LEVEL.register("4_high_acacia_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.ACACIA, 4));
  public static final DeferredHolder<LevelCondition, LevelCondition> ACACIA_PILLAR_3_HIGH = LEVEL.register("3_high_acacia_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.ACACIA, 3));
  public static final DeferredHolder<LevelCondition, LevelCondition> BIRCH_PILLAR_4_HIGH = LEVEL.register("4_high_birch_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.BIRCH, 4));
  public static final DeferredHolder<LevelCondition, LevelCondition> BIRCH_PILLAR_3_HIGH = LEVEL.register("3_high_birch_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.BIRCH, 3));
  public static final DeferredHolder<LevelCondition, LevelCondition> CRIMSON_PILLAR_4_HIGH = LEVEL.register("4_high_crimson_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.CRIMSON, 4));
  public static final DeferredHolder<LevelCondition, LevelCondition> CRIMSON_PILLAR_3_HIGH = LEVEL.register("3_high_crimson_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.CRIMSON, 3));
  public static final DeferredHolder<LevelCondition, LevelCondition> DARK_OAK_PILLAR_4_HIGH = LEVEL.register("4_high_dark_oak_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.DARK_OAK, 4));
  public static final DeferredHolder<LevelCondition, LevelCondition> DARK_OAK_PILLAR_3_HIGH = LEVEL.register("3_high_dark_oak_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.DARK_OAK, 3));
  public static final DeferredHolder<LevelCondition, LevelCondition> JUNGLE_PILLAR_4_HIGH = LEVEL.register("4_high_jungle_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.JUNGLE, 4));
  public static final DeferredHolder<LevelCondition, LevelCondition> JUNGLE_PILLAR_3_HIGH = LEVEL.register("3_high_jungle_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.JUNGLE, 3));
  public static final DeferredHolder<LevelCondition, LevelCondition> OAK_PILLAR_4_HIGH = LEVEL.register("4_high_oak_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.OAK, 4));
  public static final DeferredHolder<LevelCondition, LevelCondition> OAK_PILLAR_3_HIGH = LEVEL.register("3_high_oak_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.OAK, 3));
  public static final DeferredHolder<LevelCondition, LevelCondition> SPRUCE_PILLAR_4_HIGH = LEVEL.register("4_high_spruce_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.SPRUCE, 4));
  public static final DeferredHolder<LevelCondition, LevelCondition> SPRUCE_PILLAR_3_HIGH = LEVEL.register("3_high_spruce_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.SPRUCE, 3));
  public static final DeferredHolder<LevelCondition, LevelCondition> WARPED_PILLAR_4_HIGH = LEVEL.register("4_high_warped_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.WARPED, 4));
  public static final DeferredHolder<LevelCondition, LevelCondition> WARPED_PILLAR_3_HIGH = LEVEL.register("3_high_warped_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.WARPED, 3));
  public static final DeferredHolder<LevelCondition, LevelCondition> WILDWOOD_PILLAR_4_HIGH = LEVEL.register("4_high_wildwood_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.WILDWOOD, 4));
  public static final DeferredHolder<LevelCondition, LevelCondition> WILDWOOD_PILLAR_3_HIGH = LEVEL.register("3_high_wildwood_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.WILDWOOD, 3));
  public static final DeferredHolder<LevelCondition, LevelCondition> PRIMAL_GROVE_STONE_ANY = LEVEL.register("any_primal_grove_stone", () -> LevelCondition.groveStone(GroveType.PRIMAL, false));
  public static final DeferredHolder<LevelCondition, LevelCondition> PRIMAL_GROVE_STONE_ACTIVE = LEVEL.register("active_primal_grove_stone", () -> LevelCondition.groveStone(GroveType.PRIMAL, true));
  public static final DeferredHolder<LevelCondition, LevelCondition> PRIMAL_GROVE_STONE_INACTIVE = LEVEL.register("inactive_primal_grove_stone", () -> LevelCondition.groveStone(GroveType.PRIMAL, false, true));
  public static final DeferredHolder<LevelCondition, LevelCondition> GROVE_STONE_ANY = LEVEL.register("any_grove_stone", () -> LevelCondition.anyGroveStone(false));
  public static final DeferredHolder<LevelCondition, LevelCondition> GROVE_STONE_ACTIVE = LEVEL.register("active_grove_stone", () -> LevelCondition.anyGroveStone(true));
  public static final DeferredHolder<LevelCondition, LevelCondition> MATURE_WILDROOT_CROP = LEVEL.register("mature_wildroot_crop", () -> new LevelCondition.BlockStatePropertyCondition(new PartialBlockStateMatchWorldTest(new PartialBlockState(ModBlocks.WILDROOT_CROP.get()
      .defaultBlockState().setValue(ThreeStageCropBlock.AGE, 3), ThreeStageCropBlock.AGE))));
  public static final DeferredHolder<LevelCondition, LevelCondition> WATER_SOURCE = LEVEL.register("water_source", () -> new LevelCondition.FluidSourcePropertyCondition(FluidTags.WATER));
  private static final DeferredRegister<PlayerCondition> PLAYER = DeferredRegister.create(RootsRegistries.PLAYER_CONDITIONS, RootsAPI.MODID);

  public static void register(IEventBus bus) {
    LEVEL.register(bus);
    PLAYER.register(bus);
  }
}
